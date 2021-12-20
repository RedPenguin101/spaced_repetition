(ns spacerep.app
  (:require [java-time :as jt]))

(defn- str<= [a b] (<= (compare a b) 0))
(defn- str->date [string] (jt/local-date "yyyy-MM-dd" string))
(defn- date->str [date] (jt/format "yyyy-MM-dd" date))
(defn- today [] (date->str (jt/local-date)))

;; next date:
;; box    time
;;  1      +1
;;  2      +2
;;  3      +5
;;  4     +30

(defn- next-day [old-date new-box]
  (let [days (jt/days (case new-box 1 1, 2 2, 3 5, 4 30))]
    (-> old-date
        (str->date)
        (jt/plus days)
        (date->str))))

;; A repetition has format [date card-id box]

(defn- update-rep
  "Given a repetition and a (boolean) response, will return a new repetition,
  with the date and box updated according to the programs rules."
  [[date card-id box] correct?]
  (let [new-box (if correct? (min 4 (inc box)) (max 1 (dec box)))]
    [(next-day date new-box) card-id new-box]))

(defn- update-repetition-list
  "Given a list of existing repetitions and a new repetition, 
   will return a list of repetitions. Each card-id can only 
   exist in the list once, so if there was an existing repetition
   with the card id of the new-rep, it will be removed."
  [new-rep all-reps]
  (let [filtered-reps (remove #(= (second new-rep) (second %)) all-reps)]
    (reverse (sort-by first (conj filtered-reps new-rep)))))

;;; NEW API ;;;

(defn initial-repetition [card-id] [(today) card-id 1])

(defn next-review-id
  "Returns nil if no review is due"
  [reps]
  (when (str<= (first (last reps)) (today))
    (second (last reps))))

(defn process-review [rep-list id correct?]
  (let [old-rep (first (filter #(= (second %) id) rep-list))]
    (update-repetition-list (update-rep old-rep correct?) rep-list)))

