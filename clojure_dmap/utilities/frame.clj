;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; frame.clj
;; A frame is a hash of attribute-value pairs that is part
;; of an inheritance hierarchy.
;; This module includes an uber-hash holding all the frames.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(load-file "arities.clj")
(def frame-of {})

;; TODO 
;; - abstractions and specializatons are linked lists, you only go up or down one level and then that frame tells you the next level up/down
;;   - working on adding the abs when you add a spec, and vice-versa (see START HERE)
;; ------------ not getting both, crashes
;; - switch from defstructs to the new thing...records
;; - features not slots

(defstruct frame :name :features :specializations :abstractions)


;; stuff is specializations then abstractions
(defn create-frame	[frame-name slot-list value-list & stuff] 
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
				; specializations
				(cond (frame-of frame-name)
						(conj ((frame-of frame-name) :specializations (first stuff)))
					:t 	(cond (empty? stuff) (list) :t (list (first stuff))))

				; abstractions
				(cond (frame-of frame-name)
						(conj ((frame-of frame-name) :abstractions (second stuff)))
					:t 	(cond (empty? stuff) (list) :t (list (second stuff))))
))))

(defn get-specializations [frame-name]
	(:specializations (frame-of frame-name) :specializations))
	;;(cond (not (nil? (frame-of frame-name))) ((frame-of frame-name) :specializations)))

(defn get-abstractions [frame-name]
	(:abstractions (frame-of frame-name) ))
	;;(cond (not (nil? (frame-of frame-name))) ((frame-of frame-name) :abstractions) ))

(defn add-specialization [frame-name spec-name]

	; add the spec to the frame-name frame
	(def frame-of
		(assoc frame-of frame-name
			;; modify the frame struct
			(assoc (frame-of frame-name) :specializations
				(conj ((frame-of frame-name) :specializations) spec-name))
		))


	; add the abs to the spec-name frame

	; first check to make sure you don't have a nil frame-of for spec-name
	(cond (nil? (frame-of  spec-name))
		(create-frame spec-name (list) (list) ))
	; main work
	(def frame-of
		(assoc frame-of spec-name
			;; modify the frame struct
			(assoc (frame-of spec-name) :abstractions
				(cond (not(nil? ((frame-of spec-name) :specializations)))
						(conj ((frame-of spec-name) :specializations) frame-name)
					; wtf is spec-name null here or specializations
					;;;;;	(conj ((frame-of spec-name) :specializations) frame-name)
					:t (println "YOOOO! you got it"))
					))))

(defn add-abstraction [frame-name abs-name]
	(add-specialization abs-name frame-name))

(defn base-slot [frame-name slot-name]
	(println "fname:" frame-name "slot value:" ((frame-of frame-name) slot-name)  "slot name:" slot-name)
	;;((frame-of frame-name) slot-name)
)


;;; TODO ---> the deeper cases are returning because an empty list is true
(defn get-feature
	"looks first in the native frame, then abstractions, then specializations, returning the first found"
 [frame-name feature-name]
	(println "x"  (frame-name frame-of) )
	(cond (feature-name (:features (frame-name frame-of)) )
		; look local
		 (feature-name (:features (frame-name frame-of))) 
		:t
		; look in abstractions and specializations
		(and 
			:t
			;; for the list of frames in the specializations list, try to get the feature there recursively
			(map (fn [fr-name]  (get-feature fr-name feature-name))
				 	(get-specializations frame-name )) 
			(map (fn [fr-name]  (get-feature fr-name feature-name))
				 	(get-abstractions frame-name ))
)))

(defn print-frame [frame-name]
	(cond (and 
		(not (nil? frame-name))
		(not (nil? (frame-name frame-of))) )
	 (do
		(println "print-frame frame name:" frame-name " contents:" (frame-name frame-of))
		(doseq [s (get-specializations frame-name)] (print-frame s))
		(doseq [a (get-abstractions frame-name)] (print-frame a)) )))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(print-frame nil)
(create-frame :empty-person (list :fname :lname :mi)  nil  )
(print-frame :empty-person)
(println "empty person:")
(print-frame :empty-person)
(println "--------------")

(create-frame :new-person (list :fname :lname :mi) (list "Bob" "Jones" "X") nil nil)
(println "new person:")
(print-frame :new-person)
(println "last name of new person:")
(println (base-slot :new-person :lname))
(println "--------------")

(create-frame :base-thing (list :dbid :mode) (list 123 :test))
(create-frame :new-director (list :company :title :salary) (list "The" "Director" 1000000) :new-person :base-thing)
(println "director with base-thing as abstraction, and new-person as specialization")
(print-frame :new-director)
(println "--------------")
(println "specializations:" (get-specializations :new-director))
(println "THIS IS NULL ?print-frame of specializations") (print-frame (first(get-specializations :new-director)))
(print-frame :base-thing)
(println "------------_________________--")
(println "abstractions:" (get-abstractions :new-director))
(println "print-frame of abstractions") (print-frame (first(get-abstractions :new-director)))
(print-frame :new-person)
(println "------------_________________--")
(println "------------_________________--")
(print-frame :new-director)
(println "------------_________________--")
(println "------------_________________--")
(println "_________________")
(println "title?:" (get-feature :new-director :title))
(println "_________________")
(println "lname?:" (get-feature :new-director :lname))
(println "_________________")
(println "dbid?:" (get-feature :new-director :dbid))
(println "_________________")
