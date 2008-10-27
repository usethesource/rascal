package org.meta_environment.rascal.ast;
public abstract class Declaration extends AbstractAST
{
  public class View extends Declaration
  {
/* "view" view:Name "<:" type:Name tags:Tags alternatives:{Alternative "|"}+ ";" -> Declaration {cons("View")} */
    private View ()
    {
    }
    /*package */ View (ITree tree, Name view, Name type, Tags tags,
		       List < Alternative > alternatives)
    {
      this.tree = tree;
      this.view = view;
      this.type = type;
      this.tags = tags;
      this.alternatives = alternatives;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitViewDeclaration (this);
    }
    private final Name view;
    public Name getview ()
    {
      return view;
    }
    private void privateSetview (Name x)
    {
      this.view = x;
    }
    public View setview (Name x)
    {
      z = new View ();
      z.privateSetview (x);
      return z;
    }
    private final Name type;
    public Name gettype ()
    {
      return type;
    }
    private void privateSettype (Name x)
    {
      this.type = x;
    }
    public View settype (Name x)
    {
      z = new View ();
      z.privateSettype (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public View settags (Tags x)
    {
      z = new View ();
      z.privateSettags (x);
      return z;
    }
    private final List < Alternative > alternatives;
    public List < Alternative > getalternatives ()
    {
      return alternatives;
    }
    private void privateSetalternatives (List < Alternative > x)
    {
      this.alternatives = x;
    }
    public View setalternatives (List < Alternative > x)
    {
      z = new View ();
      z.privateSetalternatives (x);
      return z;
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTypeDeclaration (this);
    }
    private final Type base;
    public Type getbase ()
    {
      return base;
    }
    private void privateSetbase (Type x)
    {
      this.base = x;
    }
    public Type setbase (Type x)
    {
      z = new Type ();
      z.privateSetbase (x);
      return z;
    }
    private final UserType user;
    public UserType getuser ()
    {
      return user;
    }
    private void privateSetuser (UserType x)
    {
      this.user = x;
    }
    public Type setuser (UserType x)
    {
      z = new Type ();
      z.privateSetuser (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public Type settags (Tags x)
    {
      z = new Type ();
      z.privateSettags (x);
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
		       List < Variant > variants)
    {
      this.tree = tree;
      this.user = user;
      this.tags = tags;
      this.variants = variants;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDataDeclaration (this);
    }
    private final UserType user;
    public UserType getuser ()
    {
      return user;
    }
    private void privateSetuser (UserType x)
    {
      this.user = x;
    }
    public Data setuser (UserType x)
    {
      z = new Data ();
      z.privateSetuser (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public Data settags (Tags x)
    {
      z = new Data ();
      z.privateSettags (x);
      return z;
    }
    private final List < Variant > variants;
    public List < Variant > getvariants ()
    {
      return variants;
    }
    private void privateSetvariants (List < Variant > x)
    {
      this.variants = x;
    }
    public Data setvariants (List < Variant > x)
    {
      z = new Data ();
      z.privateSetvariants (x);
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitFunctionDeclaration (this);
    }
    private final FunctionDeclaration functionDeclaration;
    public FunctionDeclaration getfunctionDeclaration ()
    {
      return functionDeclaration;
    }
    private void privateSetfunctionDeclaration (FunctionDeclaration x)
    {
      this.functionDeclaration = x;
    }
    public Function setfunctionDeclaration (FunctionDeclaration x)
    {
      z = new Function ();
      z.privateSetfunctionDeclaration (x);
      return z;
    }
  }
  public class Variable extends Declaration
  {
/* type:Type variables:{Variable ","}+ ";" -> Declaration {cons("Variable")} */
    private Variable ()
    {
    }
    /*package */ Variable (ITree tree, Type type, List < Variable > variables)
    {
      this.tree = tree;
      this.type = type;
      this.variables = variables;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitVariableDeclaration (this);
    }
    private final Type type;
    public Type gettype ()
    {
      return type;
    }
    private void privateSettype (Type x)
    {
      this.type = x;
    }
    public Variable settype (Type x)
    {
      z = new Variable ();
      z.privateSettype (x);
      return z;
    }
    private final List < Variable > variables;
    public List < Variable > getvariables ()
    {
      return variables;
    }
    private void privateSetvariables (List < Variable > x)
    {
      this.variables = x;
    }
    public Variable setvariables (List < Variable > x)
    {
      z = new Variable ();
      z.privateSetvariables (x);
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitRuleDeclaration (this);
    }
    private final Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public Rule setname (Name x)
    {
      z = new Rule ();
      z.privateSetname (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public Rule settags (Tags x)
    {
      z = new Rule ();
      z.privateSettags (x);
      return z;
    }
    private final Rule rule;
    public Rule getrule ()
    {
      return rule;
    }
    private void privateSetrule (Rule x)
    {
      this.rule = x;
    }
    public Rule setrule (Rule x)
    {
      z = new Rule ();
      z.privateSetrule (x);
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
			     List < Type > types)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
      this.tags = tags;
      this.types = types;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAnnotationDeclaration (this);
    }
    private final Type type;
    public Type gettype ()
    {
      return type;
    }
    private void privateSettype (Type x)
    {
      this.type = x;
    }
    public Annotation settype (Type x)
    {
      z = new Annotation ();
      z.privateSettype (x);
      return z;
    }
    private final Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public Annotation setname (Name x)
    {
      z = new Annotation ();
      z.privateSetname (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public Annotation settags (Tags x)
    {
      z = new Annotation ();
      z.privateSettags (x);
      return z;
    }
    private final List < Type > types;
    public List < Type > gettypes ()
    {
      return types;
    }
    private void privateSettypes (List < Type > x)
    {
      this.types = x;
    }
    public Annotation settypes (List < Type > x)
    {
      z = new Annotation ();
      z.privateSettypes (x);
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
		      List < Type > types)
    {
      this.tree = tree;
      this.kind = kind;
      this.name = name;
      this.tags = tags;
      this.types = types;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTagDeclaration (this);
    }
    private final Kind kind;
    public Kind getkind ()
    {
      return kind;
    }
    private void privateSetkind (Kind x)
    {
      this.kind = x;
    }
    public Tag setkind (Kind x)
    {
      z = new Tag ();
      z.privateSetkind (x);
      return z;
    }
    private final Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public Tag setname (Name x)
    {
      z = new Tag ();
      z.privateSetname (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public Tag settags (Tags x)
    {
      z = new Tag ();
      z.privateSettags (x);
      return z;
    }
    private final List < Type > types;
    public List < Type > gettypes ()
    {
      return types;
    }
    private void privateSettypes (List < Type > x)
    {
      this.types = x;
    }
    public Tag settypes (List < Type > x)
    {
      z = new Tag ();
      z.privateSettypes (x);
      return z;
    }
  }
}
