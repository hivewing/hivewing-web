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
              [:p.intro-text "Management, Control, and Analysis for your IoT"]
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
          [:h2 "Hivewing.io is the platform"]
          [:p "Build your distributed devices upon the hivewing platform.  Let us take care of updating software, sending data to and from the devices, and even back end data processing."]
          [:p "You focus on making a great device"]
          [:p "We'll take care of the details"]
        ]
      ]
    ]
    [:section#get_started.content-section.text-center.light
      [:div.row
        [:div.col-lg-8.col-lg-offset-2
          [:p "Build your distributed devices upon the hivewing platform.  Let us take care of updating software, sending data to and from the devices, and even back end data processing."]
          [:p "You focus on making a great product."]
          [:p "We'll take care of the details."]
        ]
      ]
    ]
    [:section#contact_us.content-section.text-center
      [:div.row
        [:div.col-lg-8.col-lg-offset-2
          [:h2 "Contact Hivewing.io"]
          [:p "Contact us for a demo and information as we bring the service to life"]
          [:p [:a {:href "mailto:feedback@hivewing.io"} "feedback@hivewing.io" ]]
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
