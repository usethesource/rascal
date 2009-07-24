package org.meta_environment.rascal.std;

import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class Node {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();

	public static IValue arity(INode T)
	//@doc{arity -- number of children of a node}
	{
	   return values.integer(T.arity());
	}

	public static IValue getChildren(INode T)
	//@doc{getChildren -- get the children of a node}
	{
		Type resultType = types.listType(types.valueType());
		IListWriter w = resultType.writer(values);
		
		for(IValue v : T.getChildren()){
			w.append(v);
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
		IValue args[] = new IValue[argList.length()];
		int i = 0;
		for(IValue v : argList){
			args[i++] = v;
		}
		return values.node(N.getValue(), args);
	}
	
	public static IValue readATermFromFile(IString fileName){
	//@doc{readATermFromFile -- read an ATerm from a named file}
		ATermReader atr = new ATermReader();
		try {
			FileInputStream stream = new FileInputStream(fileName.getValue());
			return atr.read(values, stream);
		} catch (FactTypeUseException e) {
			e.printStackTrace();
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		} catch (IOException e) {
			e.printStackTrace();
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);

		}
	}
}
