(ns views.hive
  (:require [views.helpers :as helpers]
            [hivewing-web.paths :as paths])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn side-menu
  [req current-page can-manage?]
  (let [hu (:hive-uuid (:params req))]
    (vector
      {:href (paths/hive-path hu)
       :selected? (= current-page :status)
       :text "Status"}
      {:href (paths/hive-manage-path hu)
       :selected? (= current-page :manage)
        :text "Manage"
        :disabled? (not can-manage?)}
      )))

(defn status
  [req hive workers can-manage?]
  [:div
    [:h1 (:name hive) ]
    [:ul
      (map #(vector :li [:a {:href (paths/worker-path (:uuid hive) (:uuid %))} (:name %)]) workers)]
    ])

(defn manage
  [req hive]
  [:div
    [:h1 (str "Manage " (:name hive)) ]
    ])
