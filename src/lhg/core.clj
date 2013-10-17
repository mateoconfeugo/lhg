(ns lhg.core
  (:require    [compojure.core :refer [defroutes GET]]
               [compojure.handler :as handler :refer [site]]
               [compojure.route :refer[files resources not-found]]
               [ring.adapter.jetty :as ring :refer [run-jetty]]))

(defroutes app-routes
  (GET "/hello" [] "<h1>Hello World</h1>")
  (resources "/")
  (files "/" {:root "public"})
  (not-found "Not Found"))

(def webapp (handler/site app-routes))

(defn start [web-application port]
  (ring/run-jetty web-application {:port 8087 :join? false}))


(defn -main [] (start webapp 8087))
