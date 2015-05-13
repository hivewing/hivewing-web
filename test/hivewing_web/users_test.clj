(ns hivewing-web.users-test
  (:require [expectations :as e]
            [hivewing-web.data.users :refer :all]))
(def valid-pw "p@ssWord!1")

;; Lookup works
(e/expect-let [user (create "email" valid-pw)]
        user
        (lookup (:uuid user)))

(e/expect-let [user (create "email" valid-pw)]
        user
        (lookup-by-email "email"))

;; Fails to create
(e/expect
          java.lang.Exception
          (create "emailz" "pwbad"))

(e/expect-let [u1 (create "email" valid-pw)]
          java.lang.Exception
          (create "email" valid-pw))
