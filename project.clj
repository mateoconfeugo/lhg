(defproject lhg "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :heroku {:app-name "lhg" :app-url "http://lhg.herokuapp.com"}
  :ring {:handler lhg.core/webapp :auto-reload? true :auto-refresh true}
  :uberjar-name "lhg-standalone.jar"
  :min-lein-version "2.0.0"
  :main lhg.core
    :dependencies [[org.clojure/clojure "1.3.0"]
                 [clj-webdriver "0.6.0"]
                 [compojure "1.1.5"]
                 [ring-serve "0.1.2"]
                 [conch "0.3.1"]
                 [tentacles "0.2.6"]
                 [net.lstoll/utils "0.3.2"]]
  :profiles  {:dev {:dependencies [[expectations "1.4.56"]
                                   [org.clojure/tools.trace "0.7.6"]
                                   [ring-mock "0.1.5"]
                                   [ring/ring-devel "1.2.0"]
                                   [vmfest "0.3.0-beta.3"]]}}
  :plugins [[lein-ring "0.8.7"]
            [lein-marginalia "0.7.1"] ; literate programming
            [lein-expectations "0.0.7"] ; run expect test
            [lein-autoexpect "0.2.5"] ; run expect tests when files change
            [configleaf "0.4.6"] ; access this file from the application
           ])
