package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class ShortChar extends AbstractAST { 
public class Regular extends ShortChar {
/* character:[a-zA-Z0-9] -> ShortChar {cons("Regular")} */
	private Regular() { }
	/*package*/ Regular(ITree tree, List<get-sort-from-symbol([a-zA-Z0-9])> character) {
		this.tree = tree;
		this.character = character;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitRegularShortChar(this);
	}
private List<get-sort-from-symbol([a-zA-Z0-9])> character;
	public List<get-sort-from-symbol([a-zA-Z0-9])> getcharacter() { return character; }
	private void privateSetcharacter(List<get-sort-from-symbol([a-zA-Z0-9])> x) { this.character = x; }
	public Regular setcharacter(List<get-sort-from-symbol([a-zA-Z0-9])> x) { 
		Regular z = new Regular();
 		z.privateSetcharacter(x);
		return z;
	}	
}
public class Ambiguity extends ShortChar {
  private final List<ShortChar> alternatives;
  public Ambiguity(List<ShortChar> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<ShortChar> getAlternatives() {
	return alternatives;
  }
} 
public class Escaped extends ShortChar {
/* [\\] escape:~[\0-\31A-Za-mo-qsu-z0-9] -> ShortChar {cons("Escaped")} */
	private Escaped() { }
	/*package*/ Escaped(ITree tree, List<get-sort-from-symbol(~[\0-\31A-Za-mo-qsu-z0-9])> escape) {
		this.tree = tree;
		this.escape = escape;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitEscapedShortChar(this);
	}
private List<get-sort-from-symbol(~[\0-\31A-Za-mo-qsu-z0-9])> escape;
	public List<get-sort-from-symbol(~[\0-\31A-Za-mo-qsu-z0-9])> getescape() { return escape; }
	private void privateSetescape(List<get-sort-from-symbol(~[\0-\31A-Za-mo-qsu-z0-9])> x) { this.escape = x; }
	public Escaped setescape(List<get-sort-from-symbol(~[\0-\31A-Za-mo-qsu-z0-9])> x) { 
		Escaped z = new Escaped();
 		z.privateSetescape(x);
		return z;
	}	
}
}