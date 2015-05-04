(ns hivewing-web.controllers.hives
  (:require
    [hivewing-web.data.workers :as ws]
    [hivewing-web.controllers.base :refer :all]
    [taoensso.timbre :as logger]
    [hivewing-web.data.worker-key-pairs :as wkps]
  ))

(defn join
  "Attempting to join a hive.
  Returns a 409 if you can't yet (we don't know if you can or not)
  A 403 if you got a NYET
  A 200 + auth package if you are allowed in"
  [hive-uuid worker-id-string]
    ;; Look up the hive-uuid for a hive
    ;; Look up for any workers with this id-string (in that hive)
       ;; if there are - it's not allowed
    ;; Look up for a hive-join-authorization record
       ;; (hive-uuid, worker-id-string, ...)
    ;; If you find the hive authorixation record
       ;; If it is for YES (generate a worker, link it, proved the package)
       ;; If it is for a NO (send them a 403)
  {:status 409
   :headers {"Content-Type" "text/plain"}
   :body "You do not have access to this hive yet. Please retry in a few seconds and make sure the developer has approved your worker-id-string for access"
  })
