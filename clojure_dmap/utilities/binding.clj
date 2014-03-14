(def x 1)
(println x)
(binding [x 3]
	(println x) 
	(set! x 4)
	(println x) 
)
(println x)
(println "====")

;;(set! x 33)
;;(println x)


(def flag :t)
(when flag (println "flag is true"))
(def flag nil)
(when flag (println "flag is false"))


(def hsh0 { :a 1 :b 2  } )
(println hsh0)
(def hsh (hash-map :a 'aaa', :b 'bbbb', :c 'ccc) )
(println hsh)
(def hsh (assoc hsh :f "foo" ))
(println hsh)
(def hsh (assoc hsh :g "bar"  :b "bar" ))
(println hsh)
