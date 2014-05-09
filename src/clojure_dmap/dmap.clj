(ns clojure-dmap.dmap
 "pattern matching"
	(use 
		[ clojure.string ]
		[ clojure-dmap.frames ]
		[ clojure-dmap.patterns ] 
))



(defn match-patterns [input]
	(let [ tokens (split input #"\s") ]
			(println "tokens:" tokens)
			(doseq [ s tokens]
				(println "toke:" s) )))
	

(defn -main [& args] 
	(load-frames) 
	(load-phrases)
	(match-patterns "Chris rode the bus to work.")	
)


