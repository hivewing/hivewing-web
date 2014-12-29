(ns views.system
  (:require [hivewing-web.session :as sess])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))


(defn not-found []
  (html
    [:div.not-found.center
      [:h1.center "Not Found!"]
      [:p "Oh no, we can't find what you were looking for..."]
      [:p "We're really trying to be helpful here. Maybe try starting from the homepage?"]
      [:a.pure-button {:href "/"} "Find a happier place"]])
  )
