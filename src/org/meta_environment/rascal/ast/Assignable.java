package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Assignable extends AbstractAST { 
  public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() { throw new UnsupportedOperationException(); }
public boolean hasQualifiedName() { return false; }
public boolean isVariable() { return false; }
static public class Variable extends Assignable {
/* qualifiedName:QualifiedName -> Assignable {cons("Variable")} */
	private Variable() { }
	/*package*/ Variable(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) {
		this.node = node;
		this.qualifiedName = qualifiedName;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableVariable(this);
	}

	public boolean isVariable() { return true; }

	public boolean hasQualifiedName() { return true; }

private org.meta_environment.rascal.ast.QualifiedName qualifiedName;
	public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() { return qualifiedName; }
	private void $setQualifiedName(org.meta_environment.rascal.ast.QualifiedName x) { this.qualifiedName = x; }
	public Variable setQualifiedName(org.meta_environment.rascal.ast.QualifiedName x) { 
		Variable z = new Variable();
 		z.$setQualifiedName(x);
		return z;
	}	
}
static public class Ambiguity extends Assignable {
  private final java.util.List<org.meta_environment.rascal.ast.Assignable> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Assignable> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Assignable> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitAssignableAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.Assignable getReceiver() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getSubscript() { throw new UnsupportedOperationException(); } public boolean hasReceiver() { return false; } public boolean hasSubscript() { return false; }
public boolean isSubscript() { return false; }
static public class Subscript extends Assignable {
/* receiver:Assignable "[" subscript:Expression "]" -> Assignable {cons("Subscript")} */
	private Subscript() { }
	/*package*/ Subscript(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression subscript) {
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

private org.meta_environment.rascal.ast.Assignable receiver;
	public org.meta_environment.rascal.ast.Assignable getReceiver() { return receiver; }
	private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) { this.receiver = x; }
	public Subscript setReceiver(org.meta_environment.rascal.ast.Assignable x) { 
		Subscript z = new Subscript();
 		z.$setReceiver(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression subscript;
	public org.meta_environment.rascal.ast.Expression getSubscript() { return subscript; }
	private void $setSubscript(org.meta_environment.rascal.ast.Expression x) { this.subscript = x; }
	public Subscript setSubscript(org.meta_environment.rascal.ast.Expression x) { 
		Subscript z = new Subscript();
 		z.$setSubscript(x);
		return z;
	}	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.Name getField() { throw new UnsupportedOperationException(); } public boolean hasField() { return false; }
public boolean isFieldAccess() { return false; }
static public class FieldAccess extends Assignable {
/* receiver:Assignable "." field:Name -> Assignable {cons("FieldAccess")} */
	private FieldAccess() { }
	/*package*/ FieldAccess(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Name field) {
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

private org.meta_environment.rascal.ast.Assignable receiver;
	public org.meta_environment.rascal.ast.Assignable getReceiver() { return receiver; }
	private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) { this.receiver = x; }
	public FieldAccess setReceiver(org.meta_environment.rascal.ast.Assignable x) { 
		FieldAccess z = new FieldAccess();
 		z.$setReceiver(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name field;
	public org.meta_environment.rascal.ast.Name getField() { return field; }
	private void $setField(org.meta_environment.rascal.ast.Name x) { this.field = x; }
	public FieldAccess setField(org.meta_environment.rascal.ast.Name x) { 
		FieldAccess z = new FieldAccess();
 		z.$setField(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getDefaultExpression() { throw new UnsupportedOperationException(); } public boolean hasDefaultExpression() { return false; }
public boolean isIfDefinedOrDefault() { return false; }
static public class IfDefinedOrDefault extends Assignable {
/* receiver:Assignable "?" defaultExpression:Expression -> Assignable {cons("IfDefinedOrDefault")} */
	private IfDefinedOrDefault() { }
	/*package*/ IfDefinedOrDefault(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression defaultExpression) {
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

private org.meta_environment.rascal.ast.Assignable receiver;
	public org.meta_environment.rascal.ast.Assignable getReceiver() { return receiver; }
	private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) { this.receiver = x; }
	public IfDefinedOrDefault setReceiver(org.meta_environment.rascal.ast.Assignable x) { 
		IfDefinedOrDefault z = new IfDefinedOrDefault();
 		z.$setReceiver(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression defaultExpression;
	public org.meta_environment.rascal.ast.Expression getDefaultExpression() { return defaultExpression; }
	private void $setDefaultExpression(org.meta_environment.rascal.ast.Expression x) { this.defaultExpression = x; }
	public IfDefinedOrDefault setDefaultExpression(org.meta_environment.rascal.ast.Expression x) { 
		IfDefinedOrDefault z = new IfDefinedOrDefault();
 		z.$setDefaultExpression(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Name getAnnotation() { throw new UnsupportedOperationException(); } public boolean hasAnnotation() { return false; }
public boolean isAnnotation() { return false; }
static public class Annotation extends Assignable {
/* receiver:Assignable "@" annotation:Name -> Assignable {non-assoc, cons("Annotation")} */
	private Annotation() { }
	/*package*/ Annotation(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Name annotation) {
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

private org.meta_environment.rascal.ast.Assignable receiver;
	public org.meta_environment.rascal.ast.Assignable getReceiver() { return receiver; }
	private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) { this.receiver = x; }
	public Annotation setReceiver(org.meta_environment.rascal.ast.Assignable x) { 
		Annotation z = new Annotation();
 		z.$setReceiver(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name annotation;
	public org.meta_environment.rascal.ast.Name getAnnotation() { return annotation; }
	private void $setAnnotation(org.meta_environment.rascal.ast.Name x) { this.annotation = x; }
	public Annotation setAnnotation(org.meta_environment.rascal.ast.Name x) { 
		Annotation z = new Annotation();
 		z.$setAnnotation(x);
		return z;
	}	
} 
public java.util.List<org.meta_environment.rascal.ast.Assignable> getElements() { throw new UnsupportedOperationException(); }
public boolean hasElements() { return false; }
public boolean isTuple() { return false; }
static public class Tuple extends Assignable {
/* "<" elements:{Assignable ","}+ ">" -> Assignable {cons("Tuple")} */
	private Tuple() { }
	/*package*/ Tuple(INode node, java.util.List<org.meta_environment.rascal.ast.Assignable> elements) {
		this.node = node;
		this.elements = elements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableTuple(this);
	}

	public boolean isTuple() { return true; }

	public boolean hasElements() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Assignable> elements;
	public java.util.List<org.meta_environment.rascal.ast.Assignable> getElements() { return elements; }
	private void $setElements(java.util.List<org.meta_environment.rascal.ast.Assignable> x) { this.elements = x; }
	public Tuple setElements(java.util.List<org.meta_environment.rascal.ast.Assignable> x) { 
		Tuple z = new Tuple();
 		z.$setElements(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); }
	public java.util.List<org.meta_environment.rascal.ast.Assignable> getArguments() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
	public boolean hasArguments() { return false; }
public boolean isConstructor() { return false; }
static public class Constructor extends Assignable {
/* name:Name "(" arguments:{Assignable ","}+ ")" -> Assignable {non-assoc, cons("Constructor")} */
	private Constructor() { }
	/*package*/ Constructor(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.Assignable> arguments) {
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

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Constructor setName(org.meta_environment.rascal.ast.Name x) { 
		Constructor z = new Constructor();
 		z.$setName(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Assignable> arguments;
	public java.util.List<org.meta_environment.rascal.ast.Assignable> getArguments() { return arguments; }
	private void $setArguments(java.util.List<org.meta_environment.rascal.ast.Assignable> x) { this.arguments = x; }
	public Constructor setArguments(java.util.List<org.meta_environment.rascal.ast.Assignable> x) { 
		Constructor z = new Constructor();
 		z.$setArguments(x);
		return z;
	}	
}
}