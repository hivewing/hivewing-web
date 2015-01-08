(ns styles.buttons
   (:require [styles.colors :as colors]))

(def all
  [
    [:.button-error
      {:background "rgb(202, 60, 60)"
       :color "white"}
    ]
   [:button {
              :text-align "center"
              :font-family "Dosis"
              :font-size "20px"
              :background colors/solid-background
              :color colors/light-text
           }
    [:.raised
      {:box-shadow "0px 0px 3px #666"}
    ]
   ]]
  )
