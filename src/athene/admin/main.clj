(ns athene.admin.main
  (:require
    [compojure.core :refer :all]
    [compojure.route :as route]
    [athene.admin.layout :as layout]
    [athene.core :refer :all]
    [athene.admin.registration :as reg]
    [athene.admin.authentication :as auth])
  (:gen-class))

(defroutes admin-routes
    (GET "/register" [] (layout/admin-signup))
    (POST "/register" [email pwd pwdconfirm]
        (reg/reg-user email pwd pwdconfirm))
    (route/resources "/"))
