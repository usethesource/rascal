package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Assignment extends AbstractAST
{
  public class Default extends Assignment
  {
/* "=" -> Assignment {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignmentDefault (this);
    }
  }
  public class Ambiguity extends Assignment
  {
    private final List < Assignment > alternatives;
    public Ambiguity (List < Assignment > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Assignment > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Addition extends Assignment
  {
/* "+=" -> Assignment {cons("Addition")} */
    private Addition ()
    {
    }
    /*package */ Addition (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignmentAddition (this);
    }
  }
  public class Substraction extends Assignment
  {
/* "-=" -> Assignment {cons("Substraction")} */
    private Substraction ()
    {
    }
    /*package */ Substraction (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignmentSubstraction (this);
    }
  }
  public class Product extends Assignment
  {
/* "*=" -> Assignment {cons("Product")} */
    private Product ()
    {
    }
    /*package */ Product (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignmentProduct (this);
    }
  }
  public class Division extends Assignment
  {
/* "/=" -> Assignment {cons("Division")} */
    private Division ()
    {
    }
    /*package */ Division (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignmentDivision (this);
    }
  }
  public class Interesection extends Assignment
  {
/* "&=" -> Assignment {cons("Interesection")} */
    private Interesection ()
    {
    }
    /*package */ Interesection (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAssignmentInteresection (this);
    }
  }
}
