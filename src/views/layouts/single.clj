(ns views.layouts.single
  (:require [views.layouts.components :refer :all]))

(defn layout
  [req content params]
    [:body.default-layout
      (standard-header req)
      (flash req)
      content
      (standard-footer req)
    ]
  )
