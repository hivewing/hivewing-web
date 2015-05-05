(ns hivewing-web.data.workers
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [hivewing-web.data.namer :refer :all]
    [clojure.java.jdbc :as jdbc]
            ))
(comment
  ; f13bc460-f022-11e4-9de5-127ff21068a7
  (def worker1 (create "worker1"))
  (def worker2 (create "worker2"))
  (lookup "f13bc460-f022-11e4-9de5-127ff21068a7")
  (list-workers)
  (def worker-tim "f13bc460-f022-11e4-9de5-127ff21068a7")
  )

(defn list-workers []
  (jdbc/query (config/sql-db) ["SELECT * FROM workers LIMIT 100"]))

(defn lookup
  "Finds the public keys of a given beekeeper"
  [worker-uuid]
  (let [uuid (ensure-uuid worker-uuid)]
    (first (jdbc/query (config/sql-db) ["SELECT * FROM workers WHERE uuid = ? LIMIT 1" uuid]))))

(defn set-worker-id-string
  [worker worker-id-string]

  (let [worker-uuid (ensure-uuid (or (:uuid worker) worker))]
    (jdbc/update! (config/sql-db)
                   :workers
                   {:worker_id_string worker-id-string}
                   ["uuid = ?" worker-uuid])
    (lookup worker-uuid)))

(defn join-hive
  "Join this worker to a given hive"
  [worker hive]

  (let [worker-uuid (ensure-uuid (or (:uuid worker) worker))
        hive-uuid (ensure-uuid (or (:uuid hive) hive))]
    (jdbc/update! (config/sql-db)
                       :workers
                       {:hive_uuid hive-uuid}
                       ["uuid = ?" worker-uuid])
    (lookup worker-uuid)))


(defn create
  "Create a public key string, given a worker hash and a public key string!"
  ([] (create (gen-name)))
  ([name]
    (let [res (first (jdbc/insert! (config/sql-db)
                         :workers
                         {:name name}))]
      res)))
