package org.rascalmpl.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.library.rascal.syntax.RascalRascal;

public class Parser  {
	public static final String START_COMMAND = "start__$Command";
	public static final String START_MODULE = "start__$Module";
	public static final String START_PRE_MODULE = "start__$PreModule";
	
	private final static IParserInfo info = new RascalRascal();
	
	public IConstructor parseCommand(URI location, String command, IActionExecutor actionExecutor) {
		return new RascalRascal().parse(START_COMMAND, location, command, actionExecutor);
	}

	public IConstructor parseModule(URI location, InputStream source, IActionExecutor actionExecutor) throws IOException {
		return new RascalRascal().parse(START_MODULE, location, source, actionExecutor);
	}
	  
	public IConstructor preParseModule(URI location, char[] data, IActionExecutor actionExecutor) {
		return new RascalRascal().parse(START_PRE_MODULE, location, data, actionExecutor);
	}

	public IConstructor parseModule(URI location, char[] data, IActionExecutor actionExecutor) {
		return new RascalRascal().parse(START_MODULE, location, data, actionExecutor);
	}

	public IParserInfo getInfo() {
		return info;
	}
}
