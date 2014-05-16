(ns  clojure-dmap.phrasal-patterns
	(use [ clojure.string :exclude [replace reverse]]
		 ;[ clojure-dmap.frames ]
		 [ clojure-dmap.utilities.frame ]) 
)

; functions relating to and collecting phrasal patterns.
; a phrasal-pattern is a list of tokens to match. A token
; is either a literal or a keyword that names a frame produced
; by a rule or names a frame related to such a rule in the ontology.

(def phrasal-patterns-map {})
(def completed-patterns {})


(defn reset []
	(def phrasal-patterns-map {})
	(def completed-patterns {}) 
	(println ""))

; a phrasal pattern is used to define and run or advance pattern discovery
; :string  is the pattern string
; :frame is the name of the frame it fills (or nil)
; :slots are the names of slots and their (frame) types
; :token-index is the index of the token we're trying to match
; so that means that  ome syntactical order is enforced in here
(defstruct phrasal-pattern :tokens :frame :slots :token-index)


(defn dump-patterns []
	(doseq  [ key (keys phrasal-patterns-map)]
		(doseq [ rule (phrasal-patterns-map key)]
			(println "key: " key " rule: " rule) )))
	
(defn dump-completed-patterns []
	(doseq  [ key (keys completed-patterns)]
			(println key (completed-patterns key)) ))
	
(defn complete-pattern?
"a pattern is complete if its token-index is > the number of 
tokens: if it's been advanced as far as possible."
[pattern]
	(>= (:token-index pattern) (.size (:tokens pattern))))


(defn add-pattern 
"removes a pattern from the list associated with it's previous token
and adds it to the list for its current token. Border cases notwithstanding."
[ptn]
	; remove ptn from list for past token, unless it's the first token
	(let [prev-index (- (ptn :token-index) 1)]
		(cond (> prev-index 0)
			(let [  active-token 
					(nth (ptn :tokens) prev-index)
	  				active-list 
					(cond (nil? (phrasal-patterns-map active-token)) ()
						:t 	(phrasal-patterns-map active-token))]
				(def phrasal-patterns-map
			   		(assoc phrasal-patterns-map active-token 
						(remove (fn [x] (= x ptn)) active-list)) ))
			:t nil))

	; add ptn to list for current token, unless that was the last one
	(cond (not (complete-pattern? ptn))
		(let [active-token (nth (ptn :tokens) (ptn :token-index) )
			  active-list (cond (nil? (phrasal-patterns-map active-token)) ()
							:t 	(phrasal-patterns-map active-token))]
			(def phrasal-patterns-map
					(assoc phrasal-patterns-map active-token (conj active-list ptn))) )
		:t 
			(def completed-patterns (assoc completed-patterns (ptn :frame) ptn)) )
	ptn)


(defn create-phrasal-pattern
	[tokens frame token-index slots]
		(struct phrasal-pattern tokens frame slots token-index) )


(defn def-phrasal-pattern
	[token-string frame & slots]
	(add-pattern (create-phrasal-pattern  (split token-string #"\s") frame 0 slots )))


(defn advance-pattern 
"Takes a token and tries to advance or initiate an instance 
of a pattern, returns an updated pattern or nil"
[pattern token]
	(cond (and (not (complete-pattern? pattern))
				(= token (nth (:tokens pattern) (:token-index pattern))))
			(create-phrasal-pattern 
						(:tokens pattern) (:frame pattern) 
						(+ 1 (:token-index pattern)) (:slots pattern) )
		:t (do (println "advance pattern returning ***nil***") nil)))

		
(defn propagate-advances []
"After rules are advanced, new symbols may be satisfied. 
This goes through the list of symbols and tries to satisfy the other rules. 
It doesn't generalize the symbols by way of the ontology hierarchy"
	(doseq [sym (keys completed-patterns)]
			(doseq [rule (phrasal-patterns-map sym)]
				(let [adv-rule (advance-pattern rule sym)]
					(add-pattern adv-rule) ))))

(defn propagate-advances-generalize []
"After rules are advanced, new symbols may be satisfied.  This 
goes through the list of symbols and tries to satisfy the other rules. 
It *does* generalize the symbols by way of the ontology hierarchy"
; find generalizations and specializations of the symbol, 
; then look up a rule for that, and advance it. This is where 
; there is room for lots of variation in the matching.
	(let [frames-list ( apply concat
						(map (fn [ptn] (concat (get-abstractions ptn) (get-specializations ptn)))
								(keys completed-patterns))) ]
		(doseq [sym frames-list]
			(doseq [rule (phrasal-patterns-map sym)]
				(let [adv-rule (advance-pattern rule sym)]
					(add-pattern adv-rule)
				)))))


	


