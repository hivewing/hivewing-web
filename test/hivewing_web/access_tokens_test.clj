(ns hivewing-web.access-tokens-test
  (:require [expectations :as e]
            [hivewing-web.data.users :as users]
            [hivewing-web.data.access-tokens :refer :all]))
(def valid-pw "p@ssWord!1")
(defn create-user [email]
  (users/create email valid-pw))

;; Lookup works
(e/expect-let [user (create-user "email")
               at (create user "access-token-1")]
        at
        (lookup (:token at)))

(e/expect-let [user (create-user "email")
               at (create user "access-token-1")]
        [at]
        (lookup-by-user user))
