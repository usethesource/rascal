package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
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
      return visitor.visitAssignableVariable (this);
    }
    private QualifiedName qualifiedName;
    public QualifiedName getQualifiedName ()
    {
      return qualifiedName;
    }
    private void $setQualifiedName (QualifiedName x)
    {
      this.qualifiedName = x;
    }
    public Variable setQualifiedName (QualifiedName x)
    {
      Variable z = new Variable ();
      z.$setQualifiedName (x);
      return z;
    }
  }
  public class Ambiguity extends Assignable
  {
    private final java.util.List < Assignable > alternatives;
    public Ambiguity (java.util.List < Assignable > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Assignable > getAlternatives ()
    {
      return alternatives;
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
      return visitor.visitAssignableSubscript (this);
    }
    private Assignable receiver;
    public Assignable getReceiver ()
    {
      return receiver;
    }
    private void $setReceiver (Assignable x)
    {
      this.receiver = x;
    }
    public Subscript setReceiver (Assignable x)
    {
      Subscript z = new Subscript ();
      z.$setReceiver (x);
      return z;
    }
    private Expression subscript;
    public Expression getSubscript ()
    {
      return subscript;
    }
    private void $setSubscript (Expression x)
    {
      this.subscript = x;
    }
    public Subscript setSubscript (Expression x)
    {
      Subscript z = new Subscript ();
      z.$setSubscript (x);
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
      return visitor.visitAssignableFieldAccess (this);
    }
    private Assignable receiver;
    public Assignable getReceiver ()
    {
      return receiver;
    }
    private void $setReceiver (Assignable x)
    {
      this.receiver = x;
    }
    public FieldAccess setReceiver (Assignable x)
    {
      FieldAccess z = new FieldAccess ();
      z.$setReceiver (x);
      return z;
    }
    private Name field;
    public Name getField ()
    {
      return field;
    }
    private void $setField (Name x)
    {
      this.field = x;
    }
    public FieldAccess setField (Name x)
    {
      FieldAccess z = new FieldAccess ();
      z.$setField (x);
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
      return visitor.visitAssignableIfDefined (this);
    }
    private Assignable receiver;
    public Assignable getReceiver ()
    {
      return receiver;
    }
    private void $setReceiver (Assignable x)
    {
      this.receiver = x;
    }
    public IfDefined setReceiver (Assignable x)
    {
      IfDefined z = new IfDefined ();
      z.$setReceiver (x);
      return z;
    }
    private Expression condition;
    public Expression getCondition ()
    {
      return condition;
    }
    private void $setCondition (Expression x)
    {
      this.condition = x;
    }
    public IfDefined setCondition (Expression x)
    {
      IfDefined z = new IfDefined ();
      z.$setCondition (x);
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
      return visitor.visitAssignableAnnotation (this);
    }
    private Assignable receiver;
    public Assignable getReceiver ()
    {
      return receiver;
    }
    private void $setReceiver (Assignable x)
    {
      this.receiver = x;
    }
    public Annotation setReceiver (Assignable x)
    {
      Annotation z = new Annotation ();
      z.$setReceiver (x);
      return z;
    }
    private Expression annotation;
    public Expression getAnnotation ()
    {
      return annotation;
    }
    private void $setAnnotation (Expression x)
    {
      this.annotation = x;
    }
    public Annotation setAnnotation (Expression x)
    {
      Annotation z = new Annotation ();
      z.$setAnnotation (x);
      return z;
    }
  }
  public class Tuple extends Assignable
  {
/* "<" first:Assignable "," rest:{Assignable ","}+ ">" -> Assignable {cons("Tuple")} */
    private Tuple ()
    {
    }
    /*package */ Tuple (ITree tree, Assignable first,
			java.util.List < Assignable > rest)
    {
      this.tree = tree;
      this.first = first;
      this.rest = rest;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignableTuple (this);
    }
    private Assignable first;
    public Assignable getFirst ()
    {
      return first;
    }
    private void $setFirst (Assignable x)
    {
      this.first = x;
    }
    public Tuple setFirst (Assignable x)
    {
      Tuple z = new Tuple ();
      z.$setFirst (x);
      return z;
    }
    private java.util.List < Assignable > rest;
    public java.util.List < Assignable > getRest ()
    {
      return rest;
    }
    private void $setRest (java.util.List < Assignable > x)
    {
      this.rest = x;
    }
    public Tuple setRest (java.util.List < Assignable > x)
    {
      Tuple z = new Tuple ();
      z.$setRest (x);
      return z;
    }
  }
  public class Constructor extends Assignable
  {
/* name:Name "(" arguments:{Assignable ","}+ ")" -> Assignable {cons("Constructor")} */
    private Constructor ()
    {
    }
    /*package */ Constructor (ITree tree, Name name,
			      java.util.List < Assignable > arguments)
    {
      this.tree = tree;
      this.name = name;
      this.arguments = arguments;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignableConstructor (this);
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Constructor setName (Name x)
    {
      Constructor z = new Constructor ();
      z.$setName (x);
      return z;
    }
    private java.util.List < Assignable > arguments;
    public java.util.List < Assignable > getArguments ()
    {
      return arguments;
    }
    private void $setArguments (java.util.List < Assignable > x)
    {
      this.arguments = x;
    }
    public Constructor setArguments (java.util.List < Assignable > x)
    {
      Constructor z = new Constructor ();
      z.$setArguments (x);
      return z;
    }
  }
}
