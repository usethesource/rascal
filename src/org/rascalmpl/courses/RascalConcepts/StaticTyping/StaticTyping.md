# Static Typing

.Synopsis
Static type checking.

.Syntax

.Types

.Function

.Details

.Description

![Type Lattice]((type-lattice.png))

Rascal has a static and a dynamic type system, which interact with eachother. The static type system is used by a type checker (not yet released) to predict errors and give warnings where possibly slipups have been made. The dynamic type system ensures well-formedness of data structures and plays an important role while pattern matching, since many algorithms dispatch on the types of values.

Rascal's static type system does not ensure that all functions will go right:
   * functions may throw exceptions.
   * functions may not be defined for the specific pattern which occur on the call site.

However, the static type system will produce an error when a function will certainly throw an exception, or when it is certainly not defined for a certain case. Also it catches some logical tautologies and contradictions which would lead to dead code.

The Rascal types are ordered in a so-called _type lattice_ shown in the figure above.

The arrows describe a _subtype-of_ relation between types. The type `void` is the _smallest_ type and 
is included in all other types and the type `value` is the _largest_ type that includes all other types. 
We also see that `rel` is a subtype of `set` and that each ADT is a subtype of `node`. 
A special role is played by the datatype `Tree` that is the generic type of syntax trees. 
Syntax trees for specific languages are all subtypes of `Tree`. As a result, syntax trees can be addressed at two levels: 

*  in a generic fashion as `Tree` and,
*  in a specific fashion as a more precisely typed syntax tree. 
Finally, each `alias` is structurally equivalent to one or more specific other types.

Rascal does not provide an explicit casting mechanism (as in Java), but pattern matching can play that role.
 
The language provides higher-order, parametric polymorphism. 
A type aliasing mechanism allows documenting specific uses of a type. 
Built-in operators are heavily overloaded. 
For instance, the operator `+` is used for addition on integers and reals but also for list concatenation, 
set union and the like.

.Examples

Some example can illustrate the above.
```rascal-shell,error
int I = 3;
```
Since I is declared as type `int`, we cannot assign a `real` value to it:
```rascal-shell,continue,error
I = 3.5;
```

```rascal-shell
num N = 3;
```
Since N is declared as type `num`, we can assign both `int` and `real` values to it:
```rascal-shell,continue
N = 3.5;
```

Since all types are a subtype of type `value`, one can assign values of any type to a variable declared as `value`:
```rascal-shell
value V = 3;
V = "abc";
V = false;
```
We can use pattern matching to classify the actual type of a value:
```rascal-shell,continue
str classify(value V){
  switch(V){
    case str S: return "A string";
    case bool B: return "A Boolean";
    default: return "Another type"; 
  }
}
classify(V);
V = 3.5;
classify(V);
```

In addition to these standard examples, it is interesting that all [Algebraic Data Types]((Rascal:Declarations-AlgebraicDataType)) are subtypes of type `node`.
Let's introduce a simple `Color` data type:
```rascal-shell
data Color = red(int level) | blue(int level);
```
Unsurprisingly, we have:
```rascal-shell,continue
Color C = red(3);
```
Due to subtyping, we can also have:
```rascal-shell,continue
node ND = red(3);
```

One example of the actual application of subtypes can be found in 
[Count Constructors]((Recipes:Common-CountConstructors)).

.Benefits

.Pitfalls

