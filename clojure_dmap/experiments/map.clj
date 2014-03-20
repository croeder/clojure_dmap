(def mymap {})
(println (assoc mymap :a 3))
(println mymap)

; different error messages
(println (nil :field-bogus))
(def nilmap nil)
(println (nilmap :field-bogus))
