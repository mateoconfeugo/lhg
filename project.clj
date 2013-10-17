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
  :dependencies [[org.clojure/clojure "1.5.1"] ; Lisp On JVM
                 [compojure "1.1.5"] ; Web routing]
                 [ring/ring-jetty-adapter "1.2.0"] ; Web Server
                 ]
  :plugins [[lein-heroku-deploy "0.1.0"]
            [lein-ring "0.8.7"]])
