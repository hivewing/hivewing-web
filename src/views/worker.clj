(ns views.worker
  (:require [views.helpers :as helpers]
            [hivewing-web.paths :as paths])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn side-menu
  [req current-page ]
  (let [wu (:worker-uuid (:params req))
        hu (:hive-uuid (:params req))]
    (vector
      {:href (paths/worker-path hu wu)
        :selected? (= current-page :status)
        :text "Status"}
      {:href (paths/worker-manage-path hu wu)
        :selected? (= current-page :manage)
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

(defn status [req]
  [:div.text-center
    [:h1 "Worker status"]])

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
