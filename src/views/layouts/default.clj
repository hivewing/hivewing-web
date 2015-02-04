(ns views.layouts.default
  (:require [views.layouts.components :refer :all]
            [hivewing-web.paths :as paths]))

(defn layout
  [req content params]
  (let [back-link (:back-link params)
        current-name (:current-name params)
        sub-menu  (:sub-menu     params)]

    [:body.default-layout {:class (name (or (:body-class params) :default))}
      (flash req)
      (standard-header req)
      [:div.nav-pills-container
        [:ul.nav.nav-pills
          [:li.back-link [:a {:href (:href back-link) :title (:text back-link)} [:i.fa.fa-angle-double-left]]]
          [:li.current [:a {:href "#"} current-name]]

          (map #(vector
                  :li {:role :presentation :class (if (:active %) "active")}
                   [:a {:href (:href %)} (:text %)])
               sub-menu)]]

      [:div.default-content-body content ]
      (standard-footer req)
    ]))
