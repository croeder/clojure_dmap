; what fun.s to export?
;; reset-log
;; record-log
;; print-log
;; with-logging



;;; TODO: only taking one log key at a time, others lost in redef?


(load-file "logging.clj")

(defn fact [n]
  ;; TODO
  (record-log 'fact  (str "entering FACT with " n ))
  (cond
   (= n 1) 1
   :t (* (fact (- n 1)) n) ))

(defn myplus [a b]
  (record-log 'myplus  (str "entering myplus with " a " " b ))
	(+ a b))

(with-logging (myplus 3 4))
;;;;;;;;;(set-logging :t)
;;;;;(with-logging (fact 3))
(with-logging (fact 4))
;;(set-logging )
;;(with-logging (fact 3))
(print-log 'fact)
(print-log 'myplus)
(reset-log)
(print-log 'fact)
(print-log 'myplus)
(print-log "shold-be-empty-or-non-exitent")

