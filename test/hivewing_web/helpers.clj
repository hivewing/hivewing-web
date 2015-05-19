(ns hivewing-web.helpers
  (:require
    [hivewing-web.data.hives :as hives]
    [hivewing-web.data.hive-worker-approvals :as hwas]
    [hivewing-web.data.workers :as ws]
    [hivewing-web.data.access-tokens :as access-tokens]
    [hivewing-web.data.users :as users]
    [clj-time.core :as t]
    [clj-time.coerce :as tc]
   ))

(def valid-pw "p@ssWord!1")

(defn create-user []
  (users/create (str (tc/to-long (t/now)))  valid-pw))

(defn create-access-token [user]
  (access-tokens/create user (str (tc/to-long (t/now)))))

(defn create-hive []
  (hives/create)
  )

(defn hive-access
  [ & args ]
  (let [args (apply hash-map args)
        user (or (:user args) (create-user))
        access-token (or (:access-token args) (create-access-token user))
        hives (or (:hives args) [(:uuid (create-hive))])
       ]
    {:access-token access-token
     :user user
     :hives hives
     }))
