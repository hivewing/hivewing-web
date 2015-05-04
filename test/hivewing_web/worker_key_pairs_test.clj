(ns hivewing-web.worker-key-pairs-test
  (:require [expectations :as e]
            [hivewing-web.data.workers :as workers]
            [hivewing-web.data.worker-key-pairs :refer :all]))
(defn get-worker [ & args ]
  (apply workers/create args))

;; Requires Worker
(e/expect
  org.postgresql.util.PSQLException
  (create {:uuid "12345678-1234-1234-1234-123456789012"}))

;; It is lookup-able
(e/expect-let [pk "Public _key"
               worker (get-worker)
               wkp (create worker)]
              (:public_key wkp)
              (:public_key (lookup (:uuid wkp))))

;; It is lookup-able by worker
(e/expect-let [worker (get-worker)
               wkp (create worker)]
              wkp
              (lookup-for-worker worker))
