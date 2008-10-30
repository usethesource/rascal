package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Backslash extends AbstractAST { 
public class Lexical extends Backslash {
	/* [\\] -> Backslash  */
}
public class Ambiguity extends Backslash {
  private final List<Backslash> alternatives;
  public Ambiguity(List<Backslash> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<Backslash> getAlternatives() {
	return alternatives;
  }
}
}