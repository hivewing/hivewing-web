(ns hivewing-web.handler
  (:require [compojure.api.sweet :as api ]
            [compojure.core :refer :all]
            [ring.util.http-response :as r])
  )

(api/defapi api
  (api/swagger-ui)
  (api/swagger-docs)
  (api/context* "/workers" []
    (api/GET* "/:worker-uuid/public-key" []
          :path-params [worker-uuid :- String]
          :summary "Returns the public key for a worker.  This is the public key the worker wants to use to connect to other servers via SSH"
          {:status 200
           :headers {"Content-Type" "text/plain"}
           :body "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC6NUfcIfToBISwY6ejUDCDRoqAAY7ndEPAgGfZL9tj1MqOqTpQiNTeQ19eL5tNQhCZwUH9IkqcGbl6z5E46+DPPoFstjnasZDC30/sH9FqF1E57XLf+3zkASsHykIcUXKPcqR0JfpI81u1mSnZQxe7hI3xKScYZl9lrfDy0vkDolBQnZxlo8a9rsRCj5pmdMuJx3b+Z1QY1fvly47LJGIpsKHGWSdgWp2GjVwSy5Py3L0acLKoZnAUtURvWt+hAk5VhNmpsNRgRUxO6uMp6H4k9CH21bBkFdgKGrtNYvP9PD814dz3T5FlO9UBahOeP8qVEpXX7wZhEI/7lzt3KsLr cfitzhugh@cfitz-server"
          }
     )
  )
)

(defroutes application
  (GET "/" [] "Hello World. Hivewing Web. Default Compojure Route")
  (context "/api" []
    api
  )
)
