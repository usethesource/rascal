package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Assignable extends AbstractAST { 
public class Variable extends Assignable {
/* qualifiedName:QualifiedName -> Assignable {cons("Variable")} */
	private Variable() { }
	/*package*/ Variable(ITree tree, QualifiedName qualifiedName) {
		this.tree = tree;
		this.qualifiedName = qualifiedName;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitVariableAssignable(this);
	}
private QualifiedName qualifiedName;
	public QualifiedName getqualifiedName() { return qualifiedName; }
	private void privateSetqualifiedName(QualifiedName x) { this.qualifiedName = x; }
	public Variable setqualifiedName(QualifiedName x) { 
		Variable z = new Variable();
 		z.privateSetqualifiedName(x);
		return z;
	}	
}
public class Ambiguity extends Assignable {
  private final List<Assignable> alternatives;
  public Ambiguity(List<Assignable> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<Assignable> getAlternatives() {
	return alternatives;
  }
} 
public class Subscript extends Assignable {
/* receiver:Assignable "[" subscript:Expression "]" -> Assignable {cons("Subscript")} */
	private Subscript() { }
	/*package*/ Subscript(ITree tree, Assignable receiver, Expression subscript) {
		this.tree = tree;
		this.receiver = receiver;
		this.subscript = subscript;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitSubscriptAssignable(this);
	}
private Assignable receiver;
	public Assignable getreceiver() { return receiver; }
	private void privateSetreceiver(Assignable x) { this.receiver = x; }
	public Subscript setreceiver(Assignable x) { 
		Subscript z = new Subscript();
 		z.privateSetreceiver(x);
		return z;
	}
	private Expression subscript;
	public Expression getsubscript() { return subscript; }
	private void privateSetsubscript(Expression x) { this.subscript = x; }
	public Subscript setsubscript(Expression x) { 
		Subscript z = new Subscript();
 		z.privateSetsubscript(x);
		return z;
	}	
} 
public class FieldAccess extends Assignable {
/* receiver:Assignable "." field:Name -> Assignable {cons("FieldAccess")} */
	private FieldAccess() { }
	/*package*/ FieldAccess(ITree tree, Assignable receiver, Name field) {
		this.tree = tree;
		this.receiver = receiver;
		this.field = field;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitFieldAccessAssignable(this);
	}
private Assignable receiver;
	public Assignable getreceiver() { return receiver; }
	private void privateSetreceiver(Assignable x) { this.receiver = x; }
	public FieldAccess setreceiver(Assignable x) { 
		FieldAccess z = new FieldAccess();
 		z.privateSetreceiver(x);
		return z;
	}
	private Name field;
	public Name getfield() { return field; }
	private void privateSetfield(Name x) { this.field = x; }
	public FieldAccess setfield(Name x) { 
		FieldAccess z = new FieldAccess();
 		z.privateSetfield(x);
		return z;
	}	
} 
public class IfDefined extends Assignable {
/* receiver:Assignable "?" condition:Expression -> Assignable {cons("IfDefined")} */
	private IfDefined() { }
	/*package*/ IfDefined(ITree tree, Assignable receiver, Expression condition) {
		this.tree = tree;
		this.receiver = receiver;
		this.condition = condition;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitIfDefinedAssignable(this);
	}
private Assignable receiver;
	public Assignable getreceiver() { return receiver; }
	private void privateSetreceiver(Assignable x) { this.receiver = x; }
	public IfDefined setreceiver(Assignable x) { 
		IfDefined z = new IfDefined();
 		z.privateSetreceiver(x);
		return z;
	}
	private Expression condition;
	public Expression getcondition() { return condition; }
	private void privateSetcondition(Expression x) { this.condition = x; }
	public IfDefined setcondition(Expression x) { 
		IfDefined z = new IfDefined();
 		z.privateSetcondition(x);
		return z;
	}	
} 
public class Annotation extends Assignable {
/* receiver:Assignable "@" annotation:Expression -> Assignable {cons("Annotation")} */
	private Annotation() { }
	/*package*/ Annotation(ITree tree, Assignable receiver, Expression annotation) {
		this.tree = tree;
		this.receiver = receiver;
		this.annotation = annotation;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitAnnotationAssignable(this);
	}
private Assignable receiver;
	public Assignable getreceiver() { return receiver; }
	private void privateSetreceiver(Assignable x) { this.receiver = x; }
	public Annotation setreceiver(Assignable x) { 
		Annotation z = new Annotation();
 		z.privateSetreceiver(x);
		return z;
	}
	private Expression annotation;
	public Expression getannotation() { return annotation; }
	private void privateSetannotation(Expression x) { this.annotation = x; }
	public Annotation setannotation(Expression x) { 
		Annotation z = new Annotation();
 		z.privateSetannotation(x);
		return z;
	}	
} 
public class Tuple extends Assignable {
/* "<" first:Assignable "," rest:{Assignable ","}+ ">" -> Assignable {cons("Tuple")} */
	private Tuple() { }
	/*package*/ Tuple(ITree tree, Assignable first, List<Assignable> rest) {
		this.tree = tree;
		this.first = first;
		this.rest = rest;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitTupleAssignable(this);
	}
private Assignable first;
	public Assignable getfirst() { return first; }
	private void privateSetfirst(Assignable x) { this.first = x; }
	public Tuple setfirst(Assignable x) { 
		Tuple z = new Tuple();
 		z.privateSetfirst(x);
		return z;
	}
	private List<Assignable> rest;
	public List<Assignable> getrest() { return rest; }
	private void privateSetrest(List<Assignable> x) { this.rest = x; }
	public Tuple setrest(List<Assignable> x) { 
		Tuple z = new Tuple();
 		z.privateSetrest(x);
		return z;
	}	
} 
public class Constructor extends Assignable {
/* name:Name "(" arguments:{Assignable ","}+ ")" -> Assignable {cons("Constructor")} */
	private Constructor() { }
	/*package*/ Constructor(ITree tree, Name name, List<Assignable> arguments) {
		this.tree = tree;
		this.name = name;
		this.arguments = arguments;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitConstructorAssignable(this);
	}
private Name name;
	public Name getname() { return name; }
	private void privateSetname(Name x) { this.name = x; }
	public Constructor setname(Name x) { 
		Constructor z = new Constructor();
 		z.privateSetname(x);
		return z;
	}
	private List<Assignable> arguments;
	public List<Assignable> getarguments() { return arguments; }
	private void privateSetarguments(List<Assignable> x) { this.arguments = x; }
	public Constructor setarguments(List<Assignable> x) { 
		Constructor z = new Constructor();
 		z.privateSetarguments(x);
		return z;
	}	
}
}