(ns views.worker
  (:require [views.helpers :as helpers])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn status [req]
  [:div.text-center
    [:h1 "Worker status"]])

(defn manage [req]
  [:div.text-center
    [:h1 "Worker manage"]])

(defn config [req]
  [:div.text-center
    [:h1 "Worker config"]])

(defn data [req]
  [:div.text-center
    [:h1 "Worker data"]])

(defn logs [req]
  [:div.text-center
    [:h1 "Worker logs"]])
