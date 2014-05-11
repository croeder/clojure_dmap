(ns clojure-dmap.dmap
 "pattern matching"
	(use 
		[clojure.string :exclude [replace reverse]] 
		[clojure-dmap.frames]
		[clojure-dmap.patterns] 
		[clojure-dmap.phrasal-patterns]
))

(defn match-patterns [input]
	(let [tokens (split input #"\s")]
		(doseq [tkn tokens]
			(println "dmap token:" tkn)
			(doseq [pattern (phrasal-patterns-map tkn)]
				(println "dmap pattern:" pattern)
				(let [ x  (advance-pattern pattern tkn) ]
					(println "dmap wtf:" x)
					(add-pattern x))
				(dump-patterns)
			)
		)
		; need to check for matched frames completing rules
))

(defn -main [& args] 
	(load-frames) 
	(load-phrases)
	(match-patterns "x y z.")	
	(dump-patterns)
	(match-patterns "Chris rode the bus to work.")	
)
