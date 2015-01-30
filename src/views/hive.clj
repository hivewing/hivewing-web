(ns views.hive
  (:require [views.helpers :as helpers]
            [hivewing-web.paths :as paths])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn hive-image-url
  [hive-uuid]
    (str "git@images.hivewing.io/" hive-uuid ".git"))

(defn status
  [req hive workers can-manage? system-worker-logs]
  [:div.container-fluid.hive-status
    [:div.row
      [:h2 "Info"]
      [:dl.dl-horizontal
        [:dt "Hive Image Repository"]
        [:dd "Push updates to this repository - and they will be deployed to all workers"]
        [:dd [:pre (hive-image-url (:uuid hive)) ]]
        [:dt "Hive Image Branch"]
        [:dd "When you push an image to the repository for this hive.  Push it to this branch."]
        [:dd [:pre (:image_branch hive)]]
      ]]

    [:div.row
      [:h2 "Workers"]

      [:ul.list-group
        (map
          #(vector :li.list-group-item [:a {:href (paths/worker-path (:uuid hive) (:uuid %))} (:name %)])
          workers)]
    ]

    [:div.row.margin-top-row
      [:h2 "System Logs"]
      [:ul.list-group
        (if (empty? system-worker-logs)
          [:li.list-group-item.text-center [:span "No system logs available"]])

        (map #(vector :li.list-group-item [:span.col-sm-4 (:at %)] [:span.col-sm-8 (:message %)])
             system-worker-logs)
      ]
    ]
  ])

(defn manage
  [req hive]
  [:div.container-fluid
    [:div.row
      [:h2 "Update Hive"]
      [:form {:method "post"}
        (helpers/anti-forgery-field)
        [:div.form-group
          [:label "Image Branch"]
          [:input.form-control {:type :text :value (:image_branch hive) :pattern "{1,120}" :name :hive-manage-image-branch}]
        ]

        [:div.form-group
          [:label "Name"]
          [:input.form-control {:type :text :value (:name hive) :pattern "{1,120}" :name :hive-manage-name}]
        ]
        [:div.form-group.spaced
          [:button.btn.btn-primary  {:type :submit} "Save"]
        ]
      ]
    ]])



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
  [:div.container-fluid
    [:div.row
      [:h2 "Hive Data"]
      [:p "This data is not associated with a given worker but with an entire hive.
        Data in this collection only comes from data-processing pipelines"]
    ]
    [:div.row
      [:table
        [:tbody
         (if (empty? data-keys)
           [:tr.center [:h3 "No Data"]])
          (map #(render-data-row req hive % )  (sort-by first (map identity data-keys)))
        ]
      ]
    ]
  ])

(defn processing
  [req hive processing-stages]
  [:div.container-fluid
    [:a.btn.btn-primary.pull-right
     {:href (paths/hive-processing-new-choose-stage-path (:uuid hive))} "New Stage"]
    [:div.row
      [:h2 "Hive Processing Stages"]
      [:p "These are the processing stages associated with this hive"]
    ]

    [:div.row
      [:ul.list-group
        (map #(vector :li.list-group-item.clearfix
                      [:span.pull-right
                       [:form {:action (paths/hive-processing-delete-stage-path (:uuid hive) (:uuid %))
                          :method "post"}
                           (helpers/anti-forgery-field)
                           [:button.pure-button "Delete"]]]
                      [:span.lead [:strong (:stage_type %)]]
                      [:br]
                      [:span [:pre (pr-str (:params %)) ]])
                 processing-stages)
         (if (empty? processing-stages)
           [:li.list-group-item "No Stages"])
      ]
    ]
  ])

(defn processing-new-choose-stage
  [req hive stage-specs]
  [:div.container-fluid
    [:div.row
      [:h2 "New Hive Processing Stage"]
    ]

    [:div.row
      [:ul.list-group
        (map #(vector :li.list-group-item.clearfix
                      [:span.pull-right [:a.btn.btn-primary {:href (paths/hive-processing-new-stage-path (:uuid hive) (name (:type %)))} "Create"]]
                      [:span.lead [:strong (:type %)]]
                      [:br]
                      [:span (:description %)])
             (sort-by :type stage-specs))
      ]
    ]
  ])

(defn processing-stage-field
  [field-name spec]

  (let [[field-type field-desc field-details]  (get-in spec [:params field-name])
        post-name (str "stage[" (name field-name) "]")]
    (case field-type
      :email
        [:div.form-group
          [:label (name field-name)]
          [:input.form-control {:placeholder field-desc :required true :type :email :name post-name}]
        ]
      :url
        [:div.form-group
          [:label (name field-name)]
          [:input.form-control {:placeholder field-desc :required true :type :url :name post-name}]
        ]
      :string
        [:div.form-group
          [:label (name field-name)]
          [:input.form-control {:placeholder field-desc :required true :type :string :name post-name}]
        ]
      :integer
        [:div.form-group
          [:label (name field-name)]
          [:input.form-control {:placeholder field-desc :required true :type :number :name post-name}]
        ]
      :data-stream
        [:div.form-group
          [:label (name field-name)]
          [:select.form-control {:name (str post-name "[source]")}
           (map #(vector :option {:value %} %) ["worker" "hive"])
           ]
          [:input.form-control {:placeholder field-desc :required true :type :string :name (str post-name "[data-key]")}]
        ]
      :enum
        [:div.form-group
          [:label (name field-name)]
          [:select.form-control {:name post-name}
            (map #(vector :option {:value (name %)} (name %)) field-details)
           ]
        ]
      )))

(defn processing-new-stage
  [req hive hive-stage-spec]
  [:div.container-fluid
    [:div.row
      [:h2 "Create " (name (:type hive-stage-spec)) " Stage"]
    ]
    [:div.row
      [:p (:description hive-stage-spec)]
    ]
    [:div.row
      (let [param-fields (:params hive-stage-spec)
            input        (:in param-fields)
            ]
        [:form {:method "post" :action (paths/hive-processing-create-stage-path (:uuid hive) (name (:type hive-stage-spec))) }
          (helpers/anti-forgery-field)
          (map #(processing-stage-field % hive-stage-spec)
                 ;; Sort :in to the top
                 (sort-by #(if (= :in %) "a" (str "z" %)) (keys param-fields)))

            [:div.form-group.spaced
              [:button.btn.btn-primary {:type :submit} "Create"]
            ]
         ]
        )
      ]
    ])
