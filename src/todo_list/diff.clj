(ns todo-list.diff
  (:require    [clojure.string :as str]))

(defn recFunc
  "Recusively contructs a polynomial term by term "
  [numbs]
  (let [POWER (count numbs)]                                ; Number of parameters in the request
    (if (= POWER 1)
      0
      (str (* (- POWER 1) (get-in numbs [0])) "x^" (- POWER 2) "+" (recFunc (vec (rest numbs)))))))

(defn differentiator
  "This function takes in request parameters for a univariate polynomial function and return its differentiation"
  [_request]
  (let [terms     (vec (map #(Integer/parseInt %)
                            (-> (-> _request :route-params :poly)
                                ;(:* [:route-params _request])
                                (clojure.string/split    #"\/"))))]
    (recFunc terms))
    

;;
 (comment

    (defn recFunc
      "Recusively contructs a polynomial term by term "
      [numbs]
        
      (let [POWER (count  numbs)] ; Number of parameters in the request
        
        (if (= POWER 1)
          0
          (str (* (- POWER 1) (get-in numbs [0])) "x^" (- POWER 2)  "+" (recFunc (vec (rest numbs))) ) )))

    
  

    (let [terms     (vec (map #(Integer/parseInt %) 
                         (-> ; (:* [:route-params _request])        ; for later use
                                (clojure.string/split  "/1/2/3"  #"\/")
                                (rest)
 
                                )))]
     
      
       (prn (recFunc terms)))
    

    )




)
