(ns hivewing-web.beekeeper-controller
  (:require [hivewing-web.session :as session]
            [hivewing-web.controller-core :refer :all]
            [hivewing-web.paths :as paths]
            [hivewing-core.beekeeper :as bk]
            [hivewing-core.public-keys :as pk]
            [hivewing-core.hive-manager :as hm]
            [ring.util.request :as ring-request]
            [ring.util.response :as r]
            [views.beekeeper :as views]
            [views.layout :as layout]
            ))

(defn sub-menu [req current]
  "Determine the submenu listing for the apiary-controller!"
  [{:href (paths/beekeeper-profile-path)
    :text "Profile"
    :active (= current :profile)}
   {:href (paths/beekeeper-public-keys-path)
    :text "Public Keys"
    :active (= current :public-keys)}
  ])

(defn back-link [req]
   {:href (paths/apiary-path)
    :text "Apiary" })

(defn profile
  [req & args]
  (with-preconditions req [bk         (session/current-user req)]
    (render (layout/render req (views/profile req bk)
                                :style :default
                                :sub-menu (sub-menu req :profile)
                                :current-name "Beekeeper"
                                :back-link (back-link req)
                                :body-class :beekeeper
                                ))))

(defn profile-update
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [beekeeper-update]
      (let [update-result (bk/beekeeper-update (:uuid bk) (get-in req [:params :beekeeper-update]))]
            (->
              (r/redirect (paths/beekeeper-profile-path))
              (assoc :flash (str "Updated profile")))))))

(defn change-password
  [req & args]

  (with-beekeeper req bk
    (with-required-parameters req [beekeeper-password beekeeper-password-confirmation]
      (if (= beekeeper-password beekeeper-password-confirmation)
        (try
          (let [update-result (bk/beekeeper-set-password (:uuid bk) beekeeper-password )]
               (->
                  (r/redirect (paths/beekeeper-profile-path))
                  (assoc :flash (str "Updated password"))))
          (catch java.lang.Exception e
               (->
                  (r/redirect (paths/beekeeper-profile-path))
                  (assoc :flash (str (.getMessage e))))))
        (->
          (r/redirect (paths/beekeeper-profile-path))
          (assoc :flash (str "Passwords did not match"))))
        )))

(defn public-keys
  [req & args]
  (with-preconditions req [bk         (session/current-user req)
                           pks (pk/public-keys-for-beekeeper (:uuid bk))]

      (render (layout/render req (views/public-keys req pks)
                                  :style :default
                                  :sub-menu (sub-menu req :public-keys)
                                  :current-name "Beekeeper"
                                  :back-link (back-link req)
                                  :body-class :beekeeper
                                  ))))
(defn public-key-delete
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [pk-uuid]
      (let [delete-result (pk/public-keys-delete (:uuid bk) pk-uuid)]
            (->
              (r/redirect (paths/beekeeper-public-keys-path))
              (assoc :flash (str "Deleted public key")))))))

(defn public-key-create
  [req & args]
  (with-beekeeper req bk
    (with-required-parameters req [public-key]
      (let [create-result (pk/public-key-create (:uuid bk) public-key)]
            (->
              (r/redirect (paths/beekeeper-public-keys-path))
              (assoc :flash (str "Added public key")))))))
