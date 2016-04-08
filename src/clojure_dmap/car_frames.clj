(ns clojure-dmap.car-frames
"avoiding evaling these lines in by collecting them into a function here."
	(use [ clojure-dmap.utilities.frame ]
))

(defn load-frames []

	(def-frame :m-root nil)
		(def-frame :m-person :m-root)
		(def-frame :m-purchase :m-root)
		(def-frame :m-color  :m-root)
		(def-frame :m-make :m-root)
		(def-frame :m-model :m-root)
		(def-frame :m-year :m-root)
	 	(def-frame :m-vehicle :m-root)
		(def-frame :m-car :m-vehicle
			(:color :m-color)
		    (:make  :m-make)
			(:model :m-model)
			(:year  :m-year) )		
		(def-frame :m-purchase-event :m-root
			(:person :m-person)
			(:purchase :m-purchase)
			(:color  :m-color)
			(:type   :m-type)
			(:vehicle :m-vehicle))
)
