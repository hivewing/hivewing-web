(ns views.layouts.components
  (:require [hivewing-web.session :as sess]
            [ring.util.request :as ring-request]
            [hivewing-web.paths :as paths]))

(defn standard-header
  [req]
  (let [logged-in? (sess/current-user req)]
    [:nav.navbar-default.navbar-fixed-top
     [:div.container-fluid
       [:div.navbar-header
        [:a.navbar-brand {:href "/"}
          [:span "Hivewing.io"]
        ]
       ]
       [:p.navbar-text.navbar-right
         (if logged-in?
            [:a {:href "/"} [:span.fa.fa-cogs]])
         (if logged-in?
            [:a {:href (paths/logout-path)} [:span.fa.fa-sign-out]]
            [:a.fa.fa-sign-in {:href (paths/login-path :return-to (ring-request/request-url req))} ])
         ]
     ]
    ]
  ))

(defn standard-footer
  [req]
  [:footer.navbar.navbar-fixed-bottom
    [:div.footer-copyright
     [:div.container "© 2015 Copyright Hivewing.io"]]]
  )


(defn flash
  [req]
  (let [flash (:flash req)]
    (if (not (empty? flash))
      [:div#flash.flash flash]
  )))
