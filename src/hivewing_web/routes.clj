(ns hivewing-web.routes
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [hivewing-web.home-controller :as home]
              [hivewing-web.apiary-controller :as apiary]
              [hivewing-web.login-controller :as login]))

; TODO
  ; Want to have "requires-beekeeper" routes
  ; Put them into their own block
  ; http://www.luminusweb.net/docs/routes.md#restricting_access
  ; Write a macro to "decorate" all their methods with (requires beekeeper)
  ; Add.
  ; Redirect you to /apiary when you login.
  ; If you are logged in, on login, redirect to return to

(defroutes app-routes
  "Route for Hivewing-Web!"
  ;; Root Request
  (GET "/" {:as req} (home/index req))

  (GET  "/login" {:as req} (login/login req :post-to "/login"))
  (POST "/login" {:as req} (login/do-login req ))
  (GET  "/logout" {:as req} (login/logout req))
  (GET  "/apiary" {:as req} (apiary/index req))

  ; This is for when you have a new worker and want to add it
  ; individually to a selected hive / apiary.
  ;(GET "/worker"
  (route/not-found "<h1>Page not found</h1>")
  )
