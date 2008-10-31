package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Character extends AbstractAST
{
  static public class Numeric extends Character
  {
/* numChar:NumChar -> Character {cons("Numeric")} */
    private Numeric ()
    {
    }
    /*package */ Numeric (ITree tree,
			  org.meta_environment.rascal.ast.NumChar numChar)
    {
      this.tree = tree;
      this.numChar = numChar;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharacterNumeric (this);
    }
    private org.meta_environment.rascal.ast.NumChar numChar;
    public org.meta_environment.rascal.ast.NumChar getNumChar ()
    {
      return numChar;
    }
    private void $setNumChar (org.meta_environment.rascal.ast.NumChar x)
    {
      this.numChar = x;
    }
    public Numeric setNumChar (org.meta_environment.rascal.ast.NumChar x)
    {
      org.meta_environment.rascal.ast.Numeric z = new Numeric ();
      z.$setNumChar (x);
      return z;
    }
  }
  static public class Ambiguity extends Character
  {
    private final java.util.List < org.meta_environment.rascal.ast.Character >
      alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.Character >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Character >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Short extends Character
  {
/* shortChar:ShortChar -> Character {cons("Short")} */
    private Short ()
    {
    }
    /*package */ Short (ITree tree,
			org.meta_environment.rascal.ast.ShortChar shortChar)
    {
      this.tree = tree;
      this.shortChar = shortChar;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharacterShort (this);
    }
    private org.meta_environment.rascal.ast.ShortChar shortChar;
    public org.meta_environment.rascal.ast.ShortChar getShortChar ()
    {
      return shortChar;
    }
    private void $setShortChar (org.meta_environment.rascal.ast.ShortChar x)
    {
      this.shortChar = x;
    }
    public Short setShortChar (org.meta_environment.rascal.ast.ShortChar x)
    {
      org.meta_environment.rascal.ast.Short z = new Short ();
      z.$setShortChar (x);
      return z;
    }
  }
  static public class Top extends Character
  {
/* "\\TOP" -> Character {cons("Top")} */
    private Top ()
    {
    }
    /*package */ Top (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharacterTop (this);
    }
  }
  static public class EOF extends Character
  {
/* "\\EOF" -> Character {cons("EOF")} */
    private EOF ()
    {
    }
    /*package */ EOF (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharacterEOF (this);
    }
  }
  static public class Bottom extends Character
  {
/* "\\BOT" -> Character {cons("Bottom")} */
    private Bottom ()
    {
    }
    /*package */ Bottom (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharacterBottom (this);
    }
  }
  static public class LabelStart extends Character
  {
/* "\\LABEL_START" -> Character {cons("LabelStart")} */
    private LabelStart ()
    {
    }
    /*package */ LabelStart (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharacterLabelStart (this);
    }
  }
}
