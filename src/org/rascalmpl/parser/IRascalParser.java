package org.rascalmpl.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.env.ModuleEnvironment;

public interface IRascalParser {

	/**
	 * Parse a command in the context of a number of imported concrete syntax
	 * modules
	 */
	public IConstructor parseCommand(Set<String> sdfImports,
			List<String> sdfSearchPath, URI location, String command)
			throws IOException;

	public IConstructor parseModule(List<String> sdfSearchPath,
			Set<String> sdfImports, URI location, InputStream source,
			ModuleEnvironment env) throws IOException;

	public IConstructor parseModule(List<String> sdfSearchPath,
			Set<String> sdfImports, URI location, byte[] data,
			ModuleEnvironment env) throws IOException;

	/**
	 * Parse a sentence in an object language defined by the sdfImports
	 */
	public IConstructor parseString(List<String> sdfSearchPath,
			Set<String> sdfImports, String source) throws IOException;

	/**
	 * Parse a sentence in an object language defined by the sdfImports
	 */
	public IConstructor parseStream(List<String> sdfSearchPath,
			Set<String> sdfImports, InputStream source) throws IOException;

}