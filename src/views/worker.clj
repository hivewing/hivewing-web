(ns views.worker
  (:require [views.helpers :as helpers]
            [clojurewerkz.urly.core :as u]
            [clojure.data.json :as json]
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
          path-str (or (u/path-of url) "unknown/unknown")
          ref (last (clojure.string/split path-str #"\/"))]
      ref)))

(defn worker-tasks-to-array
  [worker-config]
    (let [tasks-str (get ".tasks" worker-config)]
      (if (clojure.string/blank? tasks-str)
        []
        (json/read-str tasks-str))))

(defn status [req hive worker worker-config]
  (let [tasks (worker-tasks-to-array worker-config)]
    [:div
      [:h1 "Status"]

      [:div.data-listing
        [:h3 "Name"]
        [:span (:name worker)]
        [:h3 "Hive Image Ref"
          [:span.header-sub "configured / current"]]
        [:span (hive-image-ref (get worker-config ".hive-image" ))]
        [:span "  /  "]
        [:span (hive-image-ref (get worker-config ".hive-image-current" ))]
        [:h3 "Tasks"]
        [:table.pure-table.pure-u-1-1
          (if (empty? tasks)
            [:tr [:td "None"]]
            (map #(vector :tr [:td %]) tasks))
         ]
       ]]))

(defn manage [req]
  [:div.text-center
    [:h1 "Worker manage"]])

(defn config [req]
  [:div.text-center
    [:h1 "Worker config"]])

(defn data [req]
  [:div.text-center
    [:h1 "Worker data"]])

(defn logs [req]
  [:div.text-center
    [:h1 "Worker logs"]])
