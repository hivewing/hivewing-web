(ns hivewing-web.paths
  (:require [ring.util.codec :as ring-codec]
            ))

(defn apiary-path []
  "/apiary")

(defn apiary-manage-path []
  (str (apiary-path) "/manage"))

(defn worker-path
  "Path for a worker"
  [hive-uuid worker-uuid]
  (str "/hives/" hive-uuid "/workers/" worker-uuid))

(defn worker-status-path [hu wu] (str (worker-path hu wu)))
(defn worker-manage-path [hu wu] (str (worker-path hu wu) "/manage"))
(defn worker-config-path [hu wu] (str (worker-path hu wu) "/config"))
(defn worker-config-update-path [hu wu] (str (worker-config-path hu wu) "/update"))
(defn worker-config-delete-path [hu wu] (str (worker-config-path hu wu) "/delete"))

(defn worker-data-path [hu wu] (str (worker-path hu wu) "/data"))
(defn worker-logs-path [hu wu] (str (worker-path hu wu) "/logs"))

(defn hive-path
  [hive-uuid]
  (str "/hives/" hive-uuid))
(defn hive-manage-path [hu] ( str (hive-path hu) "/manage"))
