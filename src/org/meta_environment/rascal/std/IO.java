package org.meta_environment.rascal.std;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;


public class IO {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();	

	public static void println(IValue V)
	{
		for(IValue arg : (IList)V){
	   	  if(arg.getType().isStringType()){
	   	  	System.out.print(((IString) arg).getValue().toString());
	   	  } else {
	   		System.out.print(arg.toString());
	   	  }
	   }
	   System.out.println();
	   return;
	}

	public static IValue readFile(IString filename)
	{
		IList res = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename.getValue()));
			java.lang.String line;

			IListWriter w = types.listType(types.stringType()).writer(values);
			do {
				line = in.readLine();
				if(line != null){
					w.append(values.string(line));
				}
			} while (line != null);
			in.close();
			res =  w.done();
		}
		catch (FileNotFoundException e){
			throw RuntimeExceptionFactory.fileNotFound(filename);
		}
		catch (java.io.IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()));
		}

		return res;
	}
}
