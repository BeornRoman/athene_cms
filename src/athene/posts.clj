(ns athene.posts
    (:require
        [clojure.java.jdbc :as sql]
        [athene.database :refer [db-config]]))

(defn get-posts []
    "Gets top posts from database"
    (sql/query @db-config ["SELECT * FROM posts;"]))

(defn get-titles []
    "Gets titles from database"
    (sql/query @db-config ["SELECT title FROM posts;"]))

(defn get-titles-and-link-delete []
    "Gets titles from database"
    (sql/query @db-config ["SELECT link_delete title FROM posts;"]))
