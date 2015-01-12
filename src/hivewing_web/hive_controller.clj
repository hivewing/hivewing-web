(ns hivewing-web.hive-controller
  (:require [hivewing-web.session :as session]
            [hivewing-web.controller-core :refer :all]
            [hivewing-web.paths :as paths]
            [hivewing-core.beekeeper :as bk]
            [hivewing-core.hive-manager :as hm]
            [hivewing-core.hive-logs :as hive-logs]
            [hivewing-core.hive-data :as hive-data]
            [hivewing-core.worker :as worker]
            [hivewing-core.hive :as hive]
            [hivewing-core.apiary :as apiary]
            [ring.util.response :as r]
            [views.hive :as views]
            [views.layout :as layout]
     ))
(comment
  (def bk "2599052e-903d-11e4-854c-0242ac110027")
  (def hive-uuid "25c56a10-903d-11e4-a644-0242ac110027")
  (hive/hive-can-modify? bk hive-uuid )
  (hm/hive-managers-managing bk)
  )

(defn status
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid]
      (with-preconditions req [
            hive          (hive/hive-get hive-uuid)
            access?       (hive/hive-can-read? (:uuid bk) hive-uuid)
            worker-uuids  (worker/worker-list hive-uuid :page 1 :per-page 500)
            workers       (map #(worker/worker-get (:uuid %)) worker-uuids)
            system-worker-logs    (hive-logs/hive-logs-read hive-uuid :worker-uuid nil :task nil)
          ]
        (let [can-manage? (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (render (layout/render req (views/status req hive workers can-manage? system-worker-logs)
                                        :style :side-menu
                                        :side-menu (views/side-menu req :status can-manage?)
                                        :back-link { :href (paths/apiary-path)
                                                     :text "Apiary" }
                                    )))))))
(defn show-data-values
  [req & args]

  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid data-name]
      (with-preconditions req [
            hive          (hive/hive-get hive-uuid)
            access?       (hive/hive-can-read? (:uuid bk) hive-uuid)
            data-values   (hive-data/hive-data-read hive-uuid nil data-name)
          ]
        (let [can-manage? (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (render (layout/render req (views/show-data-values req hive data-name data-values)
                                        :style :side-menu
                                        :side-menu (views/side-menu req :data can-manage?)
                                        :back-link { :href (paths/apiary-path)
                                                     :text "Apiary" }
                                    )))))))

(defn data
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid]
      (with-preconditions req [
            hive          (hive/hive-get hive-uuid)
            access?       (hive/hive-can-read? (:uuid bk) hive-uuid)
            data-keys     (hive-data/hive-data-get-keys hive-uuid)
          ]
        (let [can-manage? (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (render (layout/render req (views/data req hive data-keys)
                                        :style :side-menu
                                        :side-menu (views/side-menu req :data can-manage?)
                                        :back-link { :href (paths/apiary-path)
                                                     :text "Apiary" }
                                    )))))))


(defn update-manage
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [hive-uuid]
      (with-preconditions req [hive (hive/hive-get hive-uuid)
                               can-manage?  (hive/hive-can-modify? (:uuid bk) hive-uuid)
                               ]
        (let [new-name (:hive-manage-name (:params req))
              new-branch (:hive-manage-image-branch (:params req))
              ]
              (if new-branch (hive/hive-set-image-branch hive-uuid new-branch))
              ;; Updating to a new name
              (if new-name (hive/hive-set-name hive-uuid new-name))
            (->
              (r/redirect (paths/hive-manage-path hive-uuid))
              (assoc :flash "Updated hive")))))))
(defn manage
  [req & args]
  (with-required-parameters req [hive-uuid]
    (with-preconditions req [
          bk   (session/current-user req)
          access? (hive/hive-can-modify? (:uuid bk) hive-uuid)
          hive    (hive/hive-get hive-uuid)
          ]
      (let [can-manage? (hive/hive-can-modify? (:uuid bk) hive-uuid)]
        (render (layout/render req (views/manage req hive)
                                      :style :side-menu
                                      :side-menu (views/side-menu req :manage can-manage?)
                                      :back-link { :href (paths/apiary-path)
                                                   :text "Apiary" }
                                    ))))))
