package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
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
      return visitor.visitListStructuredType (this);
    }
    private TypeArg typeArg;
    public TypeArg gettypeArg ()
    {
      return typeArg;
    }
    private void privateSettypeArg (TypeArg x)
    {
      this.typeArg = x;
    }
    public List settypeArg (TypeArg x)
    {
      List z = new List ();
      z.privateSettypeArg (x);
      return z;
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
      return visitor.visitSetStructuredType (this);
    }
    private TypeArg typeArg;
    public TypeArg gettypeArg ()
    {
      return typeArg;
    }
    private void privateSettypeArg (TypeArg x)
    {
      this.typeArg = x;
    }
    public Set settypeArg (TypeArg x)
    {
      Set z = new Set ();
      z.privateSettypeArg (x);
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
      return visitor.visitMapStructuredType (this);
    }
    private TypeArg first;
    public TypeArg getfirst ()
    {
      return first;
    }
    private void privateSetfirst (TypeArg x)
    {
      this.first = x;
    }
    public Map setfirst (TypeArg x)
    {
      Map z = new Map ();
      z.privateSetfirst (x);
      return z;
    }
    private TypeArg second;
    public TypeArg getsecond ()
    {
      return second;
    }
    private void privateSetsecond (TypeArg x)
    {
      this.second = x;
    }
    public Map setsecond (TypeArg x)
    {
      Map z = new Map ();
      z.privateSetsecond (x);
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
      return visitor.visitRelationStructuredType (this);
    }
    private TypeArg first;
    public TypeArg getfirst ()
    {
      return first;
    }
    private void privateSetfirst (TypeArg x)
    {
      this.first = x;
    }
    public Relation setfirst (TypeArg x)
    {
      Relation z = new Relation ();
      z.privateSetfirst (x);
      return z;
    }
    private List < TypeArg > rest;
    public List < TypeArg > getrest ()
    {
      return rest;
    }
    private void privateSetrest (List < TypeArg > x)
    {
      this.rest = x;
    }
    public Relation setrest (List < TypeArg > x)
    {
      Relation z = new Relation ();
      z.privateSetrest (x);
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
      return visitor.visitTupleStructuredType (this);
    }
    private TypeArg first;
    public TypeArg getfirst ()
    {
      return first;
    }
    private void privateSetfirst (TypeArg x)
    {
      this.first = x;
    }
    public Tuple setfirst (TypeArg x)
    {
      Tuple z = new Tuple ();
      z.privateSetfirst (x);
      return z;
    }
    private List < TypeArg > rest;
    public List < TypeArg > getrest ()
    {
      return rest;
    }
    private void privateSetrest (List < TypeArg > x)
    {
      this.rest = x;
    }
    public Tuple setrest (List < TypeArg > x)
    {
      Tuple z = new Tuple ();
      z.privateSetrest (x);
      return z;
    }
  }
}
