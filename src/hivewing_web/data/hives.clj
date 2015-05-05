(ns hivewing-web.data.hives
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [hivewing-web.data.workers :as workers]
    [hivewing-web.data.namer :refer :all]
    [clojure.java.jdbc :as jdbc]
            ))

(defn list-hives []
  (jdbc/query (config/sql-db) ["SELECT * FROM hives LIMIT 100"]))

(defn lookup
  "Lookup a hive by uuid"
  [hive-uuid]
  (let [uuid (ensure-uuid hive-uuid)]
    (first (jdbc/query (config/sql-db) ["SELECT * FROM hives WHERE uuid = ? LIMIT 1" uuid]))))

(defn create-worker
  [hive-uuid worker-id-string]
  (let [worker (-> (workers/create)
                   (workers/set-worker-id-string worker-id-string)
                   (workers/join-hive hive-uuid)
                   ;; Set default hive image
                   )]
    worker))

(defn create
  "Create a hive"
  ([] (create (gen-name)))
  ([name]

    (let [safe-name (if (clojure.string/blank? name) (gen-name) name)
          res (first (jdbc/insert! (config/sql-db)
                         :hives
                         {:name safe-name}))]
      res)))
