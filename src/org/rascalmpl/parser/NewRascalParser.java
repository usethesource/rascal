package org.rascalmpl.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.rascal.syntax.RascalRascal;

public class NewRascalParser implements IRascalParser {
	public static final String START_COMMAND = "start__$Command";
	public static final String START_MODULE = "start__$Module";
	public static final String START_PRE_MODULE = "start__$PreModule";
	
	public IConstructor parseCommand(Set<String> sdfImports,
			List<String> sdfSearchPath, URI location, String command)
			throws IOException {
		return new RascalRascal().parse(START_COMMAND, location, command);
	}

	public IConstructor parseModule(List<String> sdfSearchPath,
			Set<String> sdfImports, URI location, InputStream source,
			ModuleEnvironment env) throws IOException {
		return new RascalRascal().parse(START_MODULE, location, source);
	}
	  
	public IConstructor preParseModule(URI location, byte[] data) throws IOException {
		return new RascalRascal().parse(START_PRE_MODULE, location, bytesToChars(data));
	}

	public IConstructor parseModule(List<String> sdfSearchPath,
			Set<String> sdfImports, URI location, byte[] data,
			ModuleEnvironment env) throws IOException {
		return new RascalRascal().parse(START_MODULE, location,  bytesToChars(data));
	}

	public static char[] bytesToChars(byte[] data) {
		Timing t = new Timing();
		t.start();
		char[] input = new char[data.length];
		for (int i = 0; i < data.length; i++) {
			input[i] = (char) data[i];
		}
		System.err.println("converting bytes to chars took " + (t.duration() / 1000 * 1000));
		return input;
	}

	public IConstructor parseStream(List<String> sdfSearchPath,
			Set<String> sdfImports, InputStream source) throws IOException {
		throw new NotYetImplemented("new rascal parser");
	}

	public IConstructor parseString(List<String> sdfSearchPath,
			Set<String> sdfImports, String source) throws IOException {
		throw new NotYetImplemented("new rascal parser");
	}

	public IParserInfo getInfo() {
		return new RascalRascal();
	}
}
