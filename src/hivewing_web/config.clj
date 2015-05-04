(ns hivewing-web.config
  (:require [environ.core :refer [env]]))

(defn sql-db []
  (env :jdbc-connection-string))

(defn key-pair-size []
  (or (env :key-pair-size) 4096))
