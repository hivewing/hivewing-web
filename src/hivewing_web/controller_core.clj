(ns hivewing-web.controller-core
  (:require
            [ring.util.request :as ring-request]
            [ring.util.response :as r]
            [ring.util.codec :as ring-codec]
            [hivewing-web.system-controller :as system]
            [clojurewerkz.urly.core :as u]))

;; (pprint/pprint (macroexpand '(with-required-parameters req [happy gilmore] (println "yay"))))
(defn build-redirect-missing-param
  [param]
    `(->
        (r/redirect "/")
        (assoc :flash (str "Missing parameter: " '~param))))

(defmacro with-required-parameters
  [request params-arr & body]
  (let [let-values   (map #(list (keyword %1) (list :params request)) params-arr)
        let-bindings (vec (interleave params-arr let-values))
        conditions   (map #(list 'nil? %1) params-arr)
        actions      (map build-redirect-missing-param params-arr)
        ]
    `(let ~let-bindings
      (cond ~@(interleave conditions actions)
        true (do ~@body))
        )))

(comment
  (macroexpand '(with-resources req [hive (hiveget hive)]))
  (macroexpand '(with-resources req [hive (hive/hive-get hive-uuid)
                     worker (and (worker/worker-in-hive? worker-uuid hive-uuid)
                                 (worker/worker-get worker-uuid))]
                  (println "OH")))
  )

(defmacro with-resources
  [request let-bindings & body]
  (let [values-to-test-for   (take-nth 2 let-bindings)]
    `(let ~let-bindings
      (if (and ~@values-to-test-for)
        (do ~@body)
        (system/not-found ~request)
        ))))
(comment
  (go-to "/applesauce" {:pig "feet"})
  (go-to "/applesauce?token=123" {:apple "sauce"})
  (go-to "http://fitznet41.duckdns.local:3000/applesauce?token=123" {:return-to "123"})
  (ring.util.codec/form-decode "")
  )

(defn absolute-url-from-here
  "Absolute url based on the requesting client.
  Mainly so we can create a URL that can be used in a redirection
  chain, from external points (from another page back to ours)"
  ([req path] (absolute-url-from-here req path {}))
  ([req path params]
   (let [current-url (u/url-like (ring-request/request-url req))
         host (u/host-of current-url)
         port (u/port-of current-url)
         scheme (u/protocol-of current-url)
         query  (ring.util.codec/form-encode params)

         url-string (str scheme "://" host (if (> port 80) (str ":" port)) path)
         ]
     (str (-> (u/url-like url-string)
            (u/mutate-query query))))))

(defn go-to
  ([base-url] (go-to base-url {}))
  ([base-url added-params]
    (let [url (u/url-like base-url)
          current-query-str (or (u/query-of url))
          current-query (if (empty? current-query-str) {} (ring-codec/form-decode current-query-str))
          merged-query (merge current-query added-params)
          new-query  (ring-codec/form-encode (merge current-query added-params))
          new-url (u/mutate-query url new-query)
          ]
      (r/redirect (str new-url))
      )))

(defn login-and-return
  ([] (go-to "/login"))
  ([return-to] (go-to "/login" {:return-to return-to})))
