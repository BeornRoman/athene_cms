(ns athene.main
    (:require
      [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
      [athene.installation.main :refer [installation-routes]]
      [ring.middleware.resource :refer [wrap-resource]]
      [ring.middleware.json :refer [wrap-json-params]]
      [ring.middleware.cookies :refer :all]
      [ring.middleware.session.cookie :refer :all]
      [ring.middleware.session :refer :all]
      [ring.middleware.params :refer :all]
      [athene.admin.main :refer [admin-routes]]
      [athene.admin.layout :refer [admin-login]]
      [athene.admin.authentication :as auth]
      [athene.database :refer [check-conn]]
      [athene.core :refer [check-config]]
      [athene.posts :refer [get-posts]]
      [compojure.handler :as handler]
      [compojure.core :refer :all]
      [compojure.route :as route]
      [athene.layout :as layout]
      [ring.util.response :refer :all])
    (:gen-class))


(defn wrap-checks [handler]
    "Checking MySQL Connection & Installed Flag Middleware"
    (fn [{session :session :as req}]
        (let [install-req   (assoc req :uri "/install")
              mysql-req     (assoc req :uri "/mysql-enable")
              main-req      (assoc req :uri "/")
              login-req     (assoc req :uri "/login")
              register-req  (assoc req :uri "/register")
              session-req   (session :login "null")
              login         (get (first  (get-in req [:form-params])) 1)
              pwd           (get (second (get-in req [:form-params])) 1)
              req-method    (get-in req [:request-method])]
                  (if (check-config)
                  (if (check-conn)
                      (if
                          (not= session-req "null")
                          (handler req)
                          (do (if (not= (:uri req) (:uri login-req))
                                  (handler login-req)
                                  (if (= (str req-method) ":post")
                                      (if (= (auth/auth-user login pwd) (pr-str "Authentication | Success | Login"))
                                          (assoc-in (redirect "/") [:session :login] (str login))
                                          (handler login-req))
                                      (handler login-req)))))
                      (handler mysql-req))
                  (handler install-req)))))


(defroutes athene-routes
    (GET "/" [req]
        (do (layout/index-page (get-posts))))
    (GET "/mysql-enable" []
        (layout/admin-mysql-is-not-connected))
    (GET "/logout" [req]
        (assoc-in (redirect "/") [:session :login] "null"))
    (GET "/login" [] (do
        (admin-login)))
    (POST "/login" [email pwd]
        (auth/auth-user email pwd))
    (route/resources "/"))



(def app
    (-> (wrap-json-params (routes athene-routes admin-routes installation-routes))
        (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
        (wrap-checks)
        (wrap-session {:cookie-attrs {:max-age 31536000}})
        (wrap-params)
        (wrap-resource "public")))
