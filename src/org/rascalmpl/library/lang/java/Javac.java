/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.lang.java;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Javac{
	private final IValueFactory values;
	
	public Javac(IValueFactory values){
		super();
		
		this.values = values;
	}
	
	public IList compile(IList opts) throws Exception{
		if(!opts.getType().getElementType().isStringType()){
			throw RuntimeExceptionFactory.illegalArgument(opts, null, null);
		}
		
		
		IList list = opts;
		java.lang.String[] jargs = new java.lang.String[list.length() + 1];
		jargs[0] = "javac";
		int i = 1;
		for(IValue arg: list){
			jargs[i++] = ((IString)arg).getValue();
		}
		
		Process p = Runtime.getRuntime().exec(jargs);
		
		InputStream stderr = p.getErrorStream();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		
		int bytesRead;
		while((bytesRead = stderr.read(buffer)) != -1){
			baos.write(buffer, 0, bytesRead);
		}
		
        IListWriter writer = values.listWriter();	
        for(java.lang.String line: baos.toString().split("\n")){
        	writer.append(values.string(line));
        }
        return writer.done();
    }
}
