(ns hivewing-web.workers-controller-test
  (:require [expectations :as e]
            [hivewing-web.data.workers :as workers]
            [hivewing-web.data.worker-key-pairs :as wkps]
            [ring.mock.request :as mock]
            [hivewing-web.controllers.workers :refer :all]
            ))


;; No pk
(e/expect-let [worker (workers/create)
               w-uuid (:uuid worker)]
    404 (:status (public-keys "12345678-1234-1234-1234-123456789012")))

(e/expect-let [worker (workers/create)
               pk "PublicKey"
               wkp (wkps/create worker)
               w-uuid (:uuid worker)]
    200 (:status (public-keys w-uuid)))

(e/expect-let [worker (workers/create)
               wkp (wkps/create worker)
               pk (:public_key wkp)
               w-uuid (:uuid worker)]
    (str "ssh-rsa "
         pk
         " "
         w-uuid
         "@workers.hivewing.io")
         (:body (public-keys w-uuid)))
