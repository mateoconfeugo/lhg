(ns lhg.core
  (:use compojure.core
        ring.util.serve
        ring.adapter.jetty)
  (:require [compojure.handler :as handler :refer [site]])
  (:gen-class))

(defroutes app-routes (GET "/" [] "<h1>lhg server is up</h1>"))
(def webapp (handler/site app-routes))

(defn start [web-application port]
  (run-jetty web-application {:port port :join? false}))


(defn -main [& port] (start webapp (Integer/parseInt (or (System/getenv "PORT") "8087"))))
