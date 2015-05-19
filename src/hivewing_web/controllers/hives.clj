(ns hivewing-web.controllers.hives
  (:require
    [hivewing-web.data.workers :as ws]
    [hivewing-web.data.hive-access :as hive-access]
    [hivewing-web.data.worker-key-pairs :as wkps]
    [hivewing-web.data.hives :as hs]
    [hivewing-web.data.hive-worker-approvals :as hwas]
    [hivewing-web.config :as config]
    [hivewing-web.controllers.base :refer :all]
    [taoensso.timbre :as logger]
    [hivewing-web.data.worker-key-pairs :as wkps]
  ))

(comment
  (def hive (hs/create))
  (def hive-uuid (:uuid hive))
  )

(defn- create-and-return-auth-package
  "Create a worker, get the auth package and return"
  [hive-uuid worker-id-string]
  (let [worker (hs/create-worker hive-uuid worker-id-string)
        worker-key-pair (wkps/create worker)]
    ; Create a worker (associated with the hive).
    ; Return the BootstrapAuthPackage

    (logger/info "Deleting the approval...")
    (hwas/delete! hive-uuid worker-id-string)

    (logger/info "Creating bootstrap package...")
    {:status 200
     :body { :worker_uuid  (:uuid worker)
             :public_key (wkps/kp->public-key-file worker-key-pair)
             :private_key (wkps/kp->private-key-file worker-key-pair)}
     }))

(defn- rejected-join [hive-uuid worker-id-string]
  (hwas/touch hive-uuid worker-id-string)
  {:status 403
   :headers {"Content-Type" "text/plain"}
   :body "Your application for membership has been DENIED. Don't ask again"}
)

(defn- pending-join
  [hive-uuid worker-id-string]
  (hwas/touch hive-uuid worker-id-string)
  {:status 409
   :headers {"Content-Type" "text/plain"}
   :body "Your application is pending. Please check back later"})

(defn- create-pending-join
  [hive-uuid worker-id-string]
  (hwas/create hive-uuid worker-id-string)
  (pending-join hive-uuid worker-id-string))

(defn join
  "Attempting to join a hive.
  Returns a 409 if you can't yet (we don't know if you can or not)
  A 403 if you got a NYET
  A 200 + auth package if you are allowed in"
  [hive-uuid worker-id-string]
  (let [hive (hs/lookup hive-uuid)
        approval (hwas/lookup hive-uuid worker-id-string)]
    (if hive
      (if (hwas/is-approved? approval)
        (create-and-return-auth-package hive-uuid worker-id-string)
        (if (hwas/is-rejected? approval)
          (rejected-join hive-uuid worker-id-string)
          (if approval
            (pending-join hive-uuid worker-id-string)
            (create-pending-join hive-uuid worker-id-string))))
      {:status 404
       :body "Hive not found"
       :headers {"Content-Type" "text/plain"}
       })))

(defn index
  "Show all the hive uuids"
  [access]
  {:status 200
   :body (map :uuid (hs/list-hives access))
   })

(defn create
  [hive-name access]
  (if (> (count (:hives access)) config/max-number-of-hives-per-user)
    {:status 403 :body "Would exceed maximum number of hives"}
    (if-let [hive (hs/create hive-name)]
      (do
        ;; Add hive access
        (hive-access/enable-access (:user access) hive)
        {:status 200
         :body hive})
      {:status 404
       :body "No hive found"})))

(defn reject
  [hive-uuid worker-id-string access]
  (if (some #{(str hive-uuid)} (map str (:hives access)))
  {:status 200
   :body (hwas/create hive-uuid worker-id-string false)
   }
  {:status 404
   :body "No hive found"}))

(defn approve
  [hive-uuid worker-id-string access]
  (if (some #{(str hive-uuid)} (map str (:hives access)))
  {:status 200
   :body (hwas/create hive-uuid worker-id-string true) }
  {:status 404
   :body "No hive found"}))

(defn pending-approvals
  [hive-uuid access]
  (if (some #{(str hive-uuid)} (map str (:hives access)))
    {:status 200
     :body (or (hwas/pending-approvals hive-uuid) []) }
    {:status 404
     :body "Hive not found" }
  ))

(defn show
  "Show the details on the requested hive"
  [hive-uuid access]
  (let [not-found {:status 404
                   :headers {"Content-Type" "text/plain"}
                   :body (str "Hive " hive-uuid " not found")
                  }]

  (if (some #{(str hive-uuid)} (map str (:hives access)))
    (if-let [hive (hs/lookup hive-uuid)]
      {:status 200
       :body hive}
      not-found)
    not-found)))
