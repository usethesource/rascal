package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class CommentChar extends AbstractAST { 
public class Lexical extends CommentChar {
	/* ~[\*] -> CommentChar  */
}
public class Ambiguity extends CommentChar {
  private final List<CommentChar> alternatives;
  public Ambiguity(List<CommentChar> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<CommentChar> getAlternatives() {
	return alternatives;
  }
} 
public class Lexical extends CommentChar {
	/* Asterisk -> CommentChar  */
}
}