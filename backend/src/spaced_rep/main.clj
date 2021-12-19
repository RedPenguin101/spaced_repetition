(ns spaced-rep.main
  (:require [spaced-rep.app :as app]
            [spaced-rep.io :as io]))

(comment
  (let [reps (io/load-reps!)
        card (io/next-card! reps)]
    (app/update-repetitions (app/new-repetition card false) reps))

  (let [reps (io/load-reps!)
        card (io/next-card! reps)]
    (app/update-repetitions (app/new-repetition card true) reps)))

(defn review []
  (let [reps (io/load-reps!)
        card (io/next-card! reps)]
    (println (second (last card)))
    (read-line)
    (println (last (last card)))
    (println "Did you get it right? [y/n]")
    (if (= "y" (read-line))
      (io/write-repetitions (app/update-repetitions (app/new-repetition card true) reps))
      (io/write-repetitions (app/update-repetitions (app/new-repetition card false) reps)))))

(defn new-card []
  (let [card-atom (atom {})]
    (println "Front:")
    (swap! card-atom assoc :front (read-line))
    (println "Back:")
    (swap! card-atom assoc :back (read-line))
    (println @card-atom)
    (io/new-card! @card-atom)))

(defn run [& _args]
  (println *command-line-args*)
  (case (last *command-line-args*)
    "new" (new-card)
    (review)))
