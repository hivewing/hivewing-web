(ns styles.home
   (:require [styles.colors :as colors]))

(def all
  [[:#home_index
      {
       :text-align "center"
       }
      [:.kiss-step {
          :text-align "center"
          :background colors/kiss-background
          :color colors/light-text
          :overflow "hidden"
          :min-height "130px"
          }

          ["&.explain" {
                       :background colors/kiss-explain-background
                       :color colors/dark-text
                       :margin-top "25px"
                       :margin-bottom "125px"
                       }]
          [:h1 {
                :font-family "Dosis"
                :font-size "128px"
                :line-height "128px"
                :font-weight 100
              }
            ["span.emph" {
                            :font-style "italic"
                            :font-weight 800
                            :text-shadow (str "2px 2px 4px " colors/box-shadow)
                          }
            ]
           ]
           [".points"
              { :text-align "center"}
              [".point" { :margin "10px 50px"
                          :padding "20px 100px"
                          :border (str "1px solid " colors/kiss-explain-points-border)
                          :font-size "20px"
                          :line-height "70px"
                          :box-sizing "border-box"
                          :width "25%"
                          :display "inline-block" }

               [:.fa {:font-size "70px"
                      :float "left"
                      :color colors/kiss-explain-points-border
                      :margin-left "-80px"
                      }]
              ]
            ]
          ]


      [:#decisions {
          :display "inline-block"
          :width "90%"
          :height "600px"
          :margin 0
          :margin-top "50px"
          :background "rgba(220,220,220,0.6)"
        }
         [".node rect"
          ".node circle"
          ".node ellipse"
          ".node polygon"
          {:stroke "#333"
           :fill "#fff"
           :stroke-width "1.5px"
           }]
        [".edgePath path"
         {
           :stroke "#333"
           :fill "#333"
           :stroke-width "1.5px"
          }]

       ]
      ]
  ]
  )
