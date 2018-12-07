(defproject athene "0.0.1"
  :description "Athene CMS"
  :url "http://navigator-hc.ru"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.6.1"]
                 [ring/ring-defaults "0.3.0"]
                 [ring/ring-servlet "1.2.0-RC1"]
                 [compojure "1.6.0"]
                 [org.clojure/java.jdbc "0.7.0-alpha3"]
                 [java-jdbc/dsl "0.1.3"]
                 [mysql/mysql-connector-java "5.1.18"]
                 [clj-jade "0.1.7"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-json "0.4.0"]]
  :plugins [[lein-ring "0.12.0"]]
  :min-lein-version "2.0.0"
  :ring {
         :handler athene.main/app
         :open-browser? false
        ;  :auto-reload? false
  }
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
