(ns hivewing-web.workers-controller-test
  (:require [expectations :as e]
            [hivewing-web.data.workers :as workers]
            [hivewing-web.data.worker-public-keys :as wpks]
            [ring.mock.request :as mock]
            [hivewing-web.controllers.workers :refer :all]
            ))


;; No pk
(e/expect-let [worker (workers/create)
               w-uuid (:uuid worker)]
    404 (:status (public-keys "12345678-1234-1234-1234-123456789012")))

(e/expect-let [worker (workers/create)
               pk "PublicKey"
               wpk (wpks/create worker pk)
               w-uuid (:uuid worker)]
    200 (:status (public-keys w-uuid)))
(e/expect-let [worker (workers/create)
               pk "PublicKey"
               wpk (wpks/create worker pk)
               w-uuid (:uuid worker)]
    pk (:body (public-keys w-uuid)))
