# Prettyprinter

.Synopsis
Transform an ((Abstract Syntax Tree)) into a formatted string.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
A http://en.wikipedia.org/wiki/Prettyprint[pretty printer]
formats the source code of programs. Alternative names are _formatter_ or _beautifier_.
Pretty printers differ in the inputs they accept:

*  The source text itself.
*  A ((ParseTree)) that corresponds to the source text. This variant is also called _unparser_.
*  An ((Abstract Syntax Tree)) that corresponds to the source text.


Pretty printers also differ in flexibility. They differ in:

*  The source language(s) they can accept.
*  The adaptability of the formatting rules.

.Examples
The program fragment
```rascal
if(x > 10) { System.err.println("x > 10"); } else { System.err.println("x <= 10"); }
```
can be pretty printed in many different ways. Here are two variants examples:
```rascal
if(x > 10) { 
   System.err.println("x > 10"); 
} else { 
   System.err.println("x <= 10"); 
}
```

```rascal
if( x > 10 )
{ 
  System.err.println("x > 10"); 
} else 
{ 
   System.err.println("x <= 10"); 
}
```
.Benefits

.Pitfalls

