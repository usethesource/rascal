/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Shell{
	private final BufferedReader br;
	private final OutputStream out;
	
	public Shell(InputStream in, OutputStream out){
		super();
		
		this.br = new BufferedReader(new InputStreamReader(in));
		this.out = out;
	}
	
	public String readLine() throws IOException{
		return br.readLine();
	}
	
	public void print(String s) throws IOException{
		out.write(s.getBytes());
	}
}
