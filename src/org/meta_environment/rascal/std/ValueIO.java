package org.meta_environment.rascal.std;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.eclipse.imp.pdb.facts.io.PBFWriter;
import org.eclipse.imp.pdb.facts.io.binary.BinaryReader;
import org.eclipse.imp.pdb.facts.io.binary.BinaryWriter;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;

public class ValueIO {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();
	
	public static IValue readValueFromBinaryFile(IString namePBFFile)
	//@doc{readValueFromBinaryFile -- read  a value from a binary file in PBF format}
	{
		java.lang.String fileName = namePBFFile.getValue();
		
		try {
			File file = new File(fileName);
			InputStream instream = new FileInputStream(file);
			// TODO: We need to use the TypeStore from the current environment,
			// but how can we get access to it?
			IValue result =  new BinaryReader(values, new TypeStore(), instream).deserialize();
			instream.close();
			return result;
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null);
		} catch (Exception e){
			e.printStackTrace();
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null);
		}
	}
	
	public static IValue readValueFromTextFile(IString namePBFFile)
	//@doc{readValueFromTextFile -- read a value from a text file}
	{
		java.lang.String fileName = namePBFFile.getValue();
		
		try {
			File file = new File(fileName);
			// TODO: We need to use the TypeStore from the current environment,
			// but how can we get access to it?
			return PBFReader.readValueFromFile(values, new TypeStore(), file);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null);
		}
	}
	
	public static void writeValueToBinaryFile(IString namePBFFile, IValue value)
	//@doc{writeValueToBinaryFile -- write a value to a binary file in PBF format}
	{
		java.lang.String fileName = namePBFFile.getValue();
		
		try {
			File file = new File(fileName);
			OutputStream outstream = new FileOutputStream(file);
			new BinaryWriter(value, outstream).serialize();
			outstream.close();
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null);
		}
	}
	
	public static void writeValueToTextFile(IString namePBFFile, IValue value)
	//@doc{writeValueToTextFile -- write a value to a binary file in PBF format}
	{
		java.lang.String fileName = namePBFFile.getValue();
		
		try {
			File file = new File(fileName);
			PBFWriter.writeValueToFile(value, file);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null);
		}
	}
}
