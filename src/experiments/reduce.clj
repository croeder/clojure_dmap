

; (reduce fn[val col] init-val val-coll)
; -or-  (reduce fn[accumulator val] init-val val-coll)
; (conj col thing)

(def data (list 9 7 5 3 1 ))
(println (reduce (fn [x y] (+ x y)) data))
(def myset #{ :a :b})
;;(println (reduce (fn [val col] (conj col val)) data myset))
;;(println (reduce (fn [val col] (def myset (conj col val))) data myset))



;(println (reduce (fn [val col] (conj col val))  myset data))
(println (reduce (fn [val col] (conj val col))  myset data))
(println (reduce conj   myset data))

