(ns hivewing-web.config
  (:require [environ.core :refer [env]]))

(defn sql-db []
  (env :jdbc-connection-string))
