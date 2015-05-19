(ns hivewing-web.hives-controller-test
  (:require [expectations :as e]
            [hivewing-web.data.hives :as hives]
            [hivewing-web.data.hive-worker-approvals :as hwas]
            [hivewing-web.data.workers :as ws]
            [hivewing-web.data.users :as users]
            [ring.mock.request :as mock]
            [hivewing-web.helpers :as helpers]
            [hivewing-web.controllers.hives :refer :all]
            ))

(e/expect-let [ha (helpers/hive-access)
               hive-uuid (first (:hives ha))]
    200 (:status (show hive-uuid ha)))

(e/expect-let [ha (helpers/hive-access)
               hive-uuid "Not the UUID"]
    404 (:status (show hive-uuid ha)))


;; PENDING APPROVALS
  (e/expect-let [ha (helpers/hive-access)
                 hive (hives/lookup (first (:hives ha)))
                 h-uuid "Not the uuid"
                 approval (hwas/create hive "worker-id" true)
                 ]
     404
     (:status (pending-approvals h-uuid ha)))

  (e/expect-let [ha (helpers/hive-access)
                 hive (hives/lookup (first (:hives ha)))
                 h-uuid (:uuid hive)
                 ]
      []
      (:body (pending-approvals h-uuid ha)))

  (e/expect-let [ha (helpers/hive-access)
                 hive (hives/lookup (first (:hives ha)))
                 h-uuid (:uuid hive)
                 approval (hwas/create hive "worker-id" true)
                 ]
     []
     (:body (pending-approvals h-uuid ha)))

  (e/expect-let [ha (helpers/hive-access)
                 hive (hives/lookup (first (:hives ha)))
                 h-uuid (:uuid hive)
                 approval (hwas/create hive "worker-id")
                 ]
     "worker-id"
     (:worker_id_string (first (:body (pending-approvals h-uuid ha)))))

  (e/expect-let [ha (helpers/hive-access)
                 hive (hives/lookup (first (:hives ha)))
                 h-uuid (:uuid hive)
                 approval (hwas/create hive "worker-id2")
                 approval (hwas/create hive "worker-id")
                 approval (hwas/create hive "worker-id")
                 ]
     "worker-id"
     (:worker_id_string (first (:body (pending-approvals h-uuid ha)))))

;; Developer *rejects* the worker-id
  (e/expect-let [ha (helpers/hive-access)
                 hive (hives/lookup (first (:hives ha)))
                 h-uuid (:uuid hive)
               approval (hwas/create hive "worker-id")

               ]
   false
   (:approval (:body (reject h-uuid "worker-id" ha))))

  (e/expect-let [ha (helpers/hive-access)
                 hive (hives/lookup (first (:hives ha)))
                 h-uuid "not-the-uuid"
               approval (hwas/create hive "worker-id")

               ]
   404
   (:status (reject h-uuid "worker-id" ha)))

  (e/expect-let [ha (helpers/hive-access)
                 hive (hives/lookup (first (:hives ha)))
                 h-uuid (:uuid hive)]
   true
   (:approval (:body (approve h-uuid "worker-id" ha))))

  (e/expect-let [ha (helpers/hive-access)
                 hive (hives/lookup (first (:hives ha)))
                 h-uuid "not-the-uuid"]
   404
   (:status (approve h-uuid "worker-id" ha)))

; Not ready
(e/expect-let [hive (hives/create)
               h-uuid (:uuid hive)]
    409 (:status (join h-uuid "12345678-1234-1234-1234-123456789012")))

; Not ready
(e/expect-let [hive (hives/create)
               h-uuid (:uuid hive)
               approval (hwas/create hive "worker-id" false)
               ]
    403 (:status (join h-uuid "worker-id")))

; Ready, give me the public-key
(e/expect-let [hive (hives/create)
               h-uuid (:uuid hive)
               approval (hwas/create hive "worker-id" true)
               ]
    #"ssh-rsa"
    (:public_key (:body (join h-uuid "worker-id"))))

(e/expect-let [hive (hives/create)
               h-uuid (:uuid hive)
               approval (hwas/create hive "worker-id" true)
               ]
    #"RSA PRIVATE KEY"
    (:private_key (:body (join h-uuid "worker-id"))))

(e/expect-let [hive (hives/create)
               h-uuid (:uuid hive)
               approval (hwas/create hive "worker-id" true)

               ]
    (:worker_uuid (join h-uuid "worker-id"))
    (:uuid (:body (first (ws/list-workers)))))
