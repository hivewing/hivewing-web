(ns views.apiary
  (:require [views.helpers :as helpers])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn index []
  [:div.text-center
    [:h1 "Your Apiaries!" ]])

(defn join
  [action hives worker-url]
  [:form {:method "POST" :action action}
   (helpers/anti-forgery-field)
   [:input {:type :hidden :name :worker-url :value worker-url}]
   [:div
      [:div.input-field
        [:label "Select the hive"]
        [:select {:name "hive-uuid"}
          (map #(vector :option {:value (:uuid %)} (:name %)) hives)]
      ]
      [:div.controls
        [:button {:type "submit"} "Add Worker To Hive"]
      ]
    ]
  ])
