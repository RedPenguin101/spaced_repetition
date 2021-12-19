(ns spaced-rep.app
  (:require [clojure.string :as str]))

(defn parse-int [s] (Long/parseLong s))

(defn next-day [old-date]
  (let [[year month day] (map parse-int (str/split old-date #"-"))]
    (cond (= day 30) (str/join "-" [year (inc month) 1])
          (and (= day 30)  (= month 12)) (str/join "-" [(inc year) 1 1])
          :else (str/join "-" [year month (inc day)]))))

(next-day "2021-12-19")

(defn new-repetition [[date card-id box] correct?]
  (if correct?
    [(next-day date) (min 4 (inc box)) card-id]
    [date (max 1 (dec box)) card-id]))

(defn update-repetitions [new-rep all-reps]
  (if (= (last new-rep) (second (last all-reps)))
    (sort < (conj (butlast all-reps) new-rep))
    (throw (ex-info "can't update reps, card-id doesn't match" {}))))