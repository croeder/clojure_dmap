
(use 'clojure.set)
(use '[clojure.set :only [difference]])

(def myset #{})
(def myset (conj myset :x ))
(def myset (conj myset :y ))
(def myset (conj myset :x ))
(println myset)
(def myset (conj myset nil ))
(def myset (conj myset nil ))
(def myset (conj myset nil ))
(println myset)

(def bigset #{ :a :b :c :d :e})
(println bigset)
(println (difference bigset #{:b :z}))
(println "first of empty" (first #{}))


; can't really conj a list into a set...you get  {a b c (x y z)}
(println (conj bigset (list :m :n :o)))
; union doesn't help, wrong type
;(println (union bigset (list :m :n :o)))
(println (union #{:a :b} #{:c :d}))
(println (conj bigset :m))


; can you conj a set into a list?
(println (conj (list :x :y :z) bigset))
