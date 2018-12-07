(ns athene.roles
    (:require
        [clojure.java.jdbc :as sql]
        [athene.database :refer [db-config]]))

(defn insert-into-tables []
    (sql/insert! @db-config :roles
        {:role_id "1" :role_name "admin"}
        {:role_id "2" :role_name "user" })
    (sql/insert! @db-config :users
       {:user_name "patison5" :user_role "1" :password "admin_root" :theme_id "1"}))

(defn new-user [sname role password theme_id]
    "Добавляем пользователя в базу данных"
    (sql/insert! @db-config :users
        {:user_name sname :user_role role :password password :theme_id theme_id}))


(defn update-user [sid role]
    "меняем тему"
    (sql/update! @db-config :users {:user_role role} ["id_user = ?" sid]))


(defn start-join-db []
    (sql/query @db-config "SELECT * FROM users LEFT JOIN roles ON (users.user_role = roles.role_id);"))

(defn get-roles []
    "Gets roles from database"
    (sql/query @db-config ["SELECT * FROM roles;"]))

(defn get-users []
    (sql/query @db-config ["SELECT * FROM users;"]))

(defn create-roles []
    "Создание ролей"
    (insert-into-tables)
    (new-user "Roma_Beorn" "2" "Root" "1")
    (start-join-db))
