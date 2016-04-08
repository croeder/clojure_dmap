(ns clojure-dmap.car-patterns
	""
	(use [ clojure-dmap.phrasal-patterns ] )
)
;;
;; Pattern
;; - pattern string
;; - map from symbol to frame, with slots filled
(defn load-phrases []

;; how could you attach an id to Bob? like if it came from a NER or something?
	(def-phrasal-pattern "Bob" :m-person) (def-phrasal-pattern "Chris" :m-person)
	(def-phrasal-pattern "bought" :m-purchase)

	(def-phrasal-pattern "red" :m-color) 
	(def-phrasal-pattern "blue" :m-color) 
	(def-phrasal-pattern "green" :m-color) 
	(def-phrasal-pattern "yellow" :m-color)

	(def-phrasal-pattern "Ford" :m-make) 
	(def-phrasal-pattern "Honda" :m-make) 
	(def-phrasal-pattern "Toyota" :m-make) 
	(def-phrasal-pattern "Tesla" :m-make)

	(def-phrasal-pattern "Fusion" :m-model) 
	(def-phrasal-pattern "Camry" :m-model) 
	(def-phrasal-pattern "D80" :m-model) 
	(def-phrasal-pattern "Tacoma" :m-model)

	(def-phrasal-pattern "1999" :m-year) 
	(def-phrasal-pattern "2001" :m-year) 
	(def-phrasal-pattern "2008" :m-year) 
	(def-phrasal-pattern "2013" :m-year)

	(def-phrasal-pattern "bus" :m-bus)

	(def-phrasal-pattern "work" :m-destination)
	(def-phrasal-pattern "school" :m-destination)

;;Bob bought a red Ford Fusion
	(add-pattern (create-phrasal-pattern [:m-make :m-model] :m-type 0 {}))
	(add-pattern (create-phrasal-pattern [:m-year :m-make :m-model] :m-vehicle-a 0 {}))
	(add-pattern (create-phrasal-pattern [:m-year :m-type] :m-vehicle-b 0 {}))
	(add-pattern (create-phrasal-pattern [:m-person :m-purchase :m-color :m-type :m-vehicle-a] :m-purchase-event-a  0   {}))
	(add-pattern (create-phrasal-pattern [:m-person :m-purchase :m-color :m-type :m-vehicle-b] :m-purchase-event-b  0   {}))
	;;(add-pattern (create-phrasal-pattern [:m-person :m-purchase :m-color :m-make :m-model] :m-purchase-event  0   {}))


	; gack, this splits into a list of strings that look like symbols, not actual symbols
	;(def-phrasal-pattern ":m-person :m-ride :m-bus :m-destination" :m-commute-event2 
	;		(list :vehicle :m-vehicle) (list :commute :m-commute) (list :destination :m-destination))
)

(ns clojure-dmap.frames
"avoiding evaling these lines in by collecting them into a function here."
	(use [ clojure-dmap.utilities.frame ]
))

