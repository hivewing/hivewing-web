(ns lib.routes
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [taoensso.timbre :as logger]
              [ring.util.response :as r]))

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
