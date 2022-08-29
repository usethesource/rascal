# Value

.Synopsis
Question about the value of a Rascal expression or program.

.Syntax

*  `QValue _OptName_: _TypeDescriptor_`
*  `QValue _OptName_: _QSteps_ _Test_ _Listing_`

.Types

.Function

.Details

.Description
A value question presents a Rascal expression and poses a question about its value.

_OptName_ is an optional name of the question (enclosed between `[` and `]`).
If _OptName_ is missing, the question gets a unique number as name.

The desired type of the expression is given by a ((TypeDescriptor)).

The first form presents the value generated for the _TypeDescriptor_ and asks about its value.

The second form allows more preparatory steps and also allows adding a listing to the question.
The following steps are defined:

*  `prep: _Cmd_`: execute _Cmd_ as preparatory step. Mostly used to import necessary libraries.
*  `make: _Var_ = _TypeDescriptor_`: create a new variable and use _TypeDescriptor_ to generate its value.
*  `expr: _Var_ = _Exp_`: evaluate the Rascal expression _Exp_ and assign its value to the new variable _Var_.
*  `list: _Lines_`: lines that will be displayed as a listing. The listing may contain a placeholder in the form of `<?>` and
  ends where a new step begins.
*  `test: _Exp_~1~ == _Exp_~2~`: the equality is evaluated as Rascal expression. The outcome determines the success or failure to answer this question.
*  `hint: _Text_`: a hint that will be shown when the user enters a wrong answer.

The following restrictions apply:

*  Each step starts at the beginning of a new line.
*  Every _Exp_ or _Cmd_ or listing may contain one or more variable references of the form `<` _Var_ `>`.
*  Each variable reference `<` _Var_ `>` is first replaced by the value of _Var_.
   _Var_ should have received a value in a preceeding `make` or `expr` step.
*  The listing, and the expressions in the test may contain at most one placeholder `<?>`.

.Examples
See the effect of the following value questions in the Questions section below.

##  Question 1 

The following question can be paraphrased as: _I give you a union of two sets of integers, what is its value?_
```rascal
QValue: <A:set[int]> + <B:same[A]>
```

##  Question 2 

The following question can be paraphrased as: _What is the size of a given list of integers?_
```rascal
QValue:
prep: import List;
test: size(<A:list[int]>) == <?>
```
Note that the `List` module is imported as a preparatory step.

##  Question 3 

The following question can be paraphrased as: 
_I give you a union of integers or strings and an unknown set and the result of the union; what is the value of the unknown set?_
```rascal
QValue:
make: A = set[arb[int,str]]
make: B = same[A]
expr: C = <A> + <B>
hint: <B>
test: <A> + <?> == <C>
```

Observe that we generate values for `A` and `B` and compute the value of `C`.
The value of `B` is the answer we are looking for, and we replace it by `<?>` in the posed test.
When the student gives a wrong answer, we show the value of `B` as hint.

###  Question 4

The following question can be paraphrased as: 
_Fill in the hole in the definition of funcion find to ensure that it returns all strings that contain "o"._
```rascal
QValue:
desc: Return the strings that contain "o".
list:
text = ["andra", "moi", "ennepe", "Mousa", "polutropon"];
public list[str] find(list[str] text){
  return
    for(s <- text)
      if(/o/ := s)
        <?>;
}
test: find(text) == ["moi", "Mousa", "polutropon"];
```

.Benefits

.Pitfalls

