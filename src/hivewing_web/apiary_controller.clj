(ns hivewing-web.apiary-controller
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
            [environ.core  :refer [env]]
            [clojurewerkz.urly.core :as u]
            [views.apiary :as apiary-views]
            [views.layout :as layout]
            [clojure.pprint :as pprint]
     ))


(defn index
  [req & args]
  (let [args (apply hash-map args)]
    (->
      (r/response (layout/render req (apiary-views/index)))
      (r/header "Content-Type" "text/html; charset=utf-8"))))

(defn join
  " worker-url is a query parameter that the system will return the user
    to, with the newly created worker details (:worker-uuid and access-token) as
    query parameters

    ex:
    hivewing.io/apiary/join?worker-url=http://local-worker/setup?detail1=a

    returns to
    http://local-worker/setup?detail1=a&worker-uuid=123&access-token=345
  "
  [req & args]
  (let [args (apply hash-map args)
        bk   (session/current-user req)
       ]

    (with-required-parameters req [worker-url]
      ;; If you're logged in...
      (if bk
        (let [hive-uuids (map :hive_uuid (hm/hive-managers-managing (:uuid bk)))
              hives      (map #(hive/hive-get %) hive-uuids)
              ]
          (->
            (r/response (layout/render req (apiary-views/join (:post-to args) hives worker-url)))
            (r/header "Content-Type" "text/html; charset=utf-8")))
        ; Not logged in, return to login, with a return to, to here.
        (login-and-return (ring-request/request-url req))))))

(defn do-join
  "This is the active part of joining a worker to a hive
  We get hive-uuid passed in, and we add a new worker
  To that hive and apiary.
  We redirect the response to the passed in (worker-url)
  With the access-token and worker-uuid attached"

  [req & args]
  (let [args (apply hash-map args)
        bk   (session/current-user req)
        hive-uuid (:hive-uuid (:params req))
        allowed? (hive/hive-can-modify? (:uuid bk) hive-uuid)
        ]

    (with-required-parameters req [worker-url]
      (if (and bk allowed?)
        ; Joining
        (let [apiary (apiary/apiary-find-by-beekeeper (:uuid bk))
              worker (worker/worker-create {:apiary_uuid (:uuid apiary) :hive_uuid hive-uuid})
              access-token (:access_token (worker/worker-get (:uuid worker) :include-access-token true))
              dest-url (absolute-url-from-here req
                                               (paths/worker-path (:uuid worker) hive-uuid))
              ]
            ; redirect things to the worker-url now. With these new params
            (go-to worker-url {:worker-uuid (:uuid worker)
                               :access-token access-token
                               :return-to dest-url}))
        ; Not logged in, return to login, with a return to, to here.
        (login-and-return)))))