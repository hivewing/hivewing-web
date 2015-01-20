(ns styles.application
 (:require
   [styles.worker :as worker]
   [styles.colors :as colors]
   [styles.fonts :as fonts]
   [styles.flash :as flash]
   [styles.helpers :as helpers]
   [styles.forms :as forms]
   [styles.home :as home]
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
    :background colors/main-background
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
       :color colors/logo-color}
      [:.end {:color colors/logo-end-color}]]

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
    {:background colors/header-background
     :padding "0 10px"
     :line-height header-height
     :height header-height
     :overflow "hidden"
     }]

  ; ----------------------
  ; FOOTER
  ; ----------------------
  [:footer {:text-align "center"
            :background colors/footer-background
            :color colors/light-text
            :padding [0 (px 10)]
            :position "absolute"
            :bottom 0
            :left 0
            :right 0
            :line-height footer-height
            } ]
  ;; Layouts
  [:div.content-wrapper
    {:padding-bottom footer-height}
  ]
  [:div.side-menu-layout
      {
        :position "relative"
        :margin (px 0)
        :padding "0 20px" ;(px 20)
        :background colors/main-background
        :height "100%"
      }
      [:div.side-menu-content
        {:padding  "0 20px 20px 230px"}
        [:h1 {:margin-top 0} ]
       ]
      [:div.side-menu
       [".pure-menu-heading"
          { :background colors/side-menu-heading-background
            :color colors/light-text
            :margin-top 0
          }
        [".fa.fa-chevron-left" { :position "relative" :left "-5px" }]]

       {
        :position :absolute
        :left "0px"
        :width (px 200)
        :background colors/side-menu-background
        :color colors/light-text
        :border-right (str "1px solid " colors/side-menu-border)
        :border-bottom (str "1px solid " colors/side-menu-border)
       }
      ]
   ]
  [:div.single.content
   ["&:before" {
      :content "\"\""
      :display "block"
      :position "absolute"
      :top header-height
      :left 0
      :right 0
      :bottom footer-height
      :z-index -1
      :background-color "rgba(0,0,0,0.2)"
   }]
   {
     :background colors/main-background
     :width "80%"
     :min-width (px 300)
     :max-width (px 500)
     :margin "50px auto"
     :box-shadow (str "0px 0px 10px " colors/box-shadow)
    }]
  ; ----------------------
  ; Buttons
  ; ----------------------
  helpers/all
  flash/all
  forms/all
  pure/themed
  worker/all
  home/all

  [:div.not-found
    { :padding (px 20) }
   ]

  [:span.header-sub
    {:font-weight 100
    :margin (px 10)
    }
   ]
  )
