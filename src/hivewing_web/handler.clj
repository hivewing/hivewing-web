(ns hivewing-web.handler
  (:require
    [compojure.api.sweet :as api ]
    [compojure.core :refer :all]
    [hivewing-web.authentication :as authentication]
    [taoensso.timbre :as timbre]
    [ring.middleware.logger :as logger]
    [ring.util.codec :as codec]
    [hivewing-web.controllers
       [workers :as workers]
       [hives :as hives]
       [base :as base]
    ]
    [hivewing-web.data
      [access-tokens :as access-tokens]
      [hive-access :as hive-access]
    ]
    [ring.middleware.basic-authentication :as bauth]
    [ring.middleware.file-info :as finfo]
    [ring.middleware.resource :as res]
    [ring.swagger.json-schema-dirty]
    [ring.swagger.schema :refer [describe]]
    [ring.util.http-response :as r]
    [schema.core :as s]
    )
  )

(defmethod compojure.api.meta/restructure-param :hive-access
  [_ hive-permissions {:keys [parameters lets body middlewares] :as acc}]
  "Make sure the request has X-access-token header and that it's poiting to an access token
   that is owned by the hive pointed to by hive-uuid (path_param)"
  (let [let-ds `[{{raw-token# "x-access-token"} :headers} ~'+compojure-api-request+
                 decoded-raw-token# (codec/url-decode raw-token#)
                 v (timbre/info "Decoded raw token: " decoded-raw-token#)
                 access-token# (hivewing-web.data.access-tokens/lookup decoded-raw-token#)
                 v (timbre/info "access token: " access-token#)
                 user# (hivewing-web.data.users/lookup (:user_uuid access-token#))
                 v (timbre/info "User: " user#)
                 ~hive-permissions {:access-token access-token#
                                    :user user#
                                    :hives (hivewing-web.data.hive-access/get-hives user#)}
                 ]]
    (-> acc
      (update-in [:lets] into let-ds)
      (assoc :body `((if (:access-token ~hive-permissions)
                       (if (:user ~hive-permissions)
                         (do ~@body)
                         (ring.util.http-response/not-found "User from Access Token (X-ACCESS-TOKEN) not found"))
                      (ring.util.http-response/not-found "Access Token (X-ACCESS-TOKEN) not found"))


                      )))))

(s/defschema BootstrapAuthPackage {:worker_uuid java.util.UUID
                                   :public_key String
                                   :private_key String})

(s/defschema Hive {:uuid java.util.UUID
                   :updated_at (s/maybe java.sql.Timestamp)
                   :created_at java.sql.Timestamp,
                   :name String})

(s/defschema HiveWorkerApproval {:hive_uuid java.util.UUID
                                 :approval Boolean
                                 :worker_id_string String
                                 :updated_at (s/maybe java.sql.Timestamp)
                                 :created_at java.sql.Timestamp})
(s/defschema AccessToken      {:name s/Str
                               :token s/Str
                               :user_uuid java.util.UUID
                               :updated_at (s/maybe java.sql.Timestamp)
                               :created_at java.sql.Timestamp})
(s/defschema AccessTokenBrief
  (dissoc AccessToken :token))

(defmacro wrap-auth [ & body]
  `(bauth/wrap-basic-authentication
    ~@body
    authentication/authenticated?
    "Hivewing API : Token Management"))

(api/defapi api
  (api/swagger-ui
    "/api/console"
    :swagger-docs "/api/docs"
    )

  (api/swagger-docs "/api/docs"
     :info {:title "Hivewing API Docs"}
     :tags [{:name "hives" :description "Information about the hives and worker groupings"}
            {:name "workers" :description "Dealing with workers"}
            {:name "worker-communication" :description "Dealing with how a worker communicates / bootstraps with the server."}
            {:name "tokens" :description "Token management. All these methods require basic authentication over https"}
           ])

    (api/context* "/api/tokens" {:as req}
      (wrap-auth
        (api/GET* "/" []
          :tags ["tokens"]
          :summary "Get a list of all the tokens by name for the user. Requires Basic Auth"
          :return [AccessTokenBrief]
        (let [user (:basic-authentication req)
              tokens (access-tokens/lookup-by-user user)]
          (r/ok (map #(select-keys %1 [:name :created_at :updated_at :user_uuid]) tokens)))
        ))
      (wrap-auth
        (api/POST* "/" {:as req}
          :tags ["tokens"]
          :summary "Create a new access token"
          :body-params [name :- (ring.swagger.schema/describe access-tokens/name-regex "Token name (must be unique to the user)")]
          :return AccessToken
        (let [user (:basic-authentication req)
              token (access-tokens/create user name)]
          (r/ok token))))

      (wrap-auth
        (api/DELETE* "/:token-name" {:as req}
          :tags ["tokens"]
          :summary "Delete an access token by name"
          :path-params [token-name :- (ring.swagger.schema/describe access-tokens/name-regex "Token name (must be unique to the user)")]
          :return s/Str
        (let [user (:basic-authentication req)
              v (println "USER: " user)
              v (println "token " token-name)
              token (access-tokens/delete! user token-name)]
          (r/ok "Deleted")))))

    (api/context* "/api/hives" []

      (api/GET* "/" []
            :tags ["hives"]
            :hive-access hive-access
            :summary "List hive uuids"
            :return [java.util.UUID]
            (hives/index hive-access)
      )

      (api/POST* "/" []
            :tags ["hives"]
            :body-params [hive-name :- (ring.swagger.schema/describe #"[a-zA-Z\-0-9]{0,64}" "Hive name (must be globally unique)")]
            :hive-access hive-access
            :summary "Create a named hive"
            :return Hive
            (hives/create hive-name hive-access))

      (api/GET* "/:hive-uuid/join" []
            :tags ["hives" "worker-communication"]
            ;; NO HIVE ACCESS
            ;; THIS IS A WORKER CALLING!!!
            ;; :hive-access hive-access
            :path-params [hive-uuid        :- (ring.swagger.schema/describe java.util.UUID "Hive uuid identifying the hive you are trying to join")]
            :query-params [worker-id-string :- (ring.swagger.schema/describe String "Plain-text identifier string for this worker")]
            :summary "Bootstrapping for Hivewing's workers. Attempt to join a hive. "
            :return BootstrapAuthPackage
            (hives/join hive-uuid worker-id-string)
       )

      (api/GET* "/:hive-uuid" []
            :tags ["hives"]
            :path-params [hive-uuid        :- (ring.swagger.schema/describe java.util.UUID "Hive uuid identifying the hive you are trying to get info on")]
            :summary "Retrieve data on a hive"
            :hive-access hive-access
            :return Hive
            (hives/show hive-uuid hive-access)
       )

      (api/GET* "/:hive-uuid/joins/pending" []
            :tags ["hives"]
            :path-params [hive-uuid        :- (ring.swagger.schema/describe java.util.UUID "Hive uuid identifying the hive you are trying to get info on")]
            :summary "Retrieve list of worker-id-strings trying to join a hive"
            :hive-access hive-access
            :return Hive
            (hives/pending-approvals hive-uuid hive-access)
       )

       (api/POST* "/:hive-uuid/joins/approve" []
            :tags ["hives"]
            :path-params [hive-uuid        :- (ring.swagger.schema/describe java.util.UUID "Hive uuid identifying the hive you are trying to get info on")]
            :query-params [worker-id-string :- (ring.swagger.schema/describe String "Plain-text identifier string for this worker")]
            :summary "Approve a hive to accept worker identified by worker-id-string"
            :return HiveWorkerApproval
            (hives/approve hive-uuid worker-id-string)
       )

       (api/POST* "/:hive-uuid/joins/reject" []
            :tags ["hives"]
            :path-params [hive-uuid        :- (ring.swagger.schema/describe java.util.UUID "Hive uuid identifying the hive you are trying to get info on")]
            :query-params [worker-id-string :- (ring.swagger.schema/describe String "Plain-text identifier string for this worker")]
            :summary "Reject a worker identified by worker-id-string"
            :return HiveWorkerApproval
            (hives/reject hive-uuid worker-id-string)
       )
    )

  (api/context* "/api/workers" []
    (api/GET* "/:worker-uuid/public-key" []
          :tags ["workers"]
          :path-params [worker-uuid :- (ring.swagger.schema/describe java.util.UUID "Uuid of the worker")]
          :summary "Returns the public key for a worker.  This is the public key the worker wants to use to connect to other servers via SSH"
          (workers/public-keys worker-uuid)
     )
  )
)

(defroutes internals
  (GET "/" [] "Hello World. Hivewing Web. Default Compojure Route")
  api
  (ANY "*" [] {:status 404 :body "Not Found"}))

(def application
  (logger/wrap-with-logger
    (-> internals
      (res/wrap-resource "public")
      (finfo/wrap-file-info))))
