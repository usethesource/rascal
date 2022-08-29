# Assert

.Synopsis
An executable assertion.

.Index
assert

.Syntax

*  `assert _Exp~1~_`
*  `assert _Exp~1~_ : _Exp~2~_`

.Types


|====
| `_Exp~1~_` | `_Exp~2~_` 

| `bool`    | `str`     
|====

.Function

.Details

.Description
An assert statement may occur everywhere where a declaration is allowed. It has two forms:

An assert statement consists of a Boolean expression _Exp_~1~ and an optional string expression _Exp_~2~
that serves as a identifying message for this assertion. 

When _Exp_~1~ evaluates to `false`, an `AssertionFailed` exception is thrown.

.Examples
```rascal-shell,error
assert 1==2 : "is never true";
int div(int x, int y) {
  assert y != 0 : "y must be non-zero";
  return x / y;
}
div(4,0);
```

.Benefits

.Pitfalls

