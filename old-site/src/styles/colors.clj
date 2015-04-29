(ns styles.colors
 (:require [garden.def :refer [defstyles]]
           [garden.color :as color :refer [hsl rgb]]
           [garden.units :refer [px]]))

(def body-bg "#f9f9f9")
(def body-text "#222")
(def link-color "#0e51a8");"#0773A1");;"#219ab3")
(def link-hover-color "#11505d")
(def primary "#0e51a8");"#0773a1")
(def button-primary link-color)
(def button-primary-text body-bg)

(def single-page-body-bg "#fff")

(def header-background "#00a67C");;"#b5c8a9")
(def header-text body-bg)
(def footer-background header-background) ;;"#3d6026")
(def footer-color body-bg)

(def navbar-toggle-color body-bg);;"#fff")
(def navbar-link-color body-bg);;"#fff")
(def navbar-li-hover-background "rgba(255,255,255,.3)")
(def navbar-top-collapse-background header-background)

(def intro-color "#eee")
(def intro-background-color "#fff000")
(def intro-color-more "#eee")
(def landing-section-feature-block "#0e51a8");"#0773a1")
(def landing-section-border "#f7f7f7")
(def landing-section-odd-background "#0e51a8");"#0773a1")
(def landing-section-odd-color body-bg)
(def landing-section-even-background "#ff9e00")
(def landing-section-even-color body-bg)

(def back-link-bg "rgb(77,152,184)")
(def back-link-border "rgba(77,152,184,1)")
(def back-link-color body-bg)

(def flash-bg "#fff")
(def flash-text body-text)
(def flash-border "#0e51a8");"#0773a1")
(def active-nav-pills-text body-text)
