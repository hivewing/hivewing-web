(ns styles.application
 (:require
   [styles.worker :as worker]
   [styles.colors :as colors]
   [styles.login-page :as login-page]
   [styles.landing-page :as landing-page]
   [styles.apiary :as apiary]
   [styles.hive   :as hive]
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
          :color colors/body-text
          :background-color colors/body-bg
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
  [:nav
   ["&.navbar-hivewing" {
        :border-radius 0
        :background colors/header-background
        :color colors/header-text
        :margin-bottom 0
        :border-bottom "1px solid rgba(255,255,255,.3)"
        :font-family "Dosis,Helvetica,Arial,sans-serif"
        :background-color colors/navbar-top-collapse-background
      }
      [:.navbar-brand { :color colors/header-text
                        :font-weight 700 }

        ["&:focus" { :outline 0 }]
        ["&:hover" { :color colors/link-hover-color}]
      ]

     [:a ".navbar-nav>li>a" {
          :color colors/navbar-link-color
          }
        ["&:hover" { :color colors/link-hover-color}]

      ]

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
  ]]
  [:.list-group-item :.list-group-item:first-child
  :.list-group-item:last-child {:border-radius 0}]
  [:.nav-pills-container
    [:.nav-pills
      [:li
        [:&.back-link :&.current {
                  :margin 0
                                  }
          [:a {
                       :border-right (str "1px solid " colors/back-link-border)
                       :font-weight 800
                       :font-size "28px"
                       :padding "0 10px"
                       :background colors/back-link-bg
                       :color colors/back-link-color
                       :margin "0px"
                      }
          ]
        ]
        [:&.current {
                     :margin-right "15px" :margin-left "15px" }
          [:a {:background colors/body-bg :color colors/body-text :border 0}]
         ]
        ["&>a" { :border-radius 0 } ]
        [:&.active
          [:a {:color colors/active-nav-pills-text
               :font-weight 800
               :border-radius 0
               :border-bottom (str "3px solid " colors/active-nav-pills-text)
               :background :none
               :padding-bottom "5px"
               :margin-bottom "20px"}]
        ]
      ]
    ]
  ]
  [:.btn-primary {
                  :background colors/button-primary
                  :color colors/button-primary-text
                  }]
  [:footer.navbar {
    :padding "50px 0"
    :color  colors/footer-color
    :background colors/footer-background
    :font-size "20px"
    :border-radius 0
    }]

  [".btn-group-lg>.btn" ".btn-lg" ".btn" {:border-radius 0 }]
  [:.form-control {:border-radius "1px"}]
  [:.input-group.spaced { :margin "20px 0"} ]

  [:.navbar
    [:navbar-brand {:position :relative }]
    [:span {:position :relative
            :z-index 1}]
      [:.hexagon-xs {
         :position :absolute
         :top "3px"
         :left "0px"
         :z-index 0
        }
      ]
      [:.navbar-brand { :position :absolute
                        :left "30px"}]
  ]
  [:.hexagon-xs {
    :text-align :center
    :font-size "15.399999999999999px"
    :margin "11px 0"
    :width "38.10511776632px"
    :height "22px"
    :background-color colors/link-color
    :position :relative
    :display :block
  }]
  [:.hexagon-xs:before
   :.hexagon-xs:after {
    :content "\" \""
    :width 0
    :height 0
    :position :absolute
    :border-left "19.05255888316px solid transparent"
    :border-right "19.05255888316px solid transparent"
    :left 0
    }]
  [:.hexagon-xs:before {
    :border-bottom (str "11px solid " colors/link-color)
    :top "-11px"
    }]
  [:.hexagon-xs:after {
    :border-top (str "11px solid " colors/link-color)
    :bottom "-11px"
  }]

  [:.margin-top-row {:margin-top "20px" }]
  [:.margin-bottom-row {:margin-bottom "20px" }]

  [:.flash  {
      :background colors/flash-bg
      :opacity 0.8
      :color colors/flash-text
      :text-align :center
      :border-bottom (str "1px solid " colors/flash-border)
      :border-top (str "1px solid " colors/flash-border)
      :position :absolute
      :left 0
      :right 0
      :z-index 100
      :line-height "50px"
      :font-size "22px"
      :cursor :pointer
    }]

  [:.default-content-body {
    :width "100%"
    :padding "20px 40px"
                           }]

  [:dl.extra-margin
    [:dd {:margin "10px 10px 10px 180px"}]]

  landing-page/all
  login-page/all
  apiary/all
  hive/all
  )
