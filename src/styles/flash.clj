(ns styles.flash
   (:require [styles.colors :as colors]))

(def all
  [:div.flash
    {
      :width "100%"
      :line-height "30px"
      :font-size "15px"
      :font-family "Roboto Slab"
      :text-align "center"
      :background "white"
      :color colors/dark-text
      :border-bottom (str "2px solid " colors/solid-background)
     }
   ]
  )
