(ns hivewing-web.authentication
  (:require [compojure.api.sweet :refer :all]
            [hivewing-web.data.users :as users]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(defn authenticated? [uname pass]
  (users/validate uname pass))
