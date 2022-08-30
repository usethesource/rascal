# Assignment

.Synopsis
Assign a value to a variable or more complex data structure.

.Index
= += -= *= /= ?= [ .. ] . ? @

.Syntax
`_Assignable_ _AssignmentOp_ _Exp_`

where _AssignmentOp_ may be one of `=`, `+=`, `-=`, `*=`, `/=`, or `?=`.

An _Assignable_ is one of the following:

*   `_Var_`
*   `_Assignable_ [ _Exp_ ]`
*   `_Assignable_ [ _Exp_ .. _Exp_ ]`
*   `_Assignable_ [ _Exp_, _Exp_ .. _Exp_ ]`
*   `_Assignable_ . _Name_` 
*   `< _Assignable_, _Assignable_, ..., _Assignable_ >`
*   `_Assignable_ ? _Exp_` 
*   `_Assignable_ @ _Name_`
*   `_Name_ ( _Assignable_, _Assignable_, ... )`

.Types

.Function

.Details

.Description
The purpose of an assignment is to assign a new value to a simple variable or to an element of a more complex data structure. 

The standard assignment operator is `=`. 
The other assignment operators can be expressed as abbreviations for the standard assignment operator.

|                                 |                                         |
| --- | --- |
| Assignment Operator             | Equivalent to                           |
| `_Assignable_ += _Exp_`         | `_Assignable_ = _Assignable_ + _Exp_`   |
| `_Assignable_ -= _Exp_`         | `_Assignable_ = _Assignable_ - _Exp_`   |
| `_Assignable_ *= _Exp_`         | `_Assignable_ = _Assignable_ * _Exp_`   |
| `_Assignable_ /= _Exp_`         | `_Assignable_ = _Assignable_ / _Exp_`   |
| `_Assignable_ &= _Exp_`         | `_Assignable_ = _Assignable_ & _Exp_`   |
| `_Assignable_ ?= _Exp_`         | `_Assignable_ = _Assignable_ ? _Exp_`   |




An assignable is either a single variable, (the base variable), optionally followed by subscriptions, slices or field selections.
The assignment statement always results in assigning a completely new value to the base variable. 
We distinguish the following forms of assignment:
(((TOC)))

.Examples


.Benefits

.Pitfalls

