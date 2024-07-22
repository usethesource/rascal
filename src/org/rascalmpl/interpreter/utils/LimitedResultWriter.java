/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
import java.io.StringWriter;

public class LimitedResultWriter extends StringWriter {
	private final int limit;
	private int position;
	private boolean limitReached;
	
	public LimitedResultWriter(int limit){
		super();
		this.limit = limit;
		position = 0;
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) {
		if (position + len >= limit) {
			super.write(cbuf, off, len - (limit - position));
			limitReached = true;
			throw new IOLimitReachedException();
		}
		position += len;
		super.write(cbuf, off, len);
	}
	
	@Override
	public void write(char[] cbuf) throws IOException {
		write(cbuf, 0, cbuf.length);
	}
	
	@Override
	public void write(int b) {
		if (position + 1 >= limit) {
			limitReached = true;
			throw new IOLimitReachedException();
		}
		position++;
		super.write(b);
	}
	
	public String toString(){
		String result = super.toString();
		
		if (limitReached){
			return result + "...";
		}
		else {
			return result;
		}
	}
	
	public static class IOLimitReachedException extends RuntimeException {
		private static final long serialVersionUID = -1396788285349799099L;

		public IOLimitReachedException(){
			super("Limit reached", null, true, false);
		}
	}

	@Override
	public void close() throws IOException {
		
	}

	@Override
	public void flush() {
		
	}

	
}
