(ns athene.config
    (:require [clj-jade.core :as jade]))

(jade/configure {:template-dir "resources/public/jade/"
                 :pretty-print true
                 :cache? true})