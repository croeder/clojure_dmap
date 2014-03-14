


;; alter-var-root ????


;; need macros
;; how do you set a global var in  an immutable world?
;; ...in a stateless world? bindings?
;; A log is a list of statements keyed off a symbolic form.
;;
;;
;; use binding for the flag to log or not, create it at some artificial top-level,
;;   but also at the with-logging level for local custom settings
;; use assoc with the log-of hash to add entries
;;------------------------------------------------------------------------------

; what fun.s to export?
;; reset-log
;; record-log
;; print-log
;; with-logging

(def log-of { } )
(def *logging* :t)


(defn reset-log []
  (def log-of {})
  (def  *logs* {})
  *logging*)

(defn set-logging 
		;;([] (set! *logging* :t))
		;;([value] (set! *logging* value)) )
		([] 
			(println "*logging* before " *logging*)
			(alter-var-root #'*logging* :t)
			(println "*logging* after " *logging*)
		)
		([value] 
			(println "*logging* before2 " *logging*)
			(alter-var-root #'*logging* value)
			(println "*logging* after2 " *logging*)
		)
	)



(defmacro with-logging [body#] 	
	;;(reset-log )
	(doto body#
		`body#))

(defn record-log 
  ([logname string ]
	(when *logging* 
		(when (= (log-of logname) nil) (def log-of (assoc log-of logname (list ))) )
    	(def log-of (assoc log-of logname (conj (log-of logname) string) ))
    	*logging*))
  ([logname string arg ]
	(when *logging* 
		(when (= (log-of logname) nil) (def log-of (assoc log-of logname (list )) )  )
    	(def log-of (assoc log-of logname (conj (log-of logname) (str  string " " arg) ) ))
    	*logging*))  )

(defn print-log [logname]
  (println "---- log ----" logname) (if logname (doseq [log-entry (reverse (log-of logname))] (println log-entry))))
