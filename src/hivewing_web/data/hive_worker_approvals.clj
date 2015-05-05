(ns hivewing-web.data.hive-worker-approvals
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [clojure.java.jdbc :as jdbc]
            ))
(comment
  (require 'hivewing-web.data.hives)
  (def hive (hivewing-web.data.hives/create))
  (def approval (create hive "worker-id-string"))
  (def approval-true (create hive "worker-id-string-t" true))
  (def approval-flase (create hive "worker-id-string-f" false))
  (count (list-approvals hive))
  )

(defn pending-approvals [hive]
  (let [uuid (ensure-uuid (or (:uuid hive) hive))]
    (jdbc/query (config/sql-db) ["SELECT * FROM hive_worker_approvals WHERE approval IS NULL ORDER BY updated_at DESC NULLS LAST, created_at DESC LIMIT 1000"])))

(defn list-approvals [hive]
  (let [uuid (ensure-uuid (or (:uuid hive) hive))]
    (jdbc/query (config/sql-db) ["SELECT * FROM hive_worker_approvals LIMIT 1000"])))

(defn is-approved? [h-w-a]
  (true? (:approval h-w-a)))

(defn is-unknown? [h-w-a]
  (nil? (:approval h-w-a)))

(defn is-rejected? [h-w-a]
  (false? (:approval h-w-a)))

(defn delete!
  [hive worker-id-string]
  (let [uuid (ensure-uuid (or (:uuid hive) hive))]

    (let [res (first (jdbc/delete!
                       (config/sql-db)
                       :hive_worker_approvals
                       ["hive_uuid = ? AND worker_id_string = ?"
                        uuid worker-id-string]))]
      res)))

(defn lookup
  "Finds the approval (or not) for the worker-id-string and hive"
  [hive worker-id-string]
  (let [uuid (ensure-uuid (or (:uuid hive) hive))]
    (first (jdbc/query (config/sql-db)
                       ["SELECT * FROM hive_worker_approvals WHERE hive_uuid = ? AND worker_id_string = ? LIMIT 1"
                        uuid
                        worker-id-string]))))

(defn create
  "Create a public key string, given a worker hash and a public key string!"
  ([hive worker-id-string] (create hive worker-id-string nil))
  ([hive worker-id-string approval-value]
    (let [uuid (ensure-uuid (or (:uuid hive) hive))]
      (if-let [approval (lookup hive worker-id-string)]
        (jdbc/update! (config/sql-db)
                             :hive_worker_approvals
                             {:approval approval-value}
                             ["hive_uuid = ? AND worker_id_string = ?" uuid worker-id-string])
        (jdbc/insert! (config/sql-db)
                             :hive_worker_approvals
                             {:hive_uuid  uuid
                              :approval approval-value
                              :worker_id_string worker-id-string}))
      (lookup hive worker-id-string))))

(defn touch
  "Touch the updated-at TS on this object"
  [hive worker-id-string]
  (let [uuid (ensure-uuid (or (:uuid hive) hive))]
    (jdbc/update! (config/sql-db)
                         :hive_worker_approvals
                         {:hive_uuid uuid }
                         ["hive_uuid = ? AND worker_id_string = ?" uuid worker-id-string])

    ))
