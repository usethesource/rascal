package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Renaming extends AbstractAST { 
public class Renaming extends Renaming {
/* from:Name "=>" to:Name -> Renaming {cons("Renaming")} */
	private Renaming() { }
	/*package*/ Renaming(ITree tree, Name from, Name to) {
		this.tree = tree;
		this.from = from;
		this.to = to;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitRenamingRenaming(this);
	}
private Name from;
	public Name getfrom() { return from; }
	private void privateSetfrom(Name x) { this.from = x; }
	public Renaming setfrom(Name x) { 
		Renaming z = new Renaming();
 		z.privateSetfrom(x);
		return z;
	}
	private Name to;
	public Name getto() { return to; }
	private void privateSetto(Name x) { this.to = x; }
	public Renaming setto(Name x) { 
		Renaming z = new Renaming();
 		z.privateSetto(x);
		return z;
	}	
}
public class Ambiguity extends Renaming {
  private final List<Renaming> alternatives;
  public Ambiguity(List<Renaming> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<Renaming> getAlternatives() {
	return alternatives;
  }
}
}