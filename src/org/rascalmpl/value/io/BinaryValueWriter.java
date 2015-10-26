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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.io.binary.BinaryWriter;
import org.rascalmpl.value.type.TypeStore;

/**
 * Writer for PDB Binary Files (PBF).
 * 
 * @author Arnold Lankamp
 * @deprecated binary writer currently does not support keyword parameters.
 */
public class BinaryValueWriter implements IValueBinaryWriter{
	
	/**
	 * Constructor.
	 */
	public BinaryValueWriter(){
		super();
	}
	
	/**
	 * Writes the given value to the given stream.
	 * 
	 * @param value
	 *            The value to write.
	 * @param outputStream
	 *            The output stream to write to.
	 * @throws IOException
	 *            Thrown when something goes wrong.
	 * @see IValueTextWriter#write(IValue, OutputStream)
	 */
	public void write(IValue value, OutputStream outputStream) throws IOException{
		write(value, outputStream, true);
	}
	/**
	 * Writes the given value to the given stream.
	 * 
	 * @param value
	 *            The value to write.
	 * @param outputStream
	 *            The output stream to write to.
	 * @param compression
	 * 			If we want to enable maximal sharing compression
	 * @throws IOException
	 *            Thrown when something goes wrong.
	 * @see IValueTextWriter#write(IValue, OutputStream)
	 */
	public void write(IValue value, OutputStream outputStream, boolean maximalSharing) throws IOException{
		BinaryWriter binaryWriter = new BinaryWriter(value, outputStream, maximalSharing, new TypeStore());
		binaryWriter.serialize();
		outputStream.flush();
	}
	
	/**
	 * Writes the given value to the given stream.
	 * 
	 * @param value
	 *            The value to write.
	 * @param outputStream
	 *            The output stream to write to.
	 * @param typeStore
	 *            The type store to use.
	 * @throws IOException
	 *            Thrown when something goes wrong.
	 * @see IValueTextWriter#write(IValue, OutputStream)
	 */
	public void write(IValue value, OutputStream outputStream, TypeStore typeStore) throws IOException{
		write(value, outputStream, true, typeStore);
	}
	/**
	 * Writes the given value to the given stream.
	 * 
	 * @param value
	 *            The value to write.
	 * @param outputStream
	 *            The output stream to write to.
	 * @param compression
	 * 
	 * @param typeStore
	 *            The type store to use.
	 * @throws IOException
	 *            Thrown when something goes wrong.
	 * @see IValueTextWriter#write(IValue, OutputStream)
	 */
	public void write(IValue value, OutputStream outputStream, boolean maximalSharing, TypeStore typeStore) throws IOException{
		BinaryWriter binaryWriter = new BinaryWriter(value, outputStream, maximalSharing, typeStore);
		binaryWriter.serialize();
		outputStream.flush();
	}
	
	/**
	 * Writes the given value to a file.
	 * 
	 * @param value
	 *            The value to write.
	 * @param file
	 *            The file to write to.
	 * @param typeStore
	 *            The type store to use.
	 * @throws IOException
	 *            Thrown when something goes wrong.
	           
	 */
	@Deprecated
	public static void writeValueToFile(IValue value, File file, TypeStore typeStore) throws IOException{
		OutputStream fos = null;
		try{
			fos = new BufferedOutputStream(new FileOutputStream(file));
			
			BinaryWriter binaryWriter = new BinaryWriter(value, fos, true, typeStore);
			binaryWriter.serialize();
			fos.flush();
		}finally{
			if(fos != null){
				fos.close();
			}
		}
	}
}
