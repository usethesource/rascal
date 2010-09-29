package org.rascalmpl.parser;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.ast.LanguageAction;

public interface IParserInfo {
  LanguageAction getAction(IConstructor prod);
}
