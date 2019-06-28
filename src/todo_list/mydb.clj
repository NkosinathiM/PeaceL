(ns todo-list.mydb
  (:require [datomic.api :as d]))

;; Connection to the database hello

(def db-uri "datomic:mem://hello")
  
(d/create-database db-uri)

(let [conn (d/connect db-uri) ]
    (d/basis-t (d/db conn))
)

(def topfive-schema [{:db/ident :song/title
                           :db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one
                           :db/doc "The title of the track"}

                          {:db/ident :song/genre
                           :db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one
                           :db/doc "The genre of the track"}

                          {:db/ident :song/dj
                           :db/valueType :db.type/string
                           :db/cardinality :db.cardinality/one
                           :db/doc "The dj who made the beats"}

                           {:db/ident :song/year
                           :db/valueType :db.type/long
                           :db/cardinality :db.cardinality/one
                           :db/doc "The year it was released"}])


; The transaction will take a connection and a map of data. 
(d/transact  (d/connect db-uri) topfive-schema)

(def topfive-songs [{:song/title   "Tough Love"
                     :song/genre   "EDM"
                     :song/dj      "Avicii"
                     :song/year    2019}

                    {:song/title   "SOS"
                     :song/genre   "EDM"
                     :song/dj      "Avicii"
                     :song/year    2019}

                    {:song/title   "Heaven"
                     :song/genre   "EDM"
                     :song/dj      "Avicii"
                     :song/year    2019}

                    {:song/title   "Freak"
                     :song/genre   "EDM"
                     :song/dj      "Avicii"
                     :song/year    2019}
                    
                    {:song/title   "Ain't a thing"
                     :song/genre   "EDM"
                     :song/dj      "Avicii"
                     :song/year    2019}]) 

(d/transact  d/connect db topfive-songs)

(def db (d/db (d/connect db-uri)))

(def our-songs '[:find ?title ?dj ?genre ?year
                 :where [?var :song/title ?title]
                        [?var :song/dj    ?dj]
                        [?var :song/genre ?genre]
                        [?var :song/year  ?year]])
(defn thesongs 
  []
  (d/q our-songs Db))




