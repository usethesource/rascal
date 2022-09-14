---
title: Measuring Methods
---

#### Synopsis


#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

#### Examples

```rascal-shell
import lang::java::m3::Core;
import lang::java::jdt::m3::Core;
import lang::java::jdt::m3::AST;
```
First extract our overview model
```rascal-shell,continue
myModel = createM3FromEclipseProject(|project://example-project|);
```
Now let's focus on the methods
```rascal-shell,continue
myMethods = methods(myModel);
```
What is the source code for a method?
```rascal-shell,continue
import IO;
methodSrc = readFile(|java+method:///HelloWorld/main(java.lang.String%5B%5D)|);
```
let's print it:
```rascal-shell,continue
println(methodSrc)
```
how many words in this method?
```rascal-shell,continue
(0 | it + 1 | /\W+/ := methodSrc)
```
let's get its AST
```rascal-shell,continue
methodAST = getMethodASTEclipse(|java+method:///HelloWorld/main(java.lang.String%5B%5D)|, model=myModel);
```
Now we count the number of expressions:
```rascal-shell,continue
(0 | it + 1 | /Expression _ := methodAST)
```
or give us the locations of all expressions:
```rascal-shell,continue
[m@src | /Expression m := methodAST]
```
the size should be the same, right?
```rascal-shell,continue
import List;
size([m@src | /Expression m := methodAST]) == (0 | it + 1 | /Expression _ := methodAST)
```

#### Benefits

#### Pitfalls

