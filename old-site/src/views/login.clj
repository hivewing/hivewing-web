(ns views.login
  (:require [views.helpers :as helpers])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn login [action return-to]
    [:div.wrapper
      [:form.form-signin {:method "POST" :action action}
        [:h2.form-signin-heading "Hivewing.io login"]
        (helpers/anti-forgery-field)
        [:input {:type :hidden :name :return-to :value return-to}]
        [:input#user_email.form-control {:type "email" :placeholder "Email address" :name "user[email]"}]
        [:input#user_password.form-control {:type "password" :placeholder "Password" :name "user[password]"}]
        [:button.btn.btn-lg.btn-primary.btn-block {:type "submit"} "Login"]
      ]
    ])
