package org.meta_environment.rascal.ast;
public abstract class Declaration extends AbstractAST
{
  public class View extends Declaration
  {
    private Name view;
    private Name type;
    private Annotations annos;
    private List < Alternative > alternatives;

    private View ()
    {
    }
    /*package */ View (ITree tree, Name view, Name type, Annotations annos,
		       List < Alternative > alternatives)
    {
      this.tree = tree;
      this.view = view;
      this.type = type;
      this.annos = annos;
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
    private final Annotations annos;
    public Annotations getannos ()
    {
      return annos;
    }
    private void privateSetannos (Annotations x)
    {
      this.annos = x;
    }
    public View setannos (Annotations x)
    {
      z = new View ();
      z.privateSetannos (x);
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
    private Type base;
    private UserType user;
    private Annotations annotations;

    private Type ()
    {
    }
    /*package */ Type (ITree tree, Type base, UserType user,
		       Annotations annotations)
    {
      this.tree = tree;
      this.base = base;
      this.user = user;
      this.annotations = annotations;
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
    private final Annotations annotations;
    public Annotations getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (Annotations x)
    {
      this.annotations = x;
    }
    public Type setannotations (Annotations x)
    {
      z = new Type ();
      z.privateSetannotations (x);
      return z;
    }
  }
  public class Data extends Declaration
  {
    private UserType user;
    private Annotations annotations;
    private List < Variant > variants;

    private Data ()
    {
    }
    /*package */ Data (ITree tree, UserType user, Annotations annotations,
		       List < Variant > variants)
    {
      this.tree = tree;
      this.user = user;
      this.annotations = annotations;
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
    private final Annotations annotations;
    public Annotations getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (Annotations x)
    {
      this.annotations = x;
    }
    public Data setannotations (Annotations x)
    {
      z = new Data ();
      z.privateSetannotations (x);
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
    private FunctionDeclaration functionDeclaration;

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
    private Type type;
    private List < Variable > variables;

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
    private Name name;
    private Annotations annotations;
    private Rule rule;

    private Rule ()
    {
    }
    /*package */ Rule (ITree tree, Name name, Annotations annotations,
		       Rule rule)
    {
      this.tree = tree;
      this.name = name;
      this.annotations = annotations;
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
    private final Annotations annotations;
    public Annotations getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (Annotations x)
    {
      this.annotations = x;
    }
    public Rule setannotations (Annotations x)
    {
      z = new Rule ();
      z.privateSetannotations (x);
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
    private Type type;
    private Name name;
    private Annotations annotations;
    private List < Type > types;

    private Annotation ()
    {
    }
    /*package */ Annotation (ITree tree, Type type, Name name,
			     Annotations annotations, List < Type > types)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
      this.annotations = annotations;
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
    private final Annotations annotations;
    public Annotations getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (Annotations x)
    {
      this.annotations = x;
    }
    public Annotation setannotations (Annotations x)
    {
      z = new Annotation ();
      z.privateSetannotations (x);
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
    private Kind kind;
    private Name name;
    private Annotations annotations;
    private List < Type > types;

    private Tag ()
    {
    }
    /*package */ Tag (ITree tree, Kind kind, Name name,
		      Annotations annotations, List < Type > types)
    {
      this.tree = tree;
      this.kind = kind;
      this.name = name;
      this.annotations = annotations;
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
    private final Annotations annotations;
    public Annotations getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (Annotations x)
    {
      this.annotations = x;
    }
    public Tag setannotations (Annotations x)
    {
      z = new Tag ();
      z.privateSetannotations (x);
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
