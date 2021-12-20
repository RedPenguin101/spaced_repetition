(ns spacerep.io
  (:require [clojure.string :as str]
            [clojure.java.io :refer [resource]]))

(defn- parse-int [s] (Long/parseLong s))

(defn load-card! [id] (str/split-lines (slurp (resource (str "cards/" id ".card")))))

(defn write-card! [id card]
  (spit (resource (str "resources/cards/" id ".card")) (str/join "\n" (into [id] (vals card)))))

(defn load-reps! []
  (->> (resource "repetitions.txt")
       (slurp)
       (str/split-lines)
       (remove empty?)
       (map #(vec (str/split % #" ")))
       (map #(update % 2 parse-int))))

(defn write-repetitions! [reps]
  (spit (resource "repetitions.txt") (str/join "\n" (map #(str/join " " %) reps))))

(defn append-repetition! [rep]
  (spit (resource "repetitions.txt") (str "\n" (str/join " " rep)) :append true))
