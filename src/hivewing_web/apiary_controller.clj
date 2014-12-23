(ns hivewing-web.apiary-controller
  (:require [hivewing-web.session :as session]
            [hivewing-core.beekeeper :as bk]
            [ring.util.request :as request]
            [ring.util.response :as r]
            [environ.core  :refer [env]]
            [views.apiary :as apiary-views]
            [views.layout :as layout]
     ))

(defn index
  [req & args]
  (let [args (apply hash-map args)]
    (->
      (r/response (layout/render req (apiary-views/index)))
      (r/header "Content-Type" "text/html; charset=utf-8"))))
