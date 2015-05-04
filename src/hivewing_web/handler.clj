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

(api/defapi api
  (api/swagger-ui
    "/api/console"
    :swagger-docs "/api/docs"
    )

  (api/swagger-docs "/api/docs")

  (api/context* "/api/hives" []
    (api/GET* "/:hive-uuid/join" []
          :path-params [hive-uuid        :- (ring.swagger.schema/describe String "Hive uuid identifying the hive you are trying to join")]
          :query-params [worker-id-string :- (ring.swagger.schema/describe String "Plain-text identifier string for this worker")]
          :summary "Bootstrapping for Hivewing's workers. Attempt to join a hive. "
          :return BootstrapAuthPackage
          (hives/join hive-uuid worker-id-string)
     )
  )

  (api/context* "/api/workers" []
    (api/GET* "/:worker-uuid/public-key" []
          :path-params [worker-uuid :- (ring.swagger.schema/describe String "Uuid of the worker")]
          :summary "Returns the public key for a worker.  This is the public key the worker wants to use to connect to other servers via SSH"
          (workers/public-keys worker-uuid)
     )
  )
)

(defroutes application
  (GET "/" [] "Hello World. Hivewing Web. Default Compojure Route")
  api
  (ANY "*" [] {:status 404 :body "Not Found"}))
