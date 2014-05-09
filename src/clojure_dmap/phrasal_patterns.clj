(ns  clojure-dmap.phrasal-patterns
	(use [ clojure.string ])
)

(def phrasal-patterns-seq [])


; a phrasal pattern is used to define and run or advance pattern discovery
; :string  is the pattern string
; :frame is the name of the frame it fills (or nil)
; :slots are the names of slots and their (frame) types
; :token-index is the index of the token we're trying to match
;; SO, IN FACT, THAT MEANS some syntactical ORDER is enforced in here, yuk?, TODO

(defstruct phrasal-pattern :tokens :frame :slots :token-index)



(defn create-phrasal-pattern
	[tokens frame token-index  & slots ]
	(def phrasal-patterns-map
		(conj phrasal-patterns-seq name
			(struct phrasal-pattern
				tokens
				frame
				slots
				token-index)))
	(last phrasal-patterns-seq)) ; or first?

(defmacro def-phrasal-pattern
	[token-string frame & slots]
	(create-phrasal-pattern  (split token-string #"\s") frame 0 slots ))


(defn advance-phrasal-pattern 
"takes a token and tries to advance or initiate an instance of a pattern, returns an updated pattern or nil"
[token pattern]
	(cond (= token (nth (:tokens pattern) (:token-index pattern)))
		(create-phrasal-pattern 
			(:tokens pattern) (:frame pattern) (+ 1 (:token-index pattern)) (:slots pattern) )
		:t nil))
			
	


