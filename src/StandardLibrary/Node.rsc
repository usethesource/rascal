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
