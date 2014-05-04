
(def test2 #{ "a" "bc" "cd" "de"})
(def f (fn [y x]  
			(println "x:" x "val:" (contains?  test2 x)) 
			(and y (contains?  test2 x))))
(println "-->" (f true "a" ))
(println (reduce  f true  test2))

