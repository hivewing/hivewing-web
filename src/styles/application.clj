(ns styles.application
 (:require
   [styles.worker :as worker]
   [styles.colors :as colors]
   [styles.landing-page :as landing-page]
   [garden.def :refer [defstyles defkeyframes]]
   [garden.color :as color :refer [hsl rgb]]
   [garden.stylesheet :refer [at-media]]
   [garden.arithmetic :as css-arith]
   [garden.units :refer [px]]))

(defkeyframes pulse-keyframes
  [:from {:opacity 0.8}]
  [:to   {:opacity 1.0}]
  )

(defstyles all
  [:body {
          :width "100%"
          :height "100%"
          :font-family "Dosis,Helvetica,Arial,sans-serif"
          :color "#fff"
          :background-color "#000"
          }]

  pulse-keyframes

  [:html {
          :width "100%"
          :height "100%"
          }]
  ;;
  [:h1 :h2 :h3 :h4 :h5 :h6 {
                            :margin "0 0 35px"
                            :font-family "Dosis,Helvetica,Arial,sans-serif"
                            :font-weight 700
                            :letter-spacing "1px"
                            }]

  [:p {
       :margin "0 0 25px"
       :font-size "18px"
       :line-height "1.5"
       }
   ]
  [(at-media {:min-width "767px"})
   [:p {
        :margin "0 0 35px"
        :font-size  "20px"
        :line-height 1.6
        }]
   ]

  [:a {
       :color colors/link-color
       :-webkit-transition "all .2s ease-in-out"
       :-moz-transition "all .2s ease-in-out"
       :transition "all .2s ease-in-out"
       }
   "&:hover" "&:focus" {
                        :text-decoration :none
                        :color colors/link-hover-color
                        }]

  [:.light {
            :font-weight 400
            }]
  landing-page/all
  [:footer {
    :padding "50px 0"
    :background colors/footer-background
    }]
  )
