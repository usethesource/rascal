package org.rascalmpl.parser.sgll;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;

public interface IGLL {
	public IConstructor parse(String nonterminal, URI inputURI, char[] input);
	public IConstructor parse(String nonterminal, URI inputURI, String input);
	public IConstructor parse(String nonterminal, URI inputURI, InputStream in) throws IOException;
	public IConstructor parse(String nonterminal, URI inputURI, Reader in) throws IOException;
	public IConstructor parse(String nonterminal, URI inputURI, File inputFile) throws IOException;
}
