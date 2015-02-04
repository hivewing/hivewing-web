(ns views.beekeeper
  (:require [views.helpers :as helpers]
            [hivewing-web.paths :as paths])
  (:use hiccup.core
        hiccup.util
        hiccup.page
        hiccup.def))

(defn profile
  [req bk]
  [:div.container-fluid
    [:div.row
      [:p "Your beekeeper profile"]]

    [:div.row
      [:form {:method "post"}
        (helpers/anti-forgery-field)

        [:div.form-group
          [:label "Email"]
          [:input.form-control {:type :email :value (:email bk) :name "beekeeper-update[email]"}]
        ]

        [:div.form-group.spaced
          [:button.btn.btn-primary  {:type :submit} "Update"]
        ]
      ]
    ]

    [:div.row
      [:form {:method "post" :action (paths/beekeeper-profile-change-password-path)}
        (helpers/anti-forgery-field)

        [:div.form-group
          [:label "Password"]
          [:input.form-control {:type :password :placeholder "Enter new password" :name "beekeeper-password"}]
          [:input.form-control {:type :password :placeholder "Confirm new password" :name "beekeeper-password-confirmation"}]
        ]

        [:div.form-group.spaced
          [:button.btn.btn-primary  {:type :submit} "Change Password"]
        ]
      ]
    ]
  ])
(defn public-keys
  [req pks]
  [:div.container-fluid
    [:div.row
      [:p "Your public-keys allow you to push information via git to update hive images.
          They also enable you to authenticate SSH connections into workers"]
    ]
    [:div.row
      [:ul.list-group
        [:form  {:method "post" }
          (helpers/anti-forgery-field)
          [:div.form-group
            [:textarea.form-control {:name "public-key" :placeholder "Enter your SSH public key here"} ]
          ]

          [:input.btn.btn-primary {:type :submit :value "Create"}]
        ]
      ]
    ]

    [:div.row
      [:h2 "Current Keys"]
      [:ul.list-group
        (map #(vector
                :li.list-group-item.clearfix
                  [:form.pull-right {:method "post" :action (paths/beekeeper-public-key-delete-path)}
                    (helpers/anti-forgery-field)
                    [:input {:type :hidden :name :pk-uuid :value (:uuid %)}]
                    [:button.btn.btn-danger  "Delete"]
                  ]
                  [:pre.container-fluid
                     (:key %)
                  ]
                )
          pks)
      ]
    ]
  ])
