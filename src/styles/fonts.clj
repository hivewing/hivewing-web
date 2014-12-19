(ns styles.fonts
  (:require [garden.units :refer [px]]))

(defn header-text
  []
  {:font-weight 100
   :font-size (px 18)
   :font-family "Roboto Slab"})
