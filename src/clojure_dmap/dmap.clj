(ns clojure-dmap.dmap
 "pattern matching"
	(use 
		[ clojure-dmap.frames ]
		[ clojure-dmap.patterns ]
))

(defn -main [& args] 
	(load-frames) 
	(load-phrases)
)


