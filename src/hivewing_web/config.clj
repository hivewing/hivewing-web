(ns hivewing-web.config
  (:require [environ.core :refer [env]]))

(defn sql-db []
  (env :jdbc-connection-string))

(defn key-pair-length []
  (or (env :key-pair-length) 4096))

(def max-number-of-hives-per-user 100)
