(ns hivewing-web.server
  (:require
    [ring.middleware.defaults :refer [site-defaults secure-site-defaults wrap-defaults]]
    [ring.middleware.session.cookie :as rmsc]
    [ring.middleware.flash :as rmf]
    [ring.middleware.content-type :as rmct]
    [hivewing-web.configuration :as config]
    [hivewing-web.routes :refer [app-routes]]))

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
                  :content-types true
                  :not-modified-responses true
             }
                  )
        app-defaults (assoc-in app-defaults [:params :nested] true)
        ]

      (->
        (wrap-defaults
            app-routes
            app-defaults)
        rmf/wrap-flash)
    ))
