package org.meta_environment.rascal.ast;
public abstract class StructuredType extends AbstractAST
{
  public class List extends StructuredType
  {
    private TypeArg typeArg;

    private List ()
    {
    }
    /*package */ List (ITree tree, TypeArg typeArg)
    {
      this.tree = tree;
      this.typeArg = typeArg;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitListStructuredType (this);
    }
    private final TypeArg typeArg;
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
      z = new List ();
      z.privateSettypeArg (x);
      return z;
    }
  }
  public class Set extends StructuredType
  {
    private TypeArg typeArg;

    private Set ()
    {
    }
    /*package */ Set (ITree tree, TypeArg typeArg)
    {
      this.tree = tree;
      this.typeArg = typeArg;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSetStructuredType (this);
    }
    private final TypeArg typeArg;
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
      z = new Set ();
      z.privateSettypeArg (x);
      return z;
    }
  }
  public class Map extends StructuredType
  {
    private TypeArg first;
    private TypeArg second;

    private Map ()
    {
    }
    /*package */ Map (ITree tree, TypeArg first, TypeArg second)
    {
      this.tree = tree;
      this.first = first;
      this.second = second;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitMapStructuredType (this);
    }
    private final TypeArg first;
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
      z = new Map ();
      z.privateSetfirst (x);
      return z;
    }
    private final TypeArg second;
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
      z = new Map ();
      z.privateSetsecond (x);
      return z;
    }
  }
  public class Relation extends StructuredType
  {
    private TypeArg first;
    private List < TypeArg > rest;

    private Relation ()
    {
    }
    /*package */ Relation (ITree tree, TypeArg first, List < TypeArg > rest)
    {
      this.tree = tree;
      this.first = first;
      this.rest = rest;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitRelationStructuredType (this);
    }
    private final TypeArg first;
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
      z = new Relation ();
      z.privateSetfirst (x);
      return z;
    }
    private final List < TypeArg > rest;
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
      z = new Relation ();
      z.privateSetrest (x);
      return z;
    }
  }
  public class Tuple extends StructuredType
  {
    private TypeArg first;
    private List < TypeArg > rest;

    private Tuple ()
    {
    }
    /*package */ Tuple (ITree tree, TypeArg first, List < TypeArg > rest)
    {
      this.tree = tree;
      this.first = first;
      this.rest = rest;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTupleStructuredType (this);
    }
    private final TypeArg first;
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
      z = new Tuple ();
      z.privateSetfirst (x);
      return z;
    }
    private final List < TypeArg > rest;
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
      z = new Tuple ();
      z.privateSetrest (x);
      return z;
    }
  }
}
