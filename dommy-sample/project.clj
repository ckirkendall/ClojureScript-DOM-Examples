(defproject dommy-sample "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://exampl.com/FIXME"
  :dependencies [[org.clojure/clojurescript "0.0-2080"]
                 [org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [enlive "1.1.1"]
                 [prismatic/dommy "0.1.1"]
                 [shoreleave/shoreleave-remote-ring "0.3.0"]
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [tentacles "0.2.5"]]
  :plugins [[lein-cljsbuild "1.0.1-SNAPSHOT"]
            [lein-ring "0.8.3"]]
  :cljsbuild {:builds [{:source-paths ["src"],
                        :compiler {:output-to "resources/public/js/main.js"}}]}
  :ring {:handler dommy-sample.server/app})
