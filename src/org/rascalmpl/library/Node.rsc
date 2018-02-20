@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@doc{
.Synopsis
Library functions for nodes.

.Description

For operators on nodes see link:/Rascal#Values-Node[Node] in the Rascal Language Reference.

The following functions are defined for nodes:
loctoc::[1]
}
module Node

@doc{
.Synopsis
Determine the number of children of a node.

.Examples
[source,rascal-shell]
----
import Node;
arity("f"(10, "abc"));
arity("f"(10, "abc", false));
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java int arity(node T);

@doc{
.Synopsis
Delete a specific annotation from a node.

.Examples
[source,rascal-shell]
----
import Node;
F = setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
delAnnotation(F, "size");
----
}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{To print warning}
@deprecated{Annotations are deprecated. Please use keyword fields.}
public java &T <: node delAnnotation(&T <: node x, str label);

@doc{
.Synopsis
Delete all annotations from a node.

.Examples
[source,rascal-shell]
----
import Node;
F = setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
delAnnotations(F);
----
}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{To print warning}
@deprecated{Annotations are deprecated. Please use keyword fields.}
public java &T <: node  delAnnotations(&T <: node x);

@doc{
.Synopsis
Delete recursively all annotations from all nodes in a value.

.Examples
[source,rascal-shell]
----
import Node;
G = setAnnotations("g"("def"), ("level" : "20", "direction" : "north"));
F = setAnnotations("f"(10, G), ("color" : "red", "size" : "large"));
delAnnotationsRec(F);
----
}
@deprecated{Annotations are deprecated. Please use keyword fields.}
public &T delAnnotationsRec(&T v) = visit(v) { 
     case m: node n => delAnnotations(m) 
  };

@doc{
.Synopsis

Retrieve the annotations of a node value as a map. Annotations are deprecated.

}
@javaClass{org.rascalmpl.library.Prelude}
@deprecated{Annotations are deprecated. Please use keyword fields.}
public java map[str,value] getAnnotations(node x);

@doc{
.Synopsis
Get the children of a node.

.Examples
[source,rascal-shell]
----
import Node;
getChildren("f"(10, "abc"));
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[value] getChildren(node T);

@doc{
.Synopsis
Get the keyword parameters of a node.

.Examples
[source,rascal-shell]
----
import Node;
getKeywordParameters("f"(10, "abc", height=0));
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[str,value] getKeywordParameters(node T);

@doc{
.Synopsis
Set the keyword parameters of a node.

.Examples
[source,rascal-shell]
----
import Node;
setKeywordParameters("f"(10, "abc"), ("height":0));
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node setKeywordParameters(&T <: node x, map[str,value] keywordParameters);

@doc{
.Synopsis
Determine the name of a node.

.Examples
[source,rascal-shell]
----
import Node;
getName("f"(10, "abc"));
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str getName(node T);

@doc{
.Synopsis
Create a node given its function name and arguments.

.Examples
[source,rascal-shell]
----
import Node;
makeNode("f", [10, "abc"]);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java node makeNode(str N, value V..., map[str, value] keywordParameters = ());



@doc{
.Synopsis
Add a map of annotations to a node value.

.Description
Set the annotations on node value `x` as described by the map `annotations`.

.Examples
[source,rascal-shell]
----
import Node;
setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
----

.Benefits

.Pitfalls
This function may result in run-time type errors later if
you store a value with a label that has an incomparable annotation type
declared.
}
@javaClass{org.rascalmpl.library.Prelude}
@deprecated{Annotations are deprecated. Please use keyword fields.}
public java &T <: node setAnnotations(&T <: node x, map[str, value] annotations);

@doc{
.Synopsis
Reset a specific keyword parameter back to their default on a node.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node unset(&T <: node x, str keywordParameter);


@doc{
.Synopsis
Reset a set of keyword parameters back to their default on a node.
}
public &T <: node unset(&T <: node x, set[str] keywordParameters){
    for(keywordParameter <- keywordParameters){
        x = unset(x, keywordParameter);
    }
    return x;
}


@doc{
.Synopsis
Reset all keyword parameters back to their default.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node unset(&T <: node x);


@doc{
.Synopsis
Recursively reset all keyword parameters of the node and its children back to their default.
}
public &T <: node unsetRec(&T <: node x) = visit(x) { 
  case node n => unset(n) 
};

@doc{
.Synopsis
Recursively reset a specific keyword parameter of the node and its children back to its default.
}
public &T <: node unsetRec(&T <: node x, str keywordParameter) = visit(x) { 
  case node n => unset(n, keywordParameter)
};

@doc{
.Synopsis
Recursively reset a selected set of keyword parameters of the node and its children back to their default.
}
public &T <: node unsetRec(&T <: node x, set[str] keywordParameters) = visit(x) { 
  case node n: { for(keywordParameter <- keywordParameters) n = unset(n, keywordParameter); insert n; }
};


@javaClass{org.rascalmpl.library.Prelude}
public java node arbNode();


@doc{
.Synopsis
Convert a node to a string.

.Examples
[source,rascal-shell]
----
import Node;
F = setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
toString(F);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toString(node T);


@doc{
.Synopsis
Convert a node to an indented string.

.Examples
[source,rascal-shell]
----
import Node;
F = setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
itoString(F);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str itoString(node T);