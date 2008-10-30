package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Name extends AbstractAST { 
public class Lexical extends Name {
	/* [\\]? [A-Za-z\_] [A-Za-z0-9\_\-]* -> Name  */
}
public class Ambiguity extends Name {
  private final List<Name> alternatives;
  public Ambiguity(List<Name> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<Name> getAlternatives() {
	return alternatives;
  }
}
}