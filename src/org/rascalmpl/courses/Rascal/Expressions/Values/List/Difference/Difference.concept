# List Difference

.Synopsis
The difference between two lists.

.Index
-

.Syntax
`_Exp_~1~ - _Exp_~2~`

.Types

//

|====
| `_Exp~1~_`     |  `_Exp~2~_`     | `_Exp~1~_ - _Exp~2~_`       

| `list[_T~1~_]` |  `list[_T~2~_]` | `list[lub(_T~1~_,_T~2~_)]`  
| `list[_T~1~_]` |  `_T~2~_`       | `list[lub(_T~1~_,_T~2~_)]`  
|====


.Function

.Details

.Description
If both _Exp_~1~ and _Exp_~2~ have a list as value, the result is the difference of these two list values. 
If _Exp_~2~ does not have a list as value, it is first converted to a list before the difference is computed.
The difference is computed by taking the successive elements of the second list and
removing the first occurrence of that element in the first list. 

.Examples
[source,rascal-shell]
----
[1, 2, 3, 4] - [1, 2, 3];
[1, 2, 3, 4] - [3];
[1, 2, 3, 4] - 3;
[1, 2, 3, 4] - [5, 6, 7];
[1, 2, 3, 1, 2, 3] - [1];
[1, 2, 3, 1, 2, 3] - [1, 2];
----

.Benefits

.Pitfalls

