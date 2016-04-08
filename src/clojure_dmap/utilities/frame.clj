(ns clojure-dmap.utilities.frame
	"data structures used in this dmap implementation"
	(use [ clojure.set :only [difference] ])
)

;; A frame is a hash of attribute-value pairs that is part
;; of an inheritance hierarchy.
;; This module includes an uber-hash holding all the frames.

(def  ^:dynamic frame-of {})

;; TODO 
;; - switch from defstructs to the new thing...records ?
;; - the whole frame-of global hash thing is a little awkward in an immutable world

(defstruct frame :name :features :specializations :abstractions)


(defn create-frame	
"Creates a frame as a struct with a :name and a lsit of :feature.  :specializations and :abstractions are part of a dynamic inheritance hierarchy. Their values are lists of symbols to other frames.  stuff is a list of two lists specializations then abstractsion "
	{:test #(do 
				(def a (create-frame :test-frame-a 
							(list :slot1 :slot2 :slot3)  ; features
							(list "a" "b" "c") 			 ; feature values
							(list :spec1 :spec2) 		 ; specialization
							(list :abs1 :abs2))) 		 ; abstractions
				(def x (:test-frame-a frame-of))
				(assert  (not (nil? x)))
				(assert (= (:name x) :test-frame-a))
				(assert (= (:features x) { :slot1 "a" :slot2 "b"  :slot3 "c" }))
				(assert (= (:specializations x) (list :spec1 :spec2)))
				(assert (= (:abstractions x) (list :abs1 :abs2)))

				(def b (create-frame :test-frame-b (list) (list) (list) (list)))
				(def y (:test-frame-b frame-of))
				(assert (not (nil? y)))
				(assert (= (:name y) :test-frame-b))
				(assert (= (:features y) {}))
				(assert (= (:specializations y) (list)))
				(assert (= (:abstractions y) (list)))

				(create-frame :new-person (list :fname :lname :mi) (list "Bob" "Jones" "X") (list) (list))
				(create-frame :base-thing (list :dbid :mode) (list 123 :test) (list) (list))
				(create-frame :new-director (list :company :title :salary) (list "The" "Director" 1000000) (list :new-person) (list :base-thing))
				;;(assert (= "Jones" (get-feature :new-director :lname)))
				;;(assert (= "Director" (get-feature :new-director :title)))
				;;(assert (= 123 (get-feature :new-director :dbid)))
				;;(println "spec" (get-specializations :new-director))
				
			)}
[frame-name slot-list value-list specializations abstractions] 
		(def frame-of 
			(assoc frame-of frame-name 
			  (struct frame  
				; name
				frame-name
				; features
				;; map/conj the list of results into the set
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
				;; reduce f val col
				(cond (frame-of frame-name)
						(reduce conj ((frame-of frame-name) :specializations )
					 		(cond (empty? specializations) (list) :t specializations)  )
					:t 	(cond (empty? specializations) (list) :t specializations))

				; abstractions
				(cond (frame-of frame-name)
						(reduce conj ((frame-of frame-name) :abstractions )
					 		(cond (empty? abstractions) (list) :t abstractions )
						)
					:t 	(cond (empty? abstractions) (list) :t abstractions ))  ))))

(defmacro def-frame [frame abs & slot-val-pairs]
"simplify frame creation, just one abstraction"
	(let [slot-list (map first  slot-val-pairs) 
		  value-list (map last  slot-val-pairs)]
		(create-frame frame slot-list value-list  (list) (list abs)) ))

(defn get-specializations 
"returns a list of symbols to the frames that are specializations of this one"
	{:test #(do 
		(def a (create-frame :test-frame-1 (list :slot1 :slot2 :slot3) (list "1" "2" "3") (list :spec1 :spec2) (list :abs1 :abs2)))
		(assert (= (get-specializations :test-frame-1) (list :spec1 :spec2)))
	)}
[frame-name]
	(:specializations (frame-of frame-name)) )


(defn get-abstractions 
"returns a list of symbols to the frames that are abstractions this one is based on"
	{:test #(do 
		(create-frame :test-frame-2 (list :slot1 :slot2 :slot3) (list "1" "2" "3") (list :spec1 :spec2) (list :abs1 :abs2))
		(assert (= (get-abstractions :test-frame-2) (list :abs1 :abs2)))
	)}
[frame-name]
	(:abstractions (frame-of frame-name)) )


(defn add-specialization 
"add the name of a frame that is a specialization "
	{:test #(do
		(create-frame :test-frame-3 (list) (list) (list) (list))
		(create-frame :spec-frame-1 (list) (list) (list) (list))
		(add-specialization :test-frame-3 :spec-frame-1)
		(println  "testing add-spedc" (first (get-specializations :test-frame-3)) :spec-frame-1)
		(assert (=  (first (get-specializations :test-frame-3)) :spec-frame-1))
		(assert (=  (first (get-abstractions :spec-frame-1)) :test-frame-3))
	)}
[frame-name spec-name]

	; add the spec to the frame-name frame
	(def frame-of
		(assoc frame-of frame-name
			;; modify the frame struct
			(assoc (frame-of frame-name) :specializations
				(conj ((frame-of frame-name) :specializations) spec-name)) ))


	; add the abs to the spec-name frame

	; first check to make sure you don't have a nil frame-of for spec-name
	(cond (nil? (frame-of  spec-name))
		(create-frame spec-name (list) (list) (list) (list) ))
	; main work
	(def frame-of
		(assoc frame-of spec-name
			;; modify the frame struct
			(assoc (frame-of spec-name) :abstractions
				(cond (not(nil? ((frame-of spec-name) :specializations)))
						(conj ((frame-of spec-name) :specializations) frame-name)
					:t (println "YOOOO! you got it"))  ))))

(defn add-abstraction [frame-name abs-name]
	(add-specialization abs-name frame-name))

(defn base-slot [frame-name slot-name]
	(println "fname:" frame-name "slot value:" ((frame-of frame-name) slot-name)  "slot name:" slot-name) )

(defn print-frame 
"this is disgusting. It climbs the hierarchy and recurses down making an infinite loop: FIXME TODO"
[frame-name]
	(cond (and 
		(not (nil? frame-name))
		(not (nil? (frame-name frame-of))) )
	 (do
		(println "print-frame frame name:" frame-name " contents:" (frame-name frame-of))
		(doseq [s (get-specializations frame-name)] (print-frame s))
		(doseq [a (get-abstractions frame-name)] (print-frame a)) )) )

(defn find-relatives
	"Go up the abstraction hierarchy and collect specializaionts/abstractions"
	[frame-name  traverse-fun]
		(loop [	loop-frame frame-name 
				ancestor-set #{} 
				visited-set #{frame-name} ]
				(let [new-ancestor-set 
						(difference 
							(conj 
								(reduce conj ancestor-set	(traverse-fun loop-frame) )
								loop-frame )
							nil)]
					(cond 
						(<= (count (difference  new-ancestor-set visited-set)) 0)
						new-ancestor-set  
						:t 
						(recur 
							(first (difference new-ancestor-set visited-set) )
							new-ancestor-set
							(conj visited-set loop-frame) )))))


(defn find-ancestors
"Go up the abstraction hierarchy and collect specializaionts/abstractions"
	{:test #(do
		(create-frame :abs-frame-2 (list :abs-slot-1 :abs-slot-2) (list "m" "n")    (list :test-frame-4) (list))
		(create-frame :test-frame-4 (list :test-slot-1 :test-slot-2) (list "a" "b") (list :spec-frame-2) (list :abs-frame-2) )
		(create-frame :spec-frame-2 (list :spec-slot-1 :spec-slot-2) (list "x" "z") (list)               (list :test-frame-4) )
		(assert (= (find-ancestors :abs-frame-2), #{:abs-frame-2}))
		(assert (= (find-ancestors :test-frame-4), #{ :abs-frame-2 :test-frame-4}))
		(assert (= (find-ancestors :spec-frame-2), #{ :spec-frame-2 :abs-frame-2 :test-frame-4 }))
	)}
[frame-name]
	(find-relatives frame-name get-abstractions))

(defn find-descendents
"Go up the abstraction hierarchy and collect specializaionts/abstractions"
	{:test #(do
		(println "xxxxxxx" (frame-of :abs-frame-2))
		(create-frame :abs-frame-2 (list :abs-slot-1 :abs-slot-2) (list "m" "n")    (list :test-frame-4) (list))
		(create-frame :test-frame-4 (list :test-slot-1 :test-slot-2) (list "a" "b") (list :spec-frame-2) (list :abs-frame-2) )
		(create-frame :spec-frame-2 (list :spec-slot-1 :spec-slot-2) (list "x" "z") (list)               (list :test-frame-4) )
		(assert (= (find-descendents :abs-frame-2), #{:spec-frame-2 :abs-frame-2 :test-frame-4}))
		(assert (= (find-descendents :test-frame-4), #{ :spec-frame-2 :test-frame-4}))
		(assert (= (find-descendents :spec-frame-2), #{ :spec-frame-2}))
	)}
	[frame-name]
	(find-relatives frame-name get-specializations))

(defn scan-for-match
"goes up  then down  the spec/abs hierarchy looking for a frame with the listed slots. Returns its name. Need to consider if the candidate frame has slots beyond what we're looking for or not and how that relates to the order of the search and what you assume about more slots and mroe specific. Here, I'm reducing over the set passed in verying a candidate frame has each slot.  The first found that matches wins, regardless of other slots it may have."
	{:test #(do
		(create-frame :vehicle (list :type) (list) (list :car) (list)) 
		(create-frame :car (list :country) (list) (list :sedan :sports-car) (list :vehicle) ) 
		(create-frame :sports-car (list :hp) (list) (list :luxury-sedan) (list :car) ) 
		(create-frame :sedan (list :num-seats) (list) (list) (list :car) ) 
		(create-frame :luxury-sedan (list :power-features) (list) (list) (list :sedan) )
		(assert (= :sports-car   (scan-for-match :sports-car #{:hp} ))) ;(assert (= :sports-car   (scan-for-match :car       #{:hp} ))) 
		(assert (= :luxury-sedan (scan-for-match :car       #{:power-features} ))) 
		(assert (= :sedan        (scan-for-match :car       #{:num-seats} ))) 
		(assert (= :sedan        (scan-for-match :sedan     #{:num-seats} ))) 
		(assert (= :sedan        (scan-for-match :luxury-sedan #{:num-seats} ))) ; or should this return luxury-sedan? 
		;(assert (= :sedan   (scan-for-match :car       #{:num-seats} )))
		
	 )}
[start-frame-name slot-name-set]
	(loop [done-set #{}
			count 0
			frames-set (conj (reduce conj #{} 
								(concat (get-abstractions start-frame-name) 
								    (get-specializations start-frame-name) ))
						 start-frame-name ) ]  
		(println "start frames-set:" frames-set "done-set:" done-set  )
		(cond 	(or (nil? frames-set) (nil? (first frames-set)) )
				nil
				:t
			  	(let [slots-list (:features (frame-of (first frames-set))) ]
				  (cond (or (= count 4)
						(or (reduce (fn [y x]  (and y (contains?  slots-list x))) true  slot-name-set)
						(or (empty? slots-list) 
							(every? nil? slots-list))))
					(do (println "exit;") (first frames-set) )
					:t 
					(recur 
						(conj done-set (first frames-set))
						(+ count 1)
						(difference ; add abs and spec to frame-set
							(reduce conj frames-set
								(reduce conj (get-abstractions (first frames-set))
									(get-specializations (first frames-set))))
							(conj done-set (first frames-set)) )
				))))))


(defn get-feature
"looks first in the native frame, then abstractions, then specializations, 
returning the first found this is disgusting. It climbs the hierarchy and 
recurses down making an infinite lewp FIXME TODO"
	{:test #(do
		(create-frame :test-frame-4 (list :test-slot-1 :test-slot-2) (list "a" "b") (list) (list))
		(create-frame :spec-frame-2 (list :spec-slot-1 :spec-slot-2) (list "x" "z") (list) (list))
		(create-frame :abs-frame-2 (list :abs-slot-1 :abs-slot-2) (list "m" "n") (list) (list))
		(add-specialization :test-frame-4 :spec-frame-2)
		(add-abstraction :test-frame-4 :abs-frame-2)

		(println "testing get-feature with this frame")
		(print-frame :test-frame-4)
		;(println "test get-feature"  (get-feature :test-frame-4 :test-slot-1))
		;(assert (=  (get-feature :test-frame-4 :test-slot-1) "a"))
		;(assert (=  (get-feature :test-frame-4 :test-slot-2) "b"))

		(println "test get-feature" (get-feature :test-frame-4 :abs-slot-1) )
		;;(assert (=  (get-feature :test-frame-4 :abs-slot-1) "m"))
		;(assert (=  (get-feature :test-frame-4 :abs-slot-2) "n"))
		;(println "test get-feature 2" (get-feature :test-frame-4 :spec-slot-1) )
		;(assert (=  (get-feature :test-frame-4 :spec-slot-1) "x"))
		;(assert (=  (get-feature :test-frame-4 :spec-slot-2) "z"))
		
	)}
 [frame-name feature-name]
	(cond (feature-name (:features (frame-name frame-of)) )
			(feature-name (:features (frame-name frame-of))) 
		  :t
			(and 
				(do 
					(println "branch a" frame-name feature-name)
					(map (fn [fr-name] 	
							(do 
								(println "  mapping in branch a" fr-name feature-name)
								(get-feature fr-name feature-name)))
				 		(get-specializations frame-name )))
				(do 
					(println "branch b" frame-name feature-name)
					(map (fn [fr-name]  
							(do 
								(println "  mapping in branch b" fr-name feature-name)
								(get-feature fr-name feature-name)))
			 			(get-abstractions frame-name )) )   )))



(if nil (do
(test #'create-frame)
(test #'get-abstractions)
(test #'get-abstractions)
(test #'add-specialization)
(test #'get-specializations)
(test #'get-feature)
(test #'find-ancestors)
(test #'find-descendents)
(test #'scan-for-match)))

