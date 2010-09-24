package org.rascalmpl.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.rascal.syntax.RascalRascal;

public class NewRascalParser implements IRascalParser {
	private static final String START_COMMAND = "start__Command";
	private static final String START_MODULE = "start__Module";
	
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

	public IConstructor parseModule(List<String> sdfSearchPath,
			Set<String> sdfImports, URI location, byte[] data,
			ModuleEnvironment env) throws IOException {
		Timing.start();
		char[] input = new char[data.length];
		for (int i = 0; i < data.length; i++) {
			input[i] = (char) data[i];
		}
		System.err.println("converting bytes to chars took " + (Timing.duration() / 1000 * 1000));
		return new RascalRascal().parse(START_MODULE, location,  input);
	}

	public IConstructor parseStream(List<String> sdfSearchPath,
			Set<String> sdfImports, InputStream source) throws IOException {
		throw new NotYetImplemented("new rascal parser");
	}

	public IConstructor parseString(List<String> sdfSearchPath,
			Set<String> sdfImports, String source) throws IOException {
		throw new NotYetImplemented("new rascal parser");
	}

}
