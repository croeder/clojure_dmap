(ns clojure-dmap.dmap
 "pattern matching"
	(use 
		[clojure.string :exclude [replace reverse]] 
		[clojure-dmap.frames]
		[clojure-dmap.patterns] 
		[clojure-dmap.phrasal-patterns]
))

; the top-level dmap file
; The frames and patterns are loaded here, then applied to various input strings.


(defn match-patterns [input]
"Go through the tokens of the input string matching rules and propagating matches."

	(println input)
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

(defn reset-patterns  []
	(reset)
	(load-frames)
	(load-phrases))


(defn -main [& args] 
;	(load-frames) (load-phrases)
	
	(reset-patterns) (match-patterns "Chris rode the bus to work")	
	(dump-completed-patterns)
	(reset-patterns) (match-patterns "Chris drove the car to school")	
	(dump-completed-patterns)
	(reset-patterns) (match-patterns "Chris rode his bicycle to school")	
	(dump-completed-patterns)
	(reset-patterns) (match-patterns "Bob smoked his cigs on the bus")	
	(dump-completed-patterns)
)
