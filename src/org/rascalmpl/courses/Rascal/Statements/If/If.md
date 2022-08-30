# If

.Synopsis
Conditional statement.

.Index
if else

.Syntax

*  `if ( _Exp_ ) _Statement_;`
*  `if ( _Exp_ ) _Statement~1~_ else _Statement~2~_;`

.Types
| `_Exp_` | `if ( _Exp_ ) _Statement_;`  |
| --- | --- |
| `bool`  |  `void`                      |



| `_Exp_` | `_Statement~1~_` | `_Statement~2~_` | `if ( _Exp_ ) _Statement~1~_ else _Statement~2~_;`  |
| --- | --- | --- | --- |
| `bool`  |  _T~1~_        | _T~2~_         | `lub(_T~1~_, _T~2~_)`                               |


.Function

.Details

.Description
The test _Exp_ is evaluated and its outcome determines the statement to be executed: 
_Statement_~1~ if _Exp_ yields `true` and _Statement_~2~ otherwise. 
The value of an if-then statement is equal to _Statement_ when its test is true. Otherwise it is void.
The value of an if-then-else statement is the value of the statement that was executed.

.Examples
```rascal-shell
if( 3 > 2 ) 30; else 40;
x = if( 3 > 2 ) 30; else 40;
if( 3 > 2 ) 30;
```
An if-then statement yields `void`  when its test is false
(demonstrated by the __ok__ that is printed by the Rascal system):
```rascal-shell,continue
if( 2 > 3 ) 30;
```

.Benefits

.Pitfalls

