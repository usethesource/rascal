# Typed and Labelled Pattern

.Synopsis
Typed, labelled, abstract pattern.

.Index
:

.Syntax

.Types

.Function

.Details

.Description

A typed, labelled, pattern matches when the subject value has type _Type_ and _Pat_ matches. 
The matched value is assigned to _Var_.

This construct is used for:

*  binding the _whole pattern_ to a variable while also matching some stuff out of it: `MyType t : someComplexPattern(f(int a), int b))`. 
   This is similar to ((Labelled Pattern))s but with an extra type
*  to assert that the pattern has a certain type. This can be useful in disambiguating a constructor name, as in the example below.

.Examples

```rascal-shell
import IO;
data Lang = add(Lang l, Lang r) | number(int i);
data Exp = id(str n) | add(Exp l, Exp r) | subtract(Exp l, Exp r) | otherLang(Lang a);
ex = add(id("x"), add(id("y"), otherLang(add(number(1),number(2)))));
visit (ex) {
  case Lang l:add(_,_) : println("I found a Lang <l>");
  case Exp e:add(_,_)  : println("And I found an Exp <e>");
}
```

.Benefits

.Pitfalls

