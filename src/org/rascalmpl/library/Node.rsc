module Node

@doc{Number of children of a node}
@javaClass{org.rascalmpl.library.Node}
public int java arity(node T);

@doc{retrieve the annnotations of a node value as a map}
@javaClass{org.rascalmpl.library.Node}
public map[str,value] java getAnnotations(node x);

@doc{Get the children of a node}
@javaClass{org.rascalmpl.library.Node}
public list[value] java getChildren(node T);

@doc{Get the function name of a node}
@javaClass{org.rascalmpl.library.Node}
public str java getName(node T);

@doc{Create a node given its function name and arguments}
@javaClass{org.rascalmpl.library.Node}
public node java makeNode(str N, value V...);

@doc{Read an ATerm from a named file}
@javaClass{org.rascalmpl.library.Node}
public value java readATermFromFile(str fileName);

@doc{
  Set a whole map of annotations on a value.
  
  WARNING: Note that this function may result in run-time type errors later if
  you store a value with a label that has an incomparable annotation type
  declared.
}
@javaClass{org.rascalmpl.library.Node}
public &T <: node java setAnnotations(&T <: node x, map[str, value] annotations);

@doc{remove annotation on a node}
@javaClass{org.rascalmpl.library.Node}
public &T <: node java delAnnotation(&T <: node x, str label);

@doc{removes all annotations on all nodes in a value}
@javaClass{org.rascalmpl.library.Node}
public &T delAnnotationsRec(&T v) {
  return visit(v) { case node n => delAnnotations(n) };
}

@doc{remove all annotations on a node}
@javaClass{org.rascalmpl.library.Node}
public &T <: node java delAnnotations(&T <: node x);

@doc{Convert a node to a string}
@javaClass{org.rascalmpl.library.Node}
public str java toString(node T);




