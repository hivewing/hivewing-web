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
  )

(defn lookup-by-user
  "Find the user by their email address"
  [user & opts]
  (let [uuid (ensure-uuid (or (:uuid user) user))]
    (jdbc/query
      (config/sql-db)
      [(str "SELECT * FROM access_tokens WHERE user_uuid = ?") uuid])))

(defn lookup
  "Finds the token by token"
  [token]
    (jdbc/query (config/sql-db) ["SELECT * FROM access_tokens WHERE token = ? LIMIT 1" token] :result-set-fn first))

(def name-regex
  #"^[a-zA-Z0-9_:-]{3,64}$")

(comment
  (re-find name-regex "ASDaaABC")
  (re-find name-regex "ASDaaABC-123")
  (re-find name-regex "A")
  )

(defn create
  "Create a new user (:email, :password)"
  [user name]
  (if (re-find name-regex name)
    (let [uuid (ensure-uuid (or (:uuid user) user))
          clean-params {:user_uuid uuid
                        :name name
                        :token (rand/base64 64)
                        }
          inserted (jdbc/insert! (config/sql-db)
                                 :access_tokens
                                 clean-params)]
      (first inserted))
    (throw (Exception. "Name was not valid!"))))

(defn delete!
  [token name user]
  (let [uuid (ensure-uuid (or (:uuid user) user))]
    (jdbc/delete!
       (config/sql-db)
       :access_tokens
       ["name = ? AND token = ? AND user_uuid = ?" name token uuid ]
       :result-set-fn first)))
