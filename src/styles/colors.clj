(ns styles.colors
 (:require [garden.def :refer [defstyles]]
           [garden.color :as color :refer [hsl rgb]]
           [garden.units :refer [px]]))

;(def light-text "#fefefe")
;(def dark-text "#333")
;;(def primary "#20512d")

(def logo-color "#E6E9E5");;A6B0A9");;00741E");;20512d")
(def logo-end-color "#394233");;"#55685A")
(def header-background "#00a67C");;"#b5c8a9")
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
