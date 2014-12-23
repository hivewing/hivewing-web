(ns styles.forms
  (:require
    [styles.colors :as colors ]
    [garden.def :refer [defrule defkeyframes]]
    [garden.units :refer [px]]))

;(defkeyframes inputHighlighter
;  [:from  { :background "purple" }
;   :to    { :width 0, :background "transparent"}])

(def form-inputs
  [:form
    {:padding (px 10)
     :border (str "1px solid " colors/secondary)
     :margin (px 10)
     }
    [:input {
              :line-height (px 16)
              :font-size (px 16)
              :font-family "Roboto Slab"
              :color colors/input-text
             }]
   ])
