(ns views.system
  (:require [hivewing-web.session :as sess])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))


(defn not-found []
    [:div.container-fluid.single-page-wrapper
      [:div.text-center.single-page-body-content
        [:h1 "That's Not Good!"]
        [:p "Oh no, we can't find what you were looking for..."]
        [:p "We're really trying to be helpful here. Maybe try starting from the homepage?"]
        [:a.btn.btn-primary.btn-block {:href "/"} "Find a happier place"]]
      ]

  )
