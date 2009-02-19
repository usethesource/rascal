package org.meta_environment.rascal.std;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.errors.IOError;
import org.meta_environment.rascal.interpreter.errors.NoSuchFileError;

public class IO {

	private static final ValueFactory values = ValueFactory.getInstance();
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
	throws NoSuchFileError, IOError
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
	  	throw new NoSuchFileError(filename.getValue(), null);
	  }
	  catch (IOException e){
	    throw new IOError(e.getMessage(), null);
	  }

	  return res;
	}
}
