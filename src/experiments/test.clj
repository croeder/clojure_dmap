(defn myfun  
	" this is the doc string, metadata follows"
	{:test #(do 
		(assert (= (myfun 3 4) 7))
		(assert (= (myfun 0 3) 88))
		)}
	[a b ](+ a b))

(defn myfun2  
	" this is the doc string, metadata follows"
	{:test (fun [] (do 
		(assert (= (myfun 3 4) 7))
		(assert (= (myfun 0 3) 88))
		))}
	[a b ](+ a b))



;;(test #'myfun)
(test #'myfun2)
