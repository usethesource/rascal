/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.experiments.resource.results.buffers;

import java.util.Iterator;

import org.rascalmpl.value.IValue;

	public class LazyIterator implements Iterator<IValue> {
		
		private IValue buffer[] = null;
		private int currentPos = 0;
		private boolean atEnd = false;
		private ILazyFiller filler = null;
		private int bufferSize = 0;
		
		public LazyIterator(ILazyFiller filler, int bufferSize) {
			this.bufferSize = bufferSize;
			this.filler = filler;
		}

		public void init() {
			refillBuffer();
		}
		
		private void refillBuffer() {
			buffer = filler.refill(bufferSize);
			if (buffer.length == 0) {
				this.atEnd = true;
			}
			this.currentPos = 0;
		}
		
		@Override
		public boolean hasNext() {
			return !atEnd;
		}

		@Override
		public IValue next() {
			IValue nextValue = buffer[currentPos++];
			if (currentPos >= buffer.length) {
				refillBuffer();
			}
			return nextValue;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove elements from the underlying database table or query");
		}
		
	}
