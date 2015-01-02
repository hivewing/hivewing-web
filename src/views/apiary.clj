(ns views.apiary
  (:require [views.helpers :as helpers]
            [hivewing-web.paths :as paths])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn side-menu
  [req current-page]
  (vector
    {:href (paths/apiary-path)
     :selected? (= current-page :status)
     :text "Apiary"}
    {:href (paths/apiary-manage-path)
     :selected? (= current-page :manage)
      :text "Manage"}
    ))


(defn status
  [req hives]
  [:div.text-center
    [:h1 "Apiary"]
    [:h2 "Hives"]
    [:table.pure-table
;      [:thead
;        [:tr
;          [:td {:colspan 2} "Hives" ]]]
      (map
        #(vector :tr
                  [:td (:uuid %) ]
                  [:td [:a {:href (paths/hive-path (:uuid %))} (:name %)]])
        hives)
      ]])

(defn manage
  [req]
  [:div.text-center
    [:h1 "Manage Your Apiary!" ]
    ])


(defn join
  [action hives worker-url]
  [:form.pure-form.pure-form-stacked {:method "POST" :action action}
   [:h1 "Create a Worker"]
   (helpers/anti-forgery-field)
   [:input {:type :hidden :name :worker-url :value worker-url}]
   [:label "Worker Name"]
   [:input {:type :text :name :worker-name :placeholder "Optional"}]
   [:label "Select the hive"]
   [:select {:name "hive-uuid"} (map #(vector :option {:value (:uuid %)} (:name %)) hives)]
   [:button.pure-button.pure-button-primary {:type "submit"} "Add Worker To Hive"]
  ])
