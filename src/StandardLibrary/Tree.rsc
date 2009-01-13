module Tree

public int java arity(tree T)
@doc{arity -- number of children of a tree}
{
   return values.integer(T.arity());
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

public tree java makeTree(str N, value V...)
@doc{makeTree -- create a tree given its function name and arguments}
{
    IList argList = (IList) V;
	int len = argList.length();
	IValue args[] = new IValue[len];
	for(int i = 0; i < len; i++){
		args[i] = argList.get(i);
	}
	return values.tree(N.getValue(), args);
}

