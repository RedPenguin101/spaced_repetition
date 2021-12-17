# Storage and access patterns

This is premised on the spaced repetition algorithm described [here](./spaced_rep.md)

My main access patterns will be:

1. Get a card or cards based on its next review date
2. Update a specific card (or repetition), given a correct/incorrect answer
3. Create a new card

I think the easiest way to do this will be like this:

* Store cards by ID. A Card would be something like `Card = id & front & back`. As a start this could just be a text file with line 1 being the id, line 2 being the front and line 3 being the back
* Store repetitions by next date. A simple sorted csv/space separated file of repetitions with `Repetition = review-date & card-id & box`
* Fetching the `next-repetition` would just take the top entry off the repetition storage
* `update-repetition` would add back the new repetition in the appropriate place in the file.
* `create-card` would create a new card, and add a repetition entry to the _top_ of the repetition file

The downside with this is that `update repetition` will have to load and save the whole repetitions file. Since it's effectively indexed by date, I don't see that as too much of a problem.

Another issue is that, if `next-repetition` removes the entry, there will be no repetition for that card until a response is received. So if there _is_ no response for whatever reason, their will be no repetition for that card, which should be an illegal state. However keeping the repetition in the file after it's fetched - is that OK? I think it is. The `update repetition` would in effect have to delete the old repetition as well, but since by definition it will (barring error) be at the top of the file, that should be pretty easy

Thinking about it, maybe repetitions would be better structured as a sort of stack, and the file sorted by greatest date first. So `next` would take of the _bottom_ of the file, `new` would add to the _bottom_ of the file. Update would still insert at the appropriate place.

So the file formats are like this:

_cards/1.card_
```
1
Who is the president of the United States?
Joe Biden
```

_repetitions.txt_
```
2021-12-18 1 1
```

