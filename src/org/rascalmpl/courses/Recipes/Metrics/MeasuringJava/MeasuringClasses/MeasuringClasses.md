---
title: Measuring Classes
---

#### Synopsis


#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

#### Examples

```rascal-shell
```
First we import the basic data types for representing Java. The model is called _M3_, and its definition is split acros a generic
language independent module called [Rascal:analysis/m3/Core] and a Java specific part called [Rascal:lang/java/m3/Core]. Have a look at the documentation 
of these modules later. For now we will go through using them in a few examples.
```rascal-shell,continue
import lang::java::m3::Core;
```
Then we import the API for extracting an M3 model from an Eclipse project. 
```rascal-shell,continue
import lang::java::jdt::m3::Core;
```
Calling the following function generates an enormous value representing everything the Eclipse Java compiler knows about this project:
```rascal-shell,continue
myModel = createM3FromEclipseProject(|project://example-project|);
```
Next, let's focus on the _containment_ relation. This defines what parts of the source code are parts of which other parts:
```rascal-shell,continue
myModel.containment
```
As you can read, classes contain methods, methods contain variables, etc. Classes could also contain other classes (nested classes), and methods can even contain classes (anonymous classes). Let's focus on a specific class, and project what it contains from the relation:
```rascal-shell,continue
myModel.containment[|java+class:///HelloWorld|]
```
Let's filter the methods:
```rascal-shell,continue
helloWorldMethods = [ e | e <- myModel.containment[|java+class:///HelloWorld|], e.scheme == "java+method"];
```
And we are ready to compute our first metric. How many methods does this class contain?
```rascal-shell,continue
import List;
size(helloWorldMethods)
```
No magic applied! It is just a little query on a model that knows everything about the code. Let's generalize and compute the number of methods for all classes in one big expression. First a function to compute the number for a given class:
```rascal-shell,continue
int numberOfMethods(loc cl, M3 model) = size([ m | m <- model.containment[cl], isMethod(m)]);
```
then we apply this new function to give us a map from classes to integers:
```rascal-shell,continue
map[loc class, int methodCount] numberOfMethodsPerClass = (cl:numberOfMethods(cl, myModel) | <cl,_> <- myModel.containment, isClass(cl));
```
how about the number of fields?
```rascal-shell,continue
int numberOfFields(loc cl, M3 model) = size([ m | m <- model.containment[cl], isField(m)]);
map[loc class, int fieldCount] numberOfFieldsPerClass = (cl:numberOfFields(cl, myModel) | <cl,_> <- myModel.containment, isClass(cl));
```
what is the ratio between fields and methods for each class?
```rascal-shell,continue
(cl : (numberOfFieldsPerClass[cl] * 1.0) / (numberOfMethodsPerClass[cl] * 1.0) | cl <- classes(myModel))
```

#### Benefits

#### Pitfalls

