(ns hivewing-web.handler
  (:require [compojure.api.sweet :as api ]
            [compojure.core :refer :all]
            [hivewing-web.controllers.workers :as workers]
            [hivewing-web.controllers.base :as base]
            [ring.util.http-response :as r])
  )

(api/defapi api
  (api/swagger-ui
    "/api/console"
    :swagger-docs "/api/docs"
    )

  (api/swagger-docs "/api/docs")

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
