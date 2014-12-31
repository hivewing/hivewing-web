(ns hivewing-web.server
  (:require
    [ring.middleware.defaults :refer [site-defaults secure-site-defaults wrap-defaults]]
    [ring.middleware.session.cookie :as rmsc]
    [ring.middleware.flash :as rmf]
    [hivewing-web.configuration :as config]
    [ring.adapter.jetty :as jetty]
    [hivewing-web.routes :refer [app-routes]])
  (:gen-class))

(def app
  (let [app-defaults (assoc site-defaults
            :session {
                  :flash true
                  :store (rmsc/cookie-store {:key config/cookie-key})
                  :cookie-name "hivewing-session"
                  :cookie-attrs {:max-age 3600}
                  }
            :responses {
                  :absolute-redirects false
                  :content-type true
                  :not-modified-responses true
                }
                  )]
      (->
        (wrap-defaults
            app-routes
            app-defaults)
        rmf/wrap-flash)
    ))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
