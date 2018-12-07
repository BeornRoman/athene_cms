(ns athene.layout
    (:require [clj-jade.core :as jade]
              [athene.database :refer [db-config load-config]]
              [clojure.java.jdbc :as sql]
              [clojure.edn :as edn]
              [athene.config :refer :all]))

(def themeid
    (:themeid (load-config "configs/config.edn")))

(defn get-theme []
    (sql/query @db-config ["SELECT * FROM themes;"])
    )

(defn index-page [posts]
    (jade/render "/index.jade" {
                                "pageName" "Athene CMS"
                                "theme" (str "themes/"
                                             (:theme_name (first (get-theme)))
                                             "/styles.css")
                                "posts" posts}))

(defn admin-mysql-is-not-connected []
    (jade/render "/db_is_not_conn"))

(defn signup []
    (jade/render "/signup"))

(defn login []
    (jade/render "/authentication"))
