(ns views.home
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn index []
          [:div.text-center
            [:h1 "Hivewing" ]]
          [:div.row
            [:div.col.s4 "Promo 1"]
            [:div.col.s4 "Promo 2"]
            [:div.col.s4 "Promo 3"]])
