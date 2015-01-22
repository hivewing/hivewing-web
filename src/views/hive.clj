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

    [:a.pure-button.pure-button-primary
     {:href (paths/hive-processing-new-choose-stage-path (:uuid hive))} "New Stage"]

    [:table.pure-table.pure-table-striped-horizontal.pure-table-striped
      [:thead
        [:tr
          [:td "Type" ]
          [:td "Parameters"]
          [:td "&nbsp;"]
         ]
       ]

      [:tbody
       (if (empty? processing-stages)
         [:tr.center [:td {:colspan 3} [:h3 "No Stages"]]])
       (map #(vector
               :tr
               [:td (:stage_type %)]
               [:td (str (:params %) )]
               [:td
                [:form {:action (paths/hive-processing-delete-stage-path (:uuid hive) (:uuid %))
                        :method "post"}
                 (helpers/anti-forgery-field)
                 [:button.pure-button "Delete"]]]
               ) processing-stages)
      ]
    ]
  ])

(defn processing-new-choose-stage
  [req hive stage-specs]
  [:div
    [:h1 "New Hive Processing Stage"]

    [:table.pure-table.pure-table-striped-horizontal.pure-table-striped
      [:thead
        [:tr
         [:td "Name"]
         [:td "Description"]
         [:td "&nbsp;"]
        ]
      ]
      [:tbody
        (map #(vector :tr
                      [:td (:type %)]
                      [:td (:description %)]
                      [:td
                        [:a.pure-button {:href (paths/hive-processing-new-stage-path (:uuid hive) (name (:type %)))}
                          "Create"]])  (sort-by :type stage-specs))
      ]
    ]
  ])

(defn processing-stage-field
  [field-name spec]

  (let [[field-type field-desc field-details]  (get-in spec [:params field-name])
        post-name (str "stage[" (name field-name) "]")]
    (case field-type
      :email
        [:div
          [:label (name field-name)]
          [:input {:placeholder field-desc :required true :type :email :name post-name}]
        ]
      :url
        [:div
          [:label (name field-name)]
          [:input {:placeholder field-desc :required true :type :url :name post-name}]
        ]
      :string
        [:div
          [:label (name field-name)]
          [:input {:placeholder field-desc :required true :type :string :name post-name}]
        ]
      :integer
        [:div
          [:label (name field-name)]
          [:input {:placeholder field-desc :required true :type :number :name post-name}]
        ]
      :data-stream
        [:div
          [:label (name field-name)]
          [:select {:name (str post-name "[source]")}
           (map #(vector :option {:value %} %) ["worker" "hive"])
           ]
          [:input {:placeholder field-desc :required true :type :string :name (str post-name "[data-key]")}]
        ]
      :enum
        [:div
          [:label (name field-name)]
          [:select {:name post-name}
            (map #(vector :option {:value (name %)} (name %)) field-details)
           ]
        ]
      )))

(defn processing-new-stage
  [req hive hive-stage-spec]
  [:div
    [:h1 "Create " (name (:type hive-stage-spec)) " Stage"]
    [:p (:description hive-stage-spec)]
    (let [param-fields (:params hive-stage-spec)
          input        (:in param-fields)
          ]
      [:form.pure-form.pure-form-stacked {:method "post"
                                          :action (paths/hive-processing-create-stage-path (:uuid hive) (name (:type hive-stage-spec))) }
        (helpers/anti-forgery-field)
        [:fieldset
          (map #(processing-stage-field % hive-stage-spec)
               ;; Sort :in to the top
               (sort-by #(if (= :in %) "a" (str "z" %)) (keys param-fields)))
        ]
        [:button.pure-button.pure-button-primary {:type :submit} "Create"]
       ]
      )
    ])
