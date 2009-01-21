module Node

public int java arity(node T)
@doc{arity -- number of children of a node}
{
   return values.integer(T.arity());
}

public list[value] java getChildren(node T)
@doc{getChildren -- get the children of a node}
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

public str java getName(node T)
@doc{getName -- get the function name of a node}
{
	return values.string(T.getName());
}

public node java makeNode(str N, value V...)
@doc{makeNode -- create a node given its function name and arguments}
{
    IList argList = (IList) V;
	int len = argList.length();
	IValue args[] = new IValue[len];
	for(int i = 0; i < len; i++){
		args[i] = argList.get(i);
	}
	return values.node(N.getValue(), args);
}

