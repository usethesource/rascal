package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Assignable extends AbstractAST { 
  public org.rascalmpl.ast.QualifiedName getQualifiedName() { throw new UnsupportedOperationException(); }
public boolean hasQualifiedName() { return false; }
public boolean isVariable() { return false; }
static public class Variable extends Assignable {
/** qualifiedName:QualifiedName -> Assignable {cons("Variable")} */
	protected Variable(INode node, org.rascalmpl.ast.QualifiedName qualifiedName) {
		this.node = node;
		this.qualifiedName = qualifiedName;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableVariable(this);
	}

	public boolean isVariable() { return true; }

	public boolean hasQualifiedName() { return true; }

private final org.rascalmpl.ast.QualifiedName qualifiedName;
	public org.rascalmpl.ast.QualifiedName getQualifiedName() { return qualifiedName; }	
}
static public class Ambiguity extends Assignable {
  private final java.util.List<org.rascalmpl.ast.Assignable> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Assignable> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Assignable> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitAssignableAmbiguity(this);
  }
} public org.rascalmpl.ast.Assignable getReceiver() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Expression getSubscript() { throw new UnsupportedOperationException(); } public boolean hasReceiver() { return false; } public boolean hasSubscript() { return false; }
public boolean isSubscript() { return false; }
static public class Subscript extends Assignable {
/** receiver:Assignable "[" subscript:Expression "]" -> Assignable {cons("Subscript")} */
	protected Subscript(INode node, org.rascalmpl.ast.Assignable receiver, org.rascalmpl.ast.Expression subscript) {
		this.node = node;
		this.receiver = receiver;
		this.subscript = subscript;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableSubscript(this);
	}

	public boolean isSubscript() { return true; }

	public boolean hasReceiver() { return true; }
	public boolean hasSubscript() { return true; }

private final org.rascalmpl.ast.Assignable receiver;
	public org.rascalmpl.ast.Assignable getReceiver() { return receiver; }
	private final org.rascalmpl.ast.Expression subscript;
	public org.rascalmpl.ast.Expression getSubscript() { return subscript; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.Name getField() { throw new UnsupportedOperationException(); } public boolean hasField() { return false; }
public boolean isFieldAccess() { return false; }
static public class FieldAccess extends Assignable {
/** receiver:Assignable "." field:Name -> Assignable {cons("FieldAccess")} */
	protected FieldAccess(INode node, org.rascalmpl.ast.Assignable receiver, org.rascalmpl.ast.Name field) {
		this.node = node;
		this.receiver = receiver;
		this.field = field;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableFieldAccess(this);
	}

	public boolean isFieldAccess() { return true; }

	public boolean hasReceiver() { return true; }
	public boolean hasField() { return true; }

private final org.rascalmpl.ast.Assignable receiver;
	public org.rascalmpl.ast.Assignable getReceiver() { return receiver; }
	private final org.rascalmpl.ast.Name field;
	public org.rascalmpl.ast.Name getField() { return field; }	
} public org.rascalmpl.ast.Expression getDefaultExpression() { throw new UnsupportedOperationException(); } public boolean hasDefaultExpression() { return false; }
public boolean isIfDefinedOrDefault() { return false; }
static public class IfDefinedOrDefault extends Assignable {
/** receiver:Assignable "?" defaultExpression:Expression -> Assignable {cons("IfDefinedOrDefault")} */
	protected IfDefinedOrDefault(INode node, org.rascalmpl.ast.Assignable receiver, org.rascalmpl.ast.Expression defaultExpression) {
		this.node = node;
		this.receiver = receiver;
		this.defaultExpression = defaultExpression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableIfDefinedOrDefault(this);
	}

	public boolean isIfDefinedOrDefault() { return true; }

	public boolean hasReceiver() { return true; }
	public boolean hasDefaultExpression() { return true; }

private final org.rascalmpl.ast.Assignable receiver;
	public org.rascalmpl.ast.Assignable getReceiver() { return receiver; }
	private final org.rascalmpl.ast.Expression defaultExpression;
	public org.rascalmpl.ast.Expression getDefaultExpression() { return defaultExpression; }	
} public org.rascalmpl.ast.Name getAnnotation() { throw new UnsupportedOperationException(); } public boolean hasAnnotation() { return false; }
public boolean isAnnotation() { return false; }
static public class Annotation extends Assignable {
/** receiver:Assignable "@" annotation:Name -> Assignable {non-assoc, cons("Annotation")} */
	protected Annotation(INode node, org.rascalmpl.ast.Assignable receiver, org.rascalmpl.ast.Name annotation) {
		this.node = node;
		this.receiver = receiver;
		this.annotation = annotation;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableAnnotation(this);
	}

	public boolean isAnnotation() { return true; }

	public boolean hasReceiver() { return true; }
	public boolean hasAnnotation() { return true; }

private final org.rascalmpl.ast.Assignable receiver;
	public org.rascalmpl.ast.Assignable getReceiver() { return receiver; }
	private final org.rascalmpl.ast.Name annotation;
	public org.rascalmpl.ast.Name getAnnotation() { return annotation; }	
} 
public java.util.List<org.rascalmpl.ast.Assignable> getElements() { throw new UnsupportedOperationException(); }
public boolean hasElements() { return false; }
public boolean isTuple() { return false; }
static public class Tuple extends Assignable {
/** "<" elements:{Assignable ","}+ ">" -> Assignable {cons("Tuple")} */
	protected Tuple(INode node, java.util.List<org.rascalmpl.ast.Assignable> elements) {
		this.node = node;
		this.elements = elements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableTuple(this);
	}

	public boolean isTuple() { return true; }

	public boolean hasElements() { return true; }

private final java.util.List<org.rascalmpl.ast.Assignable> elements;
	public java.util.List<org.rascalmpl.ast.Assignable> getElements() { return elements; }	
} 
public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); }
	public java.util.List<org.rascalmpl.ast.Assignable> getArguments() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
	public boolean hasArguments() { return false; }
public boolean isConstructor() { return false; }
static public class Constructor extends Assignable {
/** name:Name "(" arguments:{Assignable ","}+ ")" -> Assignable {non-assoc, cons("Constructor")} */
	protected Constructor(INode node, org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.Assignable> arguments) {
		this.node = node;
		this.name = name;
		this.arguments = arguments;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableConstructor(this);
	}

	public boolean isConstructor() { return true; }

	public boolean hasName() { return true; }
	public boolean hasArguments() { return true; }

private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }
	private final java.util.List<org.rascalmpl.ast.Assignable> arguments;
	public java.util.List<org.rascalmpl.ast.Assignable> getArguments() { return arguments; }	
}
}