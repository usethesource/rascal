package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Assignable extends AbstractAST
{
  public class Variable extends Assignable
  {
/* qualifiedName:QualifiedName -> Assignable {cons("Variable")} */
    private Variable ()
    {
    }
    /*package */ Variable (ITree tree, QualifiedName qualifiedName)
    {
      this.tree = tree;
      this.qualifiedName = qualifiedName;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVariableAssignable (this);
    }
    private QualifiedName qualifiedName;
    public QualifiedName getqualifiedName ()
    {
      return qualifiedName;
    }
    private void privateSetqualifiedName (QualifiedName x)
    {
      this.qualifiedName = x;
    }
    public Variable setqualifiedName (QualifiedName x)
    {
      z = new Variable ();
      z.privateSetqualifiedName (x);
      return z;
    }
  }
  public class Subscript extends Assignable
  {
/* receiver:Assignable "[" subscript:Expression "]" -> Assignable {cons("Subscript")} */
    private Subscript ()
    {
    }
    /*package */ Subscript (ITree tree, Assignable receiver,
			    Expression subscript)
    {
      this.tree = tree;
      this.receiver = receiver;
      this.subscript = subscript;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSubscriptAssignable (this);
    }
    private Assignable receiver;
    public Assignable getreceiver ()
    {
      return receiver;
    }
    private void privateSetreceiver (Assignable x)
    {
      this.receiver = x;
    }
    public Subscript setreceiver (Assignable x)
    {
      z = new Subscript ();
      z.privateSetreceiver (x);
      return z;
    }
    private Expression subscript;
    public Expression getsubscript ()
    {
      return subscript;
    }
    private void privateSetsubscript (Expression x)
    {
      this.subscript = x;
    }
    public Subscript setsubscript (Expression x)
    {
      z = new Subscript ();
      z.privateSetsubscript (x);
      return z;
    }
  }
  public class FieldAccess extends Assignable
  {
/* receiver:Assignable "." field:Name -> Assignable {cons("FieldAccess")} */
    private FieldAccess ()
    {
    }
    /*package */ FieldAccess (ITree tree, Assignable receiver, Name field)
    {
      this.tree = tree;
      this.receiver = receiver;
      this.field = field;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFieldAccessAssignable (this);
    }
    private Assignable receiver;
    public Assignable getreceiver ()
    {
      return receiver;
    }
    private void privateSetreceiver (Assignable x)
    {
      this.receiver = x;
    }
    public FieldAccess setreceiver (Assignable x)
    {
      z = new FieldAccess ();
      z.privateSetreceiver (x);
      return z;
    }
    private Name field;
    public Name getfield ()
    {
      return field;
    }
    private void privateSetfield (Name x)
    {
      this.field = x;
    }
    public FieldAccess setfield (Name x)
    {
      z = new FieldAccess ();
      z.privateSetfield (x);
      return z;
    }
  }
  public class IfDefined extends Assignable
  {
/* receiver:Assignable "?" condition:Expression -> Assignable {cons("IfDefined")} */
    private IfDefined ()
    {
    }
    /*package */ IfDefined (ITree tree, Assignable receiver,
			    Expression condition)
    {
      this.tree = tree;
      this.receiver = receiver;
      this.condition = condition;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitIfDefinedAssignable (this);
    }
    private Assignable receiver;
    public Assignable getreceiver ()
    {
      return receiver;
    }
    private void privateSetreceiver (Assignable x)
    {
      this.receiver = x;
    }
    public IfDefined setreceiver (Assignable x)
    {
      z = new IfDefined ();
      z.privateSetreceiver (x);
      return z;
    }
    private Expression condition;
    public Expression getcondition ()
    {
      return condition;
    }
    private void privateSetcondition (Expression x)
    {
      this.condition = x;
    }
    public IfDefined setcondition (Expression x)
    {
      z = new IfDefined ();
      z.privateSetcondition (x);
      return z;
    }
  }
  public class Annotation extends Assignable
  {
/* receiver:Assignable "@" annotation:Expression -> Assignable {cons("Annotation")} */
    private Annotation ()
    {
    }
    /*package */ Annotation (ITree tree, Assignable receiver,
			     Expression annotation)
    {
      this.tree = tree;
      this.receiver = receiver;
      this.annotation = annotation;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAnnotationAssignable (this);
    }
    private Assignable receiver;
    public Assignable getreceiver ()
    {
      return receiver;
    }
    private void privateSetreceiver (Assignable x)
    {
      this.receiver = x;
    }
    public Annotation setreceiver (Assignable x)
    {
      z = new Annotation ();
      z.privateSetreceiver (x);
      return z;
    }
    private Expression annotation;
    public Expression getannotation ()
    {
      return annotation;
    }
    private void privateSetannotation (Expression x)
    {
      this.annotation = x;
    }
    public Annotation setannotation (Expression x)
    {
      z = new Annotation ();
      z.privateSetannotation (x);
      return z;
    }
  }
}
