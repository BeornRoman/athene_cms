(ns athene.hiccuppages
    (:require [compojure.core :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [clojure.edn :as edn]
            [clojure.java.io :as io]))
        
; (defn create-post [req]
;     "page to create post"
;       (html
;         [:head
;          [:meta {:charset "UTF-8"}
;           (include-css "bower_components/material-design-lite/material.css")
;           [:body
;            [:div 
;              [:form {:action "/admin-create-posts" :class "getting_form" :method "POST"}
;               [:div {:class "top clearfix"}
;                (anti-forgery-field)
;                [:h3 "Athene-CMS"]
;                 [:h4 "Создайте пост:"]
;                 [:input {:name "title" :type "text" :placeholder "Название"}]
;                 [:textarea {:name "body" :type "text" :placeholder "Текст"}]]
;                [:input {:type "submit" :value "Далее" :class "getin right"}]]]]]]))

; (defn installation-step-1 [req]
;     "registration in AtheneCMS"
;       (html
;         [:head
;          [:meta {:charset "UTF-8"}
;           (include-css "style.css")
;           [:title "registration"]
;           [:body
;            [:div {:class "wraper"}
;             [:div {:class "center"}
;              [:form {:action "installation" :class "getting_form" :method "POST"}
;               [:div {:class "top clearfix"}
;                (anti-forgery-field)
;                [:h3 "Авторизация"]
;                 [:p "Введите данные для доступа на сервер."]
;                 [:input {:name "address" :type "text" :placeholder "Адресс сервера"}]
;                 [:input {:name "user" :type "text" :placeholder "Имя пользователя"}]
;                 [:input {:name "pass" :type "text" :placeholder "Пароль"}]
;                 [:input {:name "namedb" :type "text" :placeholder "Имя базы данных"}]]
;               [:div {:class "buttom clearfix"} 
;                [:a {:href "#" :class "registration left"} "Регистрация"]
;                [:input {:type "submit" :value "Далее" :class "mdl-button"}]]]]]]]]))


