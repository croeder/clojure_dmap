(ns  clojure-dmap.phrasal-patterns
	(use [ clojure.string :exclude [replace reverse]]
		 ;[ clojure-dmap.frames ]
		 [ clojure-dmap.utilities.frame ]) 
)

;;;;; phrasal-patterns
;;;;;
; TODO;
; remove use of rule in favor of pattern
; remove use of *tother* in favor of concept
; remove use of create-pattern or whatever
; fix up tabs
; fxi up comments by ; ;; ;;;, ;;;;; etc.
; fix up public/private functions #_
; fix up ears for dynamic variables (done)
; indent arg vectors and comment strings
; that annoying todo below about needing i computed before hand

	
(def ^:dynamic *phrasal-patterns-map* {})
(def ^:dynamic *completed-patterns* {})


(defn reset []
	(def *phrasal-patterns-map* {})
	(def *completed-patterns* {}) 
	(println ""))

; A phrasal pattern is used to define and run or advance pattern discovery.
; It's simplest form is a list of literals to be matched. Clojure
; keyword syntax is used to identify concepts to be matched.
; :string  is the pattern string consisting of unquoted literals or keywords
; :frame is the name of the frame it fills or creates 
; :slots-values is a map of types to filled values. Any reasoning-like
;   behavior to arrive at the value using the ontology or other
;   rules is not preserved.
; :token-index is the index of the token we're trying to match
(defstruct phrasal-pattern :tokens :frame :slot-values  :token-index )


(defn dump-patterns 
"print the list of known patterns"
[]
	(doseq  [ key (keys *phrasal-patterns-map*)]
		(doseq [ rule (*phrasal-patterns-map* key)]
			(println "key: " key " rule: " rule) )))
	
(defn dump-completed-patterns 
"print the list of completed patterns"
[]
	(doseq  [ key (keys *completed-patterns*)]
			(println key (*completed-patterns* key)) ))
	
(defn complete-pattern?
"a pattern is complete if its token-index is > the number of 
tokens: if it's been advanced as far as possible."
[pattern]
	(>= (:token-index pattern) (.size (:tokens pattern))))


(defn add-pattern 
"removes a pattern from the list associated with it's previous token
and adds it to the list for its current token. Border cases notwithstanding.
There's a comment in Dr Farrell's work about how advancing a rule takes
a different meaning based on the type of the token: literal or symbol.
It appears here when the pattern is completed and added to the 
*completed-patterns* list. It must be a symbol/keyword there for lookup.
"
[ptn]
	; remove ptn from list for past token, unless it's the first token
	(let [prev-index (- (ptn :token-index) 1)] 
		(cond 
			(> prev-index 0)
			(let [  active-token 
					(nth (ptn :tokens) prev-index)
	  				active-list 
					(cond (nil? (*phrasal-patterns-map* active-token)) ()
						:t 	(*phrasal-patterns-map* active-token))]

				(def *phrasal-patterns-map*
			   			(assoc *phrasal-patterns-map* active-token 
						(remove (fn [x] (= (x :token-index) (- (ptn :token-index) 1))) active-list)) ) )
			:t 
			nil))

	; add ptn to list for current token, unless that was the last one
	(cond (not (complete-pattern? ptn))
		(let [active-token (nth (ptn :tokens) (ptn :token-index) )
			  active-list (cond (nil? (*phrasal-patterns-map* active-token)) ()
							:t 	(*phrasal-patterns-map* active-token))]
			(def *phrasal-patterns-map*
					(assoc *phrasal-patterns-map* active-token (conj active-list ptn))) )
		:t 
			(def *completed-patterns* (assoc *completed-patterns* (ptn :frame) ptn)) )
	ptn)


(defn create-phrasal-pattern
"function for creating patterns, a place to localize common behavior. Historical. REMVOE?"
[tokens frame token-index  slot-values]
	(struct phrasal-pattern tokens frame slot-values token-index) )


(defn def-phrasal-pattern
"for creating patterns with a shorter/simpler list of arguments"
[token-string frame ]
	(add-pattern (create-phrasal-pattern  (split token-string #"\s") frame 0  {} )))


(defn advance-pattern-literal
"Advances a pattern with a literal. Works for patterns with a single
literal where the slot name comes from the pattern. Literal parts of longer
patterns present a problem: what slot name to use?
I will use the pattern name, but make the value a list"
[pattern  literal]
	(cond 
		(and (not (keyword? literal))
			(and (not (complete-pattern? pattern))
					(= literal (nth (:tokens pattern) (:token-index pattern)))))
		(do 
			(def i (:token-index pattern))
			(print "      advance-literal slots:" (:slot-values pattern) " frame:" (pattern :frame) " literal:" literal) 
			(let [retval (create-phrasal-pattern 
				(:tokens pattern) 
				(:frame pattern) 
				(+ 1 (:token-index pattern)) 
				(assoc (:slot-values pattern) 
					(pattern :frame) 
					(conj ((pattern :slot-values) (pattern :frame)) literal))) ]
				(if (complete-pattern? retval) 
					(println " is complete.")
					(println " is waiting for " (retval :token-index) 
							(nth (retval :tokens) (retval :token-index)) ))
				retval))
		:t nil))
		;:t (do (println "advance pattern returning ***nil***") nil)))
		

 
(defn advance-pattern-concept 
"Tries to advance or initiate an instance of a pattern based
on the concept/symbol passed in, returns an updated pattern or nil"
[pattern slot]
	(cond 
		(and (not (complete-pattern? pattern))
				(= (first (keys slot)) (nth (:tokens pattern) (:token-index pattern))))
			(do 
				(def i (:token-index pattern))
				(print "      advance-concept slots:" (:slot-values pattern) " frame:" (pattern :frame) " slot:" slot)
				(let [pp 
					(create-phrasal-pattern 
						(:tokens pattern) 
						(:frame pattern) 
						(+ 1 (:token-index pattern)) 
						(merge (:slot-values pattern)  slot)
						; TODO: why does i need save above?
						;(assoc 	(:slot-vlaues pattern) (nth (:tokens pattern) (:token-index pattern)) value )
				  )]
					(if (complete-pattern? pp) 
						(println " " (pp :frame) " is complete.")
						(println " " (pp :frame) " is waiting for " 
							(pp :token-index) 
							(nth (pp :tokens) (pp :token-index)) ))
					;;(println "concept advanced pattern" pp)
					pp  ))
		:t  nil))
		;:t (do (println "advance pattern returning ***nil***") nil)))

		
(defn propagate-advances []
"After rules are advanced, new symbols may be satisfied. 
This goes through the list of symbols and tries to satisfy the other rules. 
It doesn't generalize the symbols by way of the ontology hierarchy"
	(doseq [sym (keys *completed-patterns*)]
			(doseq [rule (*phrasal-patterns-map* sym)]
				(let [matched-sym (:slot-values (sym *completed-patterns*))
					  matched-value  (rule :slot-values)
						adv-rule (advance-pattern-concept rule  matched-sym)] ; FIX doesnt'w ork consistenly either
					;(println "GEN:  matched-sym:" matched-sym (rule :frame))
					(add-pattern adv-rule) ))))


;; these lists get the more advanced concepts, but don't includde a value

(defn propagate-advances-generalize []
"After rules are advanced, new symbols may be satisfied.  This 
goes through the list of symbols and tries to satisfy the other rules. 
The matching happens in advance-pattern, this is just throwing spaghetti
against that wall."
; find generalizations and specializations of the symbol, 
; then look up a rule for that, and advance it. This is where 
; there is room for lots of variation in the matching.
	(let [frames-list ( apply concat
						(map (fn [ptn] (concat (get-abstractions ptn) (get-specializations ptn)))
								(keys *completed-patterns*))) ]
		(doseq [sym frames-list]
			(doseq [rule (*phrasal-patterns-map* sym)]
				(let [matched-sym (:slot-values (sym *completed-patterns*))
					  matched-value  (rule :slot-values)
					  adv-rule (advance-pattern-concept rule matched-sym )] ; FIX
					;(println "ADV:" sym matched-sym matched-value)
					(cond adv-rule (add-pattern adv-rule))
				))
)))


(defn match-patterns [input]
"Go through the tokens of the input string matching rules and propagating matches."
	(println "INPUT: " input)
	(let [tokens (split input #"\s")]
		(doseq [tkn tokens]
			(println "  token:" tkn)
			(doseq [pattern (*phrasal-patterns-map* tkn)]
				(println "    ptn:" (pattern :frame))
				;(let [ x  (advance-pattern pattern (pattern :frame) tkn) ]
				(let [ x  (advance-pattern-literal pattern tkn) ]
					(add-pattern x)
				))
				(propagate-advances)
				;(propagate-advances-generalize) 
			)))

