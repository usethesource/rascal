# Type Constrained Pattern

.Synopsis

Type constrained abstract pattern.

.Index
[ ]

.Syntax

.Types

.Function

.Details

.Description

A type constrained pattern matches provided that the subject has type _Type_ and _Pat_ matches. This can be handy in case of ambiguity (say more than one constructor with the same name), or in case the pattern is completely general. See an example below:

Warning: This does not seem to work properly. There is a bug.

.Examples

```rascal-shell
import IO;
```
Some example data type which contains generic values as well as specific expressions:
```rascal-shell,continue
data Exp = val(value v) | add(Exp l, Exp r) | sub(Exp l, Exp r);
ex = add(add(val("hello"(1,2)),val("bye")), sub(val(1),val(2)));
```
Here we constrain the match to find only Exps:
```rascal-shell,continue
visit (ex) {
  case [Exp] str name(_,_) : println("node name is <name>");
}
```
Here we do not constrain the same pattern:
```rascal-shell,continue
visit (ex) {
  case str name(_,_) : println("node name is <name>");
}
```

.Benefits

.Pitfalls

