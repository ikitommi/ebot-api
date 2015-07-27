(defproject ebot-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-time "0.9.0"]                         ; required due to bug in lein-ring
                 [metosin/compojure-api "0.22.0"]
                 [korma "0.4.0"]
                 [mysql/mysql-connector-java "5.1.6"]]
  :ring {:handler ebot-api.handler/app}
  :uberjar-name "server.jar"
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [cheshire "5.3.1"]
                                  [ring-mock "0.1.5"]]
                   :plugins      [[lein-ring "0.9.6"]]}})
