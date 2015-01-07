(ns hivewing-web.controller-core
  (:require
    [clojure.pprint :as pprint]
    [hivewing-web.session :as session]
    [taoensso.timbre :as logger]
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
  (macroexpand '(with-preconditions req [hive (hiveget hive)]))
  (pprint/pprint (macroexpand '(with-preconditions req [hive (hive/hive-get hive-uuid)
                     worker (and (worker/worker-in-hive? worker-uuid hive-uuid)
                                 (worker/worker-get worker-uuid))]
                  (println "OH"))))
  (pprint/pprint (macroexpand '(with-beekeeper req bk (println "hi"))))
  )

(defmacro with-beekeeper
  [req beekeeper & body]
  `(let [~beekeeper (session/current-user ~req)]
      (if ~beekeeper
        ~@body
        (login-and-return (ring-request/request-url ~req)))))

(defmacro with-preconditions
  [request let-bindings & body]
  (let [values-to-test-for   (take-nth 2 let-bindings)
        names-to-test-for    (map str values-to-test-for) ]
    `(let ~let-bindings
      (logger/info (str "Needs resources: " ~(clojure.string/join ", " (map str values-to-test-for))))

      (if (and ~@values-to-test-for)
        (do ~@body)
        (do
          (logger/info "Failed to find all resources")

          (doseq [[name# value#] ~(zipmap names-to-test-for values-to-test-for)]
            (if (not value#)
              (logger/info (str name# " is MISSING"))))
          (system/not-found ~request))
        ))))

(def content-types
  "The content types and full headers that are responded"
  {:html "text/html; charset=utf-8"})

(defn render
  [body & opts]
  (let [opts (apply hash-map opts)
        status        (or (:status opts) 200)
        content-type  (or (:content-type opts) :html)
        ]
    (-> (r/response body)
        ;; Set the content type
        (r/header "Content-Type" (content-type content-types))
        )
    ))


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

(defn same-url-with-new-params
  [base-url added-params]
  (let [url (u/url-like base-url)
        current-query-str (or (u/query-of url))
        current-query (if (empty? current-query-str) {} (clojure.walk/keywordize-keys (ring-codec/form-decode current-query-str)))
        merged-query (merge (clojure.walk/keywordize-keys current-query )
                            (clojure.walk/keywordize-keys added-params)
                            ;; Get rid of these ALWAYS
                            {:__anti-forgery-token nil})

        merged-query (into {} (filter (comp not nil? val) merged-query))
        new-query  (ring-codec/form-encode merged-query)
        new-url (u/mutate-query url new-query)
        ]
    (str new-url)))

(defn go-to
  ([base-url] (go-to base-url {}))
  ([base-url added-params]
    (let [url (same-url-with-new-params base-url added-params)]
      (r/redirect url))))

(defn login-and-return
  ([] (go-to "/login"))
  ([return-to] (go-to "/login" {:return-to return-to})))
