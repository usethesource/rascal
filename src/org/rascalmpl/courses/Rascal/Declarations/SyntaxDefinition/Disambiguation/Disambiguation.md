# Disambiguation

.Synopsis

Disambiguation is the definition of filters on the parse trees that ((Syntax Definition))s define. 
There are several ways of defining ((Disambiguation)) in Rascal.

.Syntax

.Types

.Function

.Details

.Description
There are generally three ways of removing ambiguity from parse forests that are produced by parsers generated from ((Syntax Definition))s.

*  The first way is to add disambiguation declarations to the ((Syntax Definition)). You can choose from:
   **  ((Priority Declaration))s, which can be used to define the relative priority in expression languages
   **  ((Associativity Declaration))s, which can be used to define relative associativity between operators of 
       expression languages
   **  ((Follow Declaration))s, which can be used to implement longest match using lookahead
   **  ((Precede Declaration))s, which can be used to implement first match using look behind
   **  <<Reserve Declaration>s, which allow you to finite sets of strings from a ((Syntax Definition))
       to implement keyword reservation
*  The second way is to add ((Action))s that will be triggered just after parsing and allow you to trim a parse forest 
   using any information necessary.
*  The third way is use the ((Statement-Visit)) statement on a parse tree and implement your own filter post-parsing time, 
   or any other kind of program that processes ((Parse Trees)).

.Examples

.Benefits

.Pitfalls

