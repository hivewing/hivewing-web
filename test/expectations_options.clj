(ns expectations-options
  (:require [clojure.java.jdbc :as jdbc]
            [hivewing-web.config :as config]
           ))

(defn cleanup-database
  "loads test data"
  {:expectations-options :before-run}
  []
  (println "Cleaning Up Database!")
  (jdbc/execute! (config/sql-db) ["TRUNCATE TABLE workers,worker_key_pairs,hive_worker_approvals, hives, users, access_tokens "])
  )

(defn in-context
  "rebind a var, expecations are run in the defined context"
  {:expectations-options :in-context}
  [work]

  (jdbc/with-db-transaction [trans-conn (config/sql-db)]
    (with-redefs [hivewing-web.config/sql-db (fn [] trans-conn)]
      (work))
    (jdbc/db-set-rollback-only! trans-conn))
  )
