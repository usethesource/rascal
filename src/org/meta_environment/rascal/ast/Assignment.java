package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Assignment extends AbstractAST { 
public class Default extends Assignment {
/* "=" -> Assignment {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitDefaultAssignment(this);
	}	
}
public class Ambiguity extends Assignment {
  private final List<Assignment> alternatives;
  public Ambiguity(List<Assignment> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<Assignment> getAlternatives() {
	return alternatives;
  }
} 
public class Addition extends Assignment {
/* "+=" -> Assignment {cons("Addition")} */
	private Addition() { }
	/*package*/ Addition(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitAdditionAssignment(this);
	}	
} 
public class Substraction extends Assignment {
/* "-=" -> Assignment {cons("Substraction")} */
	private Substraction() { }
	/*package*/ Substraction(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitSubstractionAssignment(this);
	}	
} 
public class Product extends Assignment {
/* "*=" -> Assignment {cons("Product")} */
	private Product() { }
	/*package*/ Product(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitProductAssignment(this);
	}	
} 
public class Division extends Assignment {
/* "/=" -> Assignment {cons("Division")} */
	private Division() { }
	/*package*/ Division(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitDivisionAssignment(this);
	}	
} 
public class Interesection extends Assignment {
/* "&=" -> Assignment {cons("Interesection")} */
	private Interesection() { }
	/*package*/ Interesection(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitInteresectionAssignment(this);
	}	
}
}