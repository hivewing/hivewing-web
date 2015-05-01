(ns hivewing-web.handler
  (:require [compojure.api.sweet :as api ]
            [compojure.core :refer :all]
            [hivewing-web.controllers.workers :as workers]
            [ring.util.http-response :as r])
  )

(api/defapi api
  (api/swagger-ui)
  (api/swagger-docs)
  (api/context* "/workers" []
    (api/GET* "/:worker-uuid/public-key" []
          :path-params [worker-uuid :- String]
          :summary "Returns the public key for a worker.  This is the public key the worker wants to use to connect to other servers via SSH"
          (workers/public-keys worker-uuid)
     )
  )
)

(defroutes application
  (GET "/" [] "Hello World. Hivewing Web. Default Compojure Route")
  (context "/api" []
    api
  )
)
