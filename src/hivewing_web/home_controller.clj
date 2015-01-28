(ns hivewing-web.home-controller
  (:require [clojure.pprint :refer [pprint]]
            [views.layout :as layout]
            [views.home :as home-views]
     ))

(defn index [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/render req (home-views/index) :style :none)})
