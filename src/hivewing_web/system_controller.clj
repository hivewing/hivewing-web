(ns hivewing-web.system-controller
  (:require
            ;; DO NOT INCLUDE THIS
            ;;[hivewing-web.controller-core :refer :all]
            [taoensso.timbre :as logger]
            [ring.util.response :as r]
            [views.layout :as layout]
            [views.system :as system-views]
     ))


(defn not-found
  [req & args]
  (let [args (apply hash-map args)]
    (logger/info "Not Found:" (:params req))
    (->
      (r/response (layout/render req (system-views/not-found) :style :single))
      (r/header "Content-Type" "text/html; charset=utf-8")
      (r/status 404))))
