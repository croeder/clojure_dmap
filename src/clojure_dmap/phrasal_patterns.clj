(ns  clojure-dmap.phrasal-patterns
	(use [ clojure.string :exclude [replace reverse]]) 
)

; a hash-map of patterns. 
; Keys are either the first token of an inactive pattern or the
; token or symbol a pattern is waiting on.
; Values are lists of patterns waiting on that token.

(def phrasal-patterns-map {})


; a phrasal pattern is used to define and run or advance pattern discovery
; :string  is the pattern string
; :frame is the name of the frame it fills (or nil)
; :slots are the names of slots and their (frame) types
; :token-index is the index of the token we're trying to match
;; SO, IN FACT, THAT MEANS some syntactical ORDER is enforced in here, yuk?, TODO

(defstruct phrasal-pattern :tokens :frame :slots :token-index)

(defn add-pattern [ptn]
	; remove ptn from list for past token, unless it's the first token
	(let [prev-index (- (ptn :token-index) 1)]
		(cond (> prev-index 0)
			(do 
				(let 	[active-token 
						(nth (ptn :tokens) prev-index)
		  				active-list 
						(cond (nil? (phrasal-patterns-map active-token)) ()
							:t 	(phrasal-patterns-map active-token))]
					(do
						(def phrasal-patterns-map
						 (assoc phrasal-patterns-map active-token (remove (fn [x] (= x ptn)) active-list)) ))))
			:t nil))


	; add ptn to list for current token
	(let [active-token (nth (ptn :tokens) (ptn :token-index))
		  active-list (cond (nil? (phrasal-patterns-map active-token)) ()
						:t 	(phrasal-patterns-map active-token))]
		(def phrasal-patterns-map
				(assoc phrasal-patterns-map active-token (conj active-list ptn))) ))

(defn create-phrasal-pattern
	[tokens frame token-index & slots]
			(add-pattern (struct phrasal-pattern tokens frame slots token-index)))

(defmacro def-phrasal-pattern
	[token-string frame & slots]
	(create-phrasal-pattern  (split token-string #"\s") frame 0 slots ))


(defn advance-pattern 
"takes a token and tries to advance or initiate an instance of a pattern, returns an updated pattern or nil"
[pattern token]
; the problem here is taht pattern is a *list* of patterns!!! FIX TODO
	(cond (= token (nth (:tokens pattern) (:token-index pattern)))
		(create-phrasal-pattern 
			(:tokens pattern) (:frame pattern) (+ 1 (:token-index pattern)) (:slots pattern) )
		:t nil))
			
	


