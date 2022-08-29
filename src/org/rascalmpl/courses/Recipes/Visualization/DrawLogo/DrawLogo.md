# Draw a Logo

.Synopsis
Draw the Rascal logo.

.Syntax

.Types

.Function

.Details

.Description

Given a 50x50 matrix containing the colors of the Rascal logo,
we can reproduce it as visualization.

.Examples
Here is the solution:
```rascal
include::{LibDir}demo/vis/Logo.rsc[tags=module]
```

                
We can use it as follows:
```rascal-figure,width=,height=,file=logo1.png
                import demo::vis::Logo;
render(logo());
```
will produce:

![]((logo1.png))

or as a screenshot:


![]((Screenshot1.png))



.Benefits

.Pitfalls

