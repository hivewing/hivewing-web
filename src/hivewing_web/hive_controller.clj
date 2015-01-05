(ns hivewing-web.hive-controller
  (:require [hivewing-web.session :as session]
            [hivewing-web.controller-core :refer :all]
            [hivewing-web.paths :as paths]
            [hivewing-core.beekeeper :as bk]
            [hivewing-core.hive-manager :as hm]
            [hivewing-core.worker :as worker]
            [hivewing-core.hive :as hive]
            [hivewing-core.apiary :as apiary]
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
          ]
        (let [can-manage? (hive/hive-can-modify? (:uuid bk) hive-uuid)]
          (render (layout/render req (views/status req hive workers can-manage?)
                                        :style :side-menu
                                        :side-menu (views/side-menu req :status can-manage?)
                                        :back-link { :href (paths/apiary-path)
                                                     :text "Apiary" }
                                    )))))))


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