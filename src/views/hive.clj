(ns views.hive
  (:require [views.helpers :as helpers]
            [hivewing-web.paths :as paths])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn show
  [req hive workers]
  [:div
    [:h1 (:name hive) ]
    [:ul
      (map #(vector :li [:a {:href (paths/worker-path (:uuid hive) (:uuid %))} (:name %)]) workers)]
    ])
