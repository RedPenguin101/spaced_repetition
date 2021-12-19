# Backend todo

* ~~Algorithm for spaced repetition~~ DONE
* ~~Card storage format/spec~~ DONE
* ~~Next card storage format/spec~~ DONE
* Fetch and return card
* Fetch and return next card
* Fetch and return next card with spaced rep options
* Process response
* Create card method

## Pages

* [Spaced Rep Alg](./spaced_rep.md)
* [Storage and access patterns](./storage.md)

## 19th Dec

I played around with some code for a while and seems like all that stuff could work. I'm questioning the need to have cards and repetitions stored separately, but I think fine for now. At least will hide the detail about fetching them I guess.

The first goal is a simple command line app that 

* Gets the next Q
* Lets you 'flip' the card
* Asks you if you got it right or not
* moves the repetition accordingly

Easy enough. Doesn't have the proper spacing/box logic, and especially not the time stuff, which I guess I'll have to put a library on.


