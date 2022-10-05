---
title: Why Rascal
sidebar_position: 9
details:
  - Motivation
  - EASY
  - SolutionStrategies
  - CompareWithOtherParadigms
keywords:
  - why
  - "why another programming language"
  - database
  - metaprogramming
  - meta-programming
  - wysiwyg
  - provenance
---

#### Synopsis

What is the motivation for Rascal and which problems can you solve with it?

#### Description

We give various answers to these questions:

* In ((Motivation)) we summarize the distinguishing features of Rascal 
  and describe some application domains.
* ((WhyRascal-EASY)) can be used in many Rascal applications.
* We have also collected ((Solution Strategies)) for a wide range of problems.
* Last but not least we give a quick [comparison with other paradigms]((Compare With Other Paradigms)).

#### Benefits

* *No more databases/programming language separation*: Rascal integrates all data-types and operators that you need for meta-programming; files/strings, trees/hierarchies, tables/graphs/relations, and its easy to move between representations. Rascal has more powerful query operators than SQL and more powerfull pattern matching than Haskell.
* *Full data and code provenance*: Rascal has source location origins of every thing you analyze and transform made explicit via URI locations.
* *WYSIWYG*: The What You See Is What You Get paradigm means that all data in memory has a unique printed, readable, form that explains _everything_ about it.  
* *Fun and easy*: Rascal is a fun language to make meta programs in, and that most programmers learn it easily.
* *Safe*: Even beginner programmers like Rascal as a first language, and complex programs stay safe because of data immutability and a helpful type system;
* *Down to earth*: Simple things are kept simple in Rascal, while complex problems can also be solved.

#### Pitfalls

* *Advanced constructs*: Rascal integrates some advanced programming tools, like pattern matching, lexically scoped backtracking and generic traversal that may require some learning to get the hang of. Idiomatic use of the language can save up to a factor 20 lines of code, while the uninitiated may spend many lines writing unnecessary for-loops and if-then-elses.
* *False friends*: Rascal has some [false friends](https://en.wikipedia.org/wiki/False_friend) with other programming languages. This means that it behaves just a little different than expected if you are completely used to that other language. In particular "immutable data" gives a different meaning to the assignment operator. Most of Rascal is totally unsurprising after that.

