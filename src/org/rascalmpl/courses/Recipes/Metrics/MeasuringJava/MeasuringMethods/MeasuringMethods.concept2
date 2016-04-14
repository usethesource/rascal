# Measuring Methods

.Synopsis


.Syntax

.Types

.Function
       
.Usage

.Details

.Description

.Examples

[source,rascal-shell]
----
import lang::java::m3::Core;
import lang::java::jdt::m3::Core;
import lang::java::jdt::m3::AST;
----
First extract our overview model
[source,rascal-shell,continue]
----
myModel = createM3FromEclipseProject(|project://example-project|);
----
Now let's focus on the methods
[source,rascal-shell,continue]
----
myMethods = methods(myModel);
----
What is the source code for a method?
[source,rascal-shell,continue]
----
import IO;
methodSrc = readFile(|java+method:///HelloWorld/main(java.lang.String%5B%5D)|);
----
let's print it:
[source,rascal-shell,continue]
----
println(methodSrc)
----
how many words in this method?
[source,rascal-shell,continue]
----
(0 | it + 1 | /\W+/ := methodSrc)
----
let's get its AST
[source,rascal-shell,continue]
----
methodAST = getMethodASTEclipse(|java+method:///HelloWorld/main(java.lang.String%5B%5D)|, model=myModel);
----
Now we count the number of expressions:
[source,rascal-shell,continue]
----
(0 | it + 1 | /Expression _ := methodAST)
----
or give us the locations of all expressions:
[source,rascal-shell,continue]
----
[m@src | /Expression m := methodAST]
----
the size should be the same, right?
[source,rascal-shell,continue]
----
import List;
size([m@src | /Expression m := methodAST]) == (0 | it + 1 | /Expression _ := methodAST)
----

.Benefits

.Pitfalls

