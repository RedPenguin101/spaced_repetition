(ns spaced-rep.main
  (:require [clojure.string :as str]))

(defn parse-int [s] (Long/parseLong s))

(defn add-month [old-date]
  (let [[year month day] (map parse-int (str/split old-date #"-"))]
    (str/join "-" (if (= month 12)
                    [(inc year) 1 day]
                    [year (inc month) day]))))

(defn next-date [old-date box]
  (let [[year month day] (map parse-int (str/split old-date #"-"))]
    (case box
      4 (str/join "-" [year (inc month) day]))))

(defn card-by-id! [id] (str/split-lines (slurp (str "resources/cards/" id ".card"))))

; reps are date, card-id, box
(defn load-reps! []
  (->> (slurp "resources/repetitions.txt")
       (str/split-lines )
       (map #(vec (str/split % #" ")))
       (map #(update % 2 parse-int))))

(load-reps!)

; returns rep with card on the end
(defn next-card! [reps]
  (conj (last reps) (card-by-id! (second (last reps)))))

(next-card! (load-reps!))

(defn new-repetition [[_date card-id box] correct?]
  (if correct?
    ["new-date" (min 4 (inc box)) card-id]
    ["old-date" (max 1 (dec box)) card-id]))

(defn update-repetitions [new-rep all-reps]
  (if (= (last new-rep) (second (last all-reps)))
    (sort < (conj (butlast all-reps) new-rep))
    (throw (ex-info "can't update reps, card-id doesn't match" {}))))

(defn write-repetitions [reps]
  (spit "resources/repetitions.txt" (str/join "\n" (map #(str/join " " %) reps))))

(let [reps (load-reps!)
      card (next-card! reps)]
   (write-repetitions (update-repetitions (new-repetition card true) reps)))

