package org.meta_environment.rascal.std;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class ATermIO {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	
	public static IValue readATermFromTextFile(IString sfileName)
	//@doc{readValueFromTextFile -- read a value from a text file}
	{
			java.lang.String fileName = sfileName.getValue();
			
			try {
				File file = new File(fileName);
				// TODO: We need to use the TypeStore from the current environment,
				// but how can we get access to it?
				return new ATermReader().read(values, new FileInputStream(file));
			} catch (IOException e) {
				throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
			} catch (Exception e){
				e.printStackTrace();
				throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
			}
	}
}
