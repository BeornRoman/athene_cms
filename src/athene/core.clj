(ns athene.core
    (:require [clojure.java.io :as io]))

(defn check-config
    []
    (.exists (io/as-file "configs/config.edn")))

(defn check-config-dir
    []
    (.exists (io/as-file "configs/")))

(defn make-config-dir []
    (.mkdir (java.io.File. "configs/")))

(defn delete-configs [] (do
    (io/delete-file "configs/config.edn")))