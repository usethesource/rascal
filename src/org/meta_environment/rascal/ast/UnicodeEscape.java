package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class UnicodeEscape extends AbstractAST { 
public class Lexical extends UnicodeEscape {
	/* "\\" [u]+ [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] -> UnicodeEscape  */
}
public class Ambiguity extends UnicodeEscape {
  private final List<UnicodeEscape> alternatives;
  public Ambiguity(List<UnicodeEscape> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<UnicodeEscape> getAlternatives() {
	return alternatives;
  }
}
}