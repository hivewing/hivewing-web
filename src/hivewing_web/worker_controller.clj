(ns hivewing-web.worker-controller
  (:require [views.layout :as layout]
            [hivewing-web.controller-core :refer :all]
            [hivewing-core.hive :as hive]
            [hivewing-core.worker :as worker]
            [ring.util.response :as r]
            [hivewing-web.paths :as paths]
            [views.worker :as views]))

(defn status
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-preconditions req [hive (hive/hive-get hive-uuid)
                             in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                             worker (worker/worker-get worker-uuid)]
      (-> (r/response
          (layout/render req
                         (views/status req )
                         :style :side-menu
                         :side-menu (views/side-menu req :status)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"}))
        (r/header "Content-Type" "text/html; charset=utf-8")))))

(defn manage
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-preconditions req [hive (hive/hive-get hive-uuid)
                             in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                             worker (worker/worker-get worker-uuid)]
      (render (layout/render req
                         (views/manage req )
                         :style :side-menu
                         :side-menu (views/side-menu req :manage)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"})))))

(defn config
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-preconditions req [hive (hive/hive-get hive-uuid)
                             in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                             worker (worker/worker-get worker-uuid)]
      (render (layout/render req
                         (views/config req )
                         :style :side-menu
                         :side-menu (views/side-menu req :config)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"})))))
(defn data
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-preconditions req [hive (hive/hive-get hive-uuid)
                             in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                             worker (worker/worker-get worker-uuid)]
      (render (layout/render req
                         (views/data req )
                         :style :side-menu
                         :side-menu (views/side-menu req :data)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"})))))
(defn logs
  [req & args]
  (with-required-parameters req [hive-uuid worker-uuid]
    (with-preconditions req [hive (hive/hive-get hive-uuid)
                             in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                             worker (worker/worker-get worker-uuid)]

      (render (layout/render req
                         (views/logs req )
                         :style :side-menu
                         :side-menu (views/side-menu req :logs)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"})))))
