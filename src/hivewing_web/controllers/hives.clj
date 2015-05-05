(ns hivewing-web.controllers.hives
  (:require
    [hivewing-web.data.workers :as ws]
    [hivewing-web.data.worker-key-pairs :as wkps]
    [hivewing-web.data.hives :as hs]
    [hivewing-web.data.hive-worker-approvals :as hwas]
    [hivewing-web.controllers.base :refer :all]
    [taoensso.timbre :as logger]
    [hivewing-web.data.worker-key-pairs :as wkps]
  ))

(comment
  (def hive (hs/create))
  (def hive-uuid (:uuid hive))
  )

(defn create-and-return-auth-package
  "Create a worker, get the auth package and return"
  [hive-uuid worker-id-string]
  (let [worker (hs/create-worker hive-uuid worker-id-string)
        worker-key-pair (wkps/create worker)]
    ; Create a worker (associated with the hive).
    ; Return the BootstrapAuthPackage

    (logger/info "Deleting the approval...")
    (hwas/delete! hive-uuid worker-id-string)

    (logger/info "Creating bootstrap package...")
    {:worker_uuid  (:uuid worker)
     :public_key (wkps/kp->public-key-file worker-key-pair)
     :private_key (wkps/kp->private-key-file worker-key-pair)
    }))

(defn rejected-join [hive-uuid worker-id-string]
  (hwas/touch hive-uuid worker-id-string)
  {:status 403 :body "Your application for membership has been DENIED. Don't ask again"}
)

(defn pending-join
  [hive-uuid worker-id-string]
  (hwas/touch hive-uuid worker-id-string)
  {:status 409 :body "Your application is pending. Please check back later"})

(defn create-pending-join
  [hive-uuid worker-id-string]
  (hwas/create hive-uuid worker-id-string)
  (pending-join hive-uuid worker-id-string))

(defn join
  "Attempting to join a hive.
  Returns a 409 if you can't yet (we don't know if you can or not)
  A 403 if you got a NYET
  A 200 + auth package if you are allowed in"
  [hive-uuid worker-id-string]
  (let [approval (hwas/lookup hive-uuid worker-id-string)]
    (if (hwas/is-approved? approval)
      (create-and-return-auth-package hive-uuid worker-id-string)
      (if (hwas/is-rejected? approval)
        (rejected-join hive-uuid worker-id-string)
        (if approval
          (pending-join hive-uuid worker-id-string)
          (create-pending-join hive-uuid worker-id-string))))))
(defn reject
  [hive-uuid worker-id-string]
  (hwas/create hive-uuid worker-id-string false)
  )

(defn approve
  [hive-uuid worker-id-string]
  (hwas/create hive-uuid worker-id-string true)
  )



(defn pending-approvals
  [hive-uuid]
  (or (hwas/pending-approvals hive-uuid) []))

(defn show
  "Show the details on the requested hive"
  [hive-uuid]
  (let [hive (hs/lookup hive-uuid)]
    (if hive
      {:status 200
       :body hive}
      {:status 404
       :headers {"Content-Type" "text/plain"}
       :body (str "Hive " hive-uuid " not found")
      })))
