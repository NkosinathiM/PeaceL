(ns todo-list.handlers
  (:use  [hiccup.core]
         [hiccup.page]))


(defn the-handler                                                                                                         
 "When you send a request to the webapp, the ring adaptor (-main) converts the request to a map and sends it to the specified handler.This the handler function that takes the request map as its argument and returns a response map. " 
   [request]                                                                                                           (html [:h1 " Hello Good Human"]
          [:ul [:li "Huslte in the rain like no one is watching"]                       
                     [:li "Get this bread no matter what"]]))                                                                                                    

(defn goodbye
   "A route for goodbye"
   [request]
   (html [:h1 "Thank you so much for your visit."]
         [:p "We are very grateful of your presence!!"] ))


(defn about 
  "It gives you info about the prog."
  [request]
  {:status 200
   :body "<h1>Nkosinathi's simple web service, not bad...</h1>"
   :headers {}})

(defn req-info
  "Gives you information about the request that has been sent"
  [request]
  
  {:status 200
   :body (pr-str request)
   :headers {}})

(defn hello
  "Takes in a name from the request and display a personalised message"
  [request]
  (let [name (get-in request [:route-params :name])]
    {:status 200
     :body (str "Hello, "name ". How are you today ?")
     :headers {}}))

(def operators {"+" + "-" - ":" / "*" *})

(defn calculator
  "Gives a calculated result from the request's parameters"
  [request]
 (let [a  (Integer. (get-in request [:route-params :a]))
       b  (Integer. (get-in request [:route-params :b]))
       op (get-in request [:route-params :op])  
       the_op (get operators op)]
      
      
      
  (if the_op
    {:status 200
     :body  (str "Your result: " (the_op a b)) 
     :headers {}}
    {:status 404
     :body "I am not farmiliar with that operand. Try +,-,* or /"
     :headers {}})))
