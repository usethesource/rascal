package org.rascalmpl.parser;

import org.rascalmpl.library.lang.rascal.syntax.RascalRascal;

public class Parser{
	public static final String START_COMMAND = "start__$Command";
	public static final String START_COMMANDS = "start__$Commands";
	public static final String START_MODULE = "start__$Module";
	public static final String START_PRE_MODULE = "start__$PreModule";
	
	private final static IParserInfo info = new RascalRascal();
	
	private Parser(){
		super();
	}

	public static IParserInfo getInfo() {
		return info;
	}
}
