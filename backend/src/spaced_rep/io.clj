(ns spaced-rep.io
  (:require [clojure.string :as str]))

(def card-count (atom 0))

(defn parse-int [s] (Long/parseLong s))

(defn get-card-by-id! [id] (str/split-lines (slurp (str "resources/cards/" id ".card"))))

; reps are date, card-id, box
(defn load-reps! []
  (->> (slurp "resources/repetitions.txt")
       (str/split-lines)
       (map #(vec (str/split % #" ")))
       (map #(update % 2 parse-int))))

(load-reps!)

; returns rep with card on the end
(defn next-card! [reps]
  (conj (last reps) (get-card-by-id! (second (last reps)))))

(defn new-card! [card]
  (spit "resources/cards/test.card" (str/join "\n" (vals card))))

(next-card! (load-reps!))

(defn write-repetitions [reps]
  (spit "resources/repetitions.txt" (str/join "\n" (map #(str/join " " %) reps))))
