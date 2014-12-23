(ns hivewing-web.server
  (:require
    [ring.middleware.defaults :refer [site-defaults secure-site-defaults wrap-defaults]]
    [ring.middleware.session.cookie :as rmsc]
    [ring.middleware.flash :as rmf]
    [hivewing-web.configuration :as config]
    [noir.session :as nsession]
    [hivewing-web.routes :refer [app-routes]]))

(def app
  (let [app-defaults (assoc site-defaults
            :session {
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
