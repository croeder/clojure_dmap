(ns clojure-dmap.dmap
 "pattern matching"
	(use 
		[clojure.string :exclude [replace reverse]] 
		[clojure-dmap.frames]
		[clojure-dmap.patterns] 
		[clojure-dmap.phrasal-patterns]
))


(defn match-patterns [input]
"go through the tokens of the input string matching rules and propagating matches"
	(let [tokens (split input #"\s")]
		(doseq [tkn tokens]
			(doseq [pattern (phrasal-patterns-map tkn)]
				(let [ x  (advance-pattern pattern tkn) ]
					(add-pattern x)
					(propagate-advances)
					(propagate-advances-generalize)
				) )
		)
		; need to check for matched frames completing rules
))

(defn -main [& args] 
	(load-frames) 
	(load-phrases)
	(println "======================================= end")
	(match-patterns "Chris rode the bus to work")	
	(println "======================================= end")
	(dump-completed-patterns)
)
