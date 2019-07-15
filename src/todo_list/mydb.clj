(ns todo-list.mydb
  (:require [datomic.api :as d]))

;; Connection to the database hello

;; This is a constant so use DB_URI
(def db-uri "datomic:mem://hello")

;; This, as well as your transacting below, should be moved into some kind of init function that you
;; call from your main function **once** as the app boots
(d/create-database db-uri)

;; Put stuff like this in a comment block so it doesn't run every time this NS is loaded
(comment
  (let [conn (d/connect db-uri)]
    (d/basis-t (d/db conn))
    ))

(def topfive-schema [{:db/ident :task/name
                           :db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one
                           :db/doc "The name of the task."}

                          {:db/ident :task/type
                           :db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one
                           :db/doc "The type of task (home, work or school)"}

                           {:db/ident :task/estimate
                           :db/valueType :db.type/long
                           :db/cardinality :db.cardinality/one
                           :db/doc "The time estimation of how it could take to complete."}])


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

;; Again only run this once at boot
(d/transact (d/connect db-uri) topfive-tasks)

;; Ideally you don't want global references like this but dealing with that is maybe
;; something to tackle another day
(def db (d/db (d/connect db-uri)))

(def our-tasks  '[:find ?name ?type ?estimate
                 :where [?var :task/name ?name]
                        [?var :task/type    ?type]
                        [?var :task/estimate ?estimate]
                        ])
(defn thetasks 
  []
  (into [] (d/q our-tasks db)))

