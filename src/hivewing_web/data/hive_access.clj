(ns hivewing-web.data.hive-access
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [clojure.java.jdbc :as jdbc]
  )
  )
(comment

  )
(defn get-hives
  "Finds all the hive-uuids that are allowed to be accessed by the given user"
  [user]
  (let [uuid (ensure-uuid (or (:uuid user) user))]
    (map :hive_uuid (jdbc/query
      (config/sql-db)
      [(str "SELECT * FROM hive_access WHERE user_uuid = ? LIMIT 250") uuid]))))

(defn get-users
  "Finds all the users who have access to a given hive"
  [hive]
  (let [uuid (ensure-uuid (or (:uuid hive) hive))]
    (map :user_uuid (jdbc/query
      (config/sql-db)
      [(str "SELECT * FROM hive_access WHERE hive_uuid = ? LIMIT 250") uuid]))))


(defn enable-access
  "Adds the access for a given user to a given hive"
  [user hive]
  (let [user-uuid (ensure-uuid (or (:uuid user) user))
        hive-uuid (ensure-uuid (or (:uuid hive) hive))
        existing (jdbc/query (config/sql-db) ["SELECT * FROM hive_access WHERE hive_uuid = ? AND user_uuid = ? LIMIT 1" hive-uuid user-uuid] :result-set-fn first)
        ]
    (if (empty? existing)
      (first (jdbc/insert! (config/sql-db)
                           :hive_access
                           {:hive_uuid hive-uuid
                            :user_uuid user-uuid}))
      existing)))

(defn disable-access
  "removes access for a given user and a given hive"
  [user hive]
  (let [user-uuid (ensure-uuid (or (:uuid user) user))
        hive-uuid (ensure-uuid (or (:uuid hive) hive))]
    (jdbc/delete!
       (config/sql-db)
       :hive_access
       ["hive_uuid = ? AND user_uuid = ?" hive-uuid user-uuid]
       :result-set-fn first)))
