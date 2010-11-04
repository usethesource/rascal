package org.rascalmpl.parser.sgll;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.IActionExecutor;

public interface IGLL {
	public IConstructor parse(String nonterminal, URI inputURI, char[] input, IActionExecutor actionExecutor);
	public IConstructor parse(String nonterminal, URI inputURI, String input, IActionExecutor actionExecutor);
	public IConstructor parse(String nonterminal, URI inputURI, InputStream in, IActionExecutor actionExecutor) throws IOException;
	public IConstructor parse(String nonterminal, URI inputURI, Reader in, IActionExecutor actionExecutor) throws IOException;
	public IConstructor parse(String nonterminal, URI inputURI, File inputFile, IActionExecutor actionExecutor) throws IOException;
}
