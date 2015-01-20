(ns views.home
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn index []
          [:div#home_index
            [:div.kiss-step
             [:h1 [:span.emph "Deploy"]]]
            [:div.kiss-step
             [:h1 [:span.emph "Direct"]]]
            [:div.kiss-step
             [:h1 [:span.emph "Discover"]]]

            [:div.kiss-step
              [:h1
               "Keep it "
                [:span.emph "simple"]
                "."
              ]
            ]
            [:div.kiss-step.explain.pure-u-1-1
              [:h2 "Manage your remote applications from the web."]
              [:div.points
               [:div.point
                [:span.fa.fa-question-circle]
                "Know what is running"
                ]
               [:div.point
                [:span.fa.fa-refresh]
                "Update via Git"
                ]
               [:div.point
                [:span.fa.fa-link]
                "Secure Communications"
                ]
               ]
            ]
            [:div.kiss-step
              [:h1
               "Keep it "
                 [:span.emph "connected"]
               "."]
            ]
            [:div.kiss-step.explain.pure-u-1-1
              [:h2 "Cloud-based APIs for your applications"]
              [:div.points
               [:div.point
                [:span.fa.fa-cog]
                "Set configuration"
                ]
               [:div.point
                [:span.fa.fa-cloud-download]
                "Send events down"
                ]
               [:div.point
                [:span.fa.fa-bullhorn]
                "Receive data up"
                ]
               ]
            ]
            [:div.kiss-step
              [:h1
               "Keep it "
                 [:span.emph "healthy"]
               "."]
            ]
            [:div.kiss-step.explain.pure-u-1-1
              [:h2 "Remotely debug and keep your herd of "
                   [:a {:href "http://placekitten.com"} "cats"] " moving"]
              [:div.points
               [:div.point
                [:span.fa.fa-terminal]
                "Remote SSH to a device"
                ]
               [:div.point
                [:span.fa.fa-align-justify]
                "Real-time logs"
                ]
               ]
            ]
          ])
