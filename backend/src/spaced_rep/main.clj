(ns spaced-rep.main
  (:require [spaced-rep.app :as app]
            [spaced-rep.io :as io]))

(defn cli-card-review!
  "Returns a bool with the result of the review: true if success
   false if fail."
  [card]
  (println (second card))
  (read-line)
  (println (last card))
  (println "Did you get it right? [y/n]")
  (= "y" (read-line)))

(defn review [reps]
  (if (app/pending-reviews? reps)
    (recur (app/review-cycle reps io/get-card-from-rep! cli-card-review!))
    (do (io/write-repetitions! reps)
        (println "No reps"))))

(defn new-card [id]
  (let [card-atom (atom {})]
    (println "Front:")
    (swap! card-atom assoc :front (read-line))
    (println "Back:")
    (swap! card-atom assoc :back (read-line))
    (println @card-atom)
    (io/write-new-card! id @card-atom)
    (io/write-repetitions! (app/update-repetition [(app/today) id 1] (io/load-reps!)))))

(defn run [& _args]
  (println *command-line-args*)
  (case (last *command-line-args*)
    "new" (new-card (str (java.util.UUID/randomUUID)))
    (review (io/load-reps!))))
