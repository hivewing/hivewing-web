(ns hivewing-web.data.worker
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [hivewing-web.data.namer :refer :all]
    [clojure.java.jdbc :as jdbc]
            ))

(defn lookup
  "Finds the public keys of a given beekeeper"
  [worker-uuid]
  (let [uuid (ensure-uuid worker-uuid)]
    (first (jdbc/query (config/sql-db) ["SELECT * FROM workers WHERE uuid = ? LIMIT 1" uuid]))))

(defn create
  "Create a public key string, given a worker hash and a public key string!"
  ([] (create (gen-name)))
  ([name]
    (let [res (first (jdbc/insert! (config/sql-db)
                         :workers
                         {:name name}))]
      res)))
