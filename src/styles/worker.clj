(ns styles.worker
  (:require
   [styles.colors :as colors]
   [styles.fonts :as fonts]
   [styles.buttons :as buttons]
   [styles.flash :as flash]
   [styles.helpers :as helpers]
   [styles.forms :as forms]
   [styles.pure :as pure]
   [garden.def :refer [defstyles]]
   [garden.color :as color :refer [hsl rgb]]
   [garden.arithmetic :as css-arith]
   [garden.units :refer [px]]))

(def all
  [
   [:.worker-logs
      [:form
       [:.pure-button {:margin "5px"}]]
      [:.navigation {:float "right" }
       [:.pure-button {:margin "5px"}]]
    ]
  ])
