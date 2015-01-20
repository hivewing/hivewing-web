(ns hivewing-web.routes
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [taoensso.timbre :as logger]
              [ring.util.response :as r]
              [hivewing-web.paths :as paths]
              [hivewing-web.home-controller :as home]
              [hivewing-web.apiary-controller :as apiary]
              [hivewing-web.system-controller :as system]
              [hivewing-web.hive-controller :as hive]
              [hivewing-web.worker-controller :as worker]
              [hivewing-web.login-controller :as login]))

(defn log-request
  [{:keys [request-method uri remote-addr query-string params] :as req}]

  (logger/info (str "Starting "
             request-method " "
             uri (if query-string (str "?" query-string))
             " for " remote-addr
             " " (dissoc (:headers req) "authorization"))) ;; log headers, but don't log username/password, if any

  (logger/debug (str "Request details: " (select-keys req [:server-port :server-name :remote-addr :uri
                                                       :query-string :scheme :request-method
                                                       :content-type :content-length :character-encoding])))
  (if (not (empty? params))
    (logger/info (str "  \\ - - - -  Params: " params)))
  req
  )

(defn log-response
  [{:keys [status] :as resp}]
  (logger/info "Response: " status)
  resp)

(defmacro hw-route
  [action path method & args]
  `(~action ~path {:as req#} (log-response (~method (log-request req#) ~@args))))

(defroutes app-routes
  "Route for Hivewing-Web!"
  ;; Root Request
  (hw-route GET "/" home/index)
  (hw-route GET  "/login" login/login :post-to "/login")

  (hw-route POST "/login"  login/do-login)
  (hw-route GET  "/logout"  login/logout)
  (hw-route GET  (paths/apiary-path)  apiary/status)
  (hw-route GET  (paths/apiary-manage-path)  apiary/manage)
  (hw-route GET  "/apiary/join"  apiary/join :post-to "/apiary/join")
  (hw-route POST "/apiary/join"  apiary/do-join)

  (hw-route GET (paths/hive-path ":hive-uuid") hive/status)
  (hw-route GET (paths/hive-manage-path ":hive-uuid") hive/manage)
  (hw-route GET (paths/hive-processing-path ":hive-uuid") hive/processing)
  (hw-route POST (paths/hive-manage-path ":hive-uuid") hive/update-manage)
  (hw-route GET (paths/hive-data-path ":hive-uuid") hive/data)
  (hw-route GET (paths/hive-data-value-path ":hive-uuid" ":data-name") hive/show-data-values)

  (hw-route GET  (paths/worker-path ":hive-uuid" ":worker-uuid") worker/status)
  (hw-route POST (paths/worker-delete-path ":hive-uuid" ":worker-uuid") worker/delete-worker)
  (hw-route GET  (paths/worker-manage-path ":hive-uuid" ":worker-uuid")  worker/manage)
  (hw-route POST (paths/worker-manage-path ":hive-uuid" ":worker-uuid")  worker/update-manage)
  (hw-route POST (paths/worker-config-update-path ":hive-uuid" ":worker-uuid")  worker/update-config)
  (hw-route POST (paths/worker-config-delete-path ":hive-uuid" ":worker-uuid")  worker/delete-config)
  (hw-route POST (paths/worker-config-path ":hive-uuid" ":worker-uuid")  worker/create-config)
  (hw-route GET  (paths/worker-config-path ":hive-uuid" ":worker-uuid")  worker/config)

  (hw-route GET (paths/worker-data-path ":hive-uuid" ":worker-uuid") worker/data)
  (hw-route GET (paths/worker-data-value-path ":hive-uuid" ":worker-uuid" ":data-name") worker/show-data-values)
  (hw-route GET (paths/worker-logs-delta-path ":hive-uuid" ":worker-uuid") worker/logs-delta)
  (hw-route GET (paths/worker-logs-path ":hive-uuid" ":worker-uuid") worker/logs)

  (ANY "*" {:as req} (log-response (system/not-found (log-request req))))
  )
