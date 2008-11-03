package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class BasicType extends AbstractAST
{
  static public class Bool extends BasicType
  {
/* "bool" -> BasicType {cons("Bool")} */
    private Bool ()
    {
    }
    /*package */ Bool (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBasicTypeBool (this);
    }
  }
  static public class Ambiguity extends BasicType
  {
    private final java.util.List < org.meta_environment.rascal.ast.BasicType >
      alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.BasicType >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.BasicType >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Int extends BasicType
  {
/* "int" -> BasicType {cons("Int")} */
    private Int ()
    {
    }
    /*package */ Int (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBasicTypeInt (this);
    }
  }
  static public class Double extends BasicType
  {
/* "double" -> BasicType {cons("Double")} */
    private Double ()
    {
    }
    /*package */ Double (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBasicTypeDouble (this);
    }
  }
  static public class String extends BasicType
  {
/* "str" -> BasicType {cons("String")} */
    private String ()
    {
    }
    /*package */ String (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBasicTypeString (this);
    }
  }
  static public class Value extends BasicType
  {
/* "value" -> BasicType {cons("Value")} */
    private Value ()
    {
    }
    /*package */ Value (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBasicTypeValue (this);
    }
  }
  static public class Term extends BasicType
  {
/* "term" -> BasicType {cons("Term")} */
    private Term ()
    {
    }
    /*package */ Term (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBasicTypeTerm (this);
    }
  }
  static public class Void extends BasicType
  {
/* "void" -> BasicType {cons("Void")} */
    private Void ()
    {
    }
    /*package */ Void (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBasicTypeVoid (this);
    }
  }
  static public class Loc extends BasicType
  {
/* "loc" -> BasicType {cons("Loc")} */
    private Loc ()
    {
    }
    /*package */ Loc (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBasicTypeLoc (this);
    }
  }
}
