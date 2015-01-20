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
      {:href (paths/hive-data-path hu)
       :selected? (= current-page :data)
        :text "Data"
        :disabled? (not can-manage?)}
      {:href (paths/hive-processing-path hu)
       :selected? (= current-page :processing)
        :text "Processing"
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



(defn render-data-value-row
  [req hive data-name value]
    [:tr
      [:td (:at value)]
      [:td (:data value)]])

(defn show-data-values
  [req hive data-name data-values]
    [:div
     [:h1 (str data-name " Data")]
     [:table.pure-table.pure-table-striped-horizontal.pure-table-striped
        [:thead
          [:tr
            [:th "At"]
            [:th "Value"]
            ]]
        [:tbody
          (map #(render-data-value-row req hive data-name % )  (sort-by first (map identity data-values)))
        ]
      ]
    ]
  )
(defn render-data-row
  [req hive data-name]
    [:tr
      [:td
         [:a {:href (paths/hive-data-value-path (:uuid hive) data-name)}
          data-name]]])

(defn data
  [req hive data-keys]
  [:div
    [:h1 "Hive Data"]
    [:p "This data is not associated with a given worker but with an entire hive.
        Data in this collection only comes from data-processing pipelines"]

    [:table.pure-table.pure-table-striped-horizontal.pure-table-striped
      [:tbody
       (if (empty? data-keys)
         [:tr.center [:h3 "No Data"]])
        (map #(render-data-row req hive % )  (sort-by first (map identity data-keys)))
      ]
    ]
  ])

(defn processing
  [req hive processing-stages]
  [:div
    [:h1 "Hive Processing Stages"]
    [:p "These are the processing stages associated with this hive"]

    [:table.pure-table.pure-table-striped-horizontal.pure-table-striped
      [:tbody
       (if (empty? processing-stages)
         [:tr.center [:h3 "No Stages"]])
        (map #(vector :tr %)  (sort-by :created_at processing-stages))
      ]
    ]
  ])
