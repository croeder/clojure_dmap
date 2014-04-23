(def mymap {})
(println (assoc mymap :a 3))
(println mymap)


; different error messages
;	(println (nil :field-bogus))
;	(def nilmap nil)
; (println (nilmap :field-bogus))))


(defn barf [x] 
	(let [gack 3]
	(loop [xx x yy 3]
		(println xx yy x "gack:" gack)
		(if (> xx 0)
			(recur (- xx 1) (- yy 2) )))) )

(barf 4)


(def data (list 1 2 3 4 5 6 7 8 9 ))
(defn yuk [x]
	(let [gack 3]
		(println "yuk:"` gack)
		(println (map
			;(fn [x] (println x))
			(fn [x] 
				(println "inside:" gack)
				(* x 2)
			)
			data ))))

(yuk 4)

(println (map (fn [x] (+ x 3)) data))

(def complex-keys {
	(list :f :x) "value"
	(list :g :y) "other value"}
)

(println complex-keys)

