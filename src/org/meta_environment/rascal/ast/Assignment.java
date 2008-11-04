package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Assignment extends AbstractAST { 
public boolean isDefault() { return false; }
static public class Default extends Assignment {
/* "=" -> Assignment {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitAssignmentDefault(this);
	}

	public boolean isDefault() { return true; }	
}
static public class Ambiguity extends Assignment {
  private final java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Assignment> getAlternatives() {
	return alternatives;
  }
} 
public boolean isAddition() { return false; }
static public class Addition extends Assignment {
/* "+=" -> Assignment {cons("Addition")} */
	private Addition() { }
	/*package*/ Addition(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitAssignmentAddition(this);
	}

	public boolean isAddition() { return true; }	
} 
public boolean isSubstraction() { return false; }
static public class Substraction extends Assignment {
/* "-=" -> Assignment {cons("Substraction")} */
	private Substraction() { }
	/*package*/ Substraction(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitAssignmentSubstraction(this);
	}

	public boolean isSubstraction() { return true; }	
} 
public boolean isProduct() { return false; }
static public class Product extends Assignment {
/* "*=" -> Assignment {cons("Product")} */
	private Product() { }
	/*package*/ Product(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitAssignmentProduct(this);
	}

	public boolean isProduct() { return true; }	
} 
public boolean isDivision() { return false; }
static public class Division extends Assignment {
/* "/=" -> Assignment {cons("Division")} */
	private Division() { }
	/*package*/ Division(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitAssignmentDivision(this);
	}

	public boolean isDivision() { return true; }	
} 
public boolean isInteresection() { return false; }
static public class Interesection extends Assignment {
/* "&=" -> Assignment {cons("Interesection")} */
	private Interesection() { }
	/*package*/ Interesection(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitAssignmentInteresection(this);
	}

	public boolean isInteresection() { return true; }	
}
}