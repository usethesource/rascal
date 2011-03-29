package org.rascalmpl.parser.gtd;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;

public interface IGTD{
	IConstructor parse(String nonterminal, URI inputURI, char[] input, IActionExecutor actionExecutor);
	
	IConstructor parse(String nonterminal, URI inputURI, char[] input);
	
	IConstructor buildErrorTree();
}
