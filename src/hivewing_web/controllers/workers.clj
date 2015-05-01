(ns hivewing-web.controllers.workers
  (:require [hivewing-web.data.workers :as ws]
            [hivewing-web.controllers.base :refer :all]
            [taoensso.timbre :as logger]
            [hivewing-web.data.worker-public-keys :as wpks]
  ))

(defn public-keys [worker-uuid]
  (try
    (let [public-key (wpks/lookup-for-worker worker-uuid)]
      (logger/info "Lookup worker: " worker-uuid)
      (if public-key
        {:status 200
         :headers {"Content-Type" "text/plain"}
         :body (:key public-key)
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
