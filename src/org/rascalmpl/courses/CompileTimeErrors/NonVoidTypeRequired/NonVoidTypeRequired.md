# NonVoidTypeRequired

.Synopsis
A type other than `void` is needed.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
This error is generated when a non-void value is needed, but only void is provided.
The most prominent examples are splicing for 
[list]((Rascal:List-Splice)), [set]((Rascal:Set-Splice)), and [map]((Rascal:Map-Splice)).

Remedy: replace the expression that computes void by an expression that computes a non-void value.

.Examples
First define a dummy function that returns void:
```rascal-shell,error
void dummy() { return; }
[1, *dummy(), 2]
{1, *dummy(), 2}
```
A solution could be:

```rascal-shell
int dummy() { return 17; }
[1, *dummy(), 2]
{1, *dummy(), 2}
```

.Benefits

.Pitfalls

