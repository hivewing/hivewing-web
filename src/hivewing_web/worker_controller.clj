(ns hivewing-web.worker-controller
  (:require [views.layout :as layout]
            [hivewing-web.controller-core :refer :all]
            [hivewing-core.hive :as hive]
            [hivewing-core.worker :as worker]
            [hivewing-core.worker-config :as worker-config]
            [hivewing-core.hive-logs :as hive-logs]
            [ring.util.response :as r]
            [clj-time.coerce :as ctimec]
            [clj-time.core :as ctime]
            [hivewing-web.paths :as paths]
            [views.worker :as views]))
(comment
  (def hive-uuid "12345678-1234-1234-1234-123456789012")
  (def worker-uuid "cafe9192-96a4-11e4-933f-0242ac110374")
  (def worker-uuid "2a04f8e4-96bd-11e4-a761-0242ac11038c")
  (hive-logs/hive-logs-read hive-uuid)
  (hive-logs/hive-logs-read hive-uuid :worker-uuid worker-uuid)
  (hive-logs/hive-logs-push hive-uuid worker-uuid nil "System Worker Log Message")
  (doseq [x (range 200)]
    (hive-logs/hive-logs-push hive-uuid worker-uuid "workera" (str x "Worker a Worker Log Message"))
  )
  (ctimec/from-long (read-string "1420659525859"))
  (ctimec/from-long 1420659525859)
  )

(defn status
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid worker-uuid]
      (with-preconditions req [can-read-hive?  (hive/hive-can-read? (:uuid bk) hive-uuid)
                               hive            (hive/hive-get hive-uuid)
                               worker-in-hive? (worker/worker-in-hive? worker-uuid hive-uuid)
                               worker          (worker/worker-get worker-uuid)
                               worker-config   (worker-config/worker-config-get worker-uuid :include-system-keys true)
                               tasks           (worker-config/worker-config-get-tasks worker-uuid)
                               system-worker-logs    (hive-logs/hive-logs-read hive-uuid :worker-uuid worker-uuid :task nil)
                               ]

        (let [can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (-> (r/response
              (layout/render req
                             (views/status req hive worker worker-config tasks system-worker-logs)
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
                               tasks   (keys (worker-config/worker-config-get-tasks worker-uuid))
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
                               worker (worker/worker-get worker-uuid)
                              ]

        (let [
              tasks        (keys (worker-config/worker-config-get-tasks worker-uuid))
              current-task (:worker-logs-task (:params req))
              ;; If it is "ALL"
              current-task (if (= current-task "*ALL*") nil current-task)
              current-task (some #(when (= current-task %) %) tasks)

              start-time-raw (or (:worker-logs-start (:params req)) (str (ctimec/to-long (ctime/now))))
              start-at     (ctimec/to-sql-time (ctimec/from-long (read-string start-time-raw)))
              can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)
              search-args  (vector hive-uuid :start-at start-at :worker-uuid worker-uuid)
              all-search-args (if current-task (conj search-args :task current-task) search-args)
              worker-logs   (apply hive-logs/hive-logs-read all-search-args)
              ]

            (render (layout/render req
                         (views/logs req current-task start-at tasks worker-logs)
                         :style :side-menu
                         :side-menu (views/side-menu req :logs can-manage?)
                         :back-link { :href (paths/hive-path hive-uuid)
                                      :text "Hive"})))))))
