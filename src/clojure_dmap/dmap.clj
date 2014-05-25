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


(defn reset-patterns  []
	(reset)
	(load-frames)
	(load-phrases) )


(defn -main [& args] 
	(reset-patterns) (match-patterns "Chris rode the bus to work")		(dump-completed-patterns)
	;(reset-patterns) (match-patterns "Chris drove the car to school")	(dump-completed-patterns)
	;(reset-patterns) (match-patterns "Chris rode his bicycle to school")	(dump-completed-patterns)
	;(reset-patterns) (match-patterns "Bob smoked his cigs on the bus")	(dump-completed-patterns) 
)
