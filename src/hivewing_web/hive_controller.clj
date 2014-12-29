(ns hivewing-web.hive-controller
  (:require [hivewing-web.session :as session]
            [hivewing-web.controller-core :refer :all]
            [hivewing-web.paths :as paths]
            [hivewing-core.beekeeper :as bk]
            [hivewing-core.hive-manager :as hm]
            [hivewing-core.worker :as worker]
            [hivewing-core.hive :as hive]
            [hivewing-core.apiary :as apiary]
            [ring.util.request :as ring-request]
            [ring.util.response :as r]
            [views.hive :as hive-views]
            [views.layout :as layout]
     ))

(defn side-menu
  [req current-page]
  (vector
    {:href "/"
     :selected? (= current-page :status)
      :text "Status"}
    {:href "/"
     :selected? (= current-page :manage)
      :text "Manage"}
    ))

(defn status
  [req & args]
  (with-required-parameters req [hive-uuid]
    (let [bk   (session/current-user req)
          args (apply hash-map args)
          access? (hive/hive-can-modify? hive-uuid (:uuid bk))
          hive    (hive/hive-get hive-uuid)
          worker-uuids (worker/worker-list hive-uuid :page 1 :per-page 500)
          workers (map #(worker/worker-get (:uuid %)) worker-uuids)]
      (->
        (r/response (layout/render req (hive-views/show req hive workers)
                                    :style :side-menu
                                    :side-menu (side-menu req :status)
                                    :back-link { :href (paths/apiary-path)
                                                  :text "Apiary" }
                                  ))
        (r/header "Content-Type" "text/html; charset=utf-8")))))
