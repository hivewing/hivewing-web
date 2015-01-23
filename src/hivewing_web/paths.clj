(ns hivewing-web.paths
  (:require [ring.util.codec :as ring-codec]
            [ring.util.request :as ring-request]
            [clojurewerkz.urly.core :as u]
            ))

(defn add-params-to-url
  [base-url added-params]
  (let [url (u/url-like base-url)
        current-query-str (or (u/query-of url))
        current-query (if (empty? current-query-str) {} (clojure.walk/keywordize-keys (ring-codec/form-decode current-query-str)))
        merged-query (merge (clojure.walk/keywordize-keys current-query )
                            (clojure.walk/keywordize-keys added-params)
                            ;; Get rid of these ALWAYS
                            {:__anti-forgery-token nil})

        merged-query (into {} (filter (comp not nil? val) merged-query))
        new-query  (ring-codec/form-encode merged-query)
        new-url (u/mutate-query url new-query)
        ]
    (str new-url)))

(defn apiary-path []
  "/apiary")

(defn login-path
  [ & args ]
  (apply add-params-to-url "/login" (apply hash-map args)))

(defn apiary-manage-path []
  (str (apiary-path) "/manage"))

(defn worker-path
  "Path for a worker"
  [hive-uuid worker-uuid]
  (str "/hives/" hive-uuid "/workers/" worker-uuid))

(defn worker-status-path [hu wu] (str (worker-path hu wu)))
(defn worker-manage-path [hu wu] (str (worker-path hu wu) "/manage"))
(defn worker-delete-path [hu wu] (str (worker-path hu wu) "/delete-worker"))
(defn worker-config-path [hu wu] (str (worker-path hu wu) "/config"))
(defn worker-config-update-path [hu wu] (str (worker-config-path hu wu) "/update"))
(defn worker-config-delete-path [hu wu] (str (worker-config-path hu wu) "/delete"))

(defn worker-data-path [hu wu] (str (worker-path hu wu) "/data"))
(defn worker-data-value-path [hu wu dn] (str (worker-path hu wu) "/data/" dn))
(defn worker-logs-path [hu wu] (str (worker-path hu wu) "/logs"))
(defn worker-logs-delta-path [hu wu] (str (worker-path hu wu) "/logs/delta"))

(defn hive-path
  [hive-uuid]
  (str "/hives/" hive-uuid))
(defn hive-manage-path [hu] ( str (hive-path hu) "/manage"))
(defn hive-data-path [hu] ( str (hive-path hu) "/data"))
(defn hive-data-value-path [hu dn] ( str (hive-path hu) "/data/" dn))
(defn hive-processing-path [hu] ( str (hive-path hu) "/processing"))
(defn hive-processing-new-choose-stage-path [hu] ( str (hive-path hu) "/processing/new-stage"))
(defn hive-processing-new-stage-path [hu stgnm] ( str (hive-path hu) "/processing/stage/" stgnm "/new"))
(defn hive-processing-create-stage-path [hu stgnm] ( str (hive-path hu) "/processing/stage/" stgnm "/create"))
(defn hive-processing-delete-stage-path [hu stgnm] ( str (hive-path hu) "/processing/stage/" stgnm "/delete"))
