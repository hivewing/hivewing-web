(defproject hivewing-web "0.1.0-SNAPSHOT"
  :description "The website for Hivewing.io"
  :url "http://hivewing.io"
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [ring "1.3.2"]
                 [ring/ring-devel "1.3.2"]
                 [compojure "1.3.1"]
                 [hiccup "1.0.5"]
                 [garden "1.2.5"]
                 [hiccup-bridge "1.0.1"]
                ]

  ;; We use the lein-ring plugin to start ring.
  :plugins [
            [lein-ring "0.8.13"]
            [lein-garden "0.2.5"]
            [hiccup-bridge "1.0.1"]
            ]

  :hooks [leiningen.garden]

  :garden {:builds [{;; Optional name of the build:
                     :id "application"
                     ;; Source paths where the stylesheet source code is
                     :source-paths ["src/styles/application"]
                     ;; The var containing your stylesheet:
                     :stylesheet styles.application/all
                     ;; Compiler flags passed to `garden.core/css`:
                     :compiler {;; Where to save the file:
                                :output-to "resources/public/css/application.css"
                                ;; Compress the output?
                                :pretty-print? true}}]}
  ;; We tell Ring what our handler function is and
  ;; what port to start on.
  :ring {:handler hivewing-web.server/app
         :port 3000}

  :main ^:skip-aot hivewing-web.server
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
