---
title: QuestionMarkup
---

.Synopsis
Mark up for interactive questions.

.Syntax

.Types

.Function

.Description

WARNING: The specification of questions is being redesigned; The information provided here is outdated.

The following types of questions are supported:

*  _Text_: a text question with a free format answer.
*  _Choice_: a multiple choice question.
*  _Type_: question about the _type_ of a Rascal expression.
*  _Value_: question about the _value_ of a Rascal expression.


((Text)) gives the question text and lists all possible good answers.

((Choice)) is a straightforward listing of good and bad answers.

((Type)) and ((Value)) questions are based on a template that consists of an optional _listing_ and an _equality_:

![]((Question.png))


There should be exactly one _hole_ (indicated by `<?>`) in this template that is to be filled in by the student; it may occur in the listing
or in one of the sides of the equality. The general structure is therefore: _fill in the hole such that the equality holds_.
Given that the listing is optional, this template represents 5 different question styles.

((Type)) and ((Value)) questions use ((TypeDescriptor))s to describe desired values and share certain common steps (_QSteps_):

* `prep: RascalCommand` describes preparatory steps needed to execute the question. Typically, required
  imports can be listed here.
* `make: Var = TypeDescriptor`: makes a new value generated according to _TypeDescriptor_ and assigns it to a new variable _Var_.
  _Var_ can be used in later steps in the same question.
* `expr: Var = Expr`: introduces a new variable _Var_ and assigns to it the result of evaluating _Expr_. 
   _Expr_ may contain references to previously introduced variables using `<`Var`>`.
* `type: TypeDescriptor`
* `hint: Text`: a hint to be given to the student in response to a wrong answer. _Text_ may contain references to previously introduced variables.
* `test: Expr~1~ == Expr~2~`: the equality that should hold. The expressions may contain references to variables. One side may contain a hole (`<?>`).
* `list: Text`: a listing that runs until the next question or the end of the concept. It may contain a hole.

.Examples

*  `prep: import List;` imports the List module before executing the following steps.
*  `make: A = set[arb[int,str]]` introduces `A` and assigns it a value of the indicated type.
*  `make: B = same[A]` introduces `B` and assigns it a value of the same type as `A`.
*  `expr: C = <A> + <B>`: inserts the values of `A` and `B`, performs the addition, and assigns the result to `C`.
*  `type: set[int]`: the required type is `set[int]`.
*  `hint: One or more integers separated by comma's`.
*  `test: <A> + <?> == <C>`: the student has to replace `<?>` by an answwer that makes the equality true.

.Benefits

.Pitfalls

