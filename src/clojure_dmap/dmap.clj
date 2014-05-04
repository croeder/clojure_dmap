(ns clojure-dmap.dmap)


(defn load-frames [filename ] 
	(eval (slurp filename)))

(defn load-patterns [])

(defn load-phrasal-patterns [])

(defn -main [& args] (load-frames "resources/frames.data"))

