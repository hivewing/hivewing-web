(ns views.layouts.side-menu
  (:require [views.layouts.components :refer :all]))

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
  (let [side-menu-data   (:side-menu params)
        back-link (:back-link params)]

    [:body.side-menu-layout
      (standard-header req)
      (flash req)
      (side-menu side-menu-data back-link)
      content
      (standard-footer req)
    ]
  ))
