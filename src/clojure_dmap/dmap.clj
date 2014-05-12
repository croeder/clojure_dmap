(ns clojure-dmap.dmap
 "pattern matching"
	(use 
		[clojure.string :exclude [replace reverse]] 
		[clojure-dmap.frames]
		[clojure-dmap.patterns] 
		[clojure-dmap.phrasal-patterns]
))

(defn match-patterns [input]
	(doseq [k (keys phrasal-patterns-map)] (println " from pattern map  key: " k ))
	(let [tokens (split input #"\s")]
		(doseq [tkn tokens]
			(doseq [pattern (phrasal-patterns-map tkn)]
				(let [ x  (advance-pattern pattern tkn) ]
					(if (keyword? x)(println "MATCHING " x))
					(add-pattern x)
					(println "PROPOGATING ")
					(propagate-advances)
				) )
		)
		; need to check for matched frames completing rules
))

(defn -main [& args] 
	(load-frames) 
	(load-phrases)
	(dump-patterns)
	(println "======================================= end")
	(match-patterns "Chris rode the bus to work")	
	(println "======================================= end")
	(dump-patterns)
	(println "======================================= end")
	(dump-completed-patterns)
)
