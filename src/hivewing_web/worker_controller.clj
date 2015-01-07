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
                               worker-config   (worker-config/worker-config-get worker-uuid :include-system-keys true)
                               tasks   (or (worker-config/worker-config-get-tasks worker-uuid) [])
                               ]

        (let [can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (-> (r/response
              (layout/render req
                             (views/status req hive worker worker-config tasks)
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

(defn create-config
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid worker-config-key worker-config-value worker-config-task]
      (with-preconditions req [hive (hive/hive-get hive-uuid)
                               in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker (worker/worker-get worker-uuid)
                               can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]

        (do
          (worker-config/worker-config-set worker-uuid (hash-map (clojure.string/join "." (vector worker-config-task worker-config-key) ) worker-config-value))
          (r/redirect (paths/worker-config-path hive-uuid worker-uuid)))))))

(defn update-config
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid worker-config-key worker-config-value]
      (with-preconditions req [hive (hive/hive-get hive-uuid)
                               in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker (worker/worker-get worker-uuid)
                               can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]
        (do
          (worker-config/worker-config-set worker-uuid (hash-map worker-config-key worker-config-value))
          (r/redirect (paths/worker-config-path hive-uuid worker-uuid)))))))

(defn delete-config
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid worker-config-key]
      (with-preconditions req [hive (hive/hive-get hive-uuid)
                               in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker (worker/worker-get worker-uuid)
                               can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]

        (do
          (worker-config/worker-config-set worker-uuid (hash-map worker-config-key nil))
          (r/redirect (paths/worker-config-path hive-uuid worker-uuid)))))))

(defn config
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid]
      (with-preconditions req [hive (hive/hive-get hive-uuid)
                               in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker (worker/worker-get worker-uuid)
                               worker-config   (worker-config/worker-config-get worker-uuid)
                               tasks   (or (worker-config/worker-config-get-tasks worker-uuid) [])
                               ]
        (let [can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (render (layout/render req
                         (views/config req hive worker tasks worker-config can-manage? )
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
