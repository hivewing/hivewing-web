(ns hivewing-web.data.hives
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [hivewing-web.data.workers :as workers]
    [hivewing-web.data.namer :refer :all]
    [yesql.core :refer [defqueries]]
    [clojure.java.jdbc :as jdbc]
            ))
(comment
  (def  hive (create))
  )
(defqueries "hivewing_web/sql/hives.sql")

(defn list-hives [ha]
  (let [uuids (:hives ha)]
    (list-hives-with-scope (config/sql-db) (map ensure-uuid uuids))))

(defn lookup
  "Lookup a hive by uuid"
  [hive-uuid]
  (let [uuid (ensure-uuid hive-uuid)]
    (first (lookup-hives-with-scope (config/sql-db) uuid))))

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
