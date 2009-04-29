package org.meta_environment.rascal.std;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.eclipse.imp.pdb.facts.io.PBFWriter;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;

public class ValueIO {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	
	public static IValue readValueFromBinaryFile(IString namePBFFile)
	//@doc{readValueFromBinaryFile -- read  a value from a binary file in PBF format}
	{
		java.lang.String fileName = namePBFFile.getValue();
		
		try {
			File file = new File(fileName);
			// TODO: We need to use the TypeStore from the current environment,
			// but how can we get access to it?
			return PBFReader.readValueFromFile(values, new TypeStore(), file);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		} catch (Exception e){
			e.printStackTrace();
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public static IValue readValueFromTextFile(IString namePTFFile)
	//@doc{readValueFromTextFile -- read a value from a text file}
	{
		java.lang.String fileName = namePTFFile.getValue();
		
		FileInputStream fis = null;
		try {
			File file = new File(fileName);
			fis = new FileInputStream(file);
			// TODO: We need to use the TypeStore from the current environment,
			// but how can we get access to it?
			return new StandardTextReader().read(values, fis);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(fis != null){
				try{
					fis.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	public static void writeValueToBinaryFile(IString namePBFFile, IValue value)
	//@doc{writeValueToBinaryFile -- write a value to a binary file in PBF format}
	{
		java.lang.String fileName = namePBFFile.getValue();
		
		try {
			File file = new File(fileName);
			PBFWriter.writeValueToFile(value, file);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public static void writeValueToTextFile(IString namePTFFile, IValue value)
	//@doc{writeValueToTextFile -- write a value to a binary file in PBF format}
	{
		java.lang.String fileName = namePTFFile.getValue();
		
		FileOutputStream fos = null;
		
		try {
			File file = new File(fileName);
			fos = new FileOutputStream(file);
			new StandardTextWriter().write(value, fos);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(fos != null){
				try{
					fos.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
}
