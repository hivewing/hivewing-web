(ns styles.helpers
   (:require [styles.colors :as colors]))

(def all
  [
   [:.describe {:font-size "smaller" :font-style "italic"  :margin-top "-10px"}]
   [:pre {:font-family "Consolas, Courier, monospace"
          :color "#333"
         :background "rgb(250, 250, 250)"}]
   [:.center { :text-align "center"} ]
   [:.right  { :text-align "right" } ]
   [:.left  { :text-align "left" } ]
   [:.f-r {:float :right}]
   [:.f-l {:float :left}]
  ])
