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
(defn hive-image-url
  [hive-uuid]
    (str "git@images.hivewing.io/" hive-uuid ".git"))

(defn status
  [req hive workers can-manage? system-worker-logs]
  [:div
    [:h1 (:name hive) ]
    [:h3 "Hive Image Repository"]
    [:p.describe "Push updates to this repository - and they will be deployed to all workers"]
    [:pre (hive-image-url (:uuid hive)) ]

    [:h3 "Hive Image Branch"]
      [:p.describe
         "When you push an image to the repository for this hive.  Push it to this branch."]
    [:pre (:image_branch hive)]

    [:h3 "Workers"]
    [:table.pure-table
      (map
        #(vector :tr
                  [:td [:a {:href (paths/worker-path (:uuid hive) (:uuid %))} (:name %)]])
        workers)]

    [:h3 "System Logs"]
    [:table.pure-table.pure-table-horozontal.pure-table-striped.logs
      (map #(vector :tr [:td (:at %)] [:td (:message %)]) system-worker-logs)
     ]
  ])

(defn manage
  [req hive]
  [:div
    [:h1 "Manage"]
    [:form.pure-form.pure-form-stacked {:method "post"}
      (helpers/anti-forgery-field)
      [:label "Image Branch"]
      [:input {:type :text :value (:image_branch hive) :pattern "{1,120}" :name :hive-manage-image-branch}]

      [:label "Name"]
      [:input {:type :text :value (:name hive) :pattern "{1,120}" :name :hive-manage-name}]
      [:input.pure-button.pure-button-primary {:value "Save" :type :submit}]
    ]

    ])
