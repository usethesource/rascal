# NonAbstractJavaFunction

.Synopsis
A function declared with the `java` modifier has a Rascal body.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
Rascal functions can be implemented in Java. This is the case for many functions in the standard library.
This requires these elements:

*  An abstract Rascal function declaration (= a function header without a body).
*  The keyword `java` should be part of the function header.
*  The function declaration is annotated (uing `javaClass`) with the name of the Java class that implements this function.


This error is generated when the function does have a body.

Remedies:

*  Remove the `java` keyword.
*  If this happens to a library function, contact the Rascal developers:
**  Ask a question at [Rascal Ask site](http://ask.rascal-mpl.org/questions/).
**  Read the currently open issues at the Rascal's [issue tracker](https://github.com/cwi-swat/rascal/issues?state=open). If your problem has not yet been reported by someone else, please report it here.


If you are an expert developer, please check the body of the offending function first.

.Examples
```rascal-shell,error
java int incr(int x) {}
```

.Benefits

.Pitfalls

