/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import java.io.IOException;
import java.io.OutputStream;

public class LimitedResultOutputStream extends OutputStream{
	private final int limit;
	private final char[] data;
	private int position;
	private boolean limitReached;
	
	public LimitedResultOutputStream(int limit){
		super();
		
		this.limit = limit;
		data = new char[limit];
		position = 0;
		limitReached = false;
	}
	
	public void write(int b) throws IOException{
		if(limit == position){
			limitReached = true;
			throw new IOLimitReachedException();
		}
		
		data[position++] = (char) b;
	}
	
	public String toString(){
		if(limitReached){
			int length = data.length;
			data[length - 3] = '.';
			data[length - 2] = '.';
			data[length - 1] = '.';
		}
		
		return new String(data, 0, position);
	}
	
	public static class IOLimitReachedException extends IOException{
		private static final long serialVersionUID = -1396788285349799099L;

		public IOLimitReachedException(){
			super("Limit reached");
		}
	}
}
