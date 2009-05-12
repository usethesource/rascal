package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class OperatorAsValue extends AbstractAST { 
  public boolean isAddition() { return false; }
static public class Addition extends OperatorAsValue {
/* "#+" -> OperatorAsValue {cons("Addition")} */
	private Addition() {
		super();
	}
	/*package*/ Addition(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueAddition(this);
	}

	public boolean isAddition() { return true; }	
}
static public class Ambiguity extends OperatorAsValue {
  private final java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitOperatorAsValueAmbiguity(this);
  }
} 
public boolean isSubtraction() { return false; }
static public class Subtraction extends OperatorAsValue {
/* "#-" -> OperatorAsValue {cons("Subtraction")} */
	private Subtraction() {
		super();
	}
	/*package*/ Subtraction(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueSubtraction(this);
	}

	public boolean isSubtraction() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isProduct() { return false; }
static public class Product extends OperatorAsValue {
/* "#*" -> OperatorAsValue {cons("Product")} */
	private Product() {
		super();
	}
	/*package*/ Product(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueProduct(this);
	}

	public boolean isProduct() { return true; }	
} 
public boolean isDivision() { return false; }
static public class Division extends OperatorAsValue {
/* "#/" -> OperatorAsValue {cons("Division")} */
	private Division() {
		super();
	}
	/*package*/ Division(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueDivision(this);
	}

	public boolean isDivision() { return true; }	
} 
public boolean isIntersection() { return false; }
static public class Intersection extends OperatorAsValue {
/* "#&" -> OperatorAsValue {cons("Intersection")} */
	private Intersection() {
		super();
	}
	/*package*/ Intersection(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueIntersection(this);
	}

	public boolean isIntersection() { return true; }	
} 
public boolean isEquals() { return false; }
static public class Equals extends OperatorAsValue {
/* "#==" -> OperatorAsValue {cons("Equals")} */
	private Equals() {
		super();
	}
	/*package*/ Equals(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueEquals(this);
	}

	public boolean isEquals() { return true; }	
} 
public boolean isNotEquals() { return false; }
static public class NotEquals extends OperatorAsValue {
/* "#!=" -> OperatorAsValue {cons("NotEquals")} */
	private NotEquals() {
		super();
	}
	/*package*/ NotEquals(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueNotEquals(this);
	}

	public boolean isNotEquals() { return true; }	
} 
public boolean isLessThan() { return false; }
static public class LessThan extends OperatorAsValue {
/* "#<" -> OperatorAsValue {cons("LessThan")} */
	private LessThan() {
		super();
	}
	/*package*/ LessThan(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueLessThan(this);
	}

	public boolean isLessThan() { return true; }	
} 
public boolean isLessThanOrEq() { return false; }
static public class LessThanOrEq extends OperatorAsValue {
/* "#<=" -> OperatorAsValue {cons("LessThanOrEq")} */
	private LessThanOrEq() {
		super();
	}
	/*package*/ LessThanOrEq(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueLessThanOrEq(this);
	}

	public boolean isLessThanOrEq() { return true; }	
} 
public boolean isGreaterThan() { return false; }
static public class GreaterThan extends OperatorAsValue {
/* "#>" -> OperatorAsValue {cons("GreaterThan")} */
	private GreaterThan() {
		super();
	}
	/*package*/ GreaterThan(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueGreaterThan(this);
	}

	public boolean isGreaterThan() { return true; }	
} 
public boolean isGreaterThanOrEq() { return false; }
static public class GreaterThanOrEq extends OperatorAsValue {
/* "#>=" -> OperatorAsValue {cons("GreaterThanOrEq")} */
	private GreaterThanOrEq() {
		super();
	}
	/*package*/ GreaterThanOrEq(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueGreaterThanOrEq(this);
	}

	public boolean isGreaterThanOrEq() { return true; }	
} 
public boolean isAnd() { return false; }
static public class And extends OperatorAsValue {
/* "#&&" -> OperatorAsValue {cons("And")} */
	private And() {
		super();
	}
	/*package*/ And(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueAnd(this);
	}

	public boolean isAnd() { return true; }	
} 
public boolean isOr() { return false; }
static public class Or extends OperatorAsValue {
/* "#||" -> OperatorAsValue {cons("Or")} */
	private Or() {
		super();
	}
	/*package*/ Or(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueOr(this);
	}

	public boolean isOr() { return true; }	
} 
public boolean isNot() { return false; }
static public class Not extends OperatorAsValue {
/* "#!" -> OperatorAsValue {cons("Not")} */
	private Not() {
		super();
	}
	/*package*/ Not(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueNot(this);
	}

	public boolean isNot() { return true; }	
} 
public boolean isIn() { return false; }
static public class In extends OperatorAsValue {
/* "#in" -> OperatorAsValue {cons("In")} */
	private In() {
		super();
	}
	/*package*/ In(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueIn(this);
	}

	public boolean isIn() { return true; }	
} 
public boolean isNotIn() { return false; }
static public class NotIn extends OperatorAsValue {
/* "#notin" -> OperatorAsValue {cons("NotIn")} */
	private NotIn() {
		super();
	}
	/*package*/ NotIn(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueNotIn(this);
	}

	public boolean isNotIn() { return true; }	
}
}