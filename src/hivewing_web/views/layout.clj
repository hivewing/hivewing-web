(ns hivewing-web.views.layout
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))


(defn render
  [content & params-as-array]
  (let [params (apply hash-map params-as-array)
        title  (or (:title params) "Hivewing.io")
        ]
    (html
      (doctype :xhtml-strict)
      (xhtml-tag "en"
        [:head
          [:meta {:http-equiv "Content-type"
                  :content "text/html; charset=utf-8"}]
          [:title title]
          [:link {:rel "stylesheet"
                  :type "text/css"
                  :href "/css/application.css"}]

          [:link {:href "http://fonts.googleapis.com/css?family=Raleway:500|Roboto+Slab:300,100,400"
                  :rel  "stylesheet"
                  :type "text/css"}]
          ]
        [:body content]))))
