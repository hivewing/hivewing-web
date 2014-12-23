(ns views.apiary
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn index []
  [:div.text-center
    [:h1 "Your Apiaries!" ]])
