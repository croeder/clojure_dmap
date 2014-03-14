
(defn arities [v]
  (->> v 
    meta 
    :arglists 
    (map #(remove #{'&} %))
    (map count)))


