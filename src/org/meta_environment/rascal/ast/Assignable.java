package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Assignable extends AbstractAST
{
  static public class Variable extends Assignable
  {
/* qualifiedName:QualifiedName -> Assignable {cons("Variable")} */
    private Variable ()
    {
    }
    /*package */ Variable (ITree tree,
			   org.meta_environment.rascal.ast.
			   QualifiedName qualifiedName)
    {
      this.tree = tree;
      this.qualifiedName = qualifiedName;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignableVariable (this);
    }
    private org.meta_environment.rascal.ast.QualifiedName qualifiedName;
    public org.meta_environment.rascal.ast.QualifiedName getQualifiedName ()
    {
      return qualifiedName;
    }
    private void $setQualifiedName (org.meta_environment.rascal.ast.
				    QualifiedName x)
    {
      this.qualifiedName = x;
    }
    public org.meta_environment.rascal.ast.Variable setQualifiedName (org.
								      meta_environment.
								      rascal.
								      ast.
								      QualifiedName
								      x)
    {
      org.meta_environment.rascal.ast.Variable z = new Variable ();
      z.$setQualifiedName (x);
      return z;
    }
  }
  static public class Ambiguity extends Assignable
  {
    private final java.util.List < Assignable > alternatives;
    public Ambiguity (java.util.List < Assignable > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Assignable > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Subscript extends Assignable
  {
/* receiver:Assignable "[" subscript:Expression "]" -> Assignable {cons("Subscript")} */
    private Subscript ()
    {
    }
    /*package */ Subscript (ITree tree,
			    org.meta_environment.rascal.ast.
			    Assignable receiver,
			    org.meta_environment.rascal.ast.
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
    private org.meta_environment.rascal.ast.Assignable receiver;
    public org.meta_environment.rascal.ast.Assignable getReceiver ()
    {
      return receiver;
    }
    private void $setReceiver (org.meta_environment.rascal.ast.Assignable x)
    {
      this.receiver = x;
    }
    public org.meta_environment.rascal.ast.Subscript setReceiver (org.
								  meta_environment.
								  rascal.ast.
								  Assignable
								  x)
    {
      org.meta_environment.rascal.ast.Subscript z = new Subscript ();
      z.$setReceiver (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression subscript;
    public org.meta_environment.rascal.ast.Expression getSubscript ()
    {
      return subscript;
    }
    private void $setSubscript (org.meta_environment.rascal.ast.Expression x)
    {
      this.subscript = x;
    }
    public org.meta_environment.rascal.ast.Subscript setSubscript (org.
								   meta_environment.
								   rascal.ast.
								   Expression
								   x)
    {
      org.meta_environment.rascal.ast.Subscript z = new Subscript ();
      z.$setSubscript (x);
      return z;
    }
  }
  static public class FieldAccess extends Assignable
  {
/* receiver:Assignable "." field:Name -> Assignable {cons("FieldAccess")} */
    private FieldAccess ()
    {
    }
    /*package */ FieldAccess (ITree tree,
			      org.meta_environment.rascal.ast.
			      Assignable receiver,
			      org.meta_environment.rascal.ast.Name field)
    {
      this.tree = tree;
      this.receiver = receiver;
      this.field = field;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignableFieldAccess (this);
    }
    private org.meta_environment.rascal.ast.Assignable receiver;
    public org.meta_environment.rascal.ast.Assignable getReceiver ()
    {
      return receiver;
    }
    private void $setReceiver (org.meta_environment.rascal.ast.Assignable x)
    {
      this.receiver = x;
    }
    public org.meta_environment.rascal.ast.FieldAccess setReceiver (org.
								    meta_environment.
								    rascal.
								    ast.
								    Assignable
								    x)
    {
      org.meta_environment.rascal.ast.FieldAccess z = new FieldAccess ();
      z.$setReceiver (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name field;
    public org.meta_environment.rascal.ast.Name getField ()
    {
      return field;
    }
    private void $setField (org.meta_environment.rascal.ast.Name x)
    {
      this.field = x;
    }
    public org.meta_environment.rascal.ast.FieldAccess setField (org.
								 meta_environment.
								 rascal.ast.
								 Name x)
    {
      org.meta_environment.rascal.ast.FieldAccess z = new FieldAccess ();
      z.$setField (x);
      return z;
    }
  }
  static public class IfDefined extends Assignable
  {
/* receiver:Assignable "?" condition:Expression -> Assignable {cons("IfDefined")} */
    private IfDefined ()
    {
    }
    /*package */ IfDefined (ITree tree,
			    org.meta_environment.rascal.ast.
			    Assignable receiver,
			    org.meta_environment.rascal.ast.
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
    private org.meta_environment.rascal.ast.Assignable receiver;
    public org.meta_environment.rascal.ast.Assignable getReceiver ()
    {
      return receiver;
    }
    private void $setReceiver (org.meta_environment.rascal.ast.Assignable x)
    {
      this.receiver = x;
    }
    public org.meta_environment.rascal.ast.IfDefined setReceiver (org.
								  meta_environment.
								  rascal.ast.
								  Assignable
								  x)
    {
      org.meta_environment.rascal.ast.IfDefined z = new IfDefined ();
      z.$setReceiver (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression condition;
    public org.meta_environment.rascal.ast.Expression getCondition ()
    {
      return condition;
    }
    private void $setCondition (org.meta_environment.rascal.ast.Expression x)
    {
      this.condition = x;
    }
    public org.meta_environment.rascal.ast.IfDefined setCondition (org.
								   meta_environment.
								   rascal.ast.
								   Expression
								   x)
    {
      org.meta_environment.rascal.ast.IfDefined z = new IfDefined ();
      z.$setCondition (x);
      return z;
    }
  }
  static public class Annotation extends Assignable
  {
/* receiver:Assignable "@" annotation:Expression -> Assignable {cons("Annotation")} */
    private Annotation ()
    {
    }
    /*package */ Annotation (ITree tree,
			     org.meta_environment.rascal.ast.
			     Assignable receiver,
			     org.meta_environment.rascal.ast.
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
    private org.meta_environment.rascal.ast.Assignable receiver;
    public org.meta_environment.rascal.ast.Assignable getReceiver ()
    {
      return receiver;
    }
    private void $setReceiver (org.meta_environment.rascal.ast.Assignable x)
    {
      this.receiver = x;
    }
    public org.meta_environment.rascal.ast.Annotation setReceiver (org.
								   meta_environment.
								   rascal.ast.
								   Assignable
								   x)
    {
      org.meta_environment.rascal.ast.Annotation z = new Annotation ();
      z.$setReceiver (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression annotation;
    public org.meta_environment.rascal.ast.Expression getAnnotation ()
    {
      return annotation;
    }
    private void $setAnnotation (org.meta_environment.rascal.ast.Expression x)
    {
      this.annotation = x;
    }
    public org.meta_environment.rascal.ast.Annotation setAnnotation (org.
								     meta_environment.
								     rascal.
								     ast.
								     Expression
								     x)
    {
      org.meta_environment.rascal.ast.Annotation z = new Annotation ();
      z.$setAnnotation (x);
      return z;
    }
  }
  static public class Tuple extends Assignable
  {
/* "<" first:Assignable "," rest:{Assignable ","}+ ">" -> Assignable {cons("Tuple")} */
    private Tuple ()
    {
    }
    /*package */ Tuple (ITree tree,
			org.meta_environment.rascal.ast.Assignable first,
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
    private org.meta_environment.rascal.ast.Assignable first;
    public org.meta_environment.rascal.ast.Assignable getFirst ()
    {
      return first;
    }
    private void $setFirst (org.meta_environment.rascal.ast.Assignable x)
    {
      this.first = x;
    }
    public org.meta_environment.rascal.ast.Tuple setFirst (org.
							   meta_environment.
							   rascal.ast.
							   Assignable x)
    {
      org.meta_environment.rascal.ast.Tuple z = new Tuple ();
      z.$setFirst (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Assignable >
      rest;
    public java.util.List < org.meta_environment.rascal.ast.Assignable >
      getRest ()
    {
      return rest;
    }
    private void $setRest (java.util.List <
			   org.meta_environment.rascal.ast.Assignable > x)
    {
      this.rest = x;
    }
    public org.meta_environment.rascal.ast.Tuple setRest (java.util.List <
							  org.
							  meta_environment.
							  rascal.ast.
							  Assignable > x)
    {
      org.meta_environment.rascal.ast.Tuple z = new Tuple ();
      z.$setRest (x);
      return z;
    }
  }
  static public class Constructor extends Assignable
  {
/* name:Name "(" arguments:{Assignable ","}+ ")" -> Assignable {cons("Constructor")} */
    private Constructor ()
    {
    }
    /*package */ Constructor (ITree tree,
			      org.meta_environment.rascal.ast.Name name,
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
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public org.meta_environment.rascal.ast.Constructor setName (org.
								meta_environment.
								rascal.ast.
								Name x)
    {
      org.meta_environment.rascal.ast.Constructor z = new Constructor ();
      z.$setName (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Assignable >
      arguments;
    public java.util.List < org.meta_environment.rascal.ast.Assignable >
      getArguments ()
    {
      return arguments;
    }
    private void $setArguments (java.util.List <
				org.meta_environment.rascal.ast.Assignable >
				x)
    {
      this.arguments = x;
    }
    public org.meta_environment.rascal.ast.Constructor setArguments (java.
								     util.
								     List <
								     org.
								     meta_environment.
								     rascal.
								     ast.
								     Assignable
								     > x)
    {
      org.meta_environment.rascal.ast.Constructor z = new Constructor ();
      z.$setArguments (x);
      return z;
    }
  }
}
