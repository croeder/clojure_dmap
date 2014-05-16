(cond (= :foo :foo) (println "ok") :t (println "no"))

(cond (= :foo ":foo") (println "ok") :t (println "no"))
(println (symbol? "foo"))
(println (symbol? ":foo"))
(println (symbol? :foo))
(println (keyword? :foo))

