
(defn load-eval
[filepath]
	(with-open [rdr (clojure.java.io/reader "resources/frames.data")] 
		(doseq [line  (line-seq rdr)]
			(let [new-frame (eval (read-string line))]
				(println "frame name: " (:name new-frame))
				)))
	(println (.size frames)))



