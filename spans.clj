;
; spans.clj
; An exploration of spans of time in a calendar and relating them to
; a model period or epoch where values are specified. Use the model
; epoch as a source for data to populate other epochs through 
; a map of events to allow for changing event dates and durations.
;
;
; notes
; - julian numbers, not dates
; - variable epoch length, "week" length
' - fencepost spans indeces
;
; work log
; 4-15 45 minutes in front of TV
; 4-16 20 minutes en route to Louisville
;

(def epoch-length 100)
(def week-length 7)
(def months (list 31 28 31 30 31 30 31 31 30 31 30 31))

(defn week-of 
"given a month and week, returns a span in day-of-year from the start  to the end of that week"
[week month]
	(println "week-of" month week)
	(def months-part (+ (reduce + 0 (take month months) )))
	(list 
	 	(+ months-part (* (- week 1) week-length))
	 	(+ months-part (* week week-length))))

(defn day-of-year 
"takes a traditional, 1-based, date and returns day of year - leap year notwithstanding"
[month day]
	(+ (reduce + (take (- month 1) months))
		day))

(defn julian
"lame attempt at calculating julian day for demo purposes"
[year month day]
1)

; name --> (start-julian, end-julian, year)
(def events { 
	:new-years  (list (day-of-year 12 31) (day-of-year 1 2) 2012)
	:mlk 		(list (day-of-year 1 13) (day-of-year 1 14) 2012)
	:presidents (list (day-of-year 2 14) (day-of-year 2 15) 2012)
	:spring-break (week-of 3 3)
	:new-years  (list (day-of-year 12 31) (day-of-year 1 2) 2013)
	:mlk 		(list (day-of-year 1 13) (day-of-year 1 14) 2013)
	:presidents (list (day-of-year 2 14) (day-of-year 2 15) 2013)
	:spring-break (week-of 3 3)
	:new-years  (list (day-of-year 12 31) (day-of-year 1 2) 2014)
	:mlk 		(list (day-of-year 1 13) (day-of-year 1 14) 2014)
	:presidents (list (day-of-year 2 14) (day-of-year 2 15) 2014)
	:spring-break (week-of 3 3)
})

; to create a multi-epoch list of daily values,
; you need a way to collect events with the same name across epochs
; and access them by an intra-epoch offset


; julian --> (epoch-number, epoch-offset)
(defn julian-to-epoch-pair
"given a julian day number for a base-epoch start and an event day, returns an ep ch number and offset"
[event-julian epoch-length  epoch-start-julian]
	(assert (> event-julian epoch-start-julian) ; assert? TODO
	(list (% (- event-julian epoch-start-julian) epoch-length)
			(/ (- event-julian epoch-start-julian) epoch-length )))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; money ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn mapping-span
"creates a set of spans in the original epoch for an event instance in a future epoch"
[orig-epoch  epoch-length event-epoch event-offset ]
)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 --> (name, (epoch-number, epoch-offset))
(defn make-event-map
"maps events between epochs of the given length in preparation for epoch generation.
the events may have different offsets for each epoch"
[epoch-start epoch-length events]
	(let [event-names (reduce assoc #{} (keys events)) ; keys? TODO
			]
		(map (fn [event-name] 
			; get the set of epoch/offset pairs for each holiday with this name and associate
			; for each event after the original epoch calculate the spans back onto the original epoch
				)
			event-nams)

;;;;;;; epoch ;;;;;;;;;;;;;;;

(defn  make-epoch
" creates a hash of date-to-date for the given length and start
[start length val]
	(loop [event-hash {} count 0]
		(cond (< count length)
			(recur (assoc event-hash (+ count + start) val ) (+ count 1))
			:t event-hash)))

(defn add-to-epoch
"modifies (in the immutable sense) an existing events map by inserting values for the given event"
[evens span value]
	(loop [start (first span) loop-events events ]
		(cond (= start (second span)) loop-events
			:t (recur  (+ start 1) (assoc loop-events start value)))))

(defn generate-epochs
"takes a events map populated with a first epoch and " 
[])

(def epoch (sort (add-to-epoch (make-epoch 100) (week-of 2 1)  10)))
(println epoch)	

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


	
