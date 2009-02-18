package org.meta_environment.rascal.std;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class Node {

	private static final ValueFactory values = ValueFactory.getInstance();
	private static final TypeFactory types = TypeFactory.getInstance();

	public static IValue arity(INode T)
	//@doc{arity -- number of children of a node}
	{
	   return values.integer(T.arity());
	}

	public static IValue getChildren(INode T)
	//@doc{getChildren -- get the children of a node}
	{
		Iterator<IValue> iter = T.getChildren().iterator();
		Type resultType = types.listType(types.valueType());
		IListWriter w = resultType.writer(values);
		
		while(iter.hasNext()){
			w.append((IValue)iter.next());
		}
		return w.done();
	}

	public static IValue getName(INode T)
	//@doc{getName -- get the function name of a node}
	{
		return values.string(T.getName());
	}

	public static IValue makeNode(IString N, IValue V)
	//@doc{makeNode -- create a node given its function name and arguments}
	{
	    IList argList = (IList) V;
		int len = argList.length();
		IValue args[] = new IValue[len];
		for(int i = 0; i < len; i++){
			args[i] = argList.get(i);
		}
		return values.node(N.getValue(), args);
	}

}
