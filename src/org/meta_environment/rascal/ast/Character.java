package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Character extends AbstractAST
{
  public class Numeric extends Character
  {
/* numeric:NumChar -> Character {cons("Numeric")} */
    private Numeric ()
    {
    }
    /*package */ Numeric (ITree tree, NumChar numeric)
    {
      this.tree = tree;
      this.numeric = numeric;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharacterNumeric (this);
    }
    private NumChar numeric;
    public NumChar getNumeric ()
    {
      return numeric;
    }
    private void $setNumeric (NumChar x)
    {
      this.numeric = x;
    }
    public Numeric setNumeric (NumChar x)
    {
      Numeric z = new Numeric ();
      z.$setNumeric (x);
      return z;
    }
  }
  public class Ambiguity extends Character
  {
    private final java.util.List < Character > alternatives;
    public Ambiguity (java.util.List < Character > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Character > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Short extends Character
  {
/* short:ShortChar -> Character {cons("Short")} */
    private Short ()
    {
    }
    /*package */ Short (ITree tree, ShortChar short)
    {
      this.tree = tree;
      this.short = short;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharacterShort (this);
    }
    private ShortChar short;
    public ShortChar getShort ()
    {
      return short;
    }
    private void $setShort (ShortChar x)
    {
      this.short = x;
    }
    public Short setShort (ShortChar x)
    {
      Short z = new Short ();
      z.$setShort (x);
      return z;
    }
  }
  public class Top extends Character
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
  public class EOF extends Character
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
  public class Bottom extends Character
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
  public class LabelStart extends Character
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
