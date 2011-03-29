package org.rascalmpl.parser;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.library.lang.rascal.syntax.RascalRascal;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;

public class Parser{
	public static final String START_COMMAND = "start__$Command";
	public static final String START_COMMANDS = "start__$Commands";
	public static final String START_MODULE = "start__$Module";
	public static final String START_PRE_MODULE = "start__$PreModule";
	
	private final static IParserInfo info = new RascalRascal();
	
	private Parser(){
		super();
	}
	
	public static IConstructor parseCommand(URI location, char[] command, IActionExecutor actionExecutor) {
		return new RascalRascal().parse(START_COMMAND, location, command, actionExecutor);
	}

	public static IConstructor parseCommands(URI location, char[] commands, IActionExecutor actionExecutor) {
		return new RascalRascal().parse(START_COMMANDS, location, commands, actionExecutor);
	}
	  
	public static IConstructor preParseModule(URI location, char[] data, IActionExecutor actionExecutor) {
		return new RascalRascal().parse(START_PRE_MODULE, location, data, actionExecutor);
	}

	public static IConstructor parseModule(URI location, char[] data, IActionExecutor actionExecutor) {
		return new RascalRascal().parse(START_MODULE, location, data, actionExecutor);
	}

	public static IParserInfo getInfo() {
		return info;
	}
}
