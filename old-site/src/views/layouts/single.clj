(ns views.layouts.single
  (:require [views.layouts.components :refer :all]))

(defn layout
  [req content params]
    [:body.single-layout {:class (name (or (:body-class params) :none))}
      (standard-header req)
      (flash req)
      content
      (standard-footer req)
    ]
  )
