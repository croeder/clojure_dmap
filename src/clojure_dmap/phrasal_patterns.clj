(ns  clojure-dmap.phrasal-patterns)

(def phrasal-patterns-map {})

(defstruct phrasal-pattern :name :frame :slots)

(defn create-phrasal-pattern
	""
	[name frame & slots]
	(def phrasal-patterns-map
		(assoc phrasal-patterns-map name
			(struct phrasal-pattern
				name
				frame
				slots)))
	(phrasal-patterns-map name))



