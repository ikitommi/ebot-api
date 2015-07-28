(ns ebot-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [korma.core :refer :all]
            [korma.db :refer :all]
            [cheshire.core :as cheshire]
            [ring.middleware.cors :refer [wrap-cors]]
            ))

(def db-host
  (or (System/getenv "MYSQL_PORT_3306_TCP_ADDR") "esports-mysql.cr26tvpoftg3.eu-west-1.rds.amazonaws.com"))

(def db-port (read-string (or (System/getenv "MYSQL_PORT_3306_TCP_PORT") "3306")))

(defdb db (mysql {:host     db-host
                  :db       "ebotv3"
                  :user     "ebotv3"
                  :port     db-port
                  :password "ebotv3"}))

(defentity players_snapshot)

(defentity players
           (has-many players_snapshot))

(defentity round_summary)

(defentity round
           (has-many round_summary)
           (has-many players)
           )

(defentity teams
           (has-many players {:fk :team}))




(defentity matchs
           (has-many round_summary {:fk :match_id})
           (has-many players {:fk :match_id})
           (has-many players_snapshot {:fk :match_id})

           (transform (fn [{config_password :config_password :as v}]
                        (if config_password
                          (assoc v :config_password "") v)))
           (transform (fn [{config_authkey :config_authkey :as v}]
                        (if config_authkey
                          (assoc v :config_authkey "") v)))
           (transform (fn [{ip :ip :as v}]
                        (if ip
                          (assoc v :ip "") v)))

           )


(defn get-matchs [id]
  (select matchs (with round_summary) (with players) (where {:id id})))

(defn get-team [id]
  (select teams (with players) (where {:id id})))


(defn get-all-matchs []
  (select matchs (order :startdate :DESC) (limit 20)))

(defn get-round [id]
  (select round (with round_summary) (with players_snapshot) (where {:id id})))

(defn get-rounds [matchid]
  (select round (with round_summary) (with players_snapshot) (where {:match_id matchid}) (order :round_id :DESC)))


(defapi app
        (middlewares [(wrap-cors :access-control-allow-origin #".*"
                                 :access-control-allow-methods [:get]
                                 :access-control-allow-headers ["Origin" "X-Requested-With"
                                                                "Content-Type" "Accept"])])
        (swagger-ui)
        (swagger-docs
          {:info {:title       "Ebot-api"
                  :description "eBot api"}
           :tags [{:name "api", :description "EBOT rest api"}]})
        (context* "/api" []
                  :tags ["api"]
                  (GET* "/matches" []
                        :summary "Get all matches"
                        (ok (cheshire/generate-string(get-all-matchs))))
                  (GET* "/match/:id" []
                        :summary "Get match with id"
                        :path-params [id :- Long]
                        (ok (cheshire/generate-string (get-matchs id))))
                  (GET* "/rounds/:matchid" []
                        :summary "Get rounds with match id"
                        :path-params [matchid :- Long]
                        (ok (cheshire/generate-string (get-rounds matchid))))
                  (GET* "/round/:id" []
                        :summary "Get round with id"
                        :path-params [id :- Long]
                        (ok (cheshire/generate-string (get-round id))))
                  (GET* "/team/:id" []
                        :summary "Get team with id"
                        :path-params [id :- Long]
                        (ok (cheshire/generate-string(get-team id))))

                  ))
