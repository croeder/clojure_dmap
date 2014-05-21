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

	(def-phrasal-pattern "Bob" :m-person)
	(def-phrasal-pattern "Chris" :m-person)
	(def-phrasal-pattern "rode" :m-ride)
	(def-phrasal-pattern "drove" :m-drove)
	(def-phrasal-pattern "bicycle" :m-bicycle)
	(def-phrasal-pattern "car" :m-car)
	(def-phrasal-pattern "bus" :m-bus)
	(def-phrasal-pattern "work" :m-destination)
	(def-phrasal-pattern "school" :m-destination)

	(def-phrasal-pattern "Chris rode the bus to work" :m-commute-event (list :vehicle  :commute  :destination ))
	(add-pattern (create-phrasal-pattern (list :m-person :m-ride :m-bus :m-destination) :m-commute-event2 0 (list :vehicle :commute  :destination ) {} ))
	(add-pattern (create-phrasal-pattern [:m-person :m-commute :m-vehicle :m-destination] :m-commute-event3 0 (list :vehicle :commute :destination ) {} ))

	; gack, this splits into a list of strings that look like symbols, not actual symbols
	;(def-phrasal-pattern ":m-person :m-ride :m-bus :m-destination" :m-commute-event2 
	;		(list :vehicle :m-vehicle) (list :commute :m-commute) (list :destination :m-destination))
)

