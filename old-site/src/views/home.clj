(ns views.home
  (:require [hivewing-web.paths :as paths])
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
            [:span "Hivewing."]
            [:span.light "io"]
            [:div.hexagon-xs "&nbsp;"]
         ]
        ]
        [:div.collapse.navbar-collapse.navbar-right.navbar-main-collapse
          [:ul.nav.navbar-nav
            "<!-- Hidden li included to remove active class from about link when scrolled up past about section -->"
            [:li.hidden
              [:a {:href "#page_top"}]]
            [:li
              [:a.page-scroll {:href "#about"} "About" ]]
            [:li
              [:a.page-scroll {:href "#features"} "Features" ]]
            [:li
              [:a.page-scroll {:href "#get_started"} "Get Started"]]
            [:li
              [:a.page-scroll {:href "#contact_us"} "Contact"]]
            [:li
              [:a {:href (paths/login-path)} "Sign In"]]
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
    [:section#about.content-section.text-center.container
      [:div.row
        [:div.col-lg-10.col-lg-offset-1
          [:h1 "Hivewing.io is the platform"]
          [:p "Hivewing.io is the platform that you will build your IoT devices upon.
              When you have built the killer application and are ready to share it with the world, don't waste your time
              building and maintaining all the plumbing to make it production ready.  Leverage Hivewing.io to get your product in your user's hands quickly and securely.  "]
          [:h3 "Keep focused on your product and the things that make it awesome."]
        ]
      ]
    ]
    [:hr.even ]
    [:section#features.content-section.text-center
      [:div.row
        [:div.row.feature-point-row
          [:div.col-sm-4.feature-point
            [:div.wrapper
              [:h3 "Remote Management? " [:br] "Done."]
              [:p "Hivewing.io updates your devices' software remotely and securely. It lets you debug remotely and drill down to figure out what is going on."]
               [:br]
               [:ul.text-left
                 [:li "Push updates with git"]
                 [:li "Device logs centrally stored"]
                 [:li "Remote SSH access"]
               ]]]
          [:div.col-sm-4.feature-point
            [:div.wrapper
              [:h3 "Want to have an API?" [:br] " Got it."]
              [:p "We wrap your workers and expose them through our API giving you a quick and simple way to get the plumbing right. You may not even need a server - and just control devices through your client applications."
               [:br]
               [:ul.text-left
                 [:li "Persistent configuration"]
                 [:li "Transient events"]
                 [:li "Data collection"]
                 [:li "Logs"]
                 [:li [:a {:href (paths/api-docs-path)} " API Docs "]]
                 ]]]]
          [:div.col-sm-4.feature-point
            [:div.wrapper
              [:h3 "Decisions with data?" [:br] " We do that."]
              [:p "We store all the data and state from your devices.
                  We can process all the incoming data and help you make fast decisions.
                  Let us drink from the firehose and give you only what you care about."]
               [:ul.text-left
                 [:li "Notify on data changes"]
                 [:li "Aggregate data points"]
                 [:li "Dump data to S3"]
                 ]]]
        ]
        [:div.row
          [:h4 "Let us handle the details, so you can..."]
          [:h2 "Mind Your Bees"]
        ]
      ]
    ]
    [:section#get_started.content-section.text-center.odd
      [:div.row
       [:div.col-sm-6.col-sm-offset-3
        [:h2 "Become Hivewing-Enabled"]]
       [:div.col-sm-4.col-sm-offset-4
        [:p "Install the software"]]
       [:div.col-sm-6.col-sm-offset-3
         [:pre "curl https://www.hivewing.io/install-script | bash huh?"]]
       [:div.col-sm-4.col-sm-offset-4
        [:p "Point your browser at: "]]
       [:div.col-sm-6.col-sm-offset-3
        [:pre "http://where-I-installed-it/hivewing"]]
       [:div.col-sm-6.col-sm-offset-3 "&nbsp;"]
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
