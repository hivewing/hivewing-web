(ns views.layouts.components
  (:require [hivewing-web.session :as sess]
            [ring.util.request :as ring-request]
            [hivewing-web.paths :as paths]))

(defn standard-header
  [req]
  (let [logged-in? (sess/current-user req)]
    [:nav.navbar.navbar-default.navbar-hivewing
     [:div.container-fluid
       [:div.navbar-header
        [:a.navbar-brand {:href "/"}
          [:span "Hivewing"]
          [:span.light ".io"]
          [:div.hexagon-xs "&nbsp;"]
        ]
       ]
       [:div.navbar-right.collapse.navbar-collapse.navbar-main-collapse
         [:ul.nav.navbar-nav
           (if logged-in?
            [:li [:a {:href "/"} " Settings"]])
           (if logged-in?
            [:li [:a {:href (paths/logout-path)} " Sign Out"]]
            [:li [:a {:href (paths/login-path :return-to (ring-request/request-url req))} "Sign In"]])
         ]
      ]
     ]
    ]
  ))

(defn standard-footer
  [req]
  [:footer.navbar.navbar-fixe-bottom.navbar-collapse
    [:div.footer-copyright
     [:div.container "© 2015 Copyright Hivewing.io"]]]
  )

(defn flash
  [req]
  (let [flash (:flash req)]
    (if (not (empty? flash))
      [:div#flash.flash {:onclick "$('#flash').remove();"} flash]
  )))
