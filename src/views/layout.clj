(ns views.layout
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))


(defn render
  [content & params-as-array]
  (let [params (apply hash-map params-as-array)
        title  (or (:title params) "Hivewing.io")
        style  (or (:style params) :default)
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
                  :href "/materialize/css/materialize.css"}]
          [:link {:rel "stylesheet"
                  :type "text/css"
                  :href "/css/application.css"}]
          [:link {:rel "stylesheet"
                  :type "text/css"
                  :href "/font-awesome-4.2.0/css/font-awesome.min.css"}]
          [:script {:src "/materialize/js/materialize.js"}]
          [:link {:href "http://fonts.googleapis.com/css?family=Dosis:300,400|Roboto+Slab:300,100,400"
                  :rel  "stylesheet"
                  :type "text/css"}]
          ]
        [:body
          [:nav
           [:div.nav-wrapper
            [:a.brand-logo {:href "/"}
                [:span.start "Hivewing"]
                [:span.end   ".io"]]
            [:ul#nav-mobile.right.side-nav
              [:li [:a {:href=""} [:span.fa.fa-cogs]]]
              ]
            ]]
          (case style
            :default [:div.default.content content]
            :single [:div.single.content
                     [:div.single-box content]])

          [:footer
            [:div.footer-copyright
             [:div.container "© 2015 Copyright Hivewing.io"]]]]))))
