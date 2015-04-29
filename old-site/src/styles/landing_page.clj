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
                      :font-family "Dosis,Helvetica,Arial,sans-serif"
                      :background-color colors/navbar-top-collapse-background
                      }
    ]

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
            :-webkit-transition "all .3s ease-in-out"
            :-moz-transition "all .3s ease-in-out"
            :transition "all .3s ease-in-out"
            }
        ["&:hover" "&:focus" "&:active" {
                                         :outline 0
                                         :background-color colors/navbar-li-hover-background
                                         }]
        ]
       ]
      ]
     ]



   (at-media {:min-width "767px"}
     [:.navbar {
                :padding "20px 0"
                :border-bottom 0
                :letter-spacing "1px"
                :background "0 0"
                :-webkit-transition "all .5s ease-in-out,padding .5s ease-in-out"
                :-moz-transition "all .5s ease-in-out,padding .5s ease-in-out"
                :transition "all .5s ease-in-out,padding .5s ease-in-out"
                }]

     [:.top-nav-collapse {
                          :padding 0
                          :background-color colors/navbar-top-collapse-background
                          }
     ]

     [:.navbar-custom.top-nav-collapse {
                                        :border-bottom (str "1px solid "colors/navbar-li-hover-background)
                                        }]
    )

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
        }
        [:.brand-heading { :font-size "40px"
                           :font-weight 100
                           :text-shadow "0px 0px 100px #000"}
        ]
        [:.intro-text {:font-size "18px"} ]

      ]
    ]

    (at-media {:min-width "767px"}
      [:.intro {
          :height "100%"
          :padding "0"
        }
        [:.intro-body
          [:.brand-heading {:font-size "100px" }]
          [:.intro-text {:font-size "25px"}]
        ]
      ]
    )

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
       :-webkit-transition "all .3s ease-in-out"
       :-moz-transition "all .3s ease-in-out"
       :transition "all .3s ease-in-out"
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

    [:.content-section {
                        :padding-top "100px"
                       }
      [:.feature-point-row { :padding "0 25px" }]
      [:.feature-point
        {:display :inline-block}
        [:.wrapper {
          :margin "15px"
          :padding "15px"
          :-webkit-transition "all .3s ease-in-out"
          :-moz-transition "all .3s ease-in-out"
          :transition "all .3s ease-in-out"
         }
           ["&:hover" {
                     :background "#fff"
                      }]
           [:h3 {
                 :padding-bottom :10px
                 :border-bottom (str "3px solid " colors/landing-section-feature-block)
                 :margin-bottom :10px
                 :text-align :center
                 }]
           [:p {:font-size "14px"
                :text-align :left}]
           [:ul {:text-align :left
                 :margin :15px
                 :padding 0}]
        ]
      ]
    ]

    (at-media {:max-width "767px"}
      [:.feature-point { :width "100%" }]
      )

    [".content-section.odd" {
      :padding-top "100px"
      :padding-bottom "100px"
      :margin-top "50px"
      :margin-bottom "20px"
      :color colors/landing-section-odd-color
      :background colors/landing-section-odd-background
      :border-top (str "1px solid " colors/landing-section-border)
      :border-bottom (str "1px solid " colors/landing-section-border)
      }
    ]
    [".content-section.even" {
      :padding-top "100px"
      :padding-bottom "100px"
      :margin-top "50px"
      :margin-bottom "20px"
      :color colors/landing-section-even-color
      :background colors/landing-section-even-background
      :border-top (str "1px solid " colors/landing-section-border)
      :border-bottom (str "1px solid " colors/landing-section-border)

                              }]
    [:hr.even {:border-color colors/landing-section-even-background
               :border-width "50px"}]

    (at-media {:min-width "767px"}
      [:.content-section { :padding-top "125px"
                           :padding-bottom "125px"}]
    )

    [:.btn {
     :text-transform :uppercase
     :font-family "Dosis,Helvetica,Arial,sans-serif"
     :font-weight 400
     :-webkit-transition "all .3s ease-in-out"
     :-moz-transition "all .3s ease-in-out"
     :transition "all .3s ease-in-out"
    }]
    [:.btn-default {
      :border (str "1px solid " colors/link-color)
      :color colors/link-color
      :background-color :transparent
      }]

    [:.btn-default:hover
     :.btn-default:focus {
      :outline 0
      :color "#000"
      :background-color colors/link-color
    }]

    [:ul.banner-social-buttons {
      :margin-top 0
      }]

    (at-media {:max-width "1199px"}
      [:ul.banner-social-buttons {
        :margin-top "15px"
      }])

    (at-media {:max-width "767px"}
      [:ul.banner-social-buttons
       [:li {
        :display :block
        :margin-bottom "20px"
        :padding 0
        }]

       [:li:last-child {
         :margin-bottom 0
        }]
      ])

  [:footer {
    :padding "50px 0"
    :color  colors/footer-color
    :background colors/footer-background
    :font-size "20px"
    :border-radius 0
    :text-align :center
    }]

  ])
