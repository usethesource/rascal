package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class EscapeSequence extends AbstractAST { 
public class Lexical extends EscapeSequence {
	/* "\\" [0-7] -> EscapeSequence  */
}
public class Ambiguity extends EscapeSequence {
  private final List<EscapeSequence> alternatives;
  public Ambiguity(List<EscapeSequence> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<EscapeSequence> getAlternatives() {
	return alternatives;
  }
} 
public class Lexical extends EscapeSequence {
	/* "\\" [0-7] [0-7] -> EscapeSequence  */
} 
public class Lexical extends EscapeSequence {
	/* "\\" [0-3] [0-7] [0-7] -> EscapeSequence  */
} 
public class Lexical extends EscapeSequence {
	/* "\\" [btnfr\"\'\\\<] -> EscapeSequence  */
}
}