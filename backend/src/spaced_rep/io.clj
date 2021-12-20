(ns spaced-rep.io
  (:require [clojure.string :as str]))

(defn- parse-int [s] (Long/parseLong s))

(defn get-card-from-rep! [[_ id _]] (str/split-lines (slurp (str "resources/cards/" id ".card"))))

(defn load-reps! []
  (->> (slurp "resources/repetitions.txt")
       (str/split-lines)
       (map #(vec (str/split % #" ")))
       (map #(update % 2 parse-int))))

(defn write-new-card! [id card]
  (spit (str "resources/cards/" id ".card") (str/join "\n" (into [id] (vals card)))))

(defn write-repetitions! [reps]
  (spit "resources/repetitions.txt" (str/join "\n" (map #(str/join " " %) reps))))
