# Solve

.Synopsis
Solve a set of equalities by fixed-point iteration.

.Index
solve

.Syntax
`solve(_Var_~1~, _Var_~2~, ..., _Var_~n~; _Exp_) _Statement_;`

.Types

.Function

.Details

.Description
Rascal provides a solve statement for performing arbitrary fixed-point computations. This means, repeating a certain computation as long as it causes changes. This can, for instance, be used for the solution of sets of simultaneous 
linear equations but has much wider applicability.

The solve statement consists of the variables for which a fixed point will be computed and a statement. 
Optionally, an expression _Exp_ directly following the list of variables gives an upper bound on the number of iterations.

Statement can use and modify the listed variables _Var_~i~. 
The statement is executed, assigning new values to the variables _Var_~i~, and this is repeated as long as the value 
of any of the variables was changed compared to the previous repetition. 
Note that this computation will only terminate if the variables range over a so-called _bounded monotonic lattice_,
in which values can only become larger until a fixed upper bound or become smaller until a fixed lower bound.

.Examples
Let's consider transitive closure as an example (transitive closure is already available as built-in operator, 
we use it here just as a simple illustration). Transitive closure of a relation is usually defined as:
[source,rascal]
----
R+ = R + (R o R) + (R o R o R) + ...
----
In other words, it is the union of successive <<Relation-Composition>>s of `R` with itself. 
For a given relation `R` this can be expressed as follows:
[source,rascal-shell]
----
rel[int,int] R = {<1,2>, <2,3>, <3,4>};
T = R;
solve (T) {
          T = T + (T o R);
        }
----

.Benefits

.Pitfalls

