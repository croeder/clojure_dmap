(ns clojure-dmap.dmap
 "pattern matching"
	(use [clojure-dmap.utilities.frame]
		[clojure.string :exclude [reverse replace]]
		[clojure-dmap.frames]
))


(defn -main [& args] 
	(load-frames) )


