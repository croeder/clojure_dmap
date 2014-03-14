(defstruct junk :a :b :c)
(def j (struct junk "aa" "bb" "cc"))
(println j)
(def junkhash {:jnk j})
(println junkhash)
(println (junkhash :jnk))

(def fname ":a")
(println (j fname))

