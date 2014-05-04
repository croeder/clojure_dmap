(ns utilities.arities)

(defn arities 
	"tells how many items in structures"
	{:test #(do
		(def x (list :a :b :c))
		(assert (= (arities x) 3))
	)}
	[v]
  	(->> v 
    	meta 
    	:arglists 
    	(map #(remove #{'&} %))
    	(map count)))

(def x (list :a :b :c))
(println "testrun:"  (arities x) )

;;(test #'arities)
