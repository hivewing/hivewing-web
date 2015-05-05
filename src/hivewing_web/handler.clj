(ns hivewing-web.handler
  (:require [compojure.api.sweet :as api ]
            [compojure.core :refer :all]
            [hivewing-web.controllers.workers :as workers]
            [hivewing-web.controllers.hives :as hives]
            [hivewing-web.controllers.base :as base]
            [ring.swagger.schema :refer [describe]]
            [ring.swagger.json-schema-dirty]
            [schema.core :as s]
            [ring.util.http-response :as r])
  )

(s/defschema BootstrapAuthPackage {:worker_uuid java.util.UUID
                                   :public_key String
                                   :private_key String})

(s/defschema Hive {:uuid java.util.UUID
                   :updated_at (s/maybe java.sql.Timestamp)
                   :created_at java.sql.Timestamp,
                   :name String})

(s/defschema HiveWorkerApproval {:hive_uuid java.util.UUID
                                 :worker_id_string String
                                 :updated_at (s/maybe java.sql.Timestamp)
                                 :created_at java.sql.Timestamp})


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
           ])

  (api/context* "/api/hives" []
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

(defroutes application
  (GET "/" [] "Hello World. Hivewing Web. Default Compojure Route")
  api
  (ANY "*" [] {:status 404 :body "Not Found"}))
