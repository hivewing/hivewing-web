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
    [:h2 "Workers"]
    [:table.pure-table
      (map
        #(vector :tr
                  [:td [:a {:href (paths/worker-path (:uuid hive) (:uuid %))} (:name %)]])
        workers)]
  ])

(defn manage
  [req hive]
  [:div
    [:h1 "Manage"]
    [:form.pure-form.pure-form-stacked {:method "post"}
      (helpers/anti-forgery-field)
      [:label "Name"]
      [:input {:type :text :value (:name hive) :pattern "{1,120}" :name :hive-manage-name}]
      [:input.pure-button.pure-button-primary {:value "Save" :type :submit}]
    ]

    ])
