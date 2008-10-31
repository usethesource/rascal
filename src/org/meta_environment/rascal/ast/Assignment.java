package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Assignment extends AbstractAST
{
  static public class Default extends Assignment
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
  static public class Ambiguity extends Assignment
  {
    public Assignment.Ambiguity makeAssignmentAmbiguity (java.util.List <
							 Assignment >
							 alternatives)
    {
      Assignment.Ambiguity amb = new Assignment.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Assignment.Ambiguity) table.get (amb);
    }
    private final java.util.List < Assignment > alternatives;
    public Ambiguity (java.util.List < Assignment > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Assignment > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Addition extends Assignment
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
  static public class Substraction extends Assignment
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
  static public class Product extends Assignment
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
  static public class Division extends Assignment
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
  static public class Interesection extends Assignment
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
