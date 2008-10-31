package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Declaration extends AbstractAST
{
  static public class View extends Declaration
  {
/* "view" view:Name "<:" type:Name tags:Tags alternatives:{Alternative "|"}+ ";" -> Declaration {cons("View")} */
    private View ()
    {
    }
    /*package */ View (ITree tree, org.meta_environment.rascal.ast.Name view,
		       org.meta_environment.rascal.ast.Name type,
		       org.meta_environment.rascal.ast.Tags tags,
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
    private org.meta_environment.rascal.ast.Name view;
    public org.meta_environment.rascal.ast.Name getView ()
    {
      return view;
    }
    private void $setView (org.meta_environment.rascal.ast.Name x)
    {
      this.view = x;
    }
    public View setView (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.View z = new View ();
      z.$setView (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name type;
    public org.meta_environment.rascal.ast.Name getType ()
    {
      return type;
    }
    private void $setType (org.meta_environment.rascal.ast.Name x)
    {
      this.type = x;
    }
    public View setType (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.View z = new View ();
      z.$setType (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Tags tags;
    public org.meta_environment.rascal.ast.Tags getTags ()
    {
      return tags;
    }
    private void $setTags (org.meta_environment.rascal.ast.Tags x)
    {
      this.tags = x;
    }
    public View setTags (org.meta_environment.rascal.ast.Tags x)
    {
      org.meta_environment.rascal.ast.View z = new View ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Alternative >
      alternatives;
    public java.util.List < org.meta_environment.rascal.ast.Alternative >
      getAlternatives ()
    {
      return alternatives;
    }
    private void $setAlternatives (java.util.List <
				   org.meta_environment.rascal.ast.
				   Alternative > x)
    {
      this.alternatives = x;
    }
    public View setAlternatives (java.util.List <
				 org.meta_environment.rascal.ast.Alternative >
				 x)
    {
      org.meta_environment.rascal.ast.View z = new View ();
      z.$setAlternatives (x);
      return z;
    }
  }
  static public class Ambiguity extends Declaration
  {
    public Declaration.Ambiguity makeDeclarationAmbiguity (java.util.List <
							   Declaration >
							   alternatives)
    {
      Declaration.Ambiguity amb = new Declaration.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Declaration.Ambiguity) table.get (amb);
    }
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
  static public class Type extends Declaration
  {
/* "type" base:Type user:UserType tags:Tags ";" -> Declaration {cons("Type")} */
    private Type ()
    {
    }
    /*package */ Type (ITree tree, org.meta_environment.rascal.ast.Type base,
		       org.meta_environment.rascal.ast.UserType user,
		       org.meta_environment.rascal.ast.Tags tags)
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
    private org.meta_environment.rascal.ast.Type base;
    public org.meta_environment.rascal.ast.Type getBase ()
    {
      return base;
    }
    private void $setBase (org.meta_environment.rascal.ast.Type x)
    {
      this.base = x;
    }
    public Type setBase (org.meta_environment.rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.Type z = new Type ();
      z.$setBase (x);
      return z;
    }
    private org.meta_environment.rascal.ast.UserType user;
    public org.meta_environment.rascal.ast.UserType getUser ()
    {
      return user;
    }
    private void $setUser (org.meta_environment.rascal.ast.UserType x)
    {
      this.user = x;
    }
    public Type setUser (org.meta_environment.rascal.ast.UserType x)
    {
      org.meta_environment.rascal.ast.Type z = new Type ();
      z.$setUser (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Tags tags;
    public org.meta_environment.rascal.ast.Tags getTags ()
    {
      return tags;
    }
    private void $setTags (org.meta_environment.rascal.ast.Tags x)
    {
      this.tags = x;
    }
    public Type setTags (org.meta_environment.rascal.ast.Tags x)
    {
      org.meta_environment.rascal.ast.Type z = new Type ();
      z.$setTags (x);
      return z;
    }
  }
  static public class Data extends Declaration
  {
/* "data" user:UserType tags:Tags variants:{Variant "|"}+ ";" -> Declaration {cons("Data")} */
    private Data ()
    {
    }
    /*package */ Data (ITree tree,
		       org.meta_environment.rascal.ast.UserType user,
		       org.meta_environment.rascal.ast.Tags tags,
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
    private org.meta_environment.rascal.ast.UserType user;
    public org.meta_environment.rascal.ast.UserType getUser ()
    {
      return user;
    }
    private void $setUser (org.meta_environment.rascal.ast.UserType x)
    {
      this.user = x;
    }
    public Data setUser (org.meta_environment.rascal.ast.UserType x)
    {
      org.meta_environment.rascal.ast.Data z = new Data ();
      z.$setUser (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Tags tags;
    public org.meta_environment.rascal.ast.Tags getTags ()
    {
      return tags;
    }
    private void $setTags (org.meta_environment.rascal.ast.Tags x)
    {
      this.tags = x;
    }
    public Data setTags (org.meta_environment.rascal.ast.Tags x)
    {
      org.meta_environment.rascal.ast.Data z = new Data ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Variant >
      variants;
    public java.util.List < org.meta_environment.rascal.ast.Variant >
      getVariants ()
    {
      return variants;
    }
    private void $setVariants (java.util.List <
			       org.meta_environment.rascal.ast.Variant > x)
    {
      this.variants = x;
    }
    public Data setVariants (java.util.List <
			     org.meta_environment.rascal.ast.Variant > x)
    {
      org.meta_environment.rascal.ast.Data z = new Data ();
      z.$setVariants (x);
      return z;
    }
  }
  static public class Function extends Declaration
  {
/* functionDeclaration:FunctionDeclaration -> Declaration {cons("Function")} */
    private Function ()
    {
    }
    /*package */ Function (ITree tree,
			   org.meta_environment.rascal.ast.
			   FunctionDeclaration functionDeclaration)
    {
      this.tree = tree;
      this.functionDeclaration = functionDeclaration;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclarationFunction (this);
    }
    private org.meta_environment.rascal.ast.
      FunctionDeclaration functionDeclaration;
    public org.meta_environment.rascal.ast.
      FunctionDeclaration getFunctionDeclaration ()
    {
      return functionDeclaration;
    }
    private void $setFunctionDeclaration (org.meta_environment.rascal.ast.
					  FunctionDeclaration x)
    {
      this.functionDeclaration = x;
    }
    public Function setFunctionDeclaration (org.meta_environment.rascal.ast.
					    FunctionDeclaration x)
    {
      org.meta_environment.rascal.ast.Function z = new Function ();
      z.$setFunctionDeclaration (x);
      return z;
    }
  }
  static public class Variable extends Declaration
  {
/* type:Type variables:{Variable ","}+ ";" -> Declaration {cons("Variable")} */
    private Variable ()
    {
    }
    /*package */ Variable (ITree tree,
			   org.meta_environment.rascal.ast.Type type,
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
    private org.meta_environment.rascal.ast.Type type;
    public org.meta_environment.rascal.ast.Type getType ()
    {
      return type;
    }
    private void $setType (org.meta_environment.rascal.ast.Type x)
    {
      this.type = x;
    }
    public Variable setType (org.meta_environment.rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.Variable z = new Variable ();
      z.$setType (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Variable >
      variables;
    public java.util.List < org.meta_environment.rascal.ast.Variable >
      getVariables ()
    {
      return variables;
    }
    private void $setVariables (java.util.List <
				org.meta_environment.rascal.ast.Variable > x)
    {
      this.variables = x;
    }
    public Variable setVariables (java.util.List <
				  org.meta_environment.rascal.ast.Variable >
				  x)
    {
      org.meta_environment.rascal.ast.Variable z = new Variable ();
      z.$setVariables (x);
      return z;
    }
  }
  static public class Rule extends Declaration
  {
/* "rule" name:Name tags:Tags rule:Rule -> Declaration {cons("Rule")} */
    private Rule ()
    {
    }
    /*package */ Rule (ITree tree, org.meta_environment.rascal.ast.Name name,
		       org.meta_environment.rascal.ast.Tags tags,
		       org.meta_environment.rascal.ast.Rule rule)
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
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public Rule setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Rule z = new Rule ();
      z.$setName (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Tags tags;
    public org.meta_environment.rascal.ast.Tags getTags ()
    {
      return tags;
    }
    private void $setTags (org.meta_environment.rascal.ast.Tags x)
    {
      this.tags = x;
    }
    public Rule setTags (org.meta_environment.rascal.ast.Tags x)
    {
      org.meta_environment.rascal.ast.Rule z = new Rule ();
      z.$setTags (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Rule rule;
    public org.meta_environment.rascal.ast.Rule getRule ()
    {
      return rule;
    }
    private void $setRule (org.meta_environment.rascal.ast.Rule x)
    {
      this.rule = x;
    }
    public Rule setRule (org.meta_environment.rascal.ast.Rule x)
    {
      org.meta_environment.rascal.ast.Rule z = new Rule ();
      z.$setRule (x);
      return z;
    }
  }
  static public class Annotation extends Declaration
  {
/* "anno" type:Type name:Name tags:Tags types:{Type "|"}+ ";" -> Declaration {cons("Annotation")} */
    private Annotation ()
    {
    }
    /*package */ Annotation (ITree tree,
			     org.meta_environment.rascal.ast.Type type,
			     org.meta_environment.rascal.ast.Name name,
			     org.meta_environment.rascal.ast.Tags tags,
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
    private org.meta_environment.rascal.ast.Type type;
    public org.meta_environment.rascal.ast.Type getType ()
    {
      return type;
    }
    private void $setType (org.meta_environment.rascal.ast.Type x)
    {
      this.type = x;
    }
    public Annotation setType (org.meta_environment.rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.Annotation z = new Annotation ();
      z.$setType (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public Annotation setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Annotation z = new Annotation ();
      z.$setName (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Tags tags;
    public org.meta_environment.rascal.ast.Tags getTags ()
    {
      return tags;
    }
    private void $setTags (org.meta_environment.rascal.ast.Tags x)
    {
      this.tags = x;
    }
    public Annotation setTags (org.meta_environment.rascal.ast.Tags x)
    {
      org.meta_environment.rascal.ast.Annotation z = new Annotation ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Type > types;
    public java.util.List < org.meta_environment.rascal.ast.Type > getTypes ()
    {
      return types;
    }
    private void $setTypes (java.util.List <
			    org.meta_environment.rascal.ast.Type > x)
    {
      this.types = x;
    }
    public Annotation setTypes (java.util.List <
				org.meta_environment.rascal.ast.Type > x)
    {
      org.meta_environment.rascal.ast.Annotation z = new Annotation ();
      z.$setTypes (x);
      return z;
    }
  }
  static public class Tag extends Declaration
  {
/* "tag" kind:Kind name:Name tags:Tags types:{Type "|"}+ ";" -> Declaration {cons("Tag")} */
    private Tag ()
    {
    }
    /*package */ Tag (ITree tree, org.meta_environment.rascal.ast.Kind kind,
		      org.meta_environment.rascal.ast.Name name,
		      org.meta_environment.rascal.ast.Tags tags,
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
    private org.meta_environment.rascal.ast.Kind kind;
    public org.meta_environment.rascal.ast.Kind getKind ()
    {
      return kind;
    }
    private void $setKind (org.meta_environment.rascal.ast.Kind x)
    {
      this.kind = x;
    }
    public Tag setKind (org.meta_environment.rascal.ast.Kind x)
    {
      org.meta_environment.rascal.ast.Tag z = new Tag ();
      z.$setKind (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public Tag setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Tag z = new Tag ();
      z.$setName (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Tags tags;
    public org.meta_environment.rascal.ast.Tags getTags ()
    {
      return tags;
    }
    private void $setTags (org.meta_environment.rascal.ast.Tags x)
    {
      this.tags = x;
    }
    public Tag setTags (org.meta_environment.rascal.ast.Tags x)
    {
      org.meta_environment.rascal.ast.Tag z = new Tag ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Type > types;
    public java.util.List < org.meta_environment.rascal.ast.Type > getTypes ()
    {
      return types;
    }
    private void $setTypes (java.util.List <
			    org.meta_environment.rascal.ast.Type > x)
    {
      this.types = x;
    }
    public Tag setTypes (java.util.List <
			 org.meta_environment.rascal.ast.Type > x)
    {
      org.meta_environment.rascal.ast.Tag z = new Tag ();
      z.$setTypes (x);
      return z;
    }
  }
}
