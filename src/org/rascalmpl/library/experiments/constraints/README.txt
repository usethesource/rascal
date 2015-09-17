This is an experiment in using type constraints for type checking.

The basic idea is to use the typed lambda calculus with subtyping (LambdaSub) as common example
and to write type checkers using different styles:
- ClassicTC: a classical type inference algorithm with explicit unification.
- ConstraintTC: generates a set of constraints that can ultimately be shipped to Z3.
- Microkanren: an implementation of microkanren in Rascal.
- Explorations how microkanren and Rascal's backtracking relate to each other, they could include:
  - an implementation of microkanren in muRascal (which already provides corotuines that can be used to model streams)
  - ideas howto integrate this in Rascal.
