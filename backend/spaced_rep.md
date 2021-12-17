# The spaced repetition algorithm

I'm going to use a modified [Leitner system](https://e-student.org/leitner-system/)

## The basic system

There are 3 boxes. As you start reviewing the cards, the order of the cards in the box is changed.

Box 1 (the "every day box") contains new cards, and cards from box 2 that were answered incorrectly. Every card in box 1 should be answered every day. Cards from box 1 that are answered incorrectly stay in box 1, cards that are answered correctly go to box 2.

Box 2 contains cards from box 1 that were answered correctly, and cards from box 3 that were answered incorrectly. Box 2 is reviewed every other day.

Box 3 contains cards from box 2 that were answered correctly, and cards from box 3 that were answered correctly. Cards in box 3 are reviewed once per week.

## The modified system

The system has the benefit of simplicity, and extensibility. Adding a box 4 with a repetition of one month is trivial.

It means you have minimal history storage required, and no 'curve calculation' nonsense.

The biggest downside I can thing of is that it seems to make 'lumps' in review. i.e. you'll have a big lump of cards to review when the month comes up.

I think a mitigation of this would be card base system which tags cards with boxes, like this:

```
Repetition = card-id, next-review-date, box
Review :: Repetition, correct? -> Review
NewDate :: current-date, box, correct? -> new-date
```

The `NewDate` function would calculate the new date based on the current box. The `Review` function would calculate the new review date, and the new box (incremented or decremented) based on the response. Something like:

```
review(old-rep, correct?):
  new-rep = new Repetition
  new-rep.card-id = old-rep.card-id
  new-rep.box = old-rep.box + (if correct? 1 else -1) 
  new-rep.date = new-date(old-rep.date, old-rep.box, correct?)
  return new-rep
```

One thing that Anki had which I think is worth emulating is that _any_ card that you get wrong goes back into box 1, regardless of its current box. So the function would be like `new-rep.box = if correct? old-rep + 1 else 1`

The other change I'm going to make is to have a 4th box, with a period of a month (the maximum).

## Enhancements post MVP

One feature of Anki is that you can have multiple responses to a card. Fail, Pass, Easy pass. This could be implemented here by 'jumping' a box on an easy pass. But not going to bother with this initially.
