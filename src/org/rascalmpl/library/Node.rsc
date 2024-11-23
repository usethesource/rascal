@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}

@synopsis{Library functions for nodes.}
@description{
The following library functions are defined for nodes:
(((TOC)))
}
module Node


@synopsis{Determine the number of children of a node.}
@examples{
```rascal-shell
import Node;
arity("f"(10, "abc"));
arity("f"(10, "abc", false));
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java int arity(node T);


@synopsis{Get the children of a node.}
@examples{
```rascal-shell
import Node;
getChildren("f"(10, "abc"));
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[value] getChildren(node T);


@synopsis{Get the keyword parameters of a node.}
@examples{
```rascal-shell
import Node;
getKeywordParameters("f"(10, "abc", height=0));
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[str,value] getKeywordParameters(node T);

@deprecated{
Use getKeywordParameters(T)
}
public map[str, value] getAnnotations(node T) = getKeywordParameters(T);


@synopsis{Set the keyword parameters of a node.}
@examples{
```rascal-shell
import Node;
setKeywordParameters("f"(10, "abc"), ("height":0));
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node setKeywordParameters(&T <: node x, map[str,value] keywordParameters);

// @synopsis{Adds new keyword parameters to a node, keeping the existing ones unless there is an entry in the new map.}
// @examples{
// ```rascal-shell
// import Node;
// mergeKeywordParameters("f"(10, "abc", width=10), ("height":0));
// ```
// }
// @javaClass{org.rascalmpl.library.Prelude}
// TODO: uncomment after bootstrap
// public java &T <: node mergeKeywordParameters(&T <: node x, map[str,value] keywordParameters);


@deprecated{
Use setKeywordParameters(x, keywordParameters)
}
public &T <: node setAnnotations(&T <: node x, map[str,value] keywordParameters)
  = setKeywordParameters(x, keywordParameters);
  

@synopsis{Determine the name of a node.}
@examples{
```rascal-shell
import Node;
getName("f"(10, "abc"));
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str getName(node T);


@synopsis{Create a node given its function name and arguments.}
@examples{
```rascal-shell
import Node;
makeNode("f", [10, "abc"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java node makeNode(str N, value V..., map[str, value] keywordParameters = ());


@synopsis{Reset a specific keyword parameter back to their default on a node.}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node unset(&T <: node x, str keywordParameter);

@deprecated{
Use unset(x, kw)
}
public &T <: node delAnnotation(&T <:  node x, str keywordParameter) = unset(x, keywordParameter); 


@synopsis{Reset a set of keyword parameters back to their default on a node.}
public &T <: node unset(&T <: node x, set[str] keywordParameters){
    for(keywordParameter <- keywordParameters){
        x = unset(x, keywordParameter);
    }
    return x;
}



@synopsis{Reset all keyword parameters back to their default.}
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node unset(&T <: node x);

@deprecated{
Use `unset(x)`
}
public &T <: node delAnnotations(&T <: node x) = unset(x);


@synopsis{Recursively reset all keyword parameters of the node and its children back to their default.}
@javaClass{org.rascalmpl.library.Prelude}
public java &T unsetRec(&T x);

@deprecated{
Use `unsetRec(x)`
}
public &T delAnnotationsRec(&T x) = unsetRec(x);


@synopsis{Recursively reset a specific keyword parameter of the node and its children back to its default.}
public &T unsetRec(&T x, str keywordParameter) = visit(x) { 
  case node n => unset(n, keywordParameter)
};


@synopsis{Recursively reset a selected set of keyword parameters of the node and its children back to their default.}
public &T <: node unsetRec(&T <: node x, set[str] keywordParameters) = visit(x) { 
  case node n: { for(keywordParameter <- keywordParameters) n = unset(n, keywordParameter); insert n; }
};


@javaClass{org.rascalmpl.library.Prelude}
public java node arbNode();



@synopsis{Convert a node to a string.}
@examples{
```rascal-shell
import Node;
F = "f"(10, "abc", color="red", size="large");
toString(F);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toString(node T);



@synopsis{Convert a node to an indented string.}
@examples{
```rascal-shell
import Node;
F = "f"(10, "abc", color="red", size="large");
itoString(F);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str itoString(node T);
