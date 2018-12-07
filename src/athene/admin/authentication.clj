(ns athene.admin.authentication
    (:require [clojure.data.json :as json]
              [clojure.java.jdbc :as sql]
              [athene.md5 :refer [md5]]
              [athene.database :as db]
              [athene.admin.registration :refer [check-username]]))

(defn auth-user [email pwd]
  (if (= (check-username email) nil)
      (json/write-str "Authentication | Error | Unknown User")
      (if (= (md5 (str pwd))  (:password (first (sql/query @db/db-config ["select password from users where user_name=?" email]))))
          (json/write-str "Authentication | Success | Login")
          (json/write-str "Authentication | Error | Password Is Incorrect"))))
