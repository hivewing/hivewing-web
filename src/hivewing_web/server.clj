(ns hivewing-web.server
  (:require
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.middleware.resource :as rmr]
    [ring.middleware.file-info :as rmfi]
    [hivewing-web.home-controller :as home]))

(defroutes handler
  "Route for Hivewing-Web!"
  ;; Root Request
  (GET "/" {:as request} (home/index request))

  )

(def app
  (-> handler
      (rmr/wrap-resource "public")
      (rmfi/wrap-file-info)))
