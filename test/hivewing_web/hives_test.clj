(ns hivewing-web.hives-test
  (:require [expectations :as e]
            [hivewing-web.data.hives :refer :all]))


;; It is created w/ a name
(e/expect "a-valid-name" (:name (create "a-valid-name")))

;; It has a uuid
(e/expect (:uuid (create "a-valid-name")))

;; Lookup works
(e/expect-let [hive (create)]
        hive
        (lookup (:uuid hive)))

;; It is not ok to reuse names!
(e/expect-let [hive1 (create "a-valid-name")]
             org.postgresql.util.PSQLException
             (create "a-valid-name"))
