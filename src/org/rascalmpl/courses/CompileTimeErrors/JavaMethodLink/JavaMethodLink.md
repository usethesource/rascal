# JavaMethodLink

.Synopsis
Cannot link to a Java method.

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


This error is generated when the Java implementation cannot be found. Most likely, this is a missing or misspelled Java class name.
It is also generated when the function declaration _does_ have a body.

Remedy: Contact the Rascal developers:

*  Ask a question on StackOverflow using the http://stackoverflow.com/questions/tagged/rascal[Rascal Stackoverflow Tag].
*  Read the currently open issues at the Rascal's https://github.com/usethesource/rascal/issues?state=open[Issue Tracker on Github]. If your problem has not yet been reported by someone else, please report it here.


If you are an expert developer and have implemented your own extension in Java, please check your own extension code first.

.Examples
This is how the `size` function on lists is declared in the Rascal library:
```rascal-shell
@javaClass{org.rascalmpl.library.Prelude}
public java int size(list[&T] lst);
```

Misspelling the class name will generate the JavaMethodLink error:
```rascal-shell,error
@javaClass{org.rascalmpl.library.Preludexxx}
public java int size(list[&T] lst);
```
The same error message is generated if the function declaration contains a body:
```rascal-shell,error
@javaClass{org.rascalmpl.library.Preludexxx}
public java int size(list[&T] lst){
  return 0;
}
```



.Benefits

.Pitfalls

