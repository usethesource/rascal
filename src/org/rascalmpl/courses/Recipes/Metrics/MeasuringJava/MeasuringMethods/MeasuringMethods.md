---
title: Measuring Methods
---

#### Synopsis

We demonstrate how to extract interesting and accurate information about Java methods.

#### Examples

```rascal-shell
import lang::java::m3::Core;
import lang::java::m3::AST;
```

To be able to resolve all classes properly, we first collect a classpath. The 
((getProjectPathConfig)) function uses the Maven `pom.xml` file to find out what the
dependencies of the current rascal project are:
```rascal-shell,continue
import util::Reflective;
cp = getProjectPathConfig(|project://rascal|).javaCompilerPath;
```

Now we can extract our overview model, using the classpath we derived:
```rascal-shell
myModel = createM3FromDirectory(|project://rascal|, classPath=cp);
```
Now let's focus on the methods
```rascal-shell,continue
myMethods = methods(myModel);
```
What is the source code for any given method?
```rascal-shell,continue
import IO;
methodSrc = readFile(|java+method:///org/rascalmpl/ast/Assignable/isVariable()|);
```
Let's print it for readability's sake:
```rascal-shell,continue
println(methodSrc)
```
How many words in this method?
```rascal-shell,continue
(0 | it + 1 | /\W+/ := methodSrc)
```
let's get its AST
```rascal-shell,continue
methodFiles = myModel.declarations[|java+method:///org/rascalmpl/ast/Assignable/isVariable()|]
// now we know what file to look in, parse it:
fileAST = createASTFromFile(|project://rascal/src/org/rascalmpl/ast/Assignable.java|, true, classPath=cp);

// one of the declarations in this file is the method we wanted to see:
methodAST = ();
if (/Declaration d := fileAST, d.decl ==|java+method:///org/rascalmpl/ast/Assignable/isVariable()|) {
    methodAST = d;
}
```
Now we count the number of expressions:
```rascal-shell,continue
(0 | it + 1 | /Expression _ := methodAST)
```
or give us the locations of all expressions:
```rascal-shell,continue
[m.src | /Expression m := methodAST]
```
the size should be the same, right?
```rascal-shell,continue
import List;
size([m.src | /Expression m := methodAST]) == (0 | it + 1 | /Expression _ := methodAST)
```

#### Benefits

* The method AST contains all structural/syntactic information about a method and its signature. They are defined in the ((java::m3::AST)) module.
* every node in the AST has been annotated with a `src` field to explain where exactly in the file it came from
* when name and type resolution is `true` for ((createAstFromFile)), the `decl` fields on given nodes point to the resolved qualified names of a reference. These qualified names coincide with the overview ((M3)) model contents. 
* ((PatternMatching)) is a very powerful way of exploring and changing ASTs
* AST and M3 models exist for other programming languages than Java. Your skills developed here may transfer to there.
* AST and M3 creation is fully based on reusing the Eclipse JDT compiler stack, which has a high quality and can also recover from local errors in input files.

#### Pitfalls

* AST's and M3 models are a snapshot representation of the current state of the code, if source code changes on disk the models are not automatically updated.
* M3 accuracy, and also AST accuracy, depends greatly on the quality of the `classPath` provided to the extraction methods.