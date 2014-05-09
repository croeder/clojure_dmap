
(defn fun [frst & args]
	(doseq [x args] (println x)))

(fun 0)
(fun 0 1)
(fun 0 1 2)
(fun 0 1 2 3)
(defn fun2 [& args]
	(doseq [x args] (println x)))

(fun2 )
(fun2  1)
(fun2  1 2)
(fun2  1 2 3)

