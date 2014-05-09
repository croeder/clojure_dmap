(ns clojure-dmap.dmap
 "pattern matching"
	(use 
		[ clojure.string ]
		[ clojure-dmap.frames ]
		[ clojure-dmap.patterns ] 
))

;phrasal-patterns-map


;; yo start here
;; - 1 can you find a pattern for the toekn
;; - if so, adanvace it
;; - if the adavance was nil, oh well
;; - it the adanvace returns the rule, replace it in the map

(defn match-patterns [input]
	(let [ tokens (split input #"\s") ]
			(doseq [tkn tokens]
				(let [ptn (phrasal-patterns-map tkn)]
					(cond ptn (def pharsal-patterns-map 
						(assoc phrasal-patterns-map 
							(advance-pattern pt tkn)))
						:t nil)))))


(defn -main [& args] 
	(load-frames) 
	(load-phrases)
	(match-patterns "Chris rode the bus to work.")	
)


