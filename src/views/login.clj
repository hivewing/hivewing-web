(ns views.login
  (:require [views.helpers :as helpers])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn login [action return-to]
    [:form.pure-form.pure-form-stacked {:method "POST" :action action}
     [:h1 "Login"]
     (helpers/anti-forgery-field)
     [:input {:type :hidden :name :return-to :value return-to}]
     [:input#user_email {:type "email" :placeholder "Email address" :name "user[email]"}]
     [:input#user_password {:type "password" :placeholder "Password" :name "user[password]"}]
     [:button.pure-button.pure-button-primary {:type "submit"} "Login"]
    ])
