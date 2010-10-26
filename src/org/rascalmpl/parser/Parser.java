package org.rascalmpl.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.library.rascal.syntax.RascalRascal;

public class Parser  {
	public static final String START_COMMAND = "start__$Command";
	public static final String START_MODULE = "start__$Module";
	public static final String START_PRE_MODULE = "start__$PreModule";
	
	public IConstructor parseCommand(URI location, String command) {
		return new RascalRascal().parse(START_COMMAND, location, command);
	}

	public IConstructor parseModule(URI location,
			InputStream source, ModuleEnvironment env) throws IOException {
		return new RascalRascal().parse(START_MODULE, location, source);
	}
	  
	public IConstructor preParseModule(URI location, char[] data) {
		return new RascalRascal().parse(START_PRE_MODULE, location, data);
	}

	public IConstructor parseModule(URI location,
			char[] data, ModuleEnvironment env) {
		return new RascalRascal().parse(START_MODULE, location, data);
	}

	public IConstructor parseStream(InputStream source) {
		throw new NotYetImplemented("new rascal parser");
	}

	public IConstructor parseString(String source) {
		throw new NotYetImplemented("new rascal parser");
	}

	public IParserInfo getInfo() {
		return new RascalRascal();
	}
}
