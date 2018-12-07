(ns athene.installation.layout
  (:require [clj-jade.core :as jade]
    [athene.core :as core]))

(defn installation-step-1 []
  "Step 1 for installation"
  (do (if (not (core/check-config-dir))
          (core/make-config-dir))
      (jade/render "/installation/install-step-1.jade")))