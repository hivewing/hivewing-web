(ns hivewing-web.core
  (:require [ring.adapter.jetty :as jetty]
            [hivewing-web.server :as server])
  (:gen-class))

(defn -main []
  (jetty/run-jetty server/app {:port 3000}))
