(ns  clojure-dmap.phrasal-patterns
	(use [ clojure.string :exclude [replace reverse]]
		 [ clojure-dmap.utilities.frame ]) 
)

; TODO the symbols don't match right, nor is their use apropriately described using the implemenation detail of symbol any good


; a hash-map of patterns. 
; Keys are either the first token of an inactive pattern or the
; token or symbol a pattern is waiting on.
; Values are lists of patterns waiting on that token.

(def phrasal-patterns-map {})
(def completed-patterns {})


; a phrasal pattern is used to define and run or advance pattern discovery
; :string  is the pattern string
; :frame is the name of the frame it fills (or nil)
; :slots are the names of slots and their (frame) types
; :token-index is the index of the token we're trying to match
;; SO, IN FACT, THAT MEANS some syntactical ORDER is enforced in here, yuk?, TODO

(defstruct phrasal-pattern :tokens :frame :slots :token-index)

(defn dump-patterns []
	(println "-- patterns --")
	(doseq  [ key (keys phrasal-patterns-map)]
		(doseq [ rule (phrasal-patterns-map key)]
			(println "key: " key " rule: " rule) )))
	
(defn dump-completed-patterns []
	(println "-- completed patterns --")
	(doseq  [ key (keys completed-patterns)]
			(println key (completed-patterns key))
			(println "------------")))
	
(defn complete-pattern?
"a pattern is complete if its token-index is > the number of tokens: if it's been advanced as far as possible."
[pattern]
	(>= (:token-index pattern) (.size (:tokens pattern))))


(defn add-pattern 
[ptn]
	; remove ptn from list for past token, unless it's the first token
	(let [prev-index (- (ptn :token-index) 1)]
		(cond (> prev-index 0)
			(do 
				(let [  active-token 
						(nth (ptn :tokens) prev-index)
		  				active-list 
						(cond (nil? (phrasal-patterns-map active-token)) ()
							:t 	(phrasal-patterns-map active-token))]
					(do
						(def phrasal-patterns-map
						   (assoc phrasal-patterns-map active-token (remove (fn [x] (= x ptn)) active-list)) ))))
			:t nil))

	; add ptn to list for current token, unless that was the last one
	(cond (not (complete-pattern? ptn))
		(let [active-token (nth (ptn :tokens) (ptn :token-index) )
			  active-list (cond (nil? (phrasal-patterns-map active-token)) ()
							:t 	(phrasal-patterns-map active-token))]
			(do
				;;(println " adding pattern on key:" active-token "rule:"  ptn)
				(def phrasal-patterns-map
					(assoc phrasal-patterns-map active-token (conj active-list ptn))) 
			)  )
		:t (do
				(def completed-patterns (assoc completed-patterns (ptn :frame) ptn))
				;;(println "completed key:" (ptn :frame) " pattern "  ptn))
				)
	)
	ptn)


(defn create-phrasal-pattern
	[tokens frame token-index slots]
		(struct phrasal-pattern tokens frame slots token-index) )


;;(defmacro def-phrasal-pattern
(defn def-phrasal-pattern
	[token-string frame & slots]
	(add-pattern (create-phrasal-pattern  (split token-string #"\s") frame 0 slots )))



(defn advance-pattern 
"takes a token and tries to advance or initiate an instance of a pattern, returns an updated pattern or nil"
[pattern token]
	;;(if (keyword? token) (println " KEYWORD  on advance" token ))
	(cond (and  (not (complete-pattern? pattern))
				(= token (nth (:tokens pattern) (:token-index pattern))))
		(do
			;;(if (keyword? token) (do (println "") (println " advancing KEYWORD  on token" token " pattern" pattern)))
			(let [ x (create-phrasal-pattern 
						(:tokens pattern) (:frame pattern) (+ 1 (:token-index pattern)) (:slots pattern) ) ]
				(do 
					;;(println "advanced pattern......" x)
					x))
				)
		:t (do (println "advance pattern returning ***nil***") nil)))


		
(defn propagate-advances []
"after rules are advanced, new symbols may be satisfied. 
This goes through the list of symbols and tries to satisfy the other rules. 
It doesn't generalize the symbols by way of the ontology hierarchy"
	(doseq [sym (keys completed-patterns)]
			(doseq [rule (phrasal-patterns-map sym)]
				(let [adv-rule (advance-pattern rule sym)]
					;;(println "PROP:" sym adv-rule)
					(add-pattern adv-rule)
				))))

(defn propagate-advances-generalize []
"after rules are advanced, new symbols may be satisfied. 
This goes through the list of symbols and tries to satisfy the other rules. 
It *does* generalize the symbols by way of the ontology hierarchy"
; find generalizations and specializations of the symbol, then look up a rule for that, and advance it
; This is where there is room for lots of variation in the matching.

	
	(let [frames-list ( apply concat
						(map (fn [ptn] (concat (get-abstractions ptn) (get-specializations ptn)))
								(keys completed-patterns))) ]
		(println "FRAMES:" frames-list)
		(doseq [sym frames-list]
			(doseq [rule (phrasal-patterns-map sym)]
				(let [adv-rule (advance-pattern rule sym)]
					(println "PROP:" sym adv-rule)
					(add-pattern adv-rule)
				)))))


	


