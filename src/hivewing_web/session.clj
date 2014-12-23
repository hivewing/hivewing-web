(ns hivewing-web.session
  (:require [hivewing-core.beekeeper :as bk]))

(def current-user-key "beekeeper-id")

(defn current-user
  "Gets the current user's uuid"
  [req]
  (get-in req [:session current-user-key]))

(defn set-current-user
  "Set the current user uuid"
  [req uuid]
  (:session (assoc-in req [:session current-user-key] uuid)))

(defn logout!
  "Logs out - basically kills the session"
  [req]
  (dissoc req :session))
