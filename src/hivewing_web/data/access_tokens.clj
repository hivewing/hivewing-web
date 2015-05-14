(ns hivewing-web.data.access-tokens
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [crypto.password.bcrypt :as password]
    [crypto.random :as rand]
    [clojure.java.jdbc :as jdbc]
  )
  )

(comment
  (create {:uuid
           "2e748e96-f99e-11e4-9aa0-0242ac110019"
           } "token-1")
  )

(defn touch
  "Touch this token as 'used'"
  [token]
  (let [uuid (ensure-uuid (:user_uuid token))
        name (:name token)]
    (jdbc/update! (config/sql-db)
                         :access_tokens
                         {:name name }
                         ["user_uuid = ? AND name = ?"
                          uuid name])))

(defn lookup-by-user
  "Find the token by their user"
  [user & opts]
  (let [uuid (ensure-uuid (or (:uuid user) user))]
    (jdbc/query
      (config/sql-db)
      [(str "SELECT * FROM access_tokens WHERE user_uuid = ?") uuid])))

(defn lookup-by-user-and-name
  "Find the token by the user and name"
  [user name]
  (let [uuid (ensure-uuid (or (:uuid user) user))]
    (jdbc/query
      (config/sql-db)
      [(str "SELECT * FROM access_tokens WHERE user_uuid = ? AND name = ?") uuid (clojure.string/lower-case name)])))


(defn lookup
  "Finds the token by token"
  [token]
    (jdbc/query (config/sql-db) ["SELECT * FROM access_tokens WHERE token = ? LIMIT 1" token] :result-set-fn first))

(def name-regex
  #"^[a-zA-Z0-9_-]{3,64}$")

(comment
  (re-find name-regex "ASDaaABC")
  (re-find name-regex "ASDaaABC-123")
  (re-find name-regex "A")
  )

(defn create
  "Create a new access token"
  [user name]
  (if (re-find name-regex name)
    (let [uuid (ensure-uuid (or (:uuid user) user))
          clean-params {:user_uuid uuid
                        :name (clojure.string/lower-case name)
                        :token (rand/base64 64)
                        }
          inserted (jdbc/insert! (config/sql-db)
                                 :access_tokens
                                 clean-params)]
      (first inserted))
    (throw (Exception. "Name was not valid!"))))

(defn delete!
  [user name]
    (println (:uuid user))
  (let [uuid (ensure-uuid (or (:uuid user) user))]
    (println uuid)
    (jdbc/delete!
       (config/sql-db)
       :access_tokens
       ["name = ? AND user_uuid = ?" name uuid ]
       :result-set-fn first)))
