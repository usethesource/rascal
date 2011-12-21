@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Node

@doc{Number of children of a node}
@javaClass{org.rascalmpl.library.Node}
public java int arity(node T);

@doc{retrieve the annnotations of a node value as a map}
@javaClass{org.rascalmpl.library.Node}
public java map[str,value] getAnnotations(node x);

@doc{Get the children of a node}
@javaClass{org.rascalmpl.library.Node}
public java list[value] getChildren(node T);

@doc{Get the function name of a node}
@javaClass{org.rascalmpl.library.Node}
public java str getName(node T);

@doc{Create a node given its function name and arguments}
@javaClass{org.rascalmpl.library.Node}
public java node makeNode(str N, value V...);

@doc{Read an ATerm from a named file}
@javaClass{org.rascalmpl.library.Node}
public java value readATermFromFile(str fileName);

@doc{
  Set a whole map of annotations on a value.
  
  WARNING: Note that this function may result in run-time type errors later if
  you store a value with a label that has an incomparable annotation type
  declared.
}
@javaClass{org.rascalmpl.library.Node}
public java &T <: node setAnnotations(&T <: node x, map[str, value] annotations);

@doc{remove annotation on a node}
@javaClass{org.rascalmpl.library.Node}
public java &T <: node delAnnotation(&T <: node x, str label);

@doc{removes all annotations on all nodes in a value}
@javaClass{org.rascalmpl.library.Node}
public &T delAnnotationsRec(&T v) {
  return visit(v) { case m: node n => delAnnotations(m) };
}

@doc{remove all annotations on a node}
@javaClass{org.rascalmpl.library.Node}
public java &T <: node  delAnnotations(&T <: node x);

@doc{Convert a node to a string}
@javaClass{org.rascalmpl.library.Node}
public java str toString(node T);




