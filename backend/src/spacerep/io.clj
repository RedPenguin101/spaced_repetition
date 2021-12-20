(ns spacerep.io
  (:require [clojure.string :as str]
            [clojure.java.io :refer [writer reader]]))

(def file-path "./files/")

(defn- parse-int [s] (Long/parseLong s))

(defn file-load [fname]
  (with-open [r (reader (str file-path fname))]
    (slurp r)))

(defn load-card! [id]
  (str/split-lines (file-load (str "cards/" id ".card" ))))

(defn write-card! [id card]
  (with-open [w (writer (str file-path "cards/" id ".card"))]
    (.write w (str/join "\n" (into [id] (vals card))))))

(defn load-reps! []
  (->> (file-load "repetitions.txt")
       (str/split-lines)
       (remove empty?)
       (map #(vec (str/split % #" ")))
       (map #(update % 2 parse-int))))

(defn write-repetitions! [reps]
  (with-open [w (writer (str file-path "repetitions.txt"))]
    (.write w (str/join "\n" (map #(str/join " " %) reps)))))

(defn append-repetition! [rep]
  (with-open [w (writer (str file-path "repetitions.txt") :append true)]
    (.write w (str "\n" (str/join " " rep)))))

