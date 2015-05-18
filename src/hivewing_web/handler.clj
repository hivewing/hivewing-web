(ns hivewing-web.handler
  (:require
    [compojure.api.sweet :as api ]
    [compojure.core :refer :all]
    [hivewing-web.authentication :as authentication]
    [ring.middleware.logger :as logger]
    [hivewing-web.controllers
       [workers :as workers]
       [hives :as hives]
       [base :as base]
    ]
    [hivewing-web.data
      [access-tokens :as access-tokens]
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

    (bauth/wrap-basic-authentication
      (api/context* "/api/tokens" {:as req}
        (api/GET* "/" []
            :tags ["tokens"]
            :summary "Get a list of all the tokens by name for the user. Requires Basic Auth"
            :return [AccessTokenBrief]
          (let [user (:basic-authentication req)
                tokens (access-tokens/lookup-by-user user)]
            (r/ok (map #(select-keys %1 [:name :created_at :updated_at :user_uuid]) tokens)))
        )
        (api/POST* "/" {:as req}
            :tags ["tokens"]
            :summary "Create a new access token"
            :body-params [name :- (ring.swagger.schema/describe access-tokens/name-regex "Token name (must be unique to the user)")]
            :return AccessToken
          (let [user (:basic-authentication req)
                token (access-tokens/create user name)]
            (r/ok token)))

        (api/DELETE* "/:token-name" {:as req}
            :tags ["tokens"]
            :summary "Delete an access token by name"
            :path-params [token-name :- (ring.swagger.schema/describe access-tokens/name-regex "Token name (must be unique to the user)")]
            :return s/Str
          (let [user (:basic-authentication req)
                v (println "USER: " user)
                v (println "token " token-name)
                token (access-tokens/delete! user token-name)]
            (r/ok "Deleted")))
        )
      authentication/authenticated?
      "Hivewing API : Token Management")


    (api/context* "/api/hives" []

      (api/GET* "/" []
            :tags ["hives"]
            :summary "List hive uuids"
            :return [java.util.UUID]
            (hives/index)
      )
      (api/POST* "/" []
            :tags ["hives"]
            :body-params [hive-name :- (ring.swagger.schema/describe #"[a-zA-Z\-0-9]{0,64}" "Hive name (must be globally unique)")]
            :summary "Create a named hive"
            :return Hive
            (hives/create hive-name) )

      (api/GET* "/:hive-uuid/join" []
            :tags ["hives" "worker-communication"]
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
            :return Hive
            (hives/show hive-uuid)
       )

      (api/GET* "/:hive-uuid/joins/pending" []
            :tags ["hives"]
            :path-params [hive-uuid        :- (ring.swagger.schema/describe java.util.UUID "Hive uuid identifying the hive you are trying to get info on")]
            :summary "Retrieve list of worker-id-strings trying to join a hive"
            :return Hive
            (hives/pending-approvals hive-uuid)
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
