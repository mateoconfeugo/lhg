(ns lhg.core
  (:use compojure.core
        lstoll.utils
        ring.util.serve
        ring.adapter.jetty)
  (:require             [compojure.handler :as handler :refer [site]]
            [compojure.route :refer[files resources not-found]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [conch.core])
  (:gen-class))


(defn expand-path [path] (.getCanonicalPath (java.io.File. path)))

(defn run
  [app-path cmd]
  (let [exec ["sh" "-c" cmd :env (merge (into {} (System/getenv))
                                        {"GIT_SSH" (str (expand-path ".") "/bin/git-ssh")
                                         "SSH_KEY" (str app-path "/.ssh/id_rsa")})]
        proc (apply conch.core/proc exec)]
    (future (conch.core/stream-to :out proc *out*))
    (future (conch.core/stream-to :err proc *out*))
    (conch.core/exit-code proc)))

;; Dont format functions like this at home, kids.
(defn deploy
  [app]
  ;; Set up the env
  (let [app-path (expand-path app)]
    (when-not (.isDirectory (io/file app))
      (.mkdir (io/file app))
      (.mkdir (io/file (str app-path "/.ssh")))
      (spit (str app-path "/.ssh/id_rsa") (env (str app "_SSH_KEY"))))
    ;; git clone the repo if it's not there, else update it
    (if (= 0 (if (.isDirectory (io/file (str app "/repo")))
               (do
                 (log "Starting repo update")
                 (run app-path (str "cd " app "/repo && git fetch && git reset --hard origin/master")))
               (do
                 (log "Starting repo clone")
                 (run app-path (str "cd " app " && git clone " (env (str app "_GITHUB_REPO")) " repo")))))
      ;; git push it to heroku
      (do
        (log "Pushing repo to Heroku")
        (run app-path (str "cd " app "/repo && git push -f " (env (str app "_HEROKU_REPO")) " master"))
        (println "... Finished")))))

(defn valid-key?
  [key]
  (= key (env "ACCESS_KEY" "SETME")))

(defroutes app-routes
  (resources "/")
  (files "/" {:root "public"})
  (not-found "Not Found")
  (GET "/" [] "<h1>lhg server is up</h1>")
  (POST "/deploy" {{app :app key :key} :params} (if (valid-key? key)
                                                  (do (future (deploy app)) "OK")
                                                  {:status 403 :body "DENIED"})))


(def webapp (handler/site app-routes))

(defn start [web-application port]
  (run-jetty web-application {:port port :join? false}))


(defn -main [& port] (start webapp (Integer/parseInt (or (System/getenv "PORT") "8087"))))
