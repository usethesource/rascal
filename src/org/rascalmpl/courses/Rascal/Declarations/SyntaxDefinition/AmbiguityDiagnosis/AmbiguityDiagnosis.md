# Ambiguity Diagnosis

.Synopsis
Ambiguity diagnosis suggests changes to syntax definitions to make them non-ambiguous.

.Syntax

.Types

.Function

.Details

.Description
The `Ambiguity` library, a.k.a. DrAmbiguity, contains a diagnosis tool that can help you find the causes of ambiguous 
parse trees and possible ((Disambiguation))s to solve them in a <<Syntax Definition>>.
DrAmbiguity is a library that processes any parse forest produced by a parser generated from Rascal's <<Syntax Definition>>s.
Please read ((Disambiguation)) first.

.Examples
[source,rascal]
----
import analysis::grammars::Ambiguity;
diagnose(t); // for any t of which you know it contains an ambiguity
----

.Benefits

*  DrAmbiguity automatically proposes ((Disambiguation))s that will work 

.Pitfalls

*  DrAmbiguity does not scale to large parse trees, so please first make your example smaller.
*  DrAmbiguity proposes several [Disambiguation]s for each ambiguity, only few of which make sense from a language design point of view!
*  DrAmbiguity is now only a library function, while it should be integrated into the Rascal IDE

