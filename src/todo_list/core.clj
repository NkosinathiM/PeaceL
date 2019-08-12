(ns todo-list.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.util.response :as resp]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
    ;; Pretty much never use :refer :all, and only use :refer for things used all the time
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]
            [todo-list.mydb :refer :all]
            [todo-list.diff :refer :all]))

(defn the-handler
  "When you send a request to the webapp, the ring adaptor (-main) converts
   the request to a map and sends it to the specified handler. This the handler
   functyion that takes the request map as its argument and returns a response map. "
  ;; If you don't use a binding but want to name it so you know what it is, prefix it with a _ as below.
  ;; If you don't care what it is, just use an _
  [_request]
  ;; `hiccup.core/html` generates a plain HTML string, to wrap the whole thing in a valid
  ;; page use `hiccup.page/html5` and put in a response map
  {:status 200
   :body   (html5 {}
             [:h1 " Hello Good Human"]
             [:ul
              [:li "Huslte in the rain like no one is watching"]
              [:li "Get this bread no matter what"]])})


(defn html-response
  "Wraps responses in a map "
  [body]
  (-> {:status 200
       :body   body}
      ;; You should also include the content type
      (resp/content-type "application/html")))

(defn thediff
  "Route for differenting"
  [_request]
  (html-response
    (html5 
     [:h1 "Your differentiated univariate polynomial is:"]
     [:p (todo-list.diff/differentiator _request)])))


(defn req-info
  "Gives you information about the request that has been sent"
  [_request]
  ;; This won't be valid HTML but will contain characters that may confuse the browser.
  ;; Explicitly set the content-type in this case
  (-> {:status 200
       :body   (pr-str _request)}
      (resp/content-type "text/plain")))


;; For constants like this we often use SCREAMING_SNAKE_CASE
(def OPERATORS {"+" + "-" - ":" / "*" *})

(defn calculator
  "Gives a calculated result from the request's parameters"
  [_request]
  ;; You could/should add error handling here
  (let [a (Integer/parseInt (get-in _request [:route-params :a]))
        b (Integer/parseInt (get-in _request [:route-params :b]))
        op (get-in _request [:route-params :op])
        ;; Always use kebab-case in clojure, never snake_case (except for contstants)
        ; the_op (get OPERATORS op)
        the-op (get OPERATORS op)
        ]
    ;; Neither of these responses are valid HTML or set to plain text
    (if the-op
      {:status  200
       :body    (str "Your result: " (the-op a b))
       :headers {}}
      ;; Technically the correct error code for this is 400 (Bad Request) not 404 (Not Found)
      ;; see https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400
      {:status  400
       :body    "I am not farmiliar with that operand. Try +,-,* or /"
       :headers {}})))

;; You shouldn't call functions in a `def`.  Loading an application should not cause any code to run
;; automatically except for the `-main` function.  You could call this in a `let` block inside the
;; `tasks` function.
(def mylist
  (todo-list.mydb/thetasks))

(defn tasks
  "Gets your priority task list from the database."
  [request]
  (html
    [:h2 "Hello, here is your to do list for today:"]
    [:table
     [:tr [:th "Name"]
      [:th "Type"]
      [:th "Estimate (mins)"]]
     [:tr [:th (get-in mylist [0 0])]
      [:th (get-in mylist [0 1])]
      [:th (get-in mylist [0 2])]
      ]
     [:tr [:th (get-in mylist [1 0])]
      [:th (get-in mylist [1 1])]
      [:th (get-in mylist [1 2])]
      ]
     [:tr [:th (get-in mylist [2 0])]
      [:th (get-in mylist [2 1])]
      [:th (get-in mylist [2 2])]
      ]
     [:tr [:th (get-in mylist [3 0])]
      [:th (get-in mylist [3 1])]
      [:th (get-in mylist [3 2])]]]))

;; You can iterate over the tasks programatically:
;; (I haven't actually run this, let me know if it doesn't work...)
(defn tasks2
  "Gets your priority task list from the database."
  [_request]
  (let [tasks (todo-list.mydb/thetasks)]
    (html-response
      (html5
        [:h2 "Hello, here is your to do list for today:"]
        [:table
         ;; header row should be enclosed in a thead tag
         [:thead
          [:tr
           [:th "Name"]
           [:th "Type"]
           [:th "Estimate (mins)"]]]
         ;; body should be in a tbody
         (into [:tbody]
               (->> tasks
                    (map (fn [[title kind estimate]]        ; Destructuring syntax, see https://clojure.org/guides/destructuring
                           [:tr
                            [:td title]
                            [:td kind]
                            [:td estimate]]))))]))))

;---------------------------------------------------------------------------

(defroutes app

           (GET "/" [] the-handler)
           (GET "/req-info" [] req-info)
           (GET "/calculator/:op/:a/:b" [] calculator)
           (GET "/diff/*args" [] thediff )
           (GET "/mytasks" [] tasks)
           (not-found "<h1>This is not the page you are looking for</h1>
            <p>Sorry, the page you requested was not found!</p>"))

(defn -main
  "The -main function takes a port number as an argument which we pass when running the application. 
  The Ring Jetty adaptor is used to run an instance of Jetty. 
  The -main function contains an anonymous function that takes any request and returns a response map."
  [pn]                                                      ;The port number

  (jetty/run-jetty
    app
    {:port (Integer/parseInt pn)})
  )

(defn -dev-main
  "This one allows us to make changes and save then it pushes all those to the server."
  [pn]                                                      ;The port number

  (jetty/run-jetty
    (wrap-reload #'app)                                     ; Skip the evaluation of the function and use the name instead
    {:port (Integer/parseInt pn)})
  )
