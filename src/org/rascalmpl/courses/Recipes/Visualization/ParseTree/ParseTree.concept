# ParseTree

.Synopsis
Visualize a parse tree.

.Syntax

.Types

.Function

.Details

.Description
A parse tree is a (usually large) internal representation of  a parsed text.
In the rare situation that it is necessary to read or inspect a parse tree,
a visualization can be useful.

.Examples
We embark on visualizing parse trees for the language Exp:
[source,rascal-shell]
----
import demo::lang::Exp::Concrete::WithLayout::Syntax;
import ParseTree;
parse(#Exp, "1+2*3");
----
As can be seen, even for such a trivial example, the details in the parse tree representation become sizeable.

We can visualize it as follows:
[source,rascal-figure,width=100,height=100,file=t1.png]
----
import demo::lang::Exp::Concrete::WithLayout::Syntax;
import ParseTree;
import vis::Figure;
import vis::ParseTree;
import vis::Render;
render(visParsetree(parse(#Exp, "1+2*3")));
----
With as result:

image::t1.png[alt="t1"]


The figure is interactive (not available here):

*  Rectangles with blue text are terminal symbols.
*  Little circles represent non-terminals: hovering over them shows the corresponding grammar rule.
*  Little grey rectangles represent layout: hovering over them also shows the corresponding lexical rule.

.Benefits

*  A dense, structured, representation of a parse tree that provides extra information via interaction.

.Pitfalls

*  This visualization does not scale to huge trees.

