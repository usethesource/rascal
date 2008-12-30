module Tree

public int java arity(tree T)
@doc{arity -- number of children of a tree}
{
   return values.integer(T.arity());
}

public value java getChild(tree T, int n)
@doc{getChild -- get n-th child of a tree}
@java-imports{import java.util.Iterator;}
{
	Iterator iter = T.getChildren().iterator();
	int i = 0;
	int nval = n.getValue();
	
	if(nval > T.arity()){
		throw new RascalException(values, "child index out of bounds");
	}
	
	while(iter.hasNext()){
	    IValue val = (IValue) iter.next();
		if(i == nval){
			return val;
		}
		i++;
	}
	return null;
}

public list[value] java getChildren(tree T)
@doc{getChildren -- get the children of a tree}
@java-imports{import java.util.Iterator;}
{
	Iterator iter = T.getChildren().iterator();
	Type resultType = types.listType(types.valueType());
	IListWriter w = resultType.writer(values);
	
	while(iter.hasNext()){
		w.append((IValue)iter.next());
	}
	return w.done();
}

public str java getName(tree T)
@doc{getName -- get the function name of a tree}
{
	return values.string(T.getName());
}
/*
TODO: wait for implementation of varargs in java functions
public tree java makeTree(str N, value V...)
{
	int len = V.length;
	IValue args[] = new IValue[len];
	for(int i = 0; i < len; i++){
		args[i] = V[i];
	}
	return values.tree(N.getValue(), args);
}
*/

public tree java setChild(tree T, int n, value V)
@doc{setChild -- replace n-th child of given tree; returns the modified tree}
{
	return T.set(n.getValue(), V);
}

