(ns clojure-dmap.patterns
	""
	(use [ clojure-dmap.phrasal-patterns ] ))

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
	(create-phrasal-pattern "Chris" :m-person)
	(create-phrasal-pattern "bus" :m-bus)
	(create-phrasal-pattern "rode" :m-ride)
	(create-phrasal-pattern "work" :m-destination)

	(create-phrasal-pattern "Chris rode the bus to work" 
			:m-commute-event 
			(:vehicle "bus") (:commute "rode") (:destination "work"))
	(create-phrasal-pattern ":m-person :m-commute :m-vehicle :m-destination" 
			:m-commute-event 
			(:vehicle :m-vehicle) (:commute :m-commute) (:destination :m-destination))

)

