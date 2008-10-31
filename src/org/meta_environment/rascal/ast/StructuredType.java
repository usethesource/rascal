package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StructuredType extends AbstractAST
{
  static public class List extends StructuredType
  {
/* "list" "[" typeArg:TypeArg "]" -> StructuredType {cons("List")} */
    private List ()
    {
    }
    /*package */ List (ITree tree,
		       org.meta_environment.rascal.ast.TypeArg typeArg)
    {
      this.tree = tree;
      this.typeArg = typeArg;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStructuredTypeList (this);
    }
    private org.meta_environment.rascal.ast.TypeArg typeArg;
    public org.meta_environment.rascal.ast.TypeArg getTypeArg ()
    {
      return typeArg;
    }
    private void $setTypeArg (org.meta_environment.rascal.ast.TypeArg x)
    {
      this.typeArg = x;
    }
    public org.meta_environment.rascal.ast.List setTypeArg (org.
							    meta_environment.
							    rascal.ast.
							    TypeArg x)
    {
      org.meta_environment.rascal.ast.List z = new List ();
      z.$setTypeArg (x);
      return z;
    }
  }
  static public class Ambiguity extends StructuredType
  {
    private final java.util.List < StructuredType > alternatives;
    public Ambiguity (java.util.List < StructuredType > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < StructuredType > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Set extends StructuredType
  {
/* "set" "[" typeArg:TypeArg "]" -> StructuredType {cons("Set")} */
    private Set ()
    {
    }
    /*package */ Set (ITree tree,
		      org.meta_environment.rascal.ast.TypeArg typeArg)
    {
      this.tree = tree;
      this.typeArg = typeArg;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStructuredTypeSet (this);
    }
    private org.meta_environment.rascal.ast.TypeArg typeArg;
    public org.meta_environment.rascal.ast.TypeArg getTypeArg ()
    {
      return typeArg;
    }
    private void $setTypeArg (org.meta_environment.rascal.ast.TypeArg x)
    {
      this.typeArg = x;
    }
    public org.meta_environment.rascal.ast.Set setTypeArg (org.
							   meta_environment.
							   rascal.ast.
							   TypeArg x)
    {
      org.meta_environment.rascal.ast.Set z = new Set ();
      z.$setTypeArg (x);
      return z;
    }
  }
  static public class Map extends StructuredType
  {
/* "map" "[" first:TypeArg "," second:TypeArg "]" -> StructuredType {cons("Map")} */
    private Map ()
    {
    }
    /*package */ Map (ITree tree,
		      org.meta_environment.rascal.ast.TypeArg first,
		      org.meta_environment.rascal.ast.TypeArg second)
    {
      this.tree = tree;
      this.first = first;
      this.second = second;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStructuredTypeMap (this);
    }
    private org.meta_environment.rascal.ast.TypeArg first;
    public org.meta_environment.rascal.ast.TypeArg getFirst ()
    {
      return first;
    }
    private void $setFirst (org.meta_environment.rascal.ast.TypeArg x)
    {
      this.first = x;
    }
    public org.meta_environment.rascal.ast.Map setFirst (org.meta_environment.
							 rascal.ast.TypeArg x)
    {
      org.meta_environment.rascal.ast.Map z = new Map ();
      z.$setFirst (x);
      return z;
    }
    private org.meta_environment.rascal.ast.TypeArg second;
    public org.meta_environment.rascal.ast.TypeArg getSecond ()
    {
      return second;
    }
    private void $setSecond (org.meta_environment.rascal.ast.TypeArg x)
    {
      this.second = x;
    }
    public org.meta_environment.rascal.ast.Map setSecond (org.
							  meta_environment.
							  rascal.ast.
							  TypeArg x)
    {
      org.meta_environment.rascal.ast.Map z = new Map ();
      z.$setSecond (x);
      return z;
    }
  }
  static public class Relation extends StructuredType
  {
/* "rel" "[" first:TypeArg "," rest:{TypeArg ","}+ "]" -> StructuredType {cons("Relation")} */
    private Relation ()
    {
    }
    /*package */ Relation (ITree tree,
			   org.meta_environment.rascal.ast.TypeArg first,
			   java.util.List < TypeArg > rest)
    {
      this.tree = tree;
      this.first = first;
      this.rest = rest;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStructuredTypeRelation (this);
    }
    private org.meta_environment.rascal.ast.TypeArg first;
    public org.meta_environment.rascal.ast.TypeArg getFirst ()
    {
      return first;
    }
    private void $setFirst (org.meta_environment.rascal.ast.TypeArg x)
    {
      this.first = x;
    }
    public org.meta_environment.rascal.ast.Relation setFirst (org.
							      meta_environment.
							      rascal.ast.
							      TypeArg x)
    {
      org.meta_environment.rascal.ast.Relation z = new Relation ();
      z.$setFirst (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.TypeArg > rest;
    public java.util.List < org.meta_environment.rascal.ast.TypeArg >
      getRest ()
    {
      return rest;
    }
    private void $setRest (java.util.List <
			   org.meta_environment.rascal.ast.TypeArg > x)
    {
      this.rest = x;
    }
    public org.meta_environment.rascal.ast.Relation setRest (java.util.List <
							     org.
							     meta_environment.
							     rascal.ast.
							     TypeArg > x)
    {
      org.meta_environment.rascal.ast.Relation z = new Relation ();
      z.$setRest (x);
      return z;
    }
  }
  static public class Tuple extends StructuredType
  {
/* "tuple" "[" first:TypeArg "," rest:{TypeArg ","}+ "]" -> StructuredType {cons("Tuple")} */
    private Tuple ()
    {
    }
    /*package */ Tuple (ITree tree,
			org.meta_environment.rascal.ast.TypeArg first,
			java.util.List < TypeArg > rest)
    {
      this.tree = tree;
      this.first = first;
      this.rest = rest;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStructuredTypeTuple (this);
    }
    private org.meta_environment.rascal.ast.TypeArg first;
    public org.meta_environment.rascal.ast.TypeArg getFirst ()
    {
      return first;
    }
    private void $setFirst (org.meta_environment.rascal.ast.TypeArg x)
    {
      this.first = x;
    }
    public org.meta_environment.rascal.ast.Tuple setFirst (org.
							   meta_environment.
							   rascal.ast.
							   TypeArg x)
    {
      org.meta_environment.rascal.ast.Tuple z = new Tuple ();
      z.$setFirst (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.TypeArg > rest;
    public java.util.List < org.meta_environment.rascal.ast.TypeArg >
      getRest ()
    {
      return rest;
    }
    private void $setRest (java.util.List <
			   org.meta_environment.rascal.ast.TypeArg > x)
    {
      this.rest = x;
    }
    public org.meta_environment.rascal.ast.Tuple setRest (java.util.List <
							  org.
							  meta_environment.
							  rascal.ast.TypeArg >
							  x)
    {
      org.meta_environment.rascal.ast.Tuple z = new Tuple ();
      z.$setRest (x);
      return z;
    }
  }
}
