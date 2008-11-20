package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Assignable extends AbstractAST { 
  public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() { throw new UnsupportedOperationException(); }
public boolean hasQualifiedName() { return false; }
public boolean isVariable() { return false; }
static public class Variable extends Assignable {
/* qualifiedName:QualifiedName -> Assignable {cons("Variable")} */
	private Variable() { }
	/*package*/ Variable(ITree tree, org.meta_environment.rascal.ast.QualifiedName qualifiedName) {
		this.tree = tree;
		this.qualifiedName = qualifiedName;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableVariable(this);
	}

	@Override
	public boolean isVariable() { return true; }

	@Override
	public boolean hasQualifiedName() { return true; }

private org.meta_environment.rascal.ast.QualifiedName qualifiedName;
	@Override
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
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Assignable> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Assignable> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitAssignableAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.Assignable getReceiver() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getSubscript() { throw new UnsupportedOperationException(); } public boolean hasReceiver() { return false; } public boolean hasSubscript() { return false; }
public boolean isSubscript() { return false; }
static public class Subscript extends Assignable {
/* receiver:Assignable "[" subscript:Expression "]" -> Assignable {cons("Subscript")} */
	private Subscript() { }
	/*package*/ Subscript(ITree tree, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression subscript) {
		this.tree = tree;
		this.receiver = receiver;
		this.subscript = subscript;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableSubscript(this);
	}

	@Override
	public boolean isSubscript() { return true; }

	@Override
	public boolean hasReceiver() { return true; }
	@Override
	public boolean hasSubscript() { return true; }

private org.meta_environment.rascal.ast.Assignable receiver;
	@Override
	public org.meta_environment.rascal.ast.Assignable getReceiver() { return receiver; }
	private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) { this.receiver = x; }
	public Subscript setReceiver(org.meta_environment.rascal.ast.Assignable x) { 
		Subscript z = new Subscript();
 		z.$setReceiver(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression subscript;
	@Override
	public org.meta_environment.rascal.ast.Expression getSubscript() { return subscript; }
	private void $setSubscript(org.meta_environment.rascal.ast.Expression x) { this.subscript = x; }
	public Subscript setSubscript(org.meta_environment.rascal.ast.Expression x) { 
		Subscript z = new Subscript();
 		z.$setSubscript(x);
		return z;
	}	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.Name getField() { throw new UnsupportedOperationException(); } public boolean hasField() { return false; }
public boolean isFieldAccess() { return false; }
static public class FieldAccess extends Assignable {
/* receiver:Assignable "." field:Name -> Assignable {cons("FieldAccess")} */
	private FieldAccess() { }
	/*package*/ FieldAccess(ITree tree, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Name field) {
		this.tree = tree;
		this.receiver = receiver;
		this.field = field;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableFieldAccess(this);
	}

	@Override
	public boolean isFieldAccess() { return true; }

	@Override
	public boolean hasReceiver() { return true; }
	@Override
	public boolean hasField() { return true; }

private org.meta_environment.rascal.ast.Assignable receiver;
	@Override
	public org.meta_environment.rascal.ast.Assignable getReceiver() { return receiver; }
	private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) { this.receiver = x; }
	public FieldAccess setReceiver(org.meta_environment.rascal.ast.Assignable x) { 
		FieldAccess z = new FieldAccess();
 		z.$setReceiver(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name field;
	@Override
	public org.meta_environment.rascal.ast.Name getField() { return field; }
	private void $setField(org.meta_environment.rascal.ast.Name x) { this.field = x; }
	public FieldAccess setField(org.meta_environment.rascal.ast.Name x) { 
		FieldAccess z = new FieldAccess();
 		z.$setField(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getCondition() { throw new UnsupportedOperationException(); } public boolean hasCondition() { return false; }
public boolean isIfDefined() { return false; }
static public class IfDefined extends Assignable {
/* receiver:Assignable "?" condition:Expression -> Assignable {cons("IfDefined")} */
	private IfDefined() { }
	/*package*/ IfDefined(ITree tree, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression condition) {
		this.tree = tree;
		this.receiver = receiver;
		this.condition = condition;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableIfDefined(this);
	}

	@Override
	public boolean isIfDefined() { return true; }

	@Override
	public boolean hasReceiver() { return true; }
	@Override
	public boolean hasCondition() { return true; }

private org.meta_environment.rascal.ast.Assignable receiver;
	@Override
	public org.meta_environment.rascal.ast.Assignable getReceiver() { return receiver; }
	private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) { this.receiver = x; }
	public IfDefined setReceiver(org.meta_environment.rascal.ast.Assignable x) { 
		IfDefined z = new IfDefined();
 		z.$setReceiver(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression condition;
	@Override
	public org.meta_environment.rascal.ast.Expression getCondition() { return condition; }
	private void $setCondition(org.meta_environment.rascal.ast.Expression x) { this.condition = x; }
	public IfDefined setCondition(org.meta_environment.rascal.ast.Expression x) { 
		IfDefined z = new IfDefined();
 		z.$setCondition(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getAnnotation() { throw new UnsupportedOperationException(); } public boolean hasAnnotation() { return false; }
public boolean isAnnotation() { return false; }
static public class Annotation extends Assignable {
/* receiver:Assignable "@" annotation:Expression -> Assignable {cons("Annotation")} */
	private Annotation() { }
	/*package*/ Annotation(ITree tree, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression annotation) {
		this.tree = tree;
		this.receiver = receiver;
		this.annotation = annotation;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableAnnotation(this);
	}

	@Override
	public boolean isAnnotation() { return true; }

	@Override
	public boolean hasReceiver() { return true; }
	@Override
	public boolean hasAnnotation() { return true; }

private org.meta_environment.rascal.ast.Assignable receiver;
	@Override
	public org.meta_environment.rascal.ast.Assignable getReceiver() { return receiver; }
	private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) { this.receiver = x; }
	public Annotation setReceiver(org.meta_environment.rascal.ast.Assignable x) { 
		Annotation z = new Annotation();
 		z.$setReceiver(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression annotation;
	@Override
	public org.meta_environment.rascal.ast.Expression getAnnotation() { return annotation; }
	private void $setAnnotation(org.meta_environment.rascal.ast.Expression x) { this.annotation = x; }
	public Annotation setAnnotation(org.meta_environment.rascal.ast.Expression x) { 
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
	/*package*/ Tuple(ITree tree, java.util.List<org.meta_environment.rascal.ast.Assignable> elements) {
		this.tree = tree;
		this.elements = elements;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableTuple(this);
	}

	@Override
	public boolean isTuple() { return true; }

	@Override
	public boolean hasElements() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Assignable> elements;
	@Override
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
/* name:Name "(" arguments:{Assignable ","}+ ")" -> Assignable {cons("Constructor")} */
	private Constructor() { }
	/*package*/ Constructor(ITree tree, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.Assignable> arguments) {
		this.tree = tree;
		this.name = name;
		this.arguments = arguments;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssignableConstructor(this);
	}

	@Override
	public boolean isConstructor() { return true; }

	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasArguments() { return true; }

private org.meta_environment.rascal.ast.Name name;
	@Override
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Constructor setName(org.meta_environment.rascal.ast.Name x) { 
		Constructor z = new Constructor();
 		z.$setName(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Assignable> arguments;
	@Override
	public java.util.List<org.meta_environment.rascal.ast.Assignable> getArguments() { return arguments; }
	private void $setArguments(java.util.List<org.meta_environment.rascal.ast.Assignable> x) { this.arguments = x; }
	public Constructor setArguments(java.util.List<org.meta_environment.rascal.ast.Assignable> x) { 
		Constructor z = new Constructor();
 		z.$setArguments(x);
		return z;
	}	
}
}