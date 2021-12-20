(ns spaced-rep.app
  (:require [java-time :as jt]))

(defn today [] (jt/format "yyyy-MM-dd" (jt/local-date)))

;; next date:
;; box    time
;;  1      +1
;;  2      +2
;;  3      +5
;;  4     +30

(defn next-day [old-date new-box]
  (jt/format "yyyy-MM-dd" (jt/plus (jt/local-date "yyyy-MM-dd" old-date)
                                   (jt/days (case new-box 1 1, 2 2, 3 5, 4 30)))))

;; A repetition has format [date card-id box]

(defn process-response
  "Given a repetition and a (boolean) response, will return a new repetition,
  with the date and box updated according to the programs rules."
  [[date card-id box] correct?]
  (let [new-box (if correct? (min 4 (inc box)) (max 1 (dec box)))]
    [(next-day date new-box) card-id new-box]))

(defn next-rep [reps] (last reps))

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
