(ns views.home
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn index []
  [:body.landing-page
    [:nav.navbar.navbar-custom.navbar-fixed-top {:role :navigation}
      [:div.container
        [:div.navbar-header
         [:a.navbar-brand.page-scroll {:href "#page_top"}
            "Hivewing."
            [:span.light "io"]]
        ]
        [:div.collapse.navbar-collapse.navbar-right.navbar-main-collapse
          [:ul.nav.navbar-nav
            "<!-- Hidden li included to remove active class from about link when scrolled up past about section -->"
            [:li.hidden
              [:a {:href "#page_top"}]]
            [:li
              [:a.page-scroll {:href "#about"} "About" ]]
            [:li
              [:a.page-scroll {:href "#get_started"} "Get Started"]]
            [:li
              [:a.page-scroll {:href "#contact_us"} "Contact"]]
          ]
        ]
      ]
    ]
    [:header.intro {:id "page_top"}
      [:div.intro-body
        [:div.container
          [:div.row
            [:div.col-md-8.col-md-offset-2
              [:h1.brand-heading "Hivewing.io"]
              [:p.intro-text "The platform for your IoT"]
              [:a.btn.btn-circle.page-scroll {:href "#about"}
                [:i.fa.fa-angle-double-down.animated]
              ]
            ]
          ]
        ]
      ]
    ]
    [:section#about.content-section.text-center
      [:div.row
        [:div.col-lg-8.col-lg-offset-2
          [:h1 "Hivewing.io is the platform"]
          [:div.row.feature-point-row
            [:div.col-sm-4.feature-point
              [:div.wrapper
                [:h4 "Need to update? Done."]
                [:p "Hivewing.io updates your devices' software remotely and securely."
                 [:br]
                 [:ul.text-left
                   [:li "Push with git"]
                   [:li "Watch the magic"]]
                 ]]]
            [:div.col-sm-4.feature-point
              [:div.wrapper
                [:h4 "Need to have an API? Got it."]
                [:p "We wrap your workers and expose them through our API giving you a quick and simple way to get the plumbing right."
                 [:br]
                 [:ul.text-left.col-sm-offset-1
                   [:li "Persistent configuration"]
                   [:li "Transient events"]
                   [:li "Data collection"]
                   [:li "Logs"]
                   ]]]]
            [:div.col-sm-4.feature-point
              [:div.wrapper
                [:h4 "Decisions on data? Decided."]
                [:p "We can process all the incoming data you need to make decisions on.  Let us drink from the firehose and give you only what you want."
                 [:ul.text-left.col-sm-offset-1
                   [:li "Temp too high? Send a POST-hook"]
                   [:li "Calculate averages"]
                   [:li "Aggregate data points"]
                   [:li "Dump data to S3"]
                   ]]]]
          ]
          [:div.row
            [:h4 "Let us handle the details, so you can..."]
            [:h2 "Mind Your Bees"]
          ]
        ]
      ]
    ]
    [:section#get_started.content-section.text-center.odd
      [:div.row
       [:div.col-sm-4.col-sm-offset-4
        [:h2 "Become Hivewing-Enabled"]]
       [:div.col-sm-4.col-sm-offset-4
        [:p "Install the software"]]
       [:div.col-sm-6.col-sm-offset-3
         [:pre "curl https://www.hivewing.io/install-script | bash huh?"]]
       [:div.col-sm-4.col-sm-offset-4
        [:p "Point your browser at: "]]
       [:div.col-sm-6.col-sm-offset-3
        [:pre "http://where-I-installed-it/hivewing"]]
      ]
    ]
    [:section#contact_us.content-section.text-center
      [:div.row
        [:div.col-lg-8.col-lg-offset-2
          [:h2
            [:a {:href "mailto:connect.with@hivewing.io"} "connect.with@hivewing.io" ]]

          [:p "Stay up to date on the latest Hivewing happenings."]
          [:ul.list-inline.banner-social-buttons
            [:li
              [:a.btn.btn-default.btn-lg {:href "https://twitter.com/hivewing"}
                [:span.network-name "Twitter"]
              ]
            ]
            [:li
              [:a.btn.btn-default.btn-lg {:href "https://github.com/hivewing.io"}
                [:i.fa.fa-github.fa-fw]
                [:span.network-name "Github"]
              ]
            ]
            [:li
              [:a.btn.btn-default.btn-lg {:href "https://plus.google.com/+Hivewing.io"}
                [:span.network-name>"Google+"]
              ]
            ]
          ]
        ]
      ]
    ]
    [:footer.text-center
      [:div.row
        "Copyright 2015 Hivewing.io"
      ]
    ]
    [:script {:src "/js/landing-page.js"}]
  ])
