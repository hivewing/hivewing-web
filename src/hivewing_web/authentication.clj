(ns hivewing-web.authentication
  (:require [compojure.api.sweet :refer :all]
            [hivewing-web.data.access-tokens :as access-tokens]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(defmethod compojure.api.meta/restructure-param :hive-access
  [_ hive-permissions {:keys [parameters lets body middlewares] :as acc}]
  "Make sure the request has X-Access-Token header  - look it up and return the hives you can access"
  (let [let-ds `[{{raw-token# "x-access-token"} :headers} ~'+compojure-api-request+
                 token# (hivewing-web.data.access-tokens/lookup raw-token#)
                 ;; Look up the user
                 ;; Look up all their hive-access values
                 ~hive-permissions token#
                ]
        ]
    (-> acc
      (update-in [:lets] into let-ds)
      (assoc :body `((if ~hive-permissions
                     (do
                       ~@body)
                     (ring.util.http-response/not-found "Resource not found")))))))
