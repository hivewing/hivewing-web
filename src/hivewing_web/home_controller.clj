(ns hivewing-web.home-controller
  (:require [clojure.pprint :refer [pprint]]
            [hivewing-web.views.layout :as layout]
     ))

(defn index [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (layout/render (str "<h1>Request Echo</h1><pre>"
              (with-out-str (pprint request))
              "</pre>"))})
