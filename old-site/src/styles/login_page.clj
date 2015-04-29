(ns styles.login-page
  (:require
   [styles.colors :as colors]))

(def all
  [:body.login
    [:.wrapper {
	    :margin-top "80px"
      :margin-bottom "80px"
      }]
    [:.form-signin {
      :max-width "380px"
      :padding "15px 35px 45px"
      :margin "0 auto"
      :background-color colors/single-page-body-bg
      :border "1px solid rgba(0,0,0,0.1);"
     }]
    [:.form-signin-heading {
	    :margin-bottom "30px"
      }]

    [:.form-control {
      :position :relative
      :font-size "16px"
      :height :auto
      :padding "10px"
      :box-sizing :border-box
      }
      ["&:focus" {
        :z-index 2
        }]
    ]

    ["input[type=\"text\"]" {
      :margin-bottom "-1px"
      :border-bottom-left-radius 0
      :border-bottom-right-radius 0
    }]

    ["input[type=\"password\"]" {
      :margin-bottom "20px"
      :border-top-left-radius 0
      :border-top-right-radius 0
    }]
  ])
