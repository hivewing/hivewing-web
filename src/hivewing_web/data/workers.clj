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
  )
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
