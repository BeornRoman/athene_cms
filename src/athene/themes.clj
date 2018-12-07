(ns athene.themes
  (:require
    [clojure.java.jdbc :as sql]
    [athene.database :refer [db-config]]))

; (defn start-join-db []
;     (sql/query db "SELECT * FROM users LEFT JOIN themes ON (users.theme_id = themes.themes_id);"))

(defn get-themes []
      "Gets roles from database"
      (sql/query @db-config ["SELECT * FROM themes;"]))

(defn insert-into-themes []
    (sql/insert! @db-config :themes
       {:themes_id "1" :theme_name "standart_theme"}
       {:themes_id "2" :theme_name "unusual_theme"}))

;(defn change-th_name
;      [id tname]
;      "изменям название самой темы"
;      (sql/update! db :themes {:theme_name tname ["themes_id = ?" id]}))

(defn new-theme [th_name]
      "Добавляем тему в базу данных"
      (sql/insert! @db-config :themes
                   {:theme_name th_name }))

(defn get-user_theme
      "берем тему по никнейму и отправляем в jade"
      [user_name]
      (sql/query @db-config ["SELECT theme_name FROM users LEFT JOIN themes ON (users.theme_id = themes.themes_id) WHERE user_name =?" user_name]))

(defn change-theme [th_id u_name]
  "меняем тему у пользователя"
    (sql/update! @db-config :users {:theme_id th_id} ["user_name = ?" u_name]))

(defn add_in_session [flag user_name]
      "добавляем в сессию значение из базы, пока не используется"
      (defn get_theme
          [user_name]
            (sql/query ["SELECT themes_id FROM users LEFT JOIN themes ON (users.theme_id = themes.themes_id) WHERE user_name =?" user_name]))

      (def theme_name (str (get (nth (get_theme user_name) 0 "incorrect") :theme_name "incorrect" )))

      (assoc (println "seseion created") :session {:flag flag :theme theme_name}))


(defn check_apply_th
      "проверка на первый заход пользователя"
      [req]
      (def flag  (:flag  (:session req)))
      (def user_name (:user_name (:session req)))

      (if (= flag "true")
        (println "session exists")
        (add_in_session "true" user_name)))


(defn new-themes []
    "Создание тем"
    (insert-into-themes)
    (new-theme "light_theme")
    ; (change-theme "Roma_Beorn" "2")
    )
