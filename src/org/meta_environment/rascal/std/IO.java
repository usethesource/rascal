package org.meta_environment.rascal.std;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.types.ConcreteSyntaxType;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.TreeAdapter;

public class IO{
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();	
	
	private volatile static PrintStream out = System.out;
	
	public static void setOutputStream(PrintStream out){
		IO.out = out;
	}

	public static void println(IValue V){
		PrintStream currentOutStream = out;
		
		synchronized(currentOutStream){
			Iterator<IValue> valueIterator = ((IList) V).iterator();
			while(valueIterator.hasNext()){
				IValue arg = valueIterator.next();
				
				if (arg.getType().isStringType()){
					currentOutStream.print(((IString) arg).getValue().toString());
				}
				else if (arg.getType().isSubtypeOf(Factory.Tree)) {
					currentOutStream.print(new TreeAdapter((IConstructor) arg).yield());
				}
				else{
					currentOutStream.print(arg.toString());
				}
			}
			currentOutStream.println();
		}
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
			throw RuntimeExceptionFactory.fileNotFound(filename, null, null);
		}
		catch (java.io.IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}

		return res;
	}
}
