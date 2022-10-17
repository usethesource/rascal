---
title: Measuring Classes
---

#### Synopsis

A typical example of how to use M3 to analyze Java classes.

#### Examples


First we import the basic data types for representing Java. The model is called _M3_, and its definition is split acros a generic
language independent module called [Rascal:analysis/m3/Core] and a Java specific part called [Rascal:lang/java/m3/Core]. Have a look at the documentation 
of these modules later. For now we will go through using them in a few examples.

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

Now we can extract our overview model, using the classpath we derived:
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
```
Let's filter the methods:
```rascal-shell,continue
snakeMethods = [ e | e <- myModel.containment[|java+class:///snakes/Snake|], e.scheme == "java+method"];
```
And we are ready to compute our first metric. How many methods does this class contain?
```rascal-shell,continue
import List;
size(snakeMethods)
```
No magic applied! It is just a little query on a model that knows everything about the code. 

Note that some people define Java's constructors also as methods! In that case our metric is wrong and we should fix it.
The quality of a metric's implementation is defined by its independent definition.

Let's generalize and compute the number of methods for all classes in one big expression. First we make a new function to compute the number for any given class. Now we reuse `isMethod` for a change, which includes normal methods, constructors and static initializers:
```rascal-shell,continue
int numberOfMethods(loc cl, M3 model) = size([ m | m <- model.containment[cl], isMethod(m)]);
numberOfMethods(|java+class:///snakes/Snake|, myModel)
```

Then we apply this new function to give us a map from classes to integers:
```rascal-shell,continue
classes(myModel)
map[loc class, int methodCount] numberOfMethodsPerClass = (cl:numberOfMethods(cl, myModel) | cl <- classes(myModel));
```
How about the number of fields?
```rascal-shell,continue
int numberOfFields(loc cl, M3 model) = size([ m | m <- model.containment[cl], isField(m)]);
map[loc class, int fieldCount] numberOfFieldsPerClass = (cl:numberOfFields(cl, myModel) | cl <- classes(myModel));
```
What is the ratio between fields and methods for each class?
```rascal-shell,continue
(cl : (numberOfFieldsPerClass[cl] * 1.0) / (numberOfMethodsPerClass[cl] * 1.0) | cl <- classes(myModel))
```

There is a lot more to discover in M3 models. These are the currently available relations in an M3 model:
```rascal-shell,continue
import Node;
import Set;
for (r <- sort(getKeywordParameters(myModel)<0>)) println("  <r>");
```

See ((analysis::m3::Core)) for the definition of the language-independent relations and ((lang::java::m3::Core)) for the Java-specific extensions.

#### Benefits

* Rascal values, such as M3 models, are fully text-based. That means what you see on screen is what is in memory, and vice versa. Nothing is hidden or implicit. No magic!
* Using relational calculus operators like ((Set-Intersection)), ((Set-Union)), ((Relation-Composition)), ((Set-Comprehension)) you can query M3 models in many different ways.
* M3 models encapsulate *everything* there is to know about the structure of a Java project. Using the knowledge of the Eclipse JDT compiler we derive types and names, semantic relations that can be queried at will.
* M3 models are *composable*; you could extract them for different projects that depend on each-other and combine them before running an analysis on the whole
* There are other M3 models for other languages, with similar ideas and design. You can transfer your analysis skills to those lanaguges, but also make analyses that cross the boundaries of programming languages (consider javascript and java, or java and C via JNI, etc.)

#### Pitfalls

* Inaccurate query results are possible even though the M3 models are 100% accurate. 
   * M3 models are *initial* in the sense that they contain what can be statically derived from the source code in terms of declarations and relations. 
   * Analyses based directly on this information can still be *inaccurate*; because they represent harder questions.
   * For example, a Java call graph produced by the relation composition `myModel.methodInvocation o myModel.methodOverrides` shows _all_ possible concrete methods that could be invoked via virtual method invocation (over-approximation), but it skips all the invokes constructed via reflection (under-aproximation). In that sense this query is both incomplete and unsound, however useful it may be.
* Java still has the most complete M3 model in the Rascal ecosystem, so when you move to other languages prepare to extract more information from AST models instead.
