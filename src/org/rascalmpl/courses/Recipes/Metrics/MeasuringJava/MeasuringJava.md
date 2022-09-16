---
title: Measuring Java
---

#### Synopsis

A few steps using the M3 model to compute basic metrics for a Java project in Eclipse.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description


This is a recipe for computing basic or more advanced metrics from a Java project in Eclipse. We assume:

*  You have Rascal installed in an Eclipse instance.
*  You have a Java project in your Eclipse workspace that compiles without errors. Let's call it `HelloWorld`.


Now we will follow the [EASY]((EASY)) paradigm:

*  a library will be used to _parse_ the Java code generating [Rascalopedia:AbstractSyntaxTree]
*  the same library will generate a [Rascal:Values/Relation]al model to represent interesting facts between Java source code artifacts
*  then we can write queries over the generated trees and relations using [Rascal:Expressions].


These are a number of recipes for measuring different things about Java:

*  ((MeasuringClasses))
*  ((MeasuringMethods))

#### Examples


First we import the basic data types for representing Java. The model is called _M3_, and its definition is split acros a generic
language independent module called [Rascal:analysis/m3/Core] and a Java specific part called [Rascal:lang/java/m3/Core]. Have a look at the documentation 
of these modules later. For now we will go through using them in a few examples.

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
```rascal-shell,continue
myModel = createM3FromDirectory(|project://rascal|, classPath=cp);
```


#### Benefits

*  Notice that _all_ these ((Rascal:Values-Location)) literals are hyperlinks and you can click on them to go the source code that they point to. Try it!
* M3 models are great for metrics, but also they are the basis for many other static analyses
* See ((MeasuringClasses)) and ((MeasuringMethods)) for more benefits

#### Pitfalls

* See ((MeasuringClasses)) and ((MeasuringMethods)) for more pitfalls
