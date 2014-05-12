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
				(let [ x  (advance-pattern pattern tkn) ]
					(add-pattern x)
					(println "advanced pattern: " x)) )
		)
		; need to check for matched frames completing rules
))

(defn -main [& args] 
	(load-frames) 
	(load-phrases)
	(match-patterns "Chris rode the bus to work.")	
	(println "======================================= end")
	(println "======================================= end")
	(dump-patterns)
)
