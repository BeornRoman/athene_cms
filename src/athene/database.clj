(ns athene.database
    (:require [clojure.java.jdbc :as sql]
      [clojure.edn :as edn]
      [athene.core :refer [check-config]]))

(defn load-config [filename]
  "Given a filename, load & return a config file"
  (if (check-config)
      (edn/read-string (slurp filename))
      {}))

; Конфиг Postgres
; (def postgres-config (atom {
;         :subprotocol "postgresql"
;         :subname "postgresql"
;         :user ""
;         :password ""}))

; Конфиг MySql
(def db-config (atom {
        :classname "com.mysql.jdbc.Driver"
        :subprotocol "mysql"
        :subname ""
        :user ""
        :password ""
        :themeid 1
}))

(defn set-db-config [dbname dbaddress dbuser dbpassword]
    (swap! db-config
      (fn [cur]
        (merge cur {
        :dbname dbname
        :dbaddress dbaddress
        :subname (str "//"
          dbaddress
          ":3306/"
          dbname
          "?characterEncoding=UTF-8")
        :user dbuser
        :password dbpassword}))))

(defn check-db-config-filled []
    ; (println @db-config)
    (if
        (= (:user @db-config) "")
        (let [db-data (load-config "configs/config.edn")]
             (set-db-config
                       (:dbname db-data)
                       (:dbaddress db-data)
                       (:dbuser db-data)
                       (:dbpassword db-data))))
    @db-config)

(defn check-conn
  "Checks MySQL connection"
  []
  (try
         (do (sql/query (check-db-config-filled) ["SELECT version();"])
             true)
  (catch Exception e
         (do
             (println (str "caught exception: " (.getMessage e)))
             false))))
