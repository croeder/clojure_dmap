
;(use 'clojure.set)
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
