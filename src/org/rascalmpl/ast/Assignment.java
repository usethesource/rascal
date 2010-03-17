package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Assignment extends AbstractAST { 
  public boolean isDefault() { return false; }
static public class Default extends Assignment {
/** "=" -> Assignment {cons("Default")} */
	public Default(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }	
}
static public class Ambiguity extends Assignment {
  private final java.util.List<org.rascalmpl.ast.Assignment> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Assignment> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Assignment> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitAssignmentAmbiguity(this);
  }
} 
public boolean isAddition() { return false; }
static public class Addition extends Assignment {
/** "+=" -> Assignment {cons("Addition")} */
	public Addition(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentAddition(this);
	}

	@Override
	public boolean isAddition() { return true; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isSubtraction() { return false; }
static public class Subtraction extends Assignment {
/** "-=" -> Assignment {cons("Subtraction")} */
	public Subtraction(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentSubtraction(this);
	}

	@Override
	public boolean isSubtraction() { return true; }	
} 
public boolean isProduct() { return false; }
static public class Product extends Assignment {
/** "*=" -> Assignment {cons("Product")} */
	public Product(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentProduct(this);
	}

	@Override
	public boolean isProduct() { return true; }	
} 
public boolean isDivision() { return false; }
static public class Division extends Assignment {
/** "/=" -> Assignment {cons("Division")} */
	public Division(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentDivision(this);
	}

	@Override
	public boolean isDivision() { return true; }	
} 
public boolean isIntersection() { return false; }
static public class Intersection extends Assignment {
/** "&=" -> Assignment {cons("Intersection")} */
	public Intersection(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentIntersection(this);
	}

	@Override
	public boolean isIntersection() { return true; }	
} 
public boolean isIfDefined() { return false; }
static public class IfDefined extends Assignment {
/** "?=" -> Assignment {cons("IfDefined")} */
	public IfDefined(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignmentIfDefined(this);
	}

	@Override
	public boolean isIfDefined() { return true; }	
}
}