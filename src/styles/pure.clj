(ns styles.pure
   (:require [styles.colors :as colors]))

(def themed
  [
    [:.pure-button-primary :.pure-button-selected
      :a.pure-button-primary :a.pure-button-selected
     { :background-color colors/primary
       :color "#fff"}]
    [:.pure-menu
     [:li:hover "li a:hover" :li.pure-menu-selected
      {:background-color colors/primary }
     ]]
  ]
  )
