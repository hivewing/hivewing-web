(ns views.layout
  (:require [hivewing-web.session :as sess])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))


(defn render
  [req content & params-as-array]
  (let [
        logged-in? (sess/current-user req)
        flash (:flash (:session req))
        params (apply hash-map params-as-array)
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
                  :href "/css/application.css"}]
          [:link {:rel "stylesheet"
                  :type "text/css"
                  :href "/font-awesome-4.2.0/css/font-awesome.min.css"}]
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
            [:div.side-nav
             (if logged-in?
              [:a {:href "/"} [:span.fa.fa-cogs]])
             (if logged-in?
              [:a {:href "/logout"} [:span.fa.fa-sign-out]]
              [:a {:href "/login"} [:button "Login" ]])
            ]
            ]
           ]
          (if (not (empty? flash))
            [:div.flash flash]
            )
          (case style
            :default [:div.default.content content]
            :single [:div.single.content
                     [:div.single-box content]])

          [:footer
            [:div.footer-copyright
             [:div.container "© 2015 Copyright Hivewing.io"]]]]))))
