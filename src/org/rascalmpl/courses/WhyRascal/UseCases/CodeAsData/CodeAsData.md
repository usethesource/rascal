---
title: "Code is Data: Analyzing Code"
---

Code analysis has many motivations. In this example we will see how we can resolve _bugs_ by analyzing code with automated queries, instead of reading code manually. In this example we will try and identify methods and classes that are not used at all.

#### Parsing=Turning Code into Data

In this example we use Rascal's JDT library, which wraps Eclipse's Java Development Toolkit compiler for Java and makes its information available as Rascal data types. This intermediate model is called _M3_, and its definition is split acros a generic
language independent module called ((Library:module:analysis::m3::Core)) and a Java specific part called ((Library:module:lang::java::m3::Core)). 

```rascal-prepare
import IO;
copy(|zip+testdata:///m3/snakes-and-ladders-project-source.zip!/|, |tmp:///snakes-and-ladders|, recursive=true)
```

```rascal-shell
import lang::java::m3::Core;
import lang::java::m3::AST;
```

"Snakes and Ladders" is an example Java project of which we have stored the source code in `|tmp:///snakes-and-ladders/src|`
```rascal-shell,continue
|tmp:///snakes-and-ladders/src/snakes/|.ls
```

Now we can extract an overview model:
```rascal-shell,continue
myModel = createM3FromDirectory(|tmp:///snakes-and-ladders/src|);
```

Next, let's focus on the _containment_ relation. This defines what parts of the source code are parts of which other parts:
```rascal-shell,continue
myModel.containment
```

You are looking at a binary relation of type `rel[loc from,loc to]`, where `from` is the container and `to` is the contained item. Each tuple, or row if you will, maps a container to a contained item.

As you can read, classes contain methods, methods contain variables, etc. Classes could also contain other classes (nested classes), and methods can even contain classes (anonymous classes). Let's focus on a specific class, and project what it contains from the relation:
```rascal-shell,continue
import IO;
println(readFile(|java+class:///snakes/Snake|))
myModel.containment[|java+class:///snakes/Snake|]
methods(myModel)
import Set;
size(methods(myModel))
```

As you can see there are 85 methods in this system. Are they all used? That's not something you would want to check manually, so let's write some queries:

```rascal-shell,continue
allMethods = methods(myModel);
// methodInvocation reports on who calls who
myModel.methodInvocation
// so let's find out which method definitions are never invoked
allMethods - myModel.methodInvocation<1>
```

Mmmm... that's a lot! We've made a mistake. The Java language offers "virtual methods", while
we have listed many method definitions that override virtual methods they are never
explicitly called.

```rascal-shell,continue
// methodOverrides is a tabel that explains which method overrides which other method
myModel.methodOverrides
// so we can compose that to find out who is probably called:
myModel.methodInvocation o (myModel.methodOverrides<1,0>) 
// and by adding all the others as well, we have a "complete" call graph
calls = myModel.methodInvocation + myModel.methodInvocation o (myModel.methodOverrides<1,0>);
// so which methods are defined but never called int this Game?
notCalled = methods(myModel) - calls<1>;
notConstructed = constructors(myModel) - calls<1>;
```

The results are:
* `main` is not called; that makes sense
* lots of test methods are not called; that also makes sense since they are called by the Unit test framework using reflection;
* we can filter the tests automatically, but this set is small enough to go through manually.

The two unused methods are:
* `|java+method:///snakes/Square/nextSquare()|`
* `|java+method:///snakes/Player/square()|`

In an interactive environment like Eclipse or VScode we would click on these links to jump to their source code. Here we will print the bodies:

```rascal-shell,continue
println(readFile(|java+method:///snakes/Player/square()|))
println(readFile(|java+method:///snakes/Square/nextSquare()|))
```

Note that this analysis is reasonably precise, but not 100% exact:
* all name analysis and type analysis is done and used to disambiguate identifier names. There can be no confusion between overloaded methods for example and therefore overriding/implementing analysis is exact.
* the constructed call graph is:
   * an over-approximation because not all overriden methods have to be called, it's just that they might be called
   * an under-approximation because we don't see the calls made by reflection. The test methods are a good example of that
* adding more analysis code can make the result more exact.