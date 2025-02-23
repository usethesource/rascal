/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils;

import java.util.Iterator;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class SubSetGenerator implements Iterable<ISet> {
	private ISet subject;
	
	public SubSetGenerator(ISet s){
		subject = s;
	}

	@Override
	public Iterator<ISet> iterator() {
		return new SubSetIterator(subject);
	}
}

/*
 * For a set with n elements (n < 64), enumerate the numbers 0 .. n and use their binary representation
 * to determine which elements to include in the next subset.
 */
class SubSetIterator implements Iterator<ISet> {
	private final static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	private long n;
	private final int len;
	private final long max;
	private final IValue[] setElems;
	
	SubSetIterator(ISet s){
		len = s.size();
		if(len >= 64) {
			throw RuntimeExceptionFactory.illegalArgument(s, "SubSetIterator can only handle sets with less than 64 elements");
		}
		n = 0;
		max = (1L << len);
		setElems = new IValue[len];
		int i = 0;
		for(IValue elm : s){
			setElems[i++] = elm;
		}
	}

	@Override
	public boolean hasNext() {
		return n < max;
	}

	@Override
	public ISet next() {
		ISetWriter w = $VF.setWriter();
		for (int j = 0; j < len; j++) {
			// (1L<<j) is a number with jth bit 1 so when we 'and' them with the subset number 
			// we get which numbers are present in the subset and which are not 
			if ((n & (1L << j)) > 0) 
				w.insert(setElems[j]);
		}
		n++;
		return w.done();
	}
}
