(ns athene.admin.registration
    (:require [clojure.data.json :as json]
              [clojure.java.jdbc :as sql]
              [athene.md5 :refer [md5]]
              [athene.database :as db]))

(defn check-username [user_name]
    "Check username in database"
    (:user_name (first (sql/query @db/db-config ["select user_name from users where user_name=?" user_name]))))

(defn insert-user [user_name password]
    "Signup user into database"
    (sql/insert! @db/db-config :users {:user_name user_name :password (md5 (str password)) :user_role "0" :theme_id "1"}))

(defn reg-user [email pwd pwdconfirm]
    "User Registration"
    (if (= (check-username email) nil)
        (if (= pwd pwdconfirm)
            (do
                (insert-user email pwd)
                (json/write-str "Registration | Success | Complete"))
            (json/write-str     "Registration | Error | Passwords Are Not Confirmed"))
        (json/write-str         "Registration | Error | User Already Exists")))
