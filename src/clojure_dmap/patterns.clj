(ns clojure-dmap.patterns
	""
	(use [ clojure-dmap.phrasal-patterns ] )
)

;; a pattern is a string of literals and ontology mentions to be matched
;; not necessarily in order
;; Ex. "Chris rode the bus to work."
;; Ex. "Chris :trans-verb :vehicle to :destination"
;; The ontology elements are distinguished from the literals by the presence
;; of the colon that makes an element a symbol in Clojure.
;; Patterns can include slot fillers:
;; Ex. "Chris bought a new :color :type :vehicle"
;; When the concept :vehicle has slots for :color and :type, they get filled...
;; 
;;
;; Pattern
;; - pattern string
;; - map from symbol to frame, with slots filled
;; NEED TO UNDERSTAND the process of filling slot a little better



(defn load-phrases []
	;(create-phrasal-pattern (list "Chris") :m-person 0 (list))
	;(create-phrasal-pattern (list "Chris" "rode" "the" "bus" "to" "work") :m-commute-event 0 (list (:vehicle "bus") (:commute "rode") (:destination "work")))

	(def-phrasal-pattern "Chris" :m-person)
	(def-phrasal-pattern "rode" :m-ride)
	(def-phrasal-pattern "bus" :m-bus)
	(def-phrasal-pattern "work" :m-destination)

	(def-phrasal-pattern "Chris rode the bus to work" 
			:m-commute-event 
			(list :vehicle "bus") (list :commute "rode") (list :destination "work"))

	(add-pattern
		(create-phrasal-pattern 
			(list :m-person :m-ride :m-bus :m-destination) 
			:m-commute-event2 0 
			(list (:vehicle "bus") (:commute "rode") (:destination "work"))))
	; gack, this splits into a list of strings that look like symbols, not actual symbols
	;(def-phrasal-pattern ":m-person :m-ride :m-bus :m-destination" 
	;		:m-commute-event2 
	;		(list :vehicle :m-vehicle) (list :commute :m-commute) (list :destination :m-destination))

	;(def-phrasal-pattern ":m-person :m-commute :m-vehicle :m-destination" 
	;		:m-commute-event2 
	;		(list :vehicle :m-vehicle) (list :commute :m-commute) (list :destination :m-destination))
)

