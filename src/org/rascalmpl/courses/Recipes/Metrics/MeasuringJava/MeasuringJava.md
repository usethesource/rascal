# Measuring Java

.Synopsis
A few steps using the M3 model to compute basic metrics for a Java project in Eclipse

.Syntax

.Types

.Function
       
.Usage

.Details

.Description


This is a recipe for computing basic or more advanced metrics from a Java project in Eclipse. We assume:

*  You have Rascal installed in an Eclipse instance.
*  You have a Java project in your Eclipse workspace that compiles without errors. Let's call it `HelloWorld`.


Now we will follow the [EASY]((EASY)) paradigm:

*  a library will be used to _parse_ the Java code generating [Rascalopedia:AbstractSyntaxTree]
*  the same library will generate a [Rascal:Values/Relation]al model to represent interesting facts between Java source code artifacts
*  then we can write queries over the generated trees and relations using [Rascal:Expressions].


These are a number of recipes for measuring different things about Java:

*  [MeasuringClasses]
*  [MeasuringMethods]

.Examples


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

.Benefits

*  Notice that _all_ these [$Rascal:Values/Location] literals are hyperlinks and you can click on them to go the source code that they point to. Try it!

.Pitfalls

