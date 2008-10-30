package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class NoElseMayFollow extends AbstractAST { 
public class Lexical extends NoElseMayFollow {
	/*  -> NoElseMayFollow  */
}
public class Ambiguity extends NoElseMayFollow {
  private final List<NoElseMayFollow> alternatives;
  public Ambiguity(List<NoElseMayFollow> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<NoElseMayFollow> getAlternatives() {
	return alternatives;
  }
}
}