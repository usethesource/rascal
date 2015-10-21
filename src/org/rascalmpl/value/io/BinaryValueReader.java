/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*******************************************************************************/
package org.rascalmpl.value.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.BinaryReader;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;

/**
 * A reader for PDB Binary Files (PBF).
 * 
 * @author Arnold Lankamp
 */
public class BinaryValueReader implements IValueBinaryReader{
	
	/**
	 * Constructor.
	 */
	public BinaryValueReader(){
		super();
	}
	
	/**
	 * @see IValueBinaryReader#read(IValueFactory, InputStream)
	 */
	public IValue read(IValueFactory valueFactory, InputStream inputStream) throws IOException{
		return doRead(valueFactory, new TypeStore(), inputStream);
	}
	
	/**
	 * @see IValueBinaryReader#read(IValueFactory, Type, InputStream)
	 */
	public IValue read(IValueFactory valueFactory, Type type, InputStream inputStream) throws IOException{
		return doRead(valueFactory, new TypeStore(), inputStream);
	}
	
	/**
	 * @see IValueBinaryReader#read(IValueFactory, TypeStore, Type, InputStream)
	 */
	public IValue read(IValueFactory valueFactory, TypeStore typeStore, Type type, InputStream inputStream) throws IOException{
		return doRead(valueFactory, typeStore, inputStream);
	}
	
	private IValue doRead(IValueFactory valueFactory, TypeStore typeStore, InputStream inputStream) throws IOException{
		BinaryReader binaryReader = new BinaryReader(valueFactory, typeStore, inputStream);
		return binaryReader.deserialize();
	}
	
	/**
	 * Reads the value from the given file.
	 * 
	 * @param valueFactory
	 *            The value factory to use.
	 * @param typeStore
	 *            The typestore to use.
	 * @param file
	 *            The file to read from.
	 * @return The resulting value.
	 * @throws IOException
	 *            Thrown when something goes wrong.
	 */
	public static IValue readValueFromFile(IValueFactory valueFactory, TypeStore typeStore, File file) throws IOException{
		IValue result;
		
		InputStream fis = null;
		try{
			fis = new BufferedInputStream(new FileInputStream(file));
			
			BinaryReader binaryReader = new BinaryReader(valueFactory, typeStore, fis);
			result = binaryReader.deserialize();
		}finally{
			if(fis != null){
				fis.close();
			}
		}
		
		return result;
	}
}
