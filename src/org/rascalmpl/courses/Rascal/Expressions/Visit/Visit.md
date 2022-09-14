---
title: "Visit"
keywords: "visit,case,default,top-down,top-down-break,bottom-up,bottom-up-break,innermost,outermost"
---

#### Synopsis

Visit the elements in a tree or value.

#### Syntax

```rascal
Strategy visit ( _Exp_ ) {
case _PatternWithAction~1~_;
case _PatternWithAction~2~_;
...
default: ...
}
```

#### Types

#### Function

#### Description

Visiting, recursively traversing, the nodes in a deeply nested data-structure is a very common task in the [EASY]((EASY)) domain. 
In many cases (but certainly not all) this data-structure is a syntax tree of some source code file 
and the nodes correspond to expressions or statements. 

The visit expression/statement allows to focus on the points of interest in the data-structure while automating the search over the other parts for the programmer.

Computing metrics or refactoring are examples of tasks that require a tree visit. 
There are three frequently occurring scenarios:

*  Accumulator: traverse the tree and collect information (fold).

*  Transformer: traverse the tree and transform it into another tree (map).

*  Accumulating Transformer: traverse the tree, collect information and also transform the tree.


The visit expression in Rascal can accommodate all these (and more) use cases.

Given a subject term (the current value of _Exp_) and a list of cases 
(consisting of a sequence of ((Pattern with Action))s, it traverses the term. 
Depending on the precise actions it may perform replacement (mimicking a transformer), 
update local variables (mimicking an accumulator) or a combination of these two (accumulating transformer). 
If *any* of the actions contains an ((Statements-Insert)) statement, 
the value of the visit expression is a new value that is obtained by successive insertions in the subject 
term by executing one or more cases. Otherwise, the original value of the subject term is returned.


The visit expression is optionally preceded by one of the following strategy indications that 
determine the traversal order of the subject:

*  `top-down`: visit the subject from root to leaves.

*  `top-down-break`: visit the subject from root to leaves, but stop at the current path when a case matches.

*  `bottom-up`: visit the subject from leaves to root (this is the default).

*  `bottom-up-break`: visit the subject from leaves to root, but stop at the current path when a case matches.

*  `innermost`: repeat a bottom-up traversal as long as a case matches.

*  `outermost`: repeat a top-down traversal as long as a case matches.


The execution of the cases has the following effect:

*  A PatternWithAction of the form `Pattern => Exp` replaces the current subtree of the subject by the value of _Exp_. 
   Note that a copy of the subject is created at the start of the visit statement and all replacements are made in this copy. 
   As a consequence, modifications made during the visit cannot influence matches later on.
   The modified copy of the subject is ultimately returned by the visit expression.

*  A PatternWithAction of the form `Pattern : Statement` executes `Statement` and this should lead to one of the following:

   ** Execution of an Insert statement of the form `insert Exp~2~`.
      The value of _Exp_~2~ replaces the subtree of the subject that is currently being visited. 
      Once again, this modification takes place in a copy of the original subject (see above).
      Note that:

      *** An insert statement may only occur in a PatternWithAction in a visit expression or a rule.

      *** `Pattern => Exp` is equivalent to `Pattern : insert Exp;`.

   ** Execution of a ((Fail)) statement: the next case is tried.

   ** Execution of a ((Return)) statement that returns a value from the enclosing function.

The precise behaviour of the visit expression depends on the type of the subject:

*  For type node or ADT, all nodes of the tree are visited (in the order determined by the strategy). 
   Concrete patterns and abstract patterns directly match tree nodes. 
   Regular expression patterns match only values of type string.

*  For other structured types (list, set, map, tuple, rel), the elements of the structured type are visited and 
   matched against the cases. 
   When inserts are made, a new structured value is created. In these cases a strategy does not have any effect.

#### Examples

Visit a value and increment a counter for pattern `leaf(int N)` matches:
```rascal
visit(t) {
     case leaf(int N): c = c + N;   
   };
```
Replace all values that match the pattern `red(l, r)`:
```rascal
visit(t) {
     case red(l, r) => green(l, r)   
   };
```
Do a bottom-up visit of an expression and apply the function `simp` to each subexpression:
```rascal
bottom-up visit(e){
           case Exp e1 => simp(e1)
         }
```

More examples can, for instance, be found in Recipes, see [ColoredTrees]((Recipes:Common-ColoredTrees)), 
[WordReplacement]((Recipes:Common-WordReplacement)), [CountConstructors]((Recipes:Common-CountConstructors)), 
and [Derivative]((Recipes:Common-Derivative)).

#### Benefits

#### Pitfalls

