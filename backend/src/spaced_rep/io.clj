(ns spaced-rep.io
  (:require [clojure.string :as str]))

(defn- parse-int [s] (Long/parseLong s))

(defn load-card! [id] (str/split-lines (slurp (str "resources/cards/" id ".card"))))

(defn write-card! [id card]
  (spit (str "resources/cards/" id ".card") (str/join "\n" (into [id] (vals card)))))

(defn load-reps! []
  (->> (slurp "resources/repetitions.txt")
       (str/split-lines)
       (map #(vec (str/split % #" ")))
       (map #(update % 2 parse-int))))

(defn write-repetitions! [reps]
  (spit "resources/repetitions.txt" (str/join "\n" (map #(str/join " " %) reps))))

(defn append-repetition! [rep]
  (spit "resources/repetitions.txt" (str/join " " rep) :append true))
