(ns styles.application
 (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px]]))

(defstyles all
  [:body
   {:font-family "sans-serif"
    :font-size (px 16)
    :line-height 1.5}
   ])
