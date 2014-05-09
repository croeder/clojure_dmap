; srsly? let hides the greater scope?

(defn barf [x]
	(let [y x]
		(println x) 
		(println y) ))

(barf 3)
; no....

(let [x 1 y (+ 2 x)]
	(println x y))

