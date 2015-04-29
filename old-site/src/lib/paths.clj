(ns lib.paths
  (:require [ring.util.codec :as ring-codec]
            [ring.util.request :as ring-request]
            [clojurewerkz.urly.core :as u]
            ))
(comment
  (add-path-parts "/first?chickn=4" "chicken/mom" "a" "b" )
  (lib.paths/add-path-parts "/apiary")
  (lib.paths/add-path-parts "/apiary")
  (squeeze "//asdf/asdf///" \/)
  )
(defn- squeeze [string character]
  (apply str
         (reverse
           (reduce
             (fn [x y]
               (if (and (= (first x) character)
                        (= y character))
                 x
                 (conj x y)))
             (cons '() (seq string))))))

(defn add-path-parts
  "Takes the base path (which can have a query param on it)
  and successively adds the parts onto the end."
  [base-path & more-parts]

  (let [[next-part & rest-parts] more-parts]
    (if next-part
      (let [new-path              (clojure.string/join "/"
                                        [base-path
                                         next-part])
            ]
          (recur new-path rest-parts))
        (squeeze (str "/" base-path) \/))))

(defn add-params-to-url
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

(comment
    (clojure.pprint/pprint (macroexpand '(defpath apiary-path [] "/apiary")))
    (clojure.pprint/pprint (macroexpand '(defpath apiary [param1 param2] "/apiary")))
    (defpath monk [monk-name monk-hair] (str "/monk/" monk-name "/" monk-hair))
    (apiary-path)
  )

(defmacro defpath
  [path-name parameters & parts]
    `(defn ~path-name [~@parameters & addl-params#]
       (let [url# (add-path-parts ~@parts)]
        (if (empty? addl-params#)
          url#
          (add-params-to-url url# (apply hash-map addl-params#)))
        )))
