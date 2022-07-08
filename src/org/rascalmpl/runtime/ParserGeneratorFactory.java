package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

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
			this.generator = new ParserGenerator(rex, rex.getOutStream(), Collections.singletonList(rex.getModule().getClass().getClassLoader()), VF, new Configuration());
		}
		return generator;
	 }
}
