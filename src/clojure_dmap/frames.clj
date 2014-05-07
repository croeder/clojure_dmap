(ns clojure-dmap.frames
"avoiding evaling these lines in by collecting them into a function here."
	(use [clojure-dmap.utilities.frame]
))

(defn load-frames []
(def-frame :m-root nil)
(def-frame :m-reference-concept :m-root)
(def-frame :m-index :m-root)
	(def-frame :m-problem :m-index)
		(def-frame :m-water-bits :m-problem)

			(def-frame :m-coloured-water-bits :m-water-bits
			  (:colour :m-colour)
			  (:object :m-water-bits))
			(def-frame :m-black-water-bits :m-coloured-water-bits
				  (:colour :m-black)
				 ; (:object :m-water-bits)
  				)

			(def-frame :m-burst :m-problem)
	(def-frame :m-contain :m-index)
	(def-frame :m-description :m-index)
	(def-frame :m-duration :m-index)
	(def-frame :m-colour :m-index)
		(def-frame :m-black :m-colour)
	(def-frame :m-water :m-index)
	(def-frame :m-greet :m-index)
	(def-frame :m-description :m-index)
	(def-frame :m-address :m-index)
	(def-frame :m-name :m-index)
	(def-frame :m-see :m-index)
	(def-frame :m-fire-brigade :m-index)
	(def-frame :m-neighbourhood :m-index)
	(def-frame :m-lead :m-index)
	(def-frame :m-sediment :m-index)
	(def-frame :m-safe :m-index)
)
