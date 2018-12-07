(ns athene.admin.layout
     (:require [clj-jade.core :as jade]
               [athene.config]))

(defn admin-signup []
    (jade/render "/admin/admin_signup.jade"))

(defn admin-login []
    (jade/render "/admin/admin_signup0.jade"))

(defn admin-panel []
    (jade/render "/admin/index_admin_panel.jade"))

(defn admin-delete-posts [posts]
    (jade/render "/admin/index_admin_delete.jade" {"posts" posts}))

(defn admin-create-posts [req1]
    (jade/render "/admin/index_admin_create.jade"))

(defn roles [roles users]
    (jade/render "/admin/index_admin_roles.jade" {"roles" roles "users" users}))

(defn themes [theme_url themes users]
    (jade/render "/admin/index_admin_themes.jade" {"theme_url" theme_url "theme" themes "users" users}))
