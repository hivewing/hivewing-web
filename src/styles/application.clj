(ns styles.application
 (:require
   [styles.colors :as colors]
   [styles.fonts :as fonts]
   [styles.buttons :as buttons]
   [styles.flash :as flash]
   [styles.helpers :as helpers]
   [styles.forms :as forms]
   [styles.pure :as pure]
   [garden.def :refer [defstyles]]
   [garden.color :as color :refer [hsl rgb]]
   [garden.arithmetic :as css-arith]
   [garden.units :refer [px]]))

(def footer-height (px 50))
(def header-height (px 50))

; Basing on http://fezvrasta.github.io/bootstrap-material-design/bootstrap-elements.html
(defstyles all
  [:html {:height "100%"}]
  [:body
   {:font-family "Roboto Slab"
    :font-size (px 16)
    :line-height 1.5
    :padding 0
    :background "url(/images/main-bg.png)"
    :height "100%"
    :margin 0}]

  [:div.body-wrapper {
    :min-height "100%"
    :position "relative"
                      }]
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
       :line-height header-height
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
      :line-height header-height
      :font-size (px 25)
      }
     ]
    {:background colors/solid-background
     :padding "0 10px"
     :line-height header-height
     }]

  ; ----------------------
  ; FOOTER
  ; ----------------------
  [:footer {:text-align "center"
            :border-top (str "1px " "solid " colors/solid-background)
            :color colors/secondary
            :padding [0 (px 10)]
            :position "absolute"
            :bottom 0
            :left 0
            :right 0
            :line-height footer-height
            :background "white"} ]
  ;; Layouts
  [:div.content-wrapper
    {:padding-bottom footer-height}
  ]
  [:div.side-menu-layout
      {
        :position "relative"
        :margin (px 0)
        :padding "0 20px" ;(px 20)
        :background "white"
        :height "100%"
      }
      [:div.side-menu-content
        {:padding  "0 20px 20px 230px"}
        [:h1 {:margin-top 0} ]
       ]
      [:div.side-menu
       [".pure-menu-heading"
          { :background colors/primary
            :color "white"
            :margin-top 0
          }
        [".fa.fa-chevron-left" { :position "relative" :left "-5px" }]]

       {
        :position :absolute
        :left "0px"
        :top 0
        :width (px 200)
        :background colors/light-background
        :border 0
       }
      ]
   ]
  [:div.single.content
   { :background "white"
     :width "80%"
     :min-width (px 300)
     :max-width (px 500)
     :margin "50px auto"
     :box-shadow (str "0px 0px 4px " colors/box-shadow)
    }]
  ; ----------------------
  ; Buttons
  ; ----------------------
  helpers/all
  flash/all
  buttons/all
  forms/all
  pure/themed

  [:div.not-found
    { :padding (px 20) }
   ]

  [:span.header-sub
    {:font-weight 100
    :margin (px 10)
    }
   ]
  )
