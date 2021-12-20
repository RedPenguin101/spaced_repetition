(ns spacerep.main
  (:gen-class)
  (:require [spacerep.app :as app]
            [spacerep.io :as io]))

(defn cli-card-review!
  "Returns a bool with the result of the review: true if success
   false if fail."
  [card]
  (println (second card))
  (read-line)
  (println (last card))
  (println "Did you get it right? [y/n]")
  (= "y" (read-line)))

(defn cli-review []
  (let [reps (io/load-reps!)
        id  (app/next-review-id reps)]
     (if id (io/write-repetitions! (app/process-review reps id (cli-card-review! (io/load-card! id))))
       (println "No cards due for review"))))

(defn new-card [id]
  (let [card-atom (atom {})]
    (println "Front:")
    (swap! card-atom assoc :front (read-line))
    (println "Back:")
    (swap! card-atom assoc :back (read-line))
    (println @card-atom)
    (io/write-card! id @card-atom)
    (io/append-repetition! (app/initial-repetition id))))

(defn run [& _args]
  (case (last *command-line-args*)
    "new" (new-card (str (java.util.UUID/randomUUID)))
    (cli-review)))

(defn -main []
  (run nil))

