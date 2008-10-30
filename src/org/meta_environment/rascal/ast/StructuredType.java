package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
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
    public TypeArg gettypeArg ()
    {
      return typeArg;
    }
    private void $settypeArg (TypeArg x)
    {
      this.typeArg = x;
    }
    public List settypeArg (TypeArg x)
    {
      List z = new List ();
      z.$settypeArg (x);
      return z;
    }
  }
  public class Ambiguity extends StructuredType
  {
    private final List < StructuredType > alternatives;
    public Ambiguity (List < StructuredType > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < StructuredType > getAlternatives ()
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
    public TypeArg gettypeArg ()
    {
      return typeArg;
    }
    private void $settypeArg (TypeArg x)
    {
      this.typeArg = x;
    }
    public Set settypeArg (TypeArg x)
    {
      Set z = new Set ();
      z.$settypeArg (x);
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
    public TypeArg getfirst ()
    {
      return first;
    }
    private void $setfirst (TypeArg x)
    {
      this.first = x;
    }
    public Map setfirst (TypeArg x)
    {
      Map z = new Map ();
      z.$setfirst (x);
      return z;
    }
    private TypeArg second;
    public TypeArg getsecond ()
    {
      return second;
    }
    private void $setsecond (TypeArg x)
    {
      this.second = x;
    }
    public Map setsecond (TypeArg x)
    {
      Map z = new Map ();
      z.$setsecond (x);
      return z;
    }
  }
  public class Relation extends StructuredType
  {
/* "rel" "[" first:TypeArg "," rest:{TypeArg ","}+ "]" -> StructuredType {cons("Relation")} */
    private Relation ()
    {
    }
    /*package */ Relation (ITree tree, TypeArg first, List < TypeArg > rest)
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
    public TypeArg getfirst ()
    {
      return first;
    }
    private void $setfirst (TypeArg x)
    {
      this.first = x;
    }
    public Relation setfirst (TypeArg x)
    {
      Relation z = new Relation ();
      z.$setfirst (x);
      return z;
    }
    private List < TypeArg > rest;
    public List < TypeArg > getrest ()
    {
      return rest;
    }
    private void $setrest (List < TypeArg > x)
    {
      this.rest = x;
    }
    public Relation setrest (List < TypeArg > x)
    {
      Relation z = new Relation ();
      z.$setrest (x);
      return z;
    }
  }
  public class Tuple extends StructuredType
  {
/* "tuple" "[" first:TypeArg "," rest:{TypeArg ","}+ "]" -> StructuredType {cons("Tuple")} */
    private Tuple ()
    {
    }
    /*package */ Tuple (ITree tree, TypeArg first, List < TypeArg > rest)
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
    public TypeArg getfirst ()
    {
      return first;
    }
    private void $setfirst (TypeArg x)
    {
      this.first = x;
    }
    public Tuple setfirst (TypeArg x)
    {
      Tuple z = new Tuple ();
      z.$setfirst (x);
      return z;
    }
    private List < TypeArg > rest;
    public List < TypeArg > getrest ()
    {
      return rest;
    }
    private void $setrest (List < TypeArg > x)
    {
      this.rest = x;
    }
    public Tuple setrest (List < TypeArg > x)
    {
      Tuple z = new Tuple ();
      z.$setrest (x);
      return z;
    }
  }
}
