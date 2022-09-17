---
title: Measuring Methods
---

#### Synopsis

We demonstrate how to extract interesting and accurate information about Java methods.

#### Examples

```rascal-prepare
import IO;
copy(|zip+testdata:///m3/snakes-and-ladders-project-source.zip!/|, |tmp:///snakes-and-ladders|, recursive=true)
```

```rascal-shell
import lang::java::m3::Core;
import lang::java::m3::AST;
```

Now we can extract our overview model, using the classpath we derived:
```rascal-shell,continue
myModel = createM3FromDirectory(|tmp:///snakes-and-ladders/src|);
```
Now let's focus on the methods:
```rascal-shell,continue
myMethods = methods(myModel);
```
What is the source code for any given method?
```rascal-shell,continue
import IO;
methodSrc = readFile(|java+method:///snakes/Square/landHereOrGoHome()|);
```
Let's print it for readability's sake:
```rascal-shell,continue
println(methodSrc)
```
How many words in this method? Let's use a regex :-)
```rascal-shell,continue
(0 | it + 1 | /\W+/ := methodSrc)
```
But now, let's get its AST
```rascal-shell,continue
methodFiles = myModel.declarations[|java+method:///snakes/Square/landHereOrGoHome()|];
// Now we know what file to look in, parse it:
fileAST = createAstFromFile(|tmp:///snakes-and-ladders/src/snakes/Square.java|, true);
// one of the declarations in this file is the method we wanted to see:
methodASTs = {d | /Declaration d := fileAST, d.decl == |java+method:///snakes/Square/landHereOrGoHome()|};
```

If `methodASTs` would have been an empty set, then the [search pattern]((PatternMatching)) `/Declaration d` or the condition `d.decl == ...` would have failed on this example. But it didn't! It found exactly one match.

Now we count the number of expressions:
```rascal-shell,continue
(0 | it + 1 | /Expression _ := methodASTs)
```
or give us the locations of all expressions:
```rascal-shell,continue
[m.src | /Expression m := methodASTs]
```
the size should be the same, right?
```rascal-shell,continue
import List;
size([m.src | /Expression m := methodASTs]) == (0 | it + 1 | /Expression _ := methodASTs)
```

#### Benefits

* Click on any of the printed source ((Location))s in the terminal and the IDE brings you to the file.
* The method AST contains all structural/syntactic information about a method and its signature. They are defined in the ((lang::java::m3::AST)) module.
* every node in the AST has been annotated with a `src` field to explain where exactly in the file it came from
* when name and type resolution is `true` for ((createAstFromFile)), the `decl` fields on given nodes point to the resolved qualified names of a reference. These qualified names coincide with the overview [M3]((lang::java::m3::Core)) model contents. 
* ((PatternMatching)) is a very powerful way of exploring and changing ASTs
* AST and M3 models exist for other programming languages than Java. Your skills developed here may transfer to there.
* AST and M3 creation is fully based on reusing the Eclipse JDT compiler stack, which has a high quality and can also recover from local errors in input files.
* ((Values-Location)) values like `|java+method:///org/rascalmpl/ast/Statement/VariableDeclaration/clone()|` that occur in ASTs and M3 relations are _clickable_ in the terminal window and will take you to the source code identified by the URI (and the offset).

#### Pitfalls

* AST's and M3 models are a snapshot representation of the current state of the code, if source code changes on disk the models are not automatically updated.
* M3 accuracy, and also AST accuracy, depends greatly on the quality of the `classPath` provided to the extraction methods.