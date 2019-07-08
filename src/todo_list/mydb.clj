(ns todo-list.mydb
  (:require [datomic.api :as d]))

;; Connection to the database hello

(def db-uri "datomic:mem://hello")
  
(d/create-database db-uri)

(let [conn (d/connect db-uri) ]
    (d/basis-t (d/db conn))
)

(def topfive-schema [{:db/ident :task/name
                           :db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one
                           :db/doc "The title of the track"}

                          {:db/ident :task/type
                           :db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one
                           :db/doc "The genre of the track"}

                           {:db/ident :task/estimate
                           :db/valueType :db.type/long
                           :db/cardinality :db.cardinality/one
                           :db/doc "The year it was released"}])


; The transaction will take a connection and a map of data. 
(d/transact  (d/connect db-uri) topfive-schema)

(def topfive-tasks [{:task/name   "Laundry"
                     :task/type   "Home chore "
                     :task/estimate    30}

                    {:task/name   "Write Conclusion"
                     :task/type   "School"
                     :task/estimate    60}

                   { :task/name   "Code interceptor"
                     :task/type   "Work"
                     :task/estimate    120} 

                   { :task/name   "Buy fertilizer"
                     :task/type   "Home chore"
                     :task/estimate    30} 
                                      ]) 

(d/transact (d/connect db-uri) topfive-tasks)

(def db (d/db (d/connect db-uri)))

(def our-tasks  '[:find ?name ?type ?estimate
                 :where [?var :task/name ?name]
                        [?var :task/type    ?type]
                        [?var :task/estimate ?estimate]
                        ])
(defn thetasks 
  []
  (into [] (d/q our-tasks db)))

