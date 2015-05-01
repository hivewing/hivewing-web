(ns hivewing-web.worker-public-keys-test
  (:require [expectations :as e]
            [hivewing-web.data.workers :as workers]
            [hivewing-web.data.worker-public-keys :refer :all]))
(defn get-worker [ & args ]
  (apply workers/create args))

;; It is created
(e/expect-let [pk "Public _key"
               worker (get-worker)]
              pk
              (:key (create worker pk)))

;; It is lookup-able
(e/expect-let [pk "Public _key"
               worker (get-worker)
               wpk (create worker pk)]
              pk
              (:key (lookup (:uuid wpk))))

;; It is lookup-able by worker
(e/expect-let [pk "Public _key"
               worker (get-worker)
               wpk (create worker pk)]
              pk
              (:key (lookup-for-worker worker)))

;; Not ok to create 2 per worker
(e/expect-let [pk "Public _key"
               worker (get-worker)
               wpk (create worker pk)]
            org.postgresql.util.PSQLException
            (create worker pk))
