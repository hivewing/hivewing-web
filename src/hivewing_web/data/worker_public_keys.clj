(ns hivewing-web.data.worker-public-keys
  (:require
    [hivewing-web.config :as config]
    [hivewing-web.data.core :refer :all]
    [clojure.java.jdbc :as jdbc]
            ))
(comment
  ; f13bc460-f022-11e4-9de5-127ff21068a7
  (create "f13bc460-f022-11e4-9de5-127ff21068a7" "ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAu6v3F0mzbJyVin9/27I1hc99NLXA7ErQ8KHlym0f7C3WUS4+g/D+clVP/f1MOMV2glxKHR2ZgOgmRGuu2hJfE7jxJouf7E/E8OZAwHr877ekqMYyxELcZTv6SahbNrIloVUfcAbKZfjAEehxS3UfdI3LBgXNWg88fncVhMdol2KD9loHOM8yK5sz0W8E7rLil1U1OTOp5fTLZFSqIa4CQZMFTOZy8O8Obt4mRAVR5rykBa6UmuWg0MdJKc4GUNasL2/qySDzh/BH1e4KgeCAZJjDw2HcK66NHyxZqDTwigHJJmg6ou5MSXhYruxgdQexF5KA15G6la6lFXZDVFjNsQ== tramsey@devtramsey")
  ;; Cary's
  ; 269ba080-f023-11e4-a3fa-127ff21068a7
  (create "269ba080-f023-11e4-a3fa-127ff21068a7" "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC6NUfcIfToBISwY6ejUDCDRoqAAY7ndEPAgGfZL9tj1MqOqTpQiNTeQ19eL5tNQhCZwUH9IkqcGbl6z5E46+DPPoFstjnasZDC30/sH9FqF1E57XLf+3zkASsHykIcUXKPcqR0JfpI81u1mSnZQxe7hI3xKScYZl9lrfDy0vkDolBQnZxlo8a9rsRCj5pmdMuJx3b+Z1QY1fvly47LJGIpsKHGWSdgWp2GjVwSy5Py3L0acLKoZnAUtURvWt+hAk5VhNmpsNRgRUxO6uMp6H4k9CH21bBkFdgKGrtNYvP9PD814dz3T5FlO9UBahOeP8qVEpXX7wZhEI/7lzt3KsLr cfitzhugh@cfitz-server")
  (lookup-for-worker "269ba080-f023-11e4-a3fa-127ff21068a7")
  (lookup-for-worker "f13bc460-f022-11e4-9de5-127ff21068a7")
  )
(defn lookup
  "Finds the public keys of a given beekeeper"
  [uuid]
  (let [uuid (ensure-uuid uuid)]
    (first (jdbc/query (config/sql-db) ["SELECT * FROM worker_public_keys WHERE uuid = ? LIMIT 1" uuid]))))

(defn lookup-for-worker
  [worker]
  (let [uuid (ensure-uuid (or (:uuid worker) worker))]
    (first (jdbc/query (config/sql-db) ["SELECT * FROM worker_public_keys WHERE worker_uuid = ? LIMIT 1" uuid]))))

(defn create
  "Create a public key string, given a worker hash and a public key string!"
  [worker public-key-string]
  (let [uuid (ensure-uuid (or (:uuid worker) worker))]
    (first (jdbc/insert! (config/sql-db) :worker_public_keys
                         {:worker_uuid uuid
                          :key public-key-string}))))
