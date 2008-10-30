package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class StructuredType extends AbstractAST
{
  public class List extends StructuredType
  {
/* "list" "[" typeArg:TypeArg "]" -> StructuredType {cons("List")} */
    private List ()
    {
    }
    /*package */ List (ITree tree, TypeArg typeArg)
    {
      this.tree = tree;
      this.typeArg = typeArg;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStructuredTypeList (this);
    }
    private TypeArg typeArg;
    public TypeArg getTypeArg ()
    {
      return typeArg;
    }
    private void $setTypeArg (TypeArg x)
    {
      this.typeArg = x;
    }
    public List setTypeArg (TypeArg x)
    {
      List z = new List ();
      z.$setTypeArg (x);
      return z;
    }
  }
  public class Ambiguity extends StructuredType
  {
    private final java.util.List < StructuredType > alternatives;
    public Ambiguity (java.util.List < StructuredType > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < StructuredType > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Set extends StructuredType
  {
/* "set" "[" typeArg:TypeArg "]" -> StructuredType {cons("Set")} */
    private Set ()
    {
    }
    /*package */ Set (ITree tree, TypeArg typeArg)
    {
      this.tree = tree;
      this.typeArg = typeArg;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStructuredTypeSet (this);
    }
    private TypeArg typeArg;
    public TypeArg getTypeArg ()
    {
      return typeArg;
    }
    private void $setTypeArg (TypeArg x)
    {
      this.typeArg = x;
    }
    public Set setTypeArg (TypeArg x)
    {
      Set z = new Set ();
      z.$setTypeArg (x);
      return z;
    }
  }
  public class Map extends StructuredType
  {
/* "map" "[" first:TypeArg "," second:TypeArg "]" -> StructuredType {cons("Map")} */
    private Map ()
    {
    }
    /*package */ Map (ITree tree, TypeArg first, TypeArg second)
    {
      this.tree = tree;
      this.first = first;
      this.second = second;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStructuredTypeMap (this);
    }
    private TypeArg first;
    public TypeArg getFirst ()
    {
      return first;
    }
    private void $setFirst (TypeArg x)
    {
      this.first = x;
    }
    public Map setFirst (TypeArg x)
    {
      Map z = new Map ();
      z.$setFirst (x);
      return z;
    }
    private TypeArg second;
    public TypeArg getSecond ()
    {
      return second;
    }
    private void $setSecond (TypeArg x)
    {
      this.second = x;
    }
    public Map setSecond (TypeArg x)
    {
      Map z = new Map ();
      z.$setSecond (x);
      return z;
    }
  }
  public class Relation extends StructuredType
  {
/* "rel" "[" first:TypeArg "," rest:{TypeArg ","}+ "]" -> StructuredType {cons("Relation")} */
    private Relation ()
    {
    }
    /*package */ Relation (ITree tree, TypeArg first,
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
    private TypeArg first;
    public TypeArg getFirst ()
    {
      return first;
    }
    private void $setFirst (TypeArg x)
    {
      this.first = x;
    }
    public Relation setFirst (TypeArg x)
    {
      Relation z = new Relation ();
      z.$setFirst (x);
      return z;
    }
    private java.util.List < TypeArg > rest;
    public java.util.List < TypeArg > getRest ()
    {
      return rest;
    }
    private void $setRest (java.util.List < TypeArg > x)
    {
      this.rest = x;
    }
    public Relation setRest (java.util.List < TypeArg > x)
    {
      Relation z = new Relation ();
      z.$setRest (x);
      return z;
    }
  }
  public class Tuple extends StructuredType
  {
/* "tuple" "[" first:TypeArg "," rest:{TypeArg ","}+ "]" -> StructuredType {cons("Tuple")} */
    private Tuple ()
    {
    }
    /*package */ Tuple (ITree tree, TypeArg first,
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
    private TypeArg first;
    public TypeArg getFirst ()
    {
      return first;
    }
    private void $setFirst (TypeArg x)
    {
      this.first = x;
    }
    public Tuple setFirst (TypeArg x)
    {
      Tuple z = new Tuple ();
      z.$setFirst (x);
      return z;
    }
    private java.util.List < TypeArg > rest;
    public java.util.List < TypeArg > getRest ()
    {
      return rest;
    }
    private void $setRest (java.util.List < TypeArg > x)
    {
      this.rest = x;
    }
    public Tuple setRest (java.util.List < TypeArg > x)
    {
      Tuple z = new Tuple ();
      z.$setRest (x);
      return z;
    }
  }
}
