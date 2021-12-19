(ns spaced-rep.main
  (:require [clojure.string :as str]))

(defn parse-int [s] (Long/parseLong s))

(defn next-day [old-date]
  (let [[year month day] (map parse-int (str/split old-date #"-"))]
    (cond (= day 30) (str/join "-" [year (inc month) 1])
          (and (= day 30)  (= month 12)) (str/join "-" [(inc year) 1 1])
          :else (str/join "-" [year month (inc day)]))))

(next-day "2021-12-19")

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

(defn new-repetition [[date card-id box] correct?]
  (if correct?
    [(next-day date) (min 4 (inc box)) card-id]
    [date (max 1 (dec box)) card-id]))

(defn update-repetitions [new-rep all-reps]
  (if (= (last new-rep) (second (last all-reps)))
    (sort < (conj (butlast all-reps) new-rep))
    (throw (ex-info "can't update reps, card-id doesn't match" {}))))

(defn write-repetitions [reps]
  (spit "resources/repetitions.txt" (str/join "\n" (map #(str/join " " %) reps))))

(comment
  (let [reps (load-reps!)
        card (next-card! reps)]
     (update-repetitions (new-repetition card false) reps))

  (let [reps (load-reps!)
        card (next-card! reps)]
     (update-repetitions (new-repetition card true) reps)))

(defn run [& _args]
  (let [reps (load-reps!)
        card (next-card! reps)]
    (println (second (last card)))
    (read-line)
    (println (last (last card)))
    (println "Did you get it right? [y/n]")
    (if (= "y" (read-line))
      (write-repetitions (update-repetitions (new-repetition card true) reps))
      (write-repetitions (update-repetitions (new-repetition card false) reps)))))
