(ns hivewing-web.paths)

(defn worker-path
  "Path for a worker"
  [worker-uuid hive-uuid]
  (str "/hives/" hive-uuid "/workers/" worker-uuid))
