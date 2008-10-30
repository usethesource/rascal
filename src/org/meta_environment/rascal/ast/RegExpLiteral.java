package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class RegExpLiteral extends AbstractAST { 
public class Lexical extends RegExpLiteral {
	/* "/" RegExp* "/" RegExpModifier? -> RegExpLiteral  */
}
public class Ambiguity extends RegExpLiteral {
  private final List<RegExpLiteral> alternatives;
  public Ambiguity(List<RegExpLiteral> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<RegExpLiteral> getAlternatives() {
	return alternatives;
  }
}
}