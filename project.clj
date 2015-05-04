(defproject hivewing-web "0.1.0-SNAPSHOT"
  :description "Hivewing Web / API Server"
  :url "http://www.hivewing.io"
  :dependencies [
                 [org.clojure/clojure "1.7.0-alpha5"]
                 [environ "1.0.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [postgresql "9.1-901.jdbc4"]
                 [ragtime "0.3.8"]
                 [clj-time "0.9.0"]
                 [com.taoensso/timbre "3.3.1"]
                 [metosin/compojure-api "0.20.0" :exclusions [clj-time  org.clojure/tools.reader org.clojure/java.classpath commons-codec]]
                 [metosin/ring-swagger-ui "2.1.1-M2"]
                 [clj-crypto "1.0.2"]
                ]

  :plugins [[lein-environ "1.0.0"]
            [ragtime/ragtime.lein "0.3.8"  :exclusions [org.clojure/clojure]]
            [s3-wagon-private "1.1.2" :exclusions [commons-codec]]
            [lein-ring "0.9.3" :exclusions [leinjacker org.clojure/clojure]]
            [lein-beanstalk "0.2.7" :exclusions [org.clojure/clojure commons-codec]]
            ]

  :ring {:handler hivewing-web.handler/application }

  :ragtime {:migrations ragtime.sql.files/migrations}
  :repositories [["hivewing" {:url "s3p://clojars.hivewing.io/hivewing-core/releases" :sign-releases false}]]

  :aws {:beanstalk
         {
          :app-name "io-hivewing-spokesman"
          :environments [{:name "production"}]
         }
     }
  )
