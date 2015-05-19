(ns hivewing-web.hive-access-test
  (:require [expectations :as e]
            [hivewing-web.data.users :as users]
            [hivewing-web.data.hives :as hives]
            [hivewing-web.data.hive-access :refer :all]))

(def valid-pw "p@ssWord!1")
(defn create-user [email]
  (users/create email valid-pw))

(defn create-hive []  (hives/create))

;; Lookup works
(e/expect-let [user (create-user "email")
               hive (create-hive)
               ha (enable-access user hive)]
        [(:uuid hive)]
        (get-hives user))


;; Lookup works
(e/expect-let [user (create-user "email")
               hive (create-hive)
               ha (enable-access user hive)]
        [(:uuid user)]
        (get-users hive))

(e/expect-let [user (create-user "email")
               hive (create-hive)
               ha (enable-access user hive)
               dha (disable-access user hive)]
        []
        (get-users hive))

(e/expect-let [user (create-user "email")
               hive (create-hive)
               ha (enable-access user hive)
               dha1  (disable-access user hive)
               dha2 (disable-access user hive)
               dha3  (disable-access user hive)
               dha4  (disable-access user hive)]
        []
        (get-users hive))


(e/expect-let [user (create-user "email")
               hive (create-hive)
               ha1  (enable-access user hive)
               ha2  (enable-access user hive)
               ha3  (enable-access user hive)
               ha4  (enable-access user hive)]
        [(:uuid user)]
        (get-users hive))
