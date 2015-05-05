(ns hivewing-web.data.worker-key-pairs
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [clojure.java.jdbc :as jdbc]
    [clj-crypto.core :as crypto]
            ))


(comment
  (def kp (crypto/generate-key-pair :key-size (config/key-pair-length)))
  (def kp-map (crypto/get-key-pair-map kp))

  (crypto/encode-base64-as-str (.encode (.getPublic kp)))
  (str "-----BEGIN RSA PRIVATE KEY-----\n"
       (crypto/encode-base64-as-str (.encode (.getPrivate kp)))
       "\n-----END RSA PRIVATE KEY-----")
  (str "ssh-rsa "
       (crypto/encode-base64-as-str (.encode (.getPublic kp)))
       " worker-uuid@hivewing.io")
)

(defn lookup
  "Finds the key pairs of a given worker"
  [uuid]
  (let [uuid (ensure-uuid uuid)]
    (first (jdbc/query (config/sql-db) ["SELECT * FROM worker_key_pairs WHERE uuid = ? LIMIT 1" uuid]))))

(defn lookup-for-worker
  [worker]
  (let [uuid (ensure-uuid (or (:uuid worker) worker))]
    (first (jdbc/query (config/sql-db) ["SELECT * FROM worker_key_pairs WHERE worker_uuid = ? LIMIT 1" uuid]))))

(defn kp->public-key-file [key-pair]
  (str "ssh-rsa "
       (:public_key key-pair)
       " "
       (:worker_uuid key-pair)
       "@workers.hivewing.io"))

(defn kp->private-key-file [key-pair]
  (str "-----BEGIN RSA PRIVATE KEY-----\n"
       (:private_key key-pair)
       "\n-----END RSA PRIVATE KEY-----"))

(defn lookup-worker-public-key-file [worker]
  (let [uuid (ensure-uuid (or (:uuid worker) worker))
        record (lookup-for-worker uuid)]
    (if record
      (kp->public-key-file record)
      nil)))

(defn create
  "Create a public key string, given a worker hash and a public key string!"
  [worker]

  (let [kp     (crypto/generate-key-pair :key-size (config/key-pair-length))
        priv   (crypto/encode-base64-as-str (.encode (.getPrivate kp)))
        pub    (crypto/encode-base64-as-str (.encode (.getPublic kp)))
        ]
      (let [uuid (ensure-uuid (or (:uuid worker) worker))]
        (first (jdbc/insert! (config/sql-db) :worker_key_pairs
                             {:worker_uuid uuid
                              :private_key priv
                              :public_key pub})))))
