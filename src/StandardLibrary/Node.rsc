module Node

@doc{Number of children of a node}
@javaClass{org.meta_environment.rascal.std.Node}
public int java arity(node T);

@doc{Get the children of a node}
@javaClass{org.meta_environment.rascal.std.Node}
public list[value] java getChildren(node T);

@doc{Get the function name of a node}
@javaClass{org.meta_environment.rascal.std.Node}
public str java getName(node T);

@doc{Create a node given its function name and arguments}
@javaClass{org.meta_environment.rascal.std.Node}
public node java makeNode(str N, value V...);

@doc{Read an ATerm from a named file}
@javaClass{org.meta_environment.rascal.std.Node}
public value java readATermFromFile(str fileName);
  
@doc{Convert a node to a string}
@javaClass{org.meta_environment.rascal.std.Node}
public str java toString(node T);

@doc{retrieve the annnotations of a node value as a map}
@javaClass{org.meta_environment.rascal.std.Node}
public map[str,value] java getAnnotations(node x);

@doc{
  Set a whole map of annotations on a value.
  
  WARNING: Note that this function may result in run-time type errors later if
  you store a value with a label that has an incomparable annotation type
  declared.
}
@javaClass{org.meta_environment.rascal.std.Node}
public &T <: node java setAnnotations(&T <: node x, map[str, value] annotations);
