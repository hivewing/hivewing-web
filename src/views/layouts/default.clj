(ns views.layouts.default
  (:require [views.layouts.components :refer :all]
            [hivewing-web.paths :as paths]))

(defn side-menu
  [menu-items back-link]
    [:div.pure-menu.pure-menu-open.side-menu
     (if back-link
      [:a.pure-menu-heading {:href (:href back-link)}
                [:span.fa.fa-chevron-left]
                [:span (:text back-link)]])
      [:ul
        (map #(vector :li
                      {:class (str (if (:disabled? %) "pure-button-disabled") (if (:selected? %) "pure-menu-selected" nil))}
                      [:a {:href (if (:disabled? %) "#" (:href %))} (:text %)]) menu-items)
      ]
     ]
  )

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
          [:li.back-link [:a {:href (:href back-link)} [:i.fa.fa-angle-double-left]]]
          [:li.current [:a {:href "#"} current-name]]

          (map #(vector
                  :li {:role :presentation :class (if (:active %) "active")}
                   [:a {:href (:href %)} (:text %)])
               sub-menu)]]

      [:div.default-content-body content ]
      (standard-footer req)
    ]))
