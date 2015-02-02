(ns views.helpers
  (:use ring.middleware.anti-forgery))

(defn log-list-item
  [log]
    (vector :li.list-group-item.clearfix [:span.col-sm-2 (:at log)] [:span.col-md-10 (:message log)]))
(defn anti-forgery-field
  "Create a hidden field with the session anti-forgery token as its value.
  This ensures that the form it's inside won't be stopped by the anti-forgery
  middleware."
  []
  [:input {:type "hidden" :name "__anti-forgery-token" :value *anti-forgery-token*}])
