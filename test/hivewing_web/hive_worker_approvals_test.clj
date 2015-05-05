(ns hivewing-web.hive-worker-approvals-test
  (:require [expectations :as e]
            [hivewing-web.data.workers :as workers]
            [hivewing-web.data.hives :as hives]
            [hivewing-web.data.hive-worker-approvals :refer :all]))

(e/expect false (is-approved? {:approval nil}))
(e/expect true  (is-unknown?  {:approval nil}))
(e/expect false (is-rejected? {:approval nil}))

(e/expect true  (is-approved? {:approval true}))
(e/expect false (is-unknown?  {:approval true}))
(e/expect false (is-rejected? {:approval true}))

(e/expect false (is-approved? {:approval false}))
(e/expect false (is-unknown?  {:approval false}))
(e/expect true  (is-rejected? {:approval false}))

;; It is created w/ a name
(e/expect-let [hive (hives/create)]
  nil
  (:approval (lookup hive "worker-id-string")))

(e/expect-let [hive (hives/create)]
  nil
  (:approval (lookup hive "worker-id-string")))

(e/expect-let [hive (hives/create)
               hqa (create hive "worker-id-string")]
  nil
  (:approval (lookup hive "worker-id-string")))

(e/expect-let [hive (hives/create)
               wids "worker-id-string"
               hqa (create hive wids true)
               ]
  true
  (:approval (lookup hive wids)))

(e/expect-let [hive (hives/create)
               hqa (create hive "worker-id-string" false)]
  false
  (:approval (lookup hive "worker-id-string")))

(e/expect-let [hive (hives/create)
               hqa  (create hive "worker-id-string" true)
               deleted (delete! hive "worker-id-string")]
  nil
  (:approval (lookup hive "worker-id-string")))

(e/expect-let [hive (hives/create)
               hqa  (create hive "worker-id-string" true)
               hqa2 (create hive "worker-id-string" false)]
  false
  (:approval (lookup hive "worker-id-string")))
