/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.lang.jvm.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

public class BinaryClassLoader extends ClassLoader {
	private final List<ClassLoader> parents;

	public BinaryClassLoader(List<ClassLoader> parents) {
		this.parents = parents;
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		return delegate(name);
	}
	
	@Override
	public synchronized Class<?> loadClass(String name) throws ClassNotFoundException {
		return delegate(name);
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return delegate(name);
	}
	
	private Class<?> delegate(String name) throws ClassNotFoundException {
		for (ClassLoader loader : parents) {
			try {
				return loader.loadClass(name);
			}
			catch (ClassNotFoundException e) {
				// continue
			}
		}
		
		throw new ClassNotFoundException(name);
	}
	
	public Class<?> defineClass(URI classLocation) throws IOException{
		byte[] classBytes = getBytesFromFile(classLocation);
		return defineClass(null,classBytes,0,classBytes.length);
	}
	
	public static byte[] getBytesFromFile(URI fileLocation) throws IOException {
		File file = new File(fileLocation);
		InputStream is = new FileInputStream(file);
		try {
			long length = file.length();

			// You cannot create an array using a long type.
			// It needs to be an int type.
			// Before converting to an int type, check
			// to ensure that file is not larger than Integer.MAX_VALUE.
			if (length > Integer.MAX_VALUE) {
				throw new IOException("File is too large to be read into byte array!" + file.getName());
			}
			byte[] bytes = new byte[(int)length];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length) {
				numRead=is.read(bytes, offset, bytes.length-offset);
				if(numRead == 0) {
					throw new IOException("Could not completely read file "+file.getName());
				}
				offset += numRead;
			}

			return bytes;
		}
		finally {
			is.close();
		}
	}
	
}
