
;; learn macros

;; create a function to call first, clarify doseq and var args first
(defn create-frame 
[name abs  & slot-pairs ] 
	(println "-name: " name "abs:" abs (.size slot-pairs))
	(doseq [pair slot-pairs]
		(println "  -slot:" pair)))

;(create-frame :test-frame :base  (list :test-slot :slot-type) (list :other-slot :other-type))
(println "==================")

; do-nothing macro
(defmacro pass-through [name abs slot-name slot-type]
	(create-frame name abs (list slot-name slot-type)))

;;(create-frame :name :abs (list :p11 :p12) (list :21 :22))
(pass-through :name :abs :s1 :v1)
(println "==================")


; more involved macro

; (short-frame :name :abstraction (:slot-name :filler-type)
;(defmacro short-frame [& parts]
