package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class Assignment extends AbstractAST { 
  public boolean isDefault() { return false; }
static public class Default extends Assignment {
/** "=" -> Assignment {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentDefault(this);
	}

	public boolean isDefault() { return true; }	
}
static public class Ambiguity extends Assignment {
  private final java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
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
/** "+=" -> Assignment {cons("Addition")} */
	private Addition() {
		super();
	}
	public Addition(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentAddition(this);
	}

	public boolean isAddition() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isSubtraction() { return false; }
static public class Subtraction extends Assignment {
/** "-=" -> Assignment {cons("Subtraction")} */
	private Subtraction() {
		super();
	}
	public Subtraction(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentSubtraction(this);
	}

	public boolean isSubtraction() { return true; }	
} 
public boolean isProduct() { return false; }
static public class Product extends Assignment {
/** "*=" -> Assignment {cons("Product")} */
	private Product() {
		super();
	}
	public Product(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentProduct(this);
	}

	public boolean isProduct() { return true; }	
} 
public boolean isDivision() { return false; }
static public class Division extends Assignment {
/** "/=" -> Assignment {cons("Division")} */
	private Division() {
		super();
	}
	public Division(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentDivision(this);
	}

	public boolean isDivision() { return true; }	
} 
public boolean isIntersection() { return false; }
static public class Intersection extends Assignment {
/** "&=" -> Assignment {cons("Intersection")} */
	private Intersection() {
		super();
	}
	public Intersection(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentIntersection(this);
	}

	public boolean isIntersection() { return true; }	
} 
public boolean isIfDefined() { return false; }
static public class IfDefined extends Assignment {
/** "?=" -> Assignment {cons("IfDefined")} */
	private IfDefined() {
		super();
	}
	public IfDefined(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentIfDefined(this);
	}

	public boolean isIfDefined() { return true; }	
}
}