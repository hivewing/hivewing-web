(ns styles.colors
 (:require [garden.def :refer [defstyles]]
           [garden.color :as color :refer [hsl rgb]]
           [garden.units :refer [px]]))

(def body-bg "#f9f9f9")
(def body-text "#222")
(def link-color "#0773A1");;"#219ab3")
(def link-hover-color "#11505d")

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
(def landing-section-feature-block "#0773a1")
(def landing-section-border "#f7f7f7")
(def landing-section-odd-background "#0773a1")
(def landing-section-odd-color body-bg)

(def back-link-bg "rgb(77,152,184)")
(def back-link-border "rgba(77,152,184,1)")
(def back-link-color body-bg)

(def flash-bg "#fff")
(def flash-text body-text)
(def flash-border "#0773a1")
(def active-nav-pills-text body-text)


(def logo-color "#E6E9E5");;A6B0A9");;00741E");;20512d")
(def logo-end-color "#394233");;"#55685A")
(def light-text "#E6E9E5")
(def med-text "#777")
(def dark-text "#444");;"#444")
(def footer-background header-background) ;;"#3d6026")
(def main-background "#fffff8")
(def box-shadow "#00654b");;4a5b56")
(def side-menu-background "#ffffff");;"#4d98b8");;"#90AA97")
(def side-menu-heading-background "#0773a1");;"#4d98b8");; 008060");;90AA97")
(def menu-selected-background "rgba(77,152,184,0.5)");;"#4d98b8")
(def side-menu-border menu-selected-background);;"#00a67C");;#4d98b8")
(def primary "#0773a1")
(def secondary "#00bf32")

(def warning "#ff5f00")
(def error "#FF1e00")

(def kiss-background side-menu-heading-background)
(def kiss-explain-background main-background);;menu-selected-background)
(def kiss-explain-points-border side-menu-heading-background)

(def input-text dark-text)
(def light-background "#EDEDED")
(def light-background "#D0E1C4")
(def solid-background "#6D7A64")
