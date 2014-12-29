(ns hivewing-web.system-controller
  (:require [hivewing-web.session :as session]
            [hivewing-web.controller-core :refer :all]
            [hivewing-web.paths :as paths]
            [ring.util.request :as ring-request]
            [ring.util.response :as r]
            [environ.core  :refer [env]]
            [views.layout :as layout]
            [views.system :as system-views]
     ))


(defn not-found
  [req & args]
  (let [args (apply hash-map args)]
    (->
      (r/response (layout/render req (system-views/not-found) :style :single))
      (r/header "Content-Type" "text/html; charset=utf-8")
      (r/status 404))))
