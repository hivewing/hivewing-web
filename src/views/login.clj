(ns views.login
  (:require [views.helpers :as helpers])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn login [action return-to]
    [:form {:method "POST" :action action}
     (helpers/anti-forgery-field)
     [:input {:type :hidden :name :return-to :value return-to}]
     [:div
        [:div.input-field
          [:input#user_email {:type "email" :placeholder "Email address" :name "user[email]"}]
        ]
        [:div.input-field
          [:input#user_password {:type "password" :placeholder "Password" :name "user[password]"}]
        ]
        [:div.controls
          [:button {:type "submit"} "Login"]
        ]
      ]
    ])
