# Backend

## Up Next

* rest-like API interface
* AWS S3
* AWS Lambda
* AWS API GW
* Frontend

## Pages

* [Spaced Rep Alg](./spaced_rep.md)
* [Storage and access patterns](./storage.md)

## 19th Dec: CLI

I played around with some code for a while and seems like all that stuff could work. I'm questioning the need to have cards and repetitions stored separately, but I think fine for now. At least will hide the detail about fetching them I guess.

The first goal is a simple command line app that 

* Gets the next Q
* Lets you 'flip' the card
* Asks you if you got it right or not
* moves the repetition accordingly

Easy enough. Doesn't have the proper spacing/box logic, and especially not the time stuff, which I guess I'll have to put a library on.

Next: new cards, still CLI

* run will CL Arg "New"
* Prompt for front
* Prompt for back
* Add card
* Add repetition

Done. Next, date stuff :(

## 20th December

Date stuff done, actually really easy. Just have to do the IDs. Probably use UUIDs.

Right now it picks the last card off the bottom. Need a concept of 'nothing to review'. DONE

I think that's it for basic functionality. The app API is:

* `pending-reviews?`: returns true if there are reviews for today
* `review-cycle`: given a method for getting cards, and a method for reviewing cards, will update the repetitions for the review outcome
* `initial-repetition`: adds the initial repetition for a card

For IO:

* `write-new-card!`
* `get-card-from-rep!`
* `load-reps!`
* `write-reps!`

The two types are:

* `repetition: date, id, box`
* `card: id, front, back`

The public API (to be written) is:

* `review`: GET: returns a card
* `response`: POST: receives a card-id and bool
* `new-card`: POST: receives a front and back

