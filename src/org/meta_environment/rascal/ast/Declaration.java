package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Declaration extends AbstractAST
{
  public class View extends Declaration
  {
/* "view" view:Name "<:" type:Name tags:Tags alternatives:{Alternative "|"}+ ";" -> Declaration {cons("View")} */
    private View ()
    {
    }
    /*package */ View (ITree tree, Name view, Name type, Tags tags,
		       java.util.List < Alternative > alternatives)
    {
      this.tree = tree;
      this.view = view;
      this.type = type;
      this.tags = tags;
      this.alternatives = alternatives;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclarationView (this);
    }
    private Name view;
    public Name getView ()
    {
      return view;
    }
    private void $setView (Name x)
    {
      this.view = x;
    }
    public View setView (Name x)
    {
      View z = new View ();
      z.$setView (x);
      return z;
    }
    private Name type;
    public Name getType ()
    {
      return type;
    }
    private void $setType (Name x)
    {
      this.type = x;
    }
    public View setType (Name x)
    {
      View z = new View ();
      z.$setType (x);
      return z;
    }
    private Tags tags;
    public Tags getTags ()
    {
      return tags;
    }
    private void $setTags (Tags x)
    {
      this.tags = x;
    }
    public View setTags (Tags x)
    {
      View z = new View ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < Alternative > alternatives;
    public java.util.List < Alternative > getAlternatives ()
    {
      return alternatives;
    }
    private void $setAlternatives (java.util.List < Alternative > x)
    {
      this.alternatives = x;
    }
    public View setAlternatives (java.util.List < Alternative > x)
    {
      View z = new View ();
      z.$setAlternatives (x);
      return z;
    }
  }
  public class Ambiguity extends Declaration
  {
    private final java.util.List < Declaration > alternatives;
    public Ambiguity (java.util.List < Declaration > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Declaration > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Type extends Declaration
  {
/* "type" base:Type user:UserType tags:Tags ";" -> Declaration {cons("Type")} */
    private Type ()
    {
    }
    /*package */ Type (ITree tree, Type base, UserType user, Tags tags)
    {
      this.tree = tree;
      this.base = base;
      this.user = user;
      this.tags = tags;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclarationType (this);
    }
    private Type base;
    public Type getBase ()
    {
      return base;
    }
    private void $setBase (Type x)
    {
      this.base = x;
    }
    public Type setBase (Type x)
    {
      Type z = new Type ();
      z.$setBase (x);
      return z;
    }
    private UserType user;
    public UserType getUser ()
    {
      return user;
    }
    private void $setUser (UserType x)
    {
      this.user = x;
    }
    public Type setUser (UserType x)
    {
      Type z = new Type ();
      z.$setUser (x);
      return z;
    }
    private Tags tags;
    public Tags getTags ()
    {
      return tags;
    }
    private void $setTags (Tags x)
    {
      this.tags = x;
    }
    public Type setTags (Tags x)
    {
      Type z = new Type ();
      z.$setTags (x);
      return z;
    }
  }
  public class Data extends Declaration
  {
/* "data" user:UserType tags:Tags variants:{Variant "|"}+ ";" -> Declaration {cons("Data")} */
    private Data ()
    {
    }
    /*package */ Data (ITree tree, UserType user, Tags tags,
		       java.util.List < Variant > variants)
    {
      this.tree = tree;
      this.user = user;
      this.tags = tags;
      this.variants = variants;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclarationData (this);
    }
    private UserType user;
    public UserType getUser ()
    {
      return user;
    }
    private void $setUser (UserType x)
    {
      this.user = x;
    }
    public Data setUser (UserType x)
    {
      Data z = new Data ();
      z.$setUser (x);
      return z;
    }
    private Tags tags;
    public Tags getTags ()
    {
      return tags;
    }
    private void $setTags (Tags x)
    {
      this.tags = x;
    }
    public Data setTags (Tags x)
    {
      Data z = new Data ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < Variant > variants;
    public java.util.List < Variant > getVariants ()
    {
      return variants;
    }
    private void $setVariants (java.util.List < Variant > x)
    {
      this.variants = x;
    }
    public Data setVariants (java.util.List < Variant > x)
    {
      Data z = new Data ();
      z.$setVariants (x);
      return z;
    }
  }
  public class Function extends Declaration
  {
/* functionDeclaration:FunctionDeclaration -> Declaration {cons("Function")} */
    private Function ()
    {
    }
    /*package */ Function (ITree tree,
			   FunctionDeclaration functionDeclaration)
    {
      this.tree = tree;
      this.functionDeclaration = functionDeclaration;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclarationFunction (this);
    }
    private FunctionDeclaration functionDeclaration;
    public FunctionDeclaration getFunctionDeclaration ()
    {
      return functionDeclaration;
    }
    private void $setFunctionDeclaration (FunctionDeclaration x)
    {
      this.functionDeclaration = x;
    }
    public Function setFunctionDeclaration (FunctionDeclaration x)
    {
      Function z = new Function ();
      z.$setFunctionDeclaration (x);
      return z;
    }
  }
  public class Variable extends Declaration
  {
/* type:Type variables:{Variable ","}+ ";" -> Declaration {cons("Variable")} */
    private Variable ()
    {
    }
    /*package */ Variable (ITree tree, Type type,
			   java.util.List < Variable > variables)
    {
      this.tree = tree;
      this.type = type;
      this.variables = variables;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclarationVariable (this);
    }
    private Type type;
    public Type getType ()
    {
      return type;
    }
    private void $setType (Type x)
    {
      this.type = x;
    }
    public Variable setType (Type x)
    {
      Variable z = new Variable ();
      z.$setType (x);
      return z;
    }
    private java.util.List < Variable > variables;
    public java.util.List < Variable > getVariables ()
    {
      return variables;
    }
    private void $setVariables (java.util.List < Variable > x)
    {
      this.variables = x;
    }
    public Variable setVariables (java.util.List < Variable > x)
    {
      Variable z = new Variable ();
      z.$setVariables (x);
      return z;
    }
  }
  public class Rule extends Declaration
  {
/* "rule" name:Name tags:Tags rule:Rule -> Declaration {cons("Rule")} */
    private Rule ()
    {
    }
    /*package */ Rule (ITree tree, Name name, Tags tags, Rule rule)
    {
      this.tree = tree;
      this.name = name;
      this.tags = tags;
      this.rule = rule;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclarationRule (this);
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Rule setName (Name x)
    {
      Rule z = new Rule ();
      z.$setName (x);
      return z;
    }
    private Tags tags;
    public Tags getTags ()
    {
      return tags;
    }
    private void $setTags (Tags x)
    {
      this.tags = x;
    }
    public Rule setTags (Tags x)
    {
      Rule z = new Rule ();
      z.$setTags (x);
      return z;
    }
    private Rule rule;
    public Rule getRule ()
    {
      return rule;
    }
    private void $setRule (Rule x)
    {
      this.rule = x;
    }
    public Rule setRule (Rule x)
    {
      Rule z = new Rule ();
      z.$setRule (x);
      return z;
    }
  }
  public class Annotation extends Declaration
  {
/* "anno" type:Type name:Name tags:Tags types:{Type "|"}+ ";" -> Declaration {cons("Annotation")} */
    private Annotation ()
    {
    }
    /*package */ Annotation (ITree tree, Type type, Name name, Tags tags,
			     java.util.List < Type > types)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
      this.tags = tags;
      this.types = types;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclarationAnnotation (this);
    }
    private Type type;
    public Type getType ()
    {
      return type;
    }
    private void $setType (Type x)
    {
      this.type = x;
    }
    public Annotation setType (Type x)
    {
      Annotation z = new Annotation ();
      z.$setType (x);
      return z;
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Annotation setName (Name x)
    {
      Annotation z = new Annotation ();
      z.$setName (x);
      return z;
    }
    private Tags tags;
    public Tags getTags ()
    {
      return tags;
    }
    private void $setTags (Tags x)
    {
      this.tags = x;
    }
    public Annotation setTags (Tags x)
    {
      Annotation z = new Annotation ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < Type > types;
    public java.util.List < Type > getTypes ()
    {
      return types;
    }
    private void $setTypes (java.util.List < Type > x)
    {
      this.types = x;
    }
    public Annotation setTypes (java.util.List < Type > x)
    {
      Annotation z = new Annotation ();
      z.$setTypes (x);
      return z;
    }
  }
  public class Tag extends Declaration
  {
/* "tag" kind:Kind name:Name tags:Tags types:{Type "|"}+ ";" -> Declaration {cons("Tag")} */
    private Tag ()
    {
    }
    /*package */ Tag (ITree tree, Kind kind, Name name, Tags tags,
		      java.util.List < Type > types)
    {
      this.tree = tree;
      this.kind = kind;
      this.name = name;
      this.tags = tags;
      this.types = types;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclarationTag (this);
    }
    private Kind kind;
    public Kind getKind ()
    {
      return kind;
    }
    private void $setKind (Kind x)
    {
      this.kind = x;
    }
    public Tag setKind (Kind x)
    {
      Tag z = new Tag ();
      z.$setKind (x);
      return z;
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Tag setName (Name x)
    {
      Tag z = new Tag ();
      z.$setName (x);
      return z;
    }
    private Tags tags;
    public Tags getTags ()
    {
      return tags;
    }
    private void $setTags (Tags x)
    {
      this.tags = x;
    }
    public Tag setTags (Tags x)
    {
      Tag z = new Tag ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < Type > types;
    public java.util.List < Type > getTypes ()
    {
      return types;
    }
    private void $setTypes (java.util.List < Type > x)
    {
      this.types = x;
    }
    public Tag setTypes (java.util.List < Type > x)
    {
      Tag z = new Tag ();
      z.$setTypes (x);
      return z;
    }
  }
}
