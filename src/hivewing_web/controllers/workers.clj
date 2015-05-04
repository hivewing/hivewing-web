(ns hivewing-web.controllers.workers
  (:require [hivewing-web.data.workers :as ws]
            [hivewing-web.controllers.base :refer :all]
            [taoensso.timbre :as logger]
            [hivewing-web.data.worker-key-pairs :as wkps]
  ))

(defn public-keys [worker-uuid]
  (try
    (let [public-key-file (wkps/lookup-worker-public-key-file worker-uuid)]
      (if public-key-file
        {:status 200
         :headers {"Content-Type" "text/plain"}
         :body public-key-file
        }
        {:status 404
         :headers {"Content-Type" "text/plain"}
         :body ""}
        ))
    (catch Exception e
      (logger/error e "Ackblast")
        {:status 404
         :headers {"Content-Type" "text/plain"}
         :body ""}
      ) ))
