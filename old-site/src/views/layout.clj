(ns views.layout
  (:require [hivewing-web.session :as sess]
            [views.layouts.default :as default]
            [views.layouts.single :as single]
            [hivewing-web.paths :as paths])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))


(defn render
  [req content & params-as-array]
  (let [
        logged-in? (sess/current-user req)
        params (apply hash-map params-as-array)
        title  (or (:title params) "Hivewing.io")
        style  (or (:style params) :default)]
    (html5 {:lang "en-US"}
      [:head
        [:link {:rel "shortcut icon" :type "image/png" :href "/favicon.png?v=4"}]
        [:meta {:http-equiv "Content-type"
                :content "text/html; charset=utf-8"}]
        [:meta {:name "viewport" :content "width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maxiumum-scale=1.0"}]

        [:title title]

        [:link {:href "http://fonts.googleapis.com/css?family=Dosis:300,400|Roboto+Slab:300,100,400"
                :rel  "stylesheet"
                :type "text/css"}]
        [:link {:rel "stylesheet"
                :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"}]

        [:link {:rel "stylesheet"
                :type "text/css"
                :href "//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"}]

        [:link {:rel "stylesheet"
                :type "text/css"
                :href "/css/application.css"}]

        [:script {:src "/js/modernizr-2.6.2.min.js"}]
        [:script {:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"}]
        [:script {:src "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"}]
        [:script {:src "/js/jquery.easing.min.js" }]
      ]
      (case style
        :default   (default/layout req content params)
        :none      content
        :single    (single/layout req content params)
      )
      ;; Lets hide those flash messages!
      [:script "$(document).ready(function() {$('#flash').delay(5000).slideUp(1000);});"]
      )))
