(ns styles.application
 (:require
   [styles.colors :as colors]
   [styles.fonts :as fonts]
   [garden.def :refer [defstyles]]
   [garden.color :as color :refer [hsl rgb]]
   [garden.units :refer [px]]))

; Basing on http://fezvrasta.github.io/bootstrap-material-design/bootstrap-elements.html
(defstyles all
  [:body
   {:font-family "Roboto Slab"
    :font-size (px 16)
    :line-height 1.5
    :padding 0
    :margin 0}]
  [:h1
   {:font-family "Dosis"
    :font-size (px 32)
    :font-weight 400
    :line-height 2
    }]

  ; Here we define the header
  [:nav
    [:.brand-logo
      {:font-weight 300
       :font-family "Dosis"
       :line-height (px 60)
       :letter-spacing (px 4)
       :color colors/primary}
      [:.end {:color colors/secondary}]]

    [:.fa-cogs { :color colors/light-text
                 :line-height (px 65)
                 :font-size (px 30) }]
    {:background colors/solid-background
     :padding "0 10px"}]
  [:footer {:text-align "center"
            :border-top (str "2px" "solid" colors/solid-background)
            :color "white"
            :padding [0 (px 10)]} ]
  )
