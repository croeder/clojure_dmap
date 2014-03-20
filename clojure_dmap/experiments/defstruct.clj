(defstruct junk :a :b :c)
(def j (struct junk "aa" "bb" "cc"))
(println j)
(def junkhash {:jnk j})
(println junkhash)
(println (junkhash :jnk))

(def fname ":a")
(println (j fname))

(println j)
(println (assoc j :a "new-a"))

(println j)

; can you use these like maps with tags first?
(println "---------------------------")
(println (:a j))

(println (:x j))
