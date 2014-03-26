(load-file "utilities/logging.clj")
(load-file "utilities/frame.clj")

; --- stuff it's using from frame.clj
; - frame-of
; - inherited-attribute-value?

;;------------------------------------------------------------------------------
;; Data structure for predictions. These are stored in tables keyed on the
;; "target" of their first phrasal pattern element
; prediction
; prediction-target
; role-specifier
; anytime-predictions-on by prediction-target
; dynamci-predictions-on
;
;  attribute-value ???????????????????//
;
; lisp heritage conventions
; ->xxxx is function that converts to type xxxx
;;------------------------------------------------------------------------------
(defstruct prediction :base :phrasal-pattern :start :next :slots)

(def anytime-predictions-on {})

(def dynamic-predictions-on {})

; WTF
;(eval-when (:compile-toplevel :load-toplevel :execute)
;  (tables:deftable anytime-predictions-on)
;  (tables:deftable dynamic-predictions-on))

(defn make-prediction [&key base phrasal-pattern start next slots]
	" prediction constructor, necessary?"
  (struct :prediction :base :phrasal-pattern :start :next  :slots))

(defn index-anytime-prediction [prediction]
  "Put the phrasal pattern/prediction in the table for its target."
  (push prediction (anytime-predictions-on (prediction-target :prediction))))

(defn role-specifier-p [item] (symbol? item))
(defn role-specifier [item] item)


;; inherited-attribute value and attribute-value both are implemetned in frame.clj/get-feature
;; don't need this it's in frame
;; 
;;(defn inherited-attribute-value [frame attribute]
;;" if either the frame as the attribute, or one of the abstractions has it, return its value,"
;;  (or (attribute-value (->frame frame) attribute)
;;      (for abstraction in (:abstractions frame)
;;            thereis (inherited-attribute-value (->frame abstraction) attribute))))

; TODO move to frame.clj
(defn ->frame [frame]
	"takes either a symbol or an actual frame and returns an actual frame"
	(cond (symbol? frame) (frame-of frame)
		:t frame))

(defn prediction-target (prediction)
  "The target of a phrasal pattern is based on the first item in the
   phrasal pattern yet to be seen. 
   If that item is a role-specifier, then the target is the 
   inherited filler of its role;
   Otherwise, it is just the item itself.
'spec' is a variable name below in the let stmt.
"
  (let ((spec (first (:phrasal-pattern prediction))))
    (if (role-specifier-p spec)
        (let ((base (:base prediction)))
          (or (inherited-attribute-value (frame-of base) (role-specifier spec))
              (error "~S not a role in ~S" (first spec) :base)))
        spec)))


(defn add-phrasal-pattern [base phrasal-pattern]
  "Adds the phrasal pattern of base to the table of static predictions.
help eql? optinal args in struct creation"
  (if (and (eql? base (first phrasal-pattern)) (null (rest phrasal-pattern)))
    nil
    (do (index-anytime-prediction
;; optional args in struct creation
  			(struct :prediction base phrasal-pattern nil nil nil))
            ;(make-prediction :base base :phrasal-pattern phrasal-pattern)) 
           phrasal-pattern)))


;;;;;;;;;;;;;;;;;;;;
(defmacro def-phrase (base &rest phrasal-pattern)
  (if (and (eql? base (car phrasal-pattern)) (null (cdr phrasal-pattern)))
      (error "~S can't reference itself" base)
      `(progn (add-phrasal-pattern ',base ',phrasal-pattern)
              ',phrasal-pattern)))

(defmacro def-phrases (base &rest phrasal-patterns)
  `(loop for phrasal-pattern in ',phrasal-patterns doing
         (add-phrasal-pattern ',base phrasal-pattern)))

(defun index-dynamic-prediction (prediction)
  "Put the phrasal pattern/prediction in the table for its target.
help push"
  (push prediction (dynamic-predictions-on (prediction-target prediction))))

(defun predictions-on (index)
"help append"
  (append (anytime-predictions-on index)
          (dynamic-predictions-on index)))

(defun clear-predictions (&optional (which :dynamic))
"help ecase"
  (ecase which
    (:dynamic (clear-table (dynamic-predictions-on)))
    (:anytime (clear-table (anytime-predictions-on)))
    (:all (clear-table (dynamic-predictions-on))
          (clear-table (anytime-predictions-on)))))

;;------------------------------------------------------------------------------
;; Misc. data structures.
;;------------------------------------------------------------------------------

(defvar *dmap-pos* 0)           ;;global text position

;; Call backs are ad-hoc functions run when a concept (or one of its
;; specializations) is referenced. Function should take three
;; parameters: the item referenced, the start position in the text, and
;; the end position in the text.

(eval-when (:compile-toplevel :load-toplevel :execute)
  (tables:deftable call-backs))

