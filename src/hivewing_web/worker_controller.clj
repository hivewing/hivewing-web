(ns hivewing-web.worker-controller
  (:require [views.layout :as layout]
            [hivewing-web.controller-core :refer :all]
            [hivewing-core.hive :as hive]
            [hivewing-core.worker :as worker]
            [ring.util.response :as r]
            [hivewing-web.paths :as paths]

            [views.worker :as worker-views]
     ))

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

(defn status
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-resources req [hive (hive/hive-get hive-uuid)
                     worker (and (worker/worker-in-hive? worker-uuid hive-uuid)
                                 (worker/worker-get worker-uuid))]
      (-> (r/response
          (layout/render req
                         (worker-views/status req )
                         :style :side-menu
                         :side-menu (side-menu req :status)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"}))
        (r/header "Content-Type" "text/html; charset=utf-8")))))

(defn manage
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-resources req [hive (hive/hive-get hive-uuid)
                     worker (and (worker/worker-in-hive? worker-uuid hive-uuid)
                                 (worker/worker-get worker-uuid))]
      (-> (r/response
          (layout/render req
                         (worker-views/manage req )
                         :style :side-menu
                         :side-menu (side-menu req :manage)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"}))
        (r/header "Content-Type" "text/html; charset=utf-8")))))

(defn config
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-resources req [hive (hive/hive-get hive-uuid)
                     worker (and (worker/worker-in-hive? worker-uuid hive-uuid)
                                 (worker/worker-get worker-uuid))]
      (-> (r/response
          (layout/render req
                         (worker-views/config req )
                         :style :side-menu
                         :side-menu (side-menu req :config)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"}))
        (r/header "Content-Type" "text/html; charset=utf-8")))))

(defn data
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-resources req [hive (hive/hive-get hive-uuid)
                     worker (and (worker/worker-in-hive? worker-uuid hive-uuid)
                                 (worker/worker-get worker-uuid))]
      (-> (r/response
          (layout/render req
                         (worker-views/data req )
                         :style :side-menu
                         :side-menu (side-menu req :data)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"}))
        (r/header "Content-Type" "text/html; charset=utf-8")))))

(defn logs
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-resources req [hive (hive/hive-get hive-uuid)
                     worker (and (worker/worker-in-hive? worker-uuid hive-uuid)
                                 (worker/worker-get worker-uuid))]
      (-> (r/response
          (layout/render req
                         (worker-views/logs req )
                         :style :side-menu
                         :side-menu (side-menu req :logs)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"}))
        (r/header "Content-Type" "text/html; charset=utf-8")))))
