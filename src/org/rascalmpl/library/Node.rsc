@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Node

@doc{
Synopsis: Determine the number of children of a node.

Examples:
<screen>
import Node;
arity("f"(10, "abc"));
arity("f"(10, "abc", false));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java int arity(node T);

@doc{
Synopsis: Delete a specific annotation from a node.

Examples:
<screen>
import Node;
F = setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
delAnnotation(F, "size");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node delAnnotation(&T <: node x, str label);

@doc{
Synopsis: Set a specific parameter back to default on a node.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node unset(&T <: node x, str label);

@doc{
Synopsis: Set all keyword parameters back to default.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node unset(&T <: node x);

public &T <: node unsetRec(&T <: node x) = visit(x) { 
  case node n => unset(n) 
};

@doc{
Synopsis: Delete all annotations from a node.

Examples:
<screen>
import Node;
F = setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
delAnnotations(F);
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node  delAnnotations(&T <: node x);

@doc{
Synopsis: Delete recursively all annotations from all nodes in a value.

Examples:
<screen>
import Node;
G = setAnnotations("g"("def"), ("level" : "20", "direction" : "north"));
F = setAnnotations("f"(10, G), ("color" : "red", "size" : "large"));
delAnnotationsRec(F);
</screen>
}
public &T delAnnotationsRec(&T v) {
  return visit(v) { case m: node n => delAnnotations(m) };
}

@doc{
Synopsis: Retrieve the annotations of a node value as a map.

Examples:

<screen>
import Node;
// Declare two string-valued annotation on nodes, named color, respectively, size:
anno str node @ color;
anno str node @ size;
// Create a node with two annotations:
F = setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
// and retrieve those annotations:
getAnnotations(F);
F@color;
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[str,value] getAnnotations(node x);

@doc{Synopsis: Get the children of a node.
Examples:
<screen>
import Node;
getChildren("f"(10, "abc"));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[value] getChildren(node T);

@doc{Synopsis: Get the keyword parameters of a node.
Examples:
<screen>
import Node;
getKeywordParameters("f"(10, "abc", height=0));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[str,value] getKeywordParameters(node T);

@doc{
Synopsis: translates a map to keyword parameters given a type context which allows this.
}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{to generate warnings}
public java &T <: node setKeywordParameters(type[&T] context, &T <: node n, map[str,value] parameters);

public node setKeywordParameters(node n, map[str, value] params)
  = setKeywordParameters(#node, n, params);
  
@doc{
Synopsis: Determine the name of a node.

Examples:
<screen>
import Node;
getName("f"(10, "abc"));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str getName(node T);

@doc{
Synopsis: Create a node given its function name and arguments.

Examples:
<screen>
import Node;
makeNode("f", [10, "abc"]);
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java node makeNode(str N, value V..., map[str, value] keywordParameters = ());



@doc{
Synopsis: Add a map of annotations to a node value.

Description:
Set the annotations on node value `x` as described by the map `annotations`.

Examples:
<screen>
import Node;
setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
</screen>

Benefits:

Pitfalls:
 This function may result in run-time type errors later if
  you store a value with a label that has an incomparable annotation type
  declared.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node setAnnotations(&T <: node x, map[str, value] annotations);

@doc{
Synopsis: Set a specific parameter back to default on a node.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node unset(&T <: node x, str label);

@doc{
Synopsis: Set all keyword parameters back to default.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node unset(&T <: node x);

public &T <: node unsetRec(&T <: node x) = visit(x) { 
  case node n => unset(n) 
};


@doc{
Synopsis: Convert a node to a string.

Examples:
<screen>
import Node;
F = setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
toString(F);
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toString(node T);


@doc{
Synopsis: Convert a node to an indented string.

Examples:
<screen>
import Node;
F = setAnnotations("f"(10, "abc"), ("color" : "red", "size" : "large"));
itoString(F);
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str itoString(node T);


