;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; frame.clj
;; A frame is a hash of attribute-value pairs that is part
;; of an inheritance hierarchy.
;; This module includes an uber-hash holding all the frames.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(load-file "arities.clj")
(def frame-of {})


;; TODO consider implementation of relationships to base and children frames
;; put those frame names in the same space as the attr/values?
;; How does Riesbeck do it?

(defstruct frame :name :features :specializations :abstractions)

;; stuff is specializations then abstractions
(defn new-create-frame	[frame-name slot-list value-list & stuff] 
		(def frame-of 
			(assoc frame-of frame-name 
			  (struct frame  
				; name
				frame-name
				; features
				(loop [new-frame {}  local-slot-list slot-list local-value-list value-list]
					(let [slot (first local-slot-list) value (first local-value-list)]
						(cond 
							(= (count local-slot-list) 0) new-frame
							:t 
								(recur 	
									(assoc new-frame slot value)
									(rest local-slot-list) 
									(rest local-value-list) 
								))  ))
				; specialization
				(first stuff)
				; abstractions
				(second stuff)))))

(defn get-specializations [frame-name]
	((frame-of frame-name) :specializations))

(defn base-slot [frame-name slot-name]
	(println "fname:" frame-name "slot value:" ((frame-of frame-name) slot-name)  "slot name:" slot-name)
	;;((frame-of frame-name) slot-name)
)

(defn print-frame [frame-name]
	(println frame-name (frame-of frame-name))
	(map get-specializations (get-specializations frame-name)) )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;(create-frame :persona (list :fname :lname )  )
;(print-frame :persona)
;(create-frame :person (list :fname :lname :mi) (list "Bob" "Jones" "X") )
;(print-frame :persona)
;(print-frame :person) 
(println "======================================")



;;(new-create-frame :empty-person (list :fname :lname :mi)  nil nil nil )
(new-create-frame :empty-person (list :fname :lname :mi)  nil  )
(print-frame :empty-person)
(new-create-frame :new-person (list :fname :lname :mi) (list "Bob" "Jones" "X") nil nil)
(print-frame :new-person)
(println (base-slot :new-person :lname))
(new-create-frame :new-director (list :company :title :salary) (list "The" "Director" 1000000) :new-person )
(print-frame :new-person)
(print-frame :new-director)
(println (get-specializations :new-director))
(println print-frame (get-specializations :new-director))
(println "------------_________________--")
