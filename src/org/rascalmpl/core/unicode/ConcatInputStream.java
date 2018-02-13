/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Davy Landman  - Davy.Landman@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.core.unicode;

import java.io.IOException;
import java.io.InputStream;

class ConcatInputStream extends InputStream {
	private final InputStream first;
	private boolean firstEmpty;
	private final InputStream second;

	public ConcatInputStream(InputStream first, InputStream second) {
		this.first = first;
		this.firstEmpty = false;
		this.second = second;
	}

	@Override
	public int read() throws IOException {
		if (!firstEmpty) {
			int firstResult = first.read();
			if (firstResult != -1)  {
				return firstResult;
			} 
			else {
				firstEmpty = true;
			}
		}
		return second.read();
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		// not strictly needed, but can speedup the concatting
		if (!firstEmpty) {
			int readTotal = first.read(b, off, len);
			if (readTotal == len) {
				return readTotal;
			}
			else {
				if (readTotal < 0)
					readTotal = 0;
				firstEmpty = true;
				int secondRead = second.read(b, off + readTotal, len - readTotal);
				if (readTotal == 0 && secondRead <= 0) {
					return -1;
				}
				if (secondRead < 0)
					return readTotal;
				else
					return readTotal + secondRead;
			}
			
		}
		return second.read(b, off, len);
	}
	
	@Override
	public void close() throws IOException {
		try {
			first.close();
		}
		catch (IOException e) {
			// yes, but also close the second
			second.close();
			throw e;
		}
		second.close();
	}
	
}