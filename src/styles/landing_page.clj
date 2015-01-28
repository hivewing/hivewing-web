(ns styles.landing-page
  (:require
   [styles.colors :as colors]
   [garden.def :refer [defstyles]]
   [garden.color :as color :refer [hsl rgb]]
   [garden.stylesheet :refer [at-media]]
   [garden.arithmetic :as css-arith]
   [garden.units :refer [px]]))

(def all
  [:body.landing-page
    [:.navbar-custom {
                      :margin-bottom 0
                      :border-bottom "1px solid rgba(255,255,255,.3)"
                      :text-transform :smallcase
                      :font-family "Dosis,Helvetica,Arial,sans-serif"
                      :background-color colors/navbar-background
                      :text-shadow "0px 0px 20px #000"
                      }]

    [:.navbar-custom
     [:.navbar-brand {
                      :font-weight 700
                      }
      ["&:focus" { :outline 0 }]
      [:.navbar-toggle {
                        :padding "4px 6px"
                        :font-size "16px"
                        :color colors/navbar-toggle-color
                        }
       ["&:focus" "&:active" { :outline 0}]
       ]
      ]
     [:a {
          :color colors/navbar-link-color
          }]

     [:.nav
      [:li
       ["&.active" {
                    :outline :none;
                    :background-color colors/navbar-li-hover-background
                    }]
       [:a {
            :-webkit-transition "background .3s ease-in-out"
            :-moz-transition "background .3s ease-in-out"
            :transition "background .3s ease-in-out"
            }
        ["&:hover" "&:focus" "&:active" {
                                         :outline 0
                                         :background-color colors/navbar-li-hover-background
                                         }]
        ]
       ]
      ]
     ]



    [(at-media {:min-width "767px"})
     [:.navbar {
                :padding "20px 0"
                :border-bottom 0
                :letter-spacing "1px"
                :background "0 0"
                :-webkit-transition "background .5s ease-in-out,padding .5s ease-in-out"
                :-moz-transition "background .5s ease-in-out,padding .5s ease-in-out"
                :transition "background .5s ease-in-out,padding .5s ease-in-out"
                }]

     [:.top-nav-collapse {
                          :padding 0
                          :background-color colors/navbar-top-collapse-background
                          }
      ]

     [:.navbar-custom.top-nav-collapse {
                                        :border-bottom (str "1px solid "colors/navbar-li-hover-background)
                                        }]
     ]
    [:.intro {
      :display :table
      :width "100%"
      :height :auto
      :padding "100px 0"
      :text-align :center
      :color colors/intro-color
      :background "url(/images/hivewing-bg.jpg) no-repeat top center scroll"
      :background-color colors/intro-background-color
      :-webkit-background-size :cover
      :-moz-background-size :cover
      :background-size :cover
      :-o-background-size :cover
    }

      [:.intro-body {
        :display :table-cell
        :vertical-align :middle
        :box-shadow "inset 0px -30px 50px rgba(0,0,0,0.9)"
        }
        [:.brand-heading { :font-size "40px"
                           :font-weight 100
                           :text-shadow "0px 0px 100px #000"}]
        [:.intro-text {:font-size "18px"} ]

      ]
    ]

    [(at-media {:min-width "767px"})
      [:.intro {
          :height "100%"
          :padding "0"
        }
        [:.intro-body
          [:.brand-heading {:font-size "100px" }]
          [:.intro-text {:font-size "25px"}]
        ]
      ]
    ]

    [:.btn-circle {
       :width "70px"
       :height "70px"
       :margin-top "15px"
       :padding "7px 16px"
       :border "2px solid #fff"
       :border-radius "35px"
       :font-size "40px"
       :color colors/intro-color-more
       :background "0 0"
       :-webkit-transition "background .3s ease-in-out"
       :-moz-transition "background .3s ease-in-out"
       :transition "background .3s ease-in-out"
      }
      ["&:hover" "&:focus" {
          :outline 0
          :color colors/intro-color-more
          :background "rgba(255,255,255,.1)"
          }
          [:i.animated {
              :-webkit-animation-name :pulse
              :-moz-animation-name :pulse
              :-webkit-animation-duration "1.5s"
              :-moz-animation-duration "1.5s"
              :-webkit-animation-iteration-count :infinite
              :-moz-animation-iteration-count :infinite
              :-webkit-animation-timing-function :linear
              :-moz-animation-timing-function :linear
            }]
        ]
      [:i.animated {
        :-webkit-transition-property :-webkit-transform
        :-webkit-transition-duration "1s"
        :-moz-transition-property :-moz-transform
        :-moz-transition-duration "1s"
        }]
    ]

    [:.content-section { :padding-top "100px" }]

    [(at-media {:min-width "767px"})
      [:.content-section { :padding-top "250px" }]
    ]
  ])
