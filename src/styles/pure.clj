(ns styles.pure
   (:require [styles.colors :as colors]))

(def themed
  [
    [".pure-menu a"
     ".pure-menu .pure-menu-can-have-children > li:after"
      {:color colors/med-text }
     ]
    [:.pure-button
     {:background colors/primary
      :color "white"
      }
      ["&.button-error" {
        :background colors/error
                  }]
     ]
    [:.pure-button-primary :.pure-button-selected
      :a.pure-button-primary :a.pure-button-selected
     { :background-color colors/primary
       :color "#fff"}]
    [:.pure-menu
     [:li:hover "li a:hover" :li.pure-menu-selected
      {:background-color colors/menu-selected-background }
     ]]
  ]
  )
