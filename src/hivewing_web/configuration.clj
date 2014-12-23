(ns hivewing-web.configuration
  (:require [environ.core  :refer [env]]
            [taoensso.timbre :as logger]))

(def cookie-key
  (if (env :hivewing-web-cookie-key)
    (env :hivewing-web-cookie-key)
    (do
      (logger/warn "WARN. hivwing-web-cookie-key NOT set")
      "HIVEWING_COOKIE_KEY")))
