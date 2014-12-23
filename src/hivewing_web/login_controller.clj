(ns hivewing-web.login-controller
  (:require [clojure.pprint :refer [pprint]]
            [hivewing-web.session :as session]
            [hivewing-web.controller-core :refer :all]
            [hivewing-core.beekeeper :as bk]
            [ring.util.request :as ring-request]
            [ring.util.response :as r]
            [views.layout :as layout]
            [views.login :as login-views]
     ))

(comment
  (bk/beekeeper-create {:email "admin@example.com"})
  (def uuid (:uuid (bk/beekeeper-find-by-email "admin@example.com")))
  (bk/beekeeper-set-password uuid "H1vewing")
  (bk/beekeeper-validate "admin@example.com" "H1vewing")
 )

(defn- get-return-to
  ([req] (get-return-to req "/apiary"))
  ([req target] (java.net.URLDecoder/decode (or (get-in req [:params :return-to]) target))))

(defn logout
  [req & args]
  (let [args (apply hash-map args)
        return-to (get-return-to req "/")]
    (-> (go-to return-to)
        (assoc :session nil))))

(defn login
  [req & args]
  (let [args (apply hash-map args)
        bk   (session/current-user req)
        return-to (get-return-to req)]

    (if bk
      ; If you're logged in, redirect!
      (go-to return-to)
      ; If not, show the login page.
      (->
        (r/response (layout/render req (login-views/login (:post-to args) return-to)))
        (r/header "Content-Type" "text/html; charset=utf-8")))))

(defn do-login
  [req & args ]

  (let [args (apply hash-map args)
        return-to (get-return-to req)
        email (:email (:user (:params req)))
        password (:password (:user (:params req)))
        bk (bk/beekeeper-validate email password)
        ]

    (if bk
      (let [new-session (session/set-current-user req (:uuid bk))
            response (-> (go-to return-to)
                         (assoc :session new-session))]
        response)
      (->
        (go-to (ring-request/request-url req))
        (assoc :flash "Incorrect email or password")))))
