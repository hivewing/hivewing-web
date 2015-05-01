(ns hivewing-web.data.worker-public-keys
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [clojure.java.jdbc :as jdbc]
            ))

(defn lookup
  "Finds the public keys of a given beekeeper"
  [uuid]
  (let [uuid (ensure-uuid uuid)]
    (first (jdbc/query (config/sql-db) ["SELECT * FROM worker_public_keys WHERE uuid = ? LIMIT 1" uuid]))))

(defn lookup-for-worker
  [worker]
  (let [uuid (ensure-uuid (or (:uuid worker) worker))]
    (first (jdbc/query (config/sql-db) ["SELECT * FROM worker_public_keys WHERE worker_uuid = ? LIMIT 1" uuid]))))

(defn create
  "Create a public key string, given a worker hash and a public key string!"
  [worker public-key-string]
  (let [uuid (ensure-uuid (or (:uuid worker) worker))]
    (first (jdbc/insert! (config/sql-db) :worker_public_keys
                         {:worker_uuid uuid
                          :key public-key-string}))))
