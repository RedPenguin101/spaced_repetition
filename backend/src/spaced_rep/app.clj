(ns spaced-rep.app
  (:require [java-time :as jt]))

(defn str<= [a b] (<= (compare a b) 0))
(defn str->date [string] (jt/local-date "yyyy-MM-dd" string))
(defn date->str [date] (jt/format "yyyy-MM-dd" date))
(defn today [] (date->str (jt/local-date)))

;; next date:
;; box    time
;;  1      +1
;;  2      +2
;;  3      +5
;;  4     +30

(defn next-day [old-date new-box]
  (date->str (jt/plus (str->date old-date)
                      (jt/days (case new-box 1 1, 2 2, 3 5, 4 30)))))

;; A repetition has format [date card-id box]

(defn process-response
  "Given a repetition and a (boolean) response, will return a new repetition,
  with the date and box updated according to the programs rules."
  [[date card-id box] correct?]
  (let [new-box (if correct? (min 4 (inc box)) (max 1 (dec box)))]
    [(next-day date new-box) card-id new-box]))

(defn next-rep [reps]
  (when (str<= (first (last reps)) (today))
    (last reps)))

(defn update-repetition
  "Given a list of existing repetitions and a new repetition, 
   will return a list of repetitions. Each card-id can only 
   exist in the list once, so if there was an existing repetition
   with the card id of the new-rep, it will be removed."
  [new-rep all-reps]
  (let [filtered-reps (remove #(= (second new-rep) (second %)) all-reps)]
    (println "filtered-reps " filtered-reps)
    (reverse (sort-by first (conj filtered-reps new-rep)))))

(defn review-cycle
  "Picks the next card for review, presents it (by calling the card-fn),
   reviews if (by calling the review-fn, which should return true if the
   review was successful).
   Returns a new version of the reps."
  [reps card-fn review-fn]
  (update-repetition (process-response (next-rep reps) (review-fn (card-fn (last reps)))) reps))

