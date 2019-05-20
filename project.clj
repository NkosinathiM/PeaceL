(defproject todo-list "0.1.0-SNAPSHOT"
  :description "A todo-list webappp"

  :url "http://example.com/FIXME"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"

            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.9.0"] 
                 [ring "1.7.1"]
                 [compojure "1.3.4"]
                 [hiccup "1.0.5"]
                 [com.datomic/client-pro "0.8.28"]]
  
  :min-lein-version "2.0.0"

  :uberjar-name "todo-list.jar"

  :main todo-list.core

  :profiles {:dev {:main todo-list.core/-dev-main}})
