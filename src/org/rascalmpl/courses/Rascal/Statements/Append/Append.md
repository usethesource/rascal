# Append

.Synopsis
Append an element to the list value produced by various loop statements.

.Index
append

.Syntax
`append _Exp_`

.Types

.Function

.Details

.Description
An append statement may only occur in the body of a ((While)), ((Do)) or ((For)) statement. 
It appends the value of _Exp_ to the resulting list value of the loop construct in which it occurs.

.Examples
```rascal-shell
for(int i <- [1..5]) append i*i;
L = for(int i <- [1..5]) append i*i;
```

.Benefits

.Pitfalls

