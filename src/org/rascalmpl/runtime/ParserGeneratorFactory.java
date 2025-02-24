/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
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
package org.rascalmpl.runtime;

import java.util.Collections;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.values.RascalValueFactory;

public class ParserGeneratorFactory {
	private @MonotonicNonNull ParserGenerator generator;
	
	private static RascalExecutionContext rex;
	
	private static class InstanceHolder {
	    public static final ParserGeneratorFactory sInstance = new ParserGeneratorFactory();
	}
	public static ParserGeneratorFactory getInstance(RascalExecutionContext arex) {
		rex = arex;
	    return InstanceHolder.sInstance;
	}
	
	public ParserGenerator getParserGenerator(RascalValueFactory VF) {
		if (this.generator == null) {
			this.generator = new ParserGenerator(rex, rex.getOutWriter(), Collections.singletonList(rex.getModule().getClass().getClassLoader()), VF, new Configuration());
		}
		return generator;
	 }
}
