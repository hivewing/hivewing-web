(defproject hivewing-web "0.1.0-SNAPSHOT"
  :description "Hivewing Web / API Server"
  :url "http://www.hivewing.io"
  :dependencies [
                 [org.clojure/clojure "1.7.0-alpha5"]
                 [environ "1.0.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [postgresql "9.1-901.jdbc4"]
                 [ragtime "0.3.8"]
                 [com.taoensso/timbre "3.3.1"]
                 [metosin/compojure-api "0.20.0"]
                ]

  :plugins [[lein-environ "1.0.0"]
            [ragtime/ragtime.lein "0.3.8"]
            [s3-wagon-private "1.1.2"]
            [lein-ring "0.9.3"]
            [lein-expectations "0.0.7"]
            [lein-autoexpect "1.4.0"]
            ]

  :ring {:handler hivewing-web.handler/application }

  :ragtime {:migrations ragtime.sql.files/migrations}
  :repositories [["hivewing" {:url "s3p://clojars.hivewing.io/hivewing-core/releases" :sign-releases false}]]
  )
