# Ambiguity Detection

.Synopsis
Ambiguity detection helps to find ambiguities in syntax definitions.

.Syntax

.Types

.Function

.Details

.Description
AmbiDexter is a tool that analyzes <<Syntax Definition>>s, including their <<Disambiguation>>s, to try and determine which ambiguities it contains. 
Static detection of ambiguity is not decidable, nevertheless AmbiDexter does a fine job at finding them. 


.Examples

.Benefits

*  AmbiDexter can find ambiguity for you before testing the parser, after which you can use [AmbiguityDiagnosis] to explain it. 

.Pitfalls

*  AmbiDexter is now a separate command-line tool which still needs integration
*  AmbiDexter is not a silver bullet. It has a time-limit to stop after having searcher only so much of a language. After the time limit has expired, your [SyntaxDefinition] may still be ambiguous.

