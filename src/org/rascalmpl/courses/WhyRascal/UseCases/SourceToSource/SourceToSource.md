---
title: Source to Source transformations
---

Rascal is particularly good for source-to-source transformation. There are many forms of 
implementing them; here we show a case of using ((Rascal:ConcreteSyntax)) matching and
construction to rewrite Java parse trees:

```rascal
module Idiomatic

import lang::java::\syntax::Java15; // <1>

CompilationUnit idiomatic(CompilationUnit unit) = innermost /*<2>*/ visit(unit) {
   case (Stm) `if (!<Expr cond>) <Stm a> else <Stm b>` =>   // <3>
        (Stm) `if (<Expr cond>)  <Stm b> else <Stm a>`
        
   case (Stm) `if (<Expr cond>) <Stm a>` =>                 // <4>
        (Stm) `if (<Expr cond>) { <Stm a> }` 
     when (Stm) `<Block _>` !:= a                           // <5>
        
   case (Stm) `if (<Expr cond>) <Stm a> else <Stm b>` =>   
        (Stm) `if (<Expr cond>) { <Stm a> } else { <Stm b> }` 
     when (Stm) `<Block _>` !:= a
                 
   case (Stm) `if (<Expr cond>) { return true; } else { return false; }` => // <6>
        (Stm) `return <Expr cond>;`
};
```

```rascal-prepare
import lang::java::\syntax::Java15; // <1>
CompilationUnit idiomatic(CompilationUnit unit) = innermost /*<2>*/ visit(unit) {
   case (Stm) `if (!<Expr cond>) <Stm a> else <Stm b>` =>   // <3>
        (Stm) `if (<Expr cond>)
              '  <Stm b> 
              'else 
              '  <Stm a>`        
   case (Stm) `if (<Expr cond>) <Stm a>` =>                 // <4>
        (Stm) `if (<Expr cond>) { 
              '  <Stm a> 
              '}` 
     when (Stm) `<Block _>` !:= a                           // <5>
   case (Stm) `if (<Expr cond>) <Stm a> else <Stm b>` =>   
        (Stm) `if (<Expr cond>) { 
              '  <Stm a> 
              '} else { 
              '  <Stm b> 
              '}` 
     when (Stm) `<Block _>` !:= a                 
   case (Stm) `if (<Expr cond>) { return true; } else { return false; }` => // <6>
        (Stm) `return <Expr cond>;`
};
```

We are going to transform Java source code to improve its "style" a bit, removing
some code smells and introducing more idiomatic use of the language. Note that these
transformations are arbitrarily chosen for this example.

* <1> we use the parser generated from `lang::java::\syntax::Java15`
* <2> The function `idiomatic` traverses a Java parse tree recursively using a ((Statements-Visit)) statement.
* <3> The first pattern matches negated conditionals. Notice how the Java syntax is used to actually match a pattern of a Java parse tree. If `!` negation is matched as the outermost expression of the conditional,
then we replace the entire statement and flip the branches.
* <4> The second pattern introduces braces `{` `}` to improve code readability. 
* <5> Notice the `when` clause that would stop an infinite recursion creating ever more deeply nested braces `{{{{...}}}}`
* <6> The final simplification rewrite replaces a very and specific and deeply nested tree pattern with a single `return`. This shows the power of concrete pattern matching; imagine having to rewrite checks on an abstract syntax tree format to see if this pattern matches.

The following code is a small test program written to demonstrate the effect of the idiomatic function. Test functions are integrated in Rascal and will generate random input for parameters and integrate with the IDE to produce test reports.

```rascal-shell,continue
code = (CompilationUnit)  `class MyClass { 
                          '   int m() { 
                          '       if (!x) 
                          '           println("x"); 
                          '       else 
                          '           println("y");  
                          '       if (x)
                          '           return true; 
                          '       else 
                          '           return false;
                          '   } 
                          '}`;
idiomatic(code)
```

As you can see, some indentation still has to be recovered. That is another transformation.
What is guaranteed is that the output conforms to the Java15 grammar that imported above.