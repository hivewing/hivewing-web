(ns hivewing-web.routes
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [hivewing-web.paths :as paths]
              [hivewing-web.home-controller :as home]
              [hivewing-web.apiary-controller :as apiary]
              [hivewing-web.system-controller :as system]
              [hivewing-web.hive-controller :as hive]
              [hivewing-web.worker-controller :as worker]
              [hivewing-web.login-controller :as login]))


; TODO
  ; Want to have "requires-beekeeper" routes
  ; Put them into their own block
  ; http://www.luminusweb.net/docs/routes.md#restricting_access
  ; Write a macro to "decorate" all their methods with (requires beekeeper)
  ; Add.
  ; Redirect you to /apiary when you login.
  ; If you are logged in, on login, redirect to return to
  ;
  ; want to be able to name them and look them back up!

(defroutes app-routes
  "Route for Hivewing-Web!"
  ;; Root Request
  (GET "/" {:as req} (home/index req))
  (GET  "/login" {:as req} (login/login req :post-to "/login"))
  (POST "/login" {:as req} (login/do-login req ))
  (GET  "/logout" {:as req} (login/logout req))
  (GET  (paths/apiary-path) {:as req} (apiary/status req))
  (GET  (paths/apiary-manage-path) {:as req} (apiary/manage req))
  (GET  "/apiary/join" {:as req} (apiary/join req :post-to "/apiary/join"))
  (POST "/apiary/join" {:as req} (apiary/do-join req))

  (GET (paths/hive-path ":hive-uuid") {:as req } ( hive/status req))
  (GET (paths/hive-manage-path ":hive-uuid") {:as req } ( hive/manage req))

  (GET (paths/worker-path ":hive-uuid" ":worker-uuid") {:as req} (worker/status req))
  (GET (paths/worker-manage-path ":hive-uuid" ":worker-uuid") {:as req} (worker/manage req))
  (GET (paths/worker-config-path ":hive-uuid" ":worker-uuid") {:as req} (worker/config req))
  (GET (paths/worker-data-path ":hive-uuid" ":worker-uuid") {:as req} (worker/data req))
  (GET (paths/worker-logs-path ":hive-uuid" ":worker-uuid") {:as req} (worker/logs req))

  ; This is for when you have a new worker and want to add it
  ; individually to a selected hive / apiary.
  ;(GET "/worker"
  (ANY "*" {:as req} (system/not-found req))
  )
