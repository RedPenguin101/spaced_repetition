(ns spaced-rep.app
  (:require [clojure.string :as str]))

(defn parse-int [s] (Long/parseLong s))

(defn next-day [old-date]
  (let [[year month day] (map parse-int (str/split old-date #"-"))]
    (cond (= day 30) (str/join "-" [year (inc month) 1])
          (and (= day 30)  (= month 12)) (str/join "-" [(inc year) 1 1])
          :else (str/join "-" [year month (inc day)]))))

(next-day "2021-12-19")

;; A repetition has format [date card-id box]

(defn process-response
  "Given a repetition and a (boolean) response, will return a new repetition,
   with the date and box updated according to the programs rules."
  [[date card-id box] correct?]
  (if correct?
    [(next-day date) (min 4 (inc box)) card-id]
    [date (max 1 (dec box)) card-id]))

(defn next-rep [reps] (last reps))

(defn update-repetition
  "Given a list of existing repetitions and a new repetition, 
   will return a list of repetitions. Each card-id can only 
   exist in the list once, so if there was an existing repetition
   with the card id of the new-rep, it will be removed."
  [new-rep all-reps]
  (let [filtered-reps (remove #(= (second new-rep) (second %)) all-reps)]
    (reverse (sort-by first (conj filtered-reps new-rep)))))

(defn review-cycle
  "Picks the next card for review, presents it (by calling the card-fn),
   reviews if (by calling the review-fn, which should return true if the 
   review was successful).
   Returns a new version of the reps."
  [reps card-fn review-fn]
  (update-repetition (process-response (next-rep reps) (review-fn (card-fn (last reps)))) reps))
