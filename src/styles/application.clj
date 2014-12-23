(ns styles.application
 (:require
   [styles.colors :as colors]
   [styles.fonts :as fonts]
   [styles.buttons :as buttons]
   [styles.forms :as forms]
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
      {:text-decoration "none"
       :font-weight 300
       :font-family "Dosis"
       :font-size (px 25)
       :line-height (px 50)
       :letter-spacing (px 4)
       :color colors/primary}
      [:.end {:color colors/secondary}]]

    [:.side-nav

     [:a { :display :inline
           :color colors/light-text
           :line-height (px 50)
           :font-size (px 25)
           :margin-left (px 20)}]
     {:position "absolute"
      :right (px 10)
      :top   "0"
      :line-height (px 50)
      :font-size (px 25)
      }
     ]
    {:background colors/solid-background
     :padding "0 10px"
     :line-height (px 50)
     }]

  ; ----------------------
  ; FOOTER
  ; ----------------------
  [:footer {:text-align "center"
            :border-top (str "2px" "solid" colors/solid-background)
            :color colors/secondary
            :padding [0 (px 10)]
            :background colors/light-background} ]


  ; ----------------------
  ; Buttons
  ; ----------------------
  buttons/all
  forms/form-inputs
  )
