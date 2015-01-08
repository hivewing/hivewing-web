(ns views.worker
  (:require [views.helpers :as helpers]
            [clojurewerkz.urly.core :as u]
            [clojure.data.json :as json]
            [ring.util.request :as ring-request]
            [hivewing-web.controller-core :as controller-core]
            [clj-time.coerce :as ctimec]
            [ring.util.codec :as ring-codec]
            [hivewing-web.paths :as paths])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn side-menu
  [req current-page can-manage?]
  (let [wu (:worker-uuid (:params req))
        hu (:hive-uuid (:params req))]
    (vector
      {:href (paths/worker-path hu wu)
        :selected? (= current-page :status)
        :text "Status"}
      {:href (paths/worker-manage-path hu wu)
        :selected? (= current-page :manage)
        :disabled? (not can-manage?)
        :text "Manage"}
      {:href (paths/worker-config-path hu wu)
        :selected? (= current-page :config)
        :text "Config"}
      {:href (paths/worker-data-path hu wu)
        :selected? (= current-page :data)
        :text "Data"}
      {:href (paths/worker-logs-path hu wu)
        :selected? (= current-page :logs)
        :text "Logs"}
      )))

(defn hive-image-ref [image-url]
  (if (empty? image-url)
    "unknown"
    (let [url (u/url-like image-url)
          path-str (or (and url (u/path-of url)) "unknown/unknown")
          ref (last (clojure.string/split path-str #"\/"))]
      ref)))

(defn worker-tasks-to-array
  [worker-config]
    (let [tasks-str (get ".tasks" worker-config)]
      (if (clojure.string/blank? tasks-str)
        []
        (json/read-str tasks-str))))

(defn status [req hive worker worker-config tasks system-worker-logs worker-task-tracing]
  [:div
    [:h1 "Status"]

    [:div.data-listing
      [:table.pure-table.f-r
        [:thead
          [:tr
            [:th "Task"]
            [:th "State"]
            [:th "Tracing"]
           ]
        ]
        (if (empty? tasks)
          [:tr [:td.center {:colspan 3} "None"]]
          (map #(vector :tr [:td (key %)]
                            [:td (val %)]
                            [:td (if (get worker-task-tracing (key %)) "On" "Off")]) tasks))
       ]

      [:h3 "Name"]
      [:span worker]
      [:span (:name worker)]
      [:h3 "Hive Image Ref"
        [:span.header-sub "configured / current"]]
      [:span (hive-image-ref (get worker-config ".hive-image" ))]
      [:span "  /  "]
      [:span (hive-image-ref (get worker-config ".hive-image-current" ))]

      [:h3 "System Logs"]
      [:table.pure-table.pure-table-horozontal.pure-table-striped
        (map #(vector :tr [:td (:at %)] [:td (:message %)]) system-worker-logs)
       ]
     ]])

(defn manage [req hive worker tasks worker-task-tracing]
  [:div
    [:h1 "Manage"]
    [:form.pure-form.pure-form-stacked {:method "post"}
      (helpers/anti-forgery-field)
      [:label "Name"]
      [:input {:type :text :value (:name worker) :pattern "{1,120}" :name :worker-manage-name}]
      [:input.pure-button.pure-button-primary {:value "Save" :type :submit}]
    ]

    [:h2 "Worker Events"]
    [:div.pure-g.pure-u-1-1
      [:form {:method "post"}
        (helpers/anti-forgery-field)
        [:div
          [:p [:b "Reboot the worker" ]]
          [:p "This will cause all the tasks on the worker to restart. Configuration is persisted"]
          [:input.pure-button.pure-u-1-4 {:type :submit :name :worker-event-reboot :value "Reboot Worker"}]
        ]
        [:div
          [:p [:b "Reset the worker" ]]
          [:p "This will cause the worker to delete all configuration, stop all tasks, and bootstrap itself again"]
          [:input.pure-button.pure-u-1-4 {:type :submit :name :worker-event-reset :value "Reset Worker"}]
        ]
      ]
    ]

    [:h2 "Task Logging"]
    [:table.pure-table.pure-table-striped.pure-table-horizontal
      [:thead
        [:tr
          [:th "Task"]
          [:th "Tracing"]
         ]
      ]
      (if (empty? tasks)
        [:tr [:td.center {:colspan 3} "None"]]
        (map #(vector :tr [:td (key %)]
                          [:td [:form {:method "post"}
                                (helpers/anti-forgery-field)
                                [:input {:type :hidden :name :worker-task :value (key %)}]
                                [:input.pure-button {:class (if (get worker-task-tracing (key %)) "pure-button-active" "") :type :submit :name :worker-task-tracing :value "On"}]
                                [:input.pure-button {:class (if (get worker-task-tracing (key %)) "" "pure-button-active") :type :submit :name :worker-task-tracing :value "Off"}]]]) tasks))
      ]
    [:hr]
    [:h3 "Delete Worker"]
    [:p "Deleting the worker will remove it from the hive and clear the data from the device.  The device will need to be re-initialized and connected back to hive-wing"]
    [:form.pure-form {:method "post" :action (paths/worker-delete-path (:uuid hive) (:uuid worker))}
      (helpers/anti-forgery-field)
      [:input.pure-u-1-2 {:type :text :required :required :pattern "[pP][lL][eE][aA][sS][eE][ ]+[dD][eE][lL][eE][tT][eE]"
               :placeholder "To confirm type \"Please Delete\""
               :name :worker-delete-confirmation
               :title "To confirm type \"Please Delete\""}]
      [:input.pure-button.button-error.pure-u-1-4 {:type :submit :value "Delete Worker"}]
     ]

    ])

(defn split-config-name
  "Splits a config key into the task and config name."
  [config-key]
  (let [splits (clojure.string/split config-key #"\.")
        cfg-name   (last splits)
        task   (clojure.string/join "." (drop-last 1 splits))
        ]
    [task cfg-name]))

(defn render-config-row
  [[k v] hive worker can-manage?]

  (let [[task name] (split-config-name k)]
    (vector :tr
           [:td task]
           [:td name]
           [:td
              [:form.unpadded
                {:method "post" :action (paths/worker-config-update-path (:uuid hive) (:uuid worker))}
                (helpers/anti-forgery-field)
                [:input {:type :hidden :name :worker-config-key :value k}]
                [:input.pure-u-1-1 {:type :text :name :worker-config-value :value v}]
                [:input.pure-button.pure-u-1-1 {:type :submit :value "Update" :style "display: none"}]
                ]
            ]
           [:td
            (if can-manage?
              (vector :form.unpadded
                      {:method "post" :action (paths/worker-config-delete-path (:uuid hive) (:uuid worker))}
                      (helpers/anti-forgery-field)
                      [:input {:type :hidden :name :worker-config-key :value k}]
                      [:input.pure-button.pure-u-1-1 {:type :submit :value "Delete"}])
              "&nbsp;")])))

(defn config [req hive worker tasks worker-config can-manage?]
  [:div
    [:h1 "Config"]
    [:table.pure-table.pure-table-striped-horizontal.pure-table-striped
      [:thead
        [:tr
          [:th "Task"]
          [:th "Name"]
          [:th "Value"]
          [:th "&nbsp;"]
         ]
      ]
      [:tbody
        (if can-manage?
          [:tr
            [:form.unpadded {:method "post" :action (paths/worker-config-path (:uuid hive) (:uuid worker))}
              (helpers/anti-forgery-field)
              [:td
                 [:select {:name "worker-config-task" :required :required}
                    (map #(vector :option {:value %} %) tasks)
                  ]
              ]
              [:td
                 [:input.pure-u-1-1 {:type :text :required :required :name "worker-config-key" :pattern "[a-zA-Z_\\-0-9]+" :title "Worker config key.  0-9A-Za-z _ - " :placeholder "New Worker Config"}] ]
              [:td
                 [:input.pure-u-1-1 {:type :text :required :required :name "worker-config-value" :placeholder "New Worker Value"}] ]
              [:td
                 [:input.pure-button.pure-button-primary.pure-u-1-1 {:type :submit :value "Create"}] ]
             ]
           ])
        [:tr [:td "&nbsp;"] [:td "&nbsp;"] [:td "&nbsp;"] [:td "&nbsp;"] ]
        (map #(render-config-row % hive worker can-manage?) (sort-by first (map identity worker-config)))
      ]
    ]
  ]
  )

(defn data [req]
  [:div.text-center
    [:h1 "Worker data"]])

(defn render-worker-log
  [log]
    (vector :tr
            [:td (str (:at log))]
            [:td (str (:message log))]))

(comment
  (def current-url "http://localhost:8000/hives/12345678-1234-1234-1234-123456789012/workers/2a04f8e4-96bd-11e4-a761-0242ac11038c/logs?worker-logs-start=1420672137500&worker-logs-task=*ALL*&__anti-forgery-token=UHDJut0UKdeNtR3v4nDOkeWIUk1tnpWA+mOZKc0Dov%252FrYRmHA%252F7nqjPZ+oRo7OFMRRK829EINL7hBE5E")
  (if start-at (controller-core/same-url-with-new-params current-url {:worker-logs-start nil}) nil)
  )


(defn logs [req current-task start-at tasks log-messages]

  (let [current-url (ring-request/request-url req)
        rewind-link (if start-at (controller-core/same-url-with-new-params current-url {:worker-logs-start nil}) nil)
        next-start-at (ctimec/to-long (:at (last log-messages)))
        next-link (controller-core/same-url-with-new-params current-url {:worker-logs-start next-start-at})
        ]
    [:div.worker-logs
      [:h1 "Logs"]

      [:form.pure-form.unpadded.right
        (helpers/anti-forgery-field)
        [:select {:name "worker-logs-task"}
          [:option {:selected (nil? current-task) :value "*ALL*"} "All Tasks"]
          (map #(vector :option {:selected (= current-task %1) :value %1} %1) tasks)
         ]
        [:input.pure-button {:type :submit :value "Filter"}]
      ]
      [:div.left
        [:div.navigation
          (if rewind-link
            [:a.pure-button {:href rewind-link} "Reset"])
          [:a.pure-button {:href next-link} "Next"]
        ]
        [:h3 (str "Logs start from: " start-at)]]

      (if (empty? log-messages)
         [:h3 "No Log Messages"])
      [:table.logs.pure-table.pure-table-striped-horizontal.pure-table-striped.pure-u-1-1
        [:tbody

          (map render-worker-log log-messages)
        ]]]))
