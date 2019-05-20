(ns todo_list.mydb 
  (:require    [datomic.client.api :as d]
))



;; Connection to the database hello

(def db-uri (d/client {:server-type :local 
                       :endpoint   "datomic:dev://localhost:4334/hello"}))

(d/create-database db-uri {:db-name "hello"})

(def conn (d/connect db-uri))







;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; (def cfg {:server-type :peer-server                ;;
;;           :access-key  "myaccesskey"               ;;
;;           :secret      "mysecret"                  ;;
;;           :endpoint    "localhost:4334"})          ;;
;;                                                    ;;
;; (def client (d/client cfg))                        ;;
;;                                                    ;;
;;                                                    ;;
;; (def conn (d/connect client {:db-name "hello"}))   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
