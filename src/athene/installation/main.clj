(ns athene.installation.main
  (:require
    [athene.database :refer [set-db-config db-config]]
    [athene.installation.layout :refer :all]
    [athene.database :refer [check-conn]]
    [athene.core :refer [check-config]]
    [clojure.data.json :as json]
    [compojure.core :refer :all]
    [ring.util.response :refer [redirect]]
    [compojure.route :as route]
    [clojure.java.jdbc :as sql]
    [java-jdbc.ddl :as ddl])
  (:use clojure.java.io))

(defn create-db-structure [dbname dbaddress dbuser dbpassword]
   "AtheneCMS table structure"
   (set-db-config dbname dbaddress dbuser dbpassword)

   (sql/execute! @db-config ["drop table if exists user_role"])
   (sql/execute! @db-config ["drop table if exists user_post"])
   (sql/execute! @db-config ["drop table if exists roles"])
   (sql/execute! @db-config ["drop table if exists posts"])
   (sql/execute! @db-config ["drop table if exists users"])
   (sql/execute! @db-config ["drop table if exists themes"])
   (sql/execute! @db-config ["drop table if exists user_theme"])

   (sql/db-do-commands @db-config
    (ddl/create-table :users
                          [:id_user "integer" "PRIMARY KEY" "AUTO_INCREMENT"]
                          [:user_name "text"]
                          [:user_role "integer"]
                          [:password "varchar(255)"]
                          [:theme_id "varchar(255)"]))

    ; (sql/create-table-ddl :user_role
    ;                       [:user_id "integer"]
    ;                       [:post_id "integer"])

    ; (sql/create-table-ddl :roles
    ;                       [:role_id :integer "references users (user_role)"]
    ;                       [:role_name "text"])

   (sql/db-do-commands @db-config
    (ddl/create-table :posts
                          [:post_id "integer" "PRIMARY KEY" "AUTO_INCREMENT"]
                          [:title "varchar(255)"]
                          [:body "text"]
                          [:date "TIMESTAMP" "NOT NULL DEFAULT NOW()"]))

   (sql/db-do-commands @db-config
    (ddl/create-table :user_post
                          [:user_id "integer"]
                          [:post_id "integer"]))

   (sql/db-do-commands @db-config
    (ddl/create-table :themes
                          [:theme_id "integer" "PRIMARY KEY" "AUTO_INCREMENT"]
                          [:theme_name "text"]))

   (sql/db-do-commands @db-config
    (ddl/create-table :user_theme
                          [:user_name "varchar(255)"]
                          [:themes_id "varchar(255)"]))

    ; UTF8 explicit conversion
   (sql/execute! @db-config ["ALTER TABLE posts CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;"])

   (sql/insert! @db-config :posts {:title "Star Wars Episode 7" :body "Three decades after the defeat of the Galactic Empire, a new threat arises. The First Order attempts to rule the galaxy and only a rag-tag group of heroes can stop them, along with the help of the Resistance."})
   (sql/insert! @db-config :posts {:title "Star Wars: Episode III - Revenge of the Sith" :body "Three years after the onset of the Clone Wars; the noble Jedi Knights are spread out across the galaxy leading a massive clone army in the war against the Separatists. After Chancellor Palpatine is kidnapped, Jedi Master Obi-Wan Kenobi and his former Padawan, Anakin Skywalker, are dispatched to eliminate the evil General Grievous. Meanwhile, Anakin's friendship with the Chancellor arouses suspicion in the Jedi Order, and dangerous to the Jedi Knight himself. When the sinister Sith Lord, Darth Sidious, unveils a plot to take over the galaxy, the fate of Anakin, the Jedi order, and the entire galaxy is at stake. Upon his return, Anakin Skywalker's wife Padme Amidala is pregnant, but he is having visions of her dying in childbirth. Anakin Skywalker ultimately turns his back on the Jedi, thus completing his journey to the dark side and his transformation into Darth Vader. Obi-Wan Kenobi must face his former apprentice in a ferocious lightsaber duel on the fiery world of Mustafar."
   })
   (sql/insert! @db-config :users  {:user_name "root" :user_role "7" :password "21232f297a57a5a743894a0e4a801fc3" :theme_id "1"})
   (sql/insert! @db-config :themes {:theme_name "santorini"})
   (sql/insert! @db-config :themes {:theme_name "napa"}))


(defn convert-inform-to-edn
    "Converting a Map to EDN"
    [dbname dbaddress dbuser dbpassword]
    (let [default-config {
        :dbaddress dbaddress
        :dbuser dbuser
        :dbpassword dbpassword
        :dbname dbname
        :themeid 1}]
        (spit (str "configs/config.edn") default-config)))

(defroutes installation-routes
    (GET "/install" [req]
        (if (check-config)
            (redirect "/")
            (installation-step-1)))

  (POST "/install" [dbname dbaddress dbuser dbpassword :as req]
    (let [condition (atom {:key "Installation | Connected"})]
      (do (if (try (do (sql/query ((set-db-config dbname dbaddress dbuser dbpassword)
                        @db-config @db-config) ["select version();"]) true)
              (catch Exception e  (do (cond (re-find #"Communications link failure" (str e))  (swap! condition (fn [cur] (merge cur {:key "Installation | Host Is Not Accessible"})))
                                            (re-find #"Unknown database" (str e))             (swap! condition (fn [cur] (merge cur {:key "Installation | Unknown Database"})))
                                            (re-find #"Access denied" (str e))                (swap! condition (fn [cur] (merge cur {:key "Installation | Access Denied"})))
                                            :else                                             (swap! condition (fn [cur] (merge cur {:key "Installation | Unknown Error"})))) false)))
              (do
                (convert-inform-to-edn dbname dbaddress dbuser dbpassword)
                (create-db-structure dbname dbaddress dbuser dbpassword)
                (json/write-str {:message (get @condition :key)}))
              (do
                (json/write-str {:message (get @condition :key)}))))))

    (route/resources "/"))
