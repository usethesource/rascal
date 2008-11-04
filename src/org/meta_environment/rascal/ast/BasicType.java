package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class BasicType extends AbstractAST
{
  public boolean isBool ()
  {
    return false;
  }
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
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBasicTypeBool (this);
    }

    public boolean isBool ()
    {
      return true;
    }
  }
  static public class Ambiguity extends BasicType
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.BasicType >
      alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.BasicType >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.BasicType >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public boolean isInt ()
  {
    return false;
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
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBasicTypeInt (this);
    }

    public boolean isInt ()
    {
      return true;
    }
  }
  public boolean isDouble ()
  {
    return false;
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
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBasicTypeDouble (this);
    }

    public boolean isDouble ()
    {
      return true;
    }
  }
  public boolean isString ()
  {
    return false;
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
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBasicTypeString (this);
    }

    public boolean isString ()
    {
      return true;
    }
  }
  public boolean isValue ()
  {
    return false;
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
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBasicTypeValue (this);
    }

    public boolean isValue ()
    {
      return true;
    }
  }
  public boolean isTerm ()
  {
    return false;
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
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBasicTypeTerm (this);
    }

    public boolean isTerm ()
    {
      return true;
    }
  }
  public boolean isVoid ()
  {
    return false;
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
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBasicTypeVoid (this);
    }

    public boolean isVoid ()
    {
      return true;
    }
  }
  public boolean isLoc ()
  {
    return false;
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
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBasicTypeLoc (this);
    }

    public boolean isLoc ()
    {
      return true;
    }
  }
}
