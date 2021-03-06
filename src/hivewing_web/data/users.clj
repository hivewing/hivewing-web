(ns hivewing-web.data.users
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [crypto.password.bcrypt :as password]
    [clojure.java.jdbc :as jdbc]
  )
)

(comment
  (do
    (require 'hivewing-web.data.access-tokens)
    (require 'hivewing-web.data.hives)
    (require 'hivewing-web.data.hive-access)
    (require 'clj-time.core)

    (def user (create  (str "cary-api-test" (clj-time.core/now)) "1234carycary"))
    (def hive (hivewing-web.data.hives/create))

    (hivewing-web.data.hive-access/enable-access user hive)

    (def hive (hivewing-web.data.hives/create))
    (hivewing-web.data.hive-access/enable-access user hive)

    (def hive (hivewing-web.data.hives/create))
    (hivewing-web.data.hive-access/enable-access user hive)

    (def at (hivewing-web.data.access-tokens/create user "test-token"))
    (def tok (:token at))
    (println tok)
  ))

(defn lookup-by-email
  "Find the user by their email address"
  [email & opts]
  (jdbc/query (config/sql-db) [(str "SELECT * FROM users WHERE LOWER(email) = LOWER(?)") email] :result-set-fn first))

(defn lookup
  "Finds the user by uuid"
  [user]
  (let [uuid (ensure-uuid (or (:uuid user) user))]
    (jdbc/query (config/sql-db) ["SELECT * FROM users WHERE uuid = ? LIMIT 1" uuid] :result-set-fn first)))

(defn validate
  "Validate that a user can be logged in, given email and pw"
  [email password]
  (let [user (lookup-by-email email) ]
    (if user
      (let [stored-pw   (:encrypted_password user)
            valid (password/check password stored-pw)
            ]
        (if valid
          user
          nil))
      nil)))

(def password-regex
  #"^(?=.{6,32}$)(?=.*[A-Za-z])(?=.*[0-9$!_-]).*")

(defn create
  "Create a new user (:email, :password)"
  [email password]
  ; it returns a vector, we want the first value.

  (if (re-find password-regex password)
    (let [clean-params {:email email
                        :encrypted_password (password/encrypt password)
                        }
          inserted (jdbc/insert! (config/sql-db) :users clean-params)]
      (first inserted))
    (throw (Exception. "Password was not complicated enough!"))))

(defn delete!
  [user]
  (let [uuid (ensure-uuid (or (:uuid user) user))]
    (jdbc/delete!
                       (config/sql-db)
                       :users
                       ["uuid = ?" uuid ]
                       :result-set-fn first)))
