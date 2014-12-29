(ns views.layout
  (:require [hivewing-web.session :as sess])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))
(comment
  (def side-menu-data [{:text "a" :href "/"}])
    (side-menu side-menu-data)
    (html (side-menu-layout "Content" {:side-menu side-menu-data}))
  )

(defn side-menu
  [menu-items back-link]
    [:div.pure-menu.pure-menu-open.side-menu
     (if back-link
      [:a.pure-menu-heading {:href (:href back-link)}
                [:span.fa.fa-chevron-left]
                [:span (:text back-link)]])
      [:ul
        (map #(vector :li {:class (if (:selected? %) "pure-menu-selected" nil)} [:a {:href (:href %)} (:text %)]) menu-items)
      ]
     ]
  )

(defn side-menu-layout
  [content params]
  (let [side-menu-data   (:side-menu params)
        back-link (:back-link params)]

    [:div.side-menu-layout
     (side-menu side-menu-data back-link)
      [:div.side-menu-content.content
        content
      ]
    ]
    )
  )

(defn render
  [req content & params-as-array]
  (let [
        logged-in? (sess/current-user req)
        flash (:flash req)
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

          "<!--  Purecss.io Copyright 2014 Yahoo! Inc. All rights reserved. -->"
          "<!--  Normalize.css Copyright (c) Nicolas Gallagher and Jonathan Neal -->"
          [:link {:href "/pure-release-0.5.0/pure.css"
                  :rel  "stylesheet"
                  :type "text/css"}]


          [:link {:rel "stylesheet"
                  :type "text/css"
                  :href "/css/application.css"}]
          [:link {:rel "stylesheet"
                  :type "text/css"
                  :href "/font-awesome-4.2.0/css/font-awesome.min.css"}]
          [:link {:href "http://fonts.googleapis.com/css?family=Dosis:300,400|Roboto+Slab:300,100,400"
                  :rel  "stylesheet"
                  :type "text/css"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
          ]
        [:body
          [:div.body-wrapper
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
                [:a.pure-button.pure-button-primary {:href "/login"} "Login" ])
              ]
              ]
             ]
            (if (not (empty? flash))
              [:div.flash flash]
              )
            [:div.content-wrapper
             (case style
              :side-menu (side-menu-layout content params)
              :default [:div.default.content content ]
              :single [:div.single.content
                       [:div.single-box content]])]

            [:footer
              [:div.footer-copyright
               [:div.container "© 2015 Copyright Hivewing.io"]]]
            ]]))))
