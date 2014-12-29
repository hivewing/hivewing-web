(ns styles.pure
   (:require [styles.colors :as colors]))

(def themed
  [:.pure-button-primary :.pure-button-selected
    :a.pure-button-primary :a.pure-button-selected
   { :background-color colors/primary
     :color "#fff"}]
  )
