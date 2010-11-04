package org.rascalmpl.parser;

import org.eclipse.imp.pdb.facts.IConstructor;

public interface IActionExecutor{
	IConstructor filterAppl(IConstructor tree);
}
