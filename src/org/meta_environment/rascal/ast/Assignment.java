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
	public <T> T accept(IASTVisitor<T> visitor) {
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
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitAssignmentAmbiguity(this);
  }
} 
public boolean isAddition() { return false; }
static public class Addition extends Assignment {
/* "+=" -> Assignment {cons("Addition")} */
	private Addition() { }
	/*package*/ Addition(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentAddition(this);
	}

	public boolean isAddition() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isSubtraction() { return false; }
static public class Subtraction extends Assignment {
/* "-=" -> Assignment {cons("Subtraction")} */
	private Subtraction() { }
	/*package*/ Subtraction(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentSubtraction(this);
	}

	public boolean isSubtraction() { return true; }	
} 
public boolean isProduct() { return false; }
static public class Product extends Assignment {
/* "*=" -> Assignment {cons("Product")} */
	private Product() { }
	/*package*/ Product(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
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
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentDivision(this);
	}

	public boolean isDivision() { return true; }	
} 
public boolean isIntersection() { return false; }
static public class Intersection extends Assignment {
/* "&=" -> Assignment {cons("Intersection")} */
	private Intersection() { }
	/*package*/ Intersection(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentIntersection(this);
	}

	public boolean isIntersection() { return true; }	
}
}