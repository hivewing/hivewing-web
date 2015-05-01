(ns hivewing-web.controllers.base
  (:require
    [ring.util.request :as ring-request]
    [ring.util.response :as r]
    [ring.util.codec :as ring-codec]
   ))

(defn no-value? [v]
  (or (not v) (and (seq? v) (empty? v))))

(defn dispatch [req & m-list]
    (let [ m (apply hash-map m-list)
           accept  (get (:headers req) "accept")
           accept? #(re-find (re-pattern (str "^" %)) accept)
           key     (cond
                     (accept? "application/json") :json
                     (accept? "text/html")        :html
                     :else                        :default)]
          ((key m))))

(comment
  (macroexpand '(with-required-parameters {} [a b c] (println "OK!")))
  (with-required-parameters {} [a b c] (println "OK!"))
  )

(defmacro with-required-parameters
  [request params-arr & body]
  (let [let-values   (map #(list (keyword %1) (list :params request)) params-arr)
        let-bindings (vec (interleave params-arr let-values))
        ]
    `(let ~let-bindings
       (if (and ~@params-arr)
         (do ~@body)
         (-> (r/response
               (str "Missing required parameters: " (clojure.string/join ","
                 (sort (reduce (fn [arr# [i# v#]]
                         (if (no-value? v#)
                           (conj arr# (nth '~(map str params-arr) i#))
                           arr#))
                       []
                       (map-indexed #(vector %1 %2) ~params-arr))))))
              (r/status 412)
              (r/content-type "text/html"))))))

(comment
  (macroexpand '(with-preconditions req [a nil b true] (println "OK")))
  (with-preconditions req [a 11 b "farafal"] (println "OK"))
  )

(defmacro with-preconditions
  [request let-bindings & body]
  (let [values-to-test-for   (take-nth 2 let-bindings)
        names-to-test-for    (map str values-to-test-for) ]
    `(let ~let-bindings
      (if (and ~@values-to-test-for)
        (do ~@body)
        (-> (r/response
              (str "Missing required precondition: " (clojure.string/join ","
                (sort (reduce (fn [arr# [i# v#]]
                       (if (no-value? v#)
                         (conj arr# (nth '~names-to-test-for i#))
                         arr#))
                     []
                     (map-indexed #(vector %1 %2) [~@values-to-test-for]))))))
              (r/status 412)
              (r/content-type "text/html"))))))
