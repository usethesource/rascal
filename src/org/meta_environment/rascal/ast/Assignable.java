package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
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
    public QualifiedName getqualifiedName ()
    {
      return qualifiedName;
    }
    private void $setqualifiedName (QualifiedName x)
    {
      this.qualifiedName = x;
    }
    public Variable setqualifiedName (QualifiedName x)
    {
      Variable z = new Variable ();
      z.$setqualifiedName (x);
      return z;
    }
  }
  public class Ambiguity extends Assignable
  {
    private final List < Assignable > alternatives;
    public Ambiguity (List < Assignable > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Assignable > getAlternatives ()
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
    public Assignable getreceiver ()
    {
      return receiver;
    }
    private void $setreceiver (Assignable x)
    {
      this.receiver = x;
    }
    public Subscript setreceiver (Assignable x)
    {
      Subscript z = new Subscript ();
      z.$setreceiver (x);
      return z;
    }
    private Expression subscript;
    public Expression getsubscript ()
    {
      return subscript;
    }
    private void $setsubscript (Expression x)
    {
      this.subscript = x;
    }
    public Subscript setsubscript (Expression x)
    {
      Subscript z = new Subscript ();
      z.$setsubscript (x);
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
    public Assignable getreceiver ()
    {
      return receiver;
    }
    private void $setreceiver (Assignable x)
    {
      this.receiver = x;
    }
    public FieldAccess setreceiver (Assignable x)
    {
      FieldAccess z = new FieldAccess ();
      z.$setreceiver (x);
      return z;
    }
    private Name field;
    public Name getfield ()
    {
      return field;
    }
    private void $setfield (Name x)
    {
      this.field = x;
    }
    public FieldAccess setfield (Name x)
    {
      FieldAccess z = new FieldAccess ();
      z.$setfield (x);
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
    public Assignable getreceiver ()
    {
      return receiver;
    }
    private void $setreceiver (Assignable x)
    {
      this.receiver = x;
    }
    public IfDefined setreceiver (Assignable x)
    {
      IfDefined z = new IfDefined ();
      z.$setreceiver (x);
      return z;
    }
    private Expression condition;
    public Expression getcondition ()
    {
      return condition;
    }
    private void $setcondition (Expression x)
    {
      this.condition = x;
    }
    public IfDefined setcondition (Expression x)
    {
      IfDefined z = new IfDefined ();
      z.$setcondition (x);
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
    public Assignable getreceiver ()
    {
      return receiver;
    }
    private void $setreceiver (Assignable x)
    {
      this.receiver = x;
    }
    public Annotation setreceiver (Assignable x)
    {
      Annotation z = new Annotation ();
      z.$setreceiver (x);
      return z;
    }
    private Expression annotation;
    public Expression getannotation ()
    {
      return annotation;
    }
    private void $setannotation (Expression x)
    {
      this.annotation = x;
    }
    public Annotation setannotation (Expression x)
    {
      Annotation z = new Annotation ();
      z.$setannotation (x);
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
			List < Assignable > rest)
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
    public Assignable getfirst ()
    {
      return first;
    }
    private void $setfirst (Assignable x)
    {
      this.first = x;
    }
    public Tuple setfirst (Assignable x)
    {
      Tuple z = new Tuple ();
      z.$setfirst (x);
      return z;
    }
    private List < Assignable > rest;
    public List < Assignable > getrest ()
    {
      return rest;
    }
    private void $setrest (List < Assignable > x)
    {
      this.rest = x;
    }
    public Tuple setrest (List < Assignable > x)
    {
      Tuple z = new Tuple ();
      z.$setrest (x);
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
			      List < Assignable > arguments)
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
    public Name getname ()
    {
      return name;
    }
    private void $setname (Name x)
    {
      this.name = x;
    }
    public Constructor setname (Name x)
    {
      Constructor z = new Constructor ();
      z.$setname (x);
      return z;
    }
    private List < Assignable > arguments;
    public List < Assignable > getarguments ()
    {
      return arguments;
    }
    private void $setarguments (List < Assignable > x)
    {
      this.arguments = x;
    }
    public Constructor setarguments (List < Assignable > x)
    {
      Constructor z = new Constructor ();
      z.$setarguments (x);
      return z;
    }
  }
}
