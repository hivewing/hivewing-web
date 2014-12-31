(ns hivewing-web.worker-controller
  (:require [views.layout :as layout]
            [hivewing-web.controller-core :refer :all]
            [hivewing-core.hive :as hive]
            [hivewing-core.worker :as worker]
            [hivewing-core.worker-config :as worker-config]
            [ring.util.response :as r]
            [hivewing-web.paths :as paths]
            [views.worker :as views]))

(defn status
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid]
      (with-preconditions req [can-read-hive?  (hive/hive-can-read? (:uuid bk) hive-uuid)
                               hive            (hive/hive-get hive-uuid)
                               worker-in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker          (worker/worker-get worker-uuid)
                               worker-config   (worker-config/worker-config-get worker-uuid :include-system-keys true)]

        (let [can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (-> (r/response
              (layout/render req
                             (views/status req hive worker worker-config)
                             :style :side-menu
                             :side-menu (views/side-menu req :status can-manage?)
                             :back-link { :href (paths/hive-path hive-uuid)
                                          :text "Hive"}))
            (r/header "Content-Type" "text/html; charset=utf-8")))))))

(defn manage
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid]
      (with-preconditions req [hive (hive/hive-get hive-uuid)
                               in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker (worker/worker-get worker-uuid)]
        (let [can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (render (layout/render req
                           (views/manage req )
                           :style :side-menu
                           :side-menu (views/side-menu req :manage can-manage?)
                           :back-link { :href (paths/hive-path hive-uuid)
                                        :text "Hive"})))))))

(defn config
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid]
      (with-preconditions req [hive (hive/hive-get hive-uuid)
                               in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker (worker/worker-get worker-uuid)]
        (let [can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (render (layout/render req
                         (views/config req )
                         :style :side-menu
                         :side-menu (views/side-menu req :config can-manage?)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"})))))))
(defn data
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid]
      (with-preconditions req [hive (hive/hive-get hive-uuid)
                               in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker (worker/worker-get worker-uuid)]
        (let [can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]
        (render (layout/render req
                         (views/data req )
                         :style :side-menu
                         :side-menu (views/side-menu req :data can-manage?)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"})))))))
(defn logs
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid]
      (with-preconditions req [hive (hive/hive-get hive-uuid)
                               in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker (worker/worker-get worker-uuid)]

        (let [can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (render (layout/render req
                         (views/logs req )
                         :style :side-menu
                         :side-menu (views/side-menu req :logs can-manage?)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"})))))))
