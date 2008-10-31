package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Expression extends AbstractAST
{
  static public class Closure extends Expression
  {
/* "fun" type:Type Parameters "{" statements:Statement* "}" -> Expression {cons("Closure")} */
    private Closure ()
    {
    }
    /*package */ Closure (ITree tree,
			  org.meta_environment.rascal.ast.Type type,
			  java.util.List < Statement > statements)
    {
      this.tree = tree;
      this.type = type;
      this.statements = statements;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionClosure (this);
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
    public org.meta_environment.rascal.ast.Closure setType (org.
							    meta_environment.
							    rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.Closure z = new Closure ();
      z.$setType (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Statement >
      statements;
    public java.util.List < org.meta_environment.rascal.ast.Statement >
      getStatements ()
    {
      return statements;
    }
    private void $setStatements (java.util.List <
				 org.meta_environment.rascal.ast.Statement >
				 x)
    {
      this.statements = x;
    }
    public org.meta_environment.rascal.ast.Closure setStatements (java.util.
								  List <
								  org.
								  meta_environment.
								  rascal.ast.
								  Statement >
								  x)
    {
      org.meta_environment.rascal.ast.Closure z = new Closure ();
      z.$setStatements (x);
      return z;
    }
  }
  static public class Ambiguity extends Expression
  {
    private final java.util.List < Expression > alternatives;
    public Ambiguity (java.util.List < Expression > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Expression > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Bracket extends Expression
  {
/* "(" Expression ")" -> Expression {bracket} */
    private Bracket ()
    {
    }
    /*package */ Bracket (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionBracket (this);
    }
  }
  static public class ClosureCall extends Expression
  {
/* "(" closure:Expression ")" "(" arguments:{Expression ","}* ")" -> Expression {cons("ClosureCall")} */
    private ClosureCall ()
    {
    }
    /*package */ ClosureCall (ITree tree,
			      org.meta_environment.rascal.ast.
			      Expression closure,
			      java.util.List < Expression > arguments)
    {
      this.tree = tree;
      this.closure = closure;
      this.arguments = arguments;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionClosureCall (this);
    }
    private org.meta_environment.rascal.ast.Expression closure;
    public org.meta_environment.rascal.ast.Expression getClosure ()
    {
      return closure;
    }
    private void $setClosure (org.meta_environment.rascal.ast.Expression x)
    {
      this.closure = x;
    }
    public org.meta_environment.rascal.ast.ClosureCall setClosure (org.
								   meta_environment.
								   rascal.ast.
								   Expression
								   x)
    {
      org.meta_environment.rascal.ast.ClosureCall z = new ClosureCall ();
      z.$setClosure (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Expression >
      arguments;
    public java.util.List < org.meta_environment.rascal.ast.Expression >
      getArguments ()
    {
      return arguments;
    }
    private void $setArguments (java.util.List <
				org.meta_environment.rascal.ast.Expression >
				x)
    {
      this.arguments = x;
    }
    public org.meta_environment.rascal.ast.ClosureCall setArguments (java.
								     util.
								     List <
								     org.
								     meta_environment.
								     rascal.
								     ast.
								     Expression
								     > x)
    {
      org.meta_environment.rascal.ast.ClosureCall z = new ClosureCall ();
      z.$setArguments (x);
      return z;
    }
  }
  static public class Range extends Expression
  {
/* "[" from:Expression ".." to:Expression "]" -> Expression {cons("Range")} */
    private Range ()
    {
    }
    /*package */ Range (ITree tree,
			org.meta_environment.rascal.ast.Expression from,
			org.meta_environment.rascal.ast.Expression to)
    {
      this.tree = tree;
      this.from = from;
      this.to = to;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionRange (this);
    }
    private org.meta_environment.rascal.ast.Expression from;
    public org.meta_environment.rascal.ast.Expression getFrom ()
    {
      return from;
    }
    private void $setFrom (org.meta_environment.rascal.ast.Expression x)
    {
      this.from = x;
    }
    public org.meta_environment.rascal.ast.Range setFrom (org.
							  meta_environment.
							  rascal.ast.
							  Expression x)
    {
      org.meta_environment.rascal.ast.Range z = new Range ();
      z.$setFrom (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression to;
    public org.meta_environment.rascal.ast.Expression getTo ()
    {
      return to;
    }
    private void $setTo (org.meta_environment.rascal.ast.Expression x)
    {
      this.to = x;
    }
    public org.meta_environment.rascal.ast.Range setTo (org.meta_environment.
							rascal.ast.
							Expression x)
    {
      org.meta_environment.rascal.ast.Range z = new Range ();
      z.$setTo (x);
      return z;
    }
  }
  static public class StepRange extends Expression
  {
/* "[" from:Expression "," by:Expression ",.." to:Expression "]" -> Expression {cons("StepRange")} */
    private StepRange ()
    {
    }
    /*package */ StepRange (ITree tree,
			    org.meta_environment.rascal.ast.Expression from,
			    org.meta_environment.rascal.ast.Expression by,
			    org.meta_environment.rascal.ast.Expression to)
    {
      this.tree = tree;
      this.from = from;
      this.by = by;
      this.to = to;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionStepRange (this);
    }
    private org.meta_environment.rascal.ast.Expression from;
    public org.meta_environment.rascal.ast.Expression getFrom ()
    {
      return from;
    }
    private void $setFrom (org.meta_environment.rascal.ast.Expression x)
    {
      this.from = x;
    }
    public org.meta_environment.rascal.ast.StepRange setFrom (org.
							      meta_environment.
							      rascal.ast.
							      Expression x)
    {
      org.meta_environment.rascal.ast.StepRange z = new StepRange ();
      z.$setFrom (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression by;
    public org.meta_environment.rascal.ast.Expression getBy ()
    {
      return by;
    }
    private void $setBy (org.meta_environment.rascal.ast.Expression x)
    {
      this.by = x;
    }
    public org.meta_environment.rascal.ast.StepRange setBy (org.
							    meta_environment.
							    rascal.ast.
							    Expression x)
    {
      org.meta_environment.rascal.ast.StepRange z = new StepRange ();
      z.$setBy (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression to;
    public org.meta_environment.rascal.ast.Expression getTo ()
    {
      return to;
    }
    private void $setTo (org.meta_environment.rascal.ast.Expression x)
    {
      this.to = x;
    }
    public org.meta_environment.rascal.ast.StepRange setTo (org.
							    meta_environment.
							    rascal.ast.
							    Expression x)
    {
      org.meta_environment.rascal.ast.StepRange z = new StepRange ();
      z.$setTo (x);
      return z;
    }
  }
  static public class FieldUpdate extends Expression
  {
/* expression:Expression "[" key:Name "->" replacement:Expression "]" -> Expression {cons("FieldUpdate")} */
    private FieldUpdate ()
    {
    }
    /*package */ FieldUpdate (ITree tree,
			      org.meta_environment.rascal.ast.
			      Expression expression,
			      org.meta_environment.rascal.ast.Name key,
			      org.meta_environment.rascal.ast.
			      Expression replacement)
    {
      this.tree = tree;
      this.expression = expression;
      this.key = key;
      this.replacement = replacement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionFieldUpdate (this);
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public org.meta_environment.rascal.ast.FieldUpdate setExpression (org.
								      meta_environment.
								      rascal.
								      ast.
								      Expression
								      x)
    {
      org.meta_environment.rascal.ast.FieldUpdate z = new FieldUpdate ();
      z.$setExpression (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name key;
    public org.meta_environment.rascal.ast.Name getKey ()
    {
      return key;
    }
    private void $setKey (org.meta_environment.rascal.ast.Name x)
    {
      this.key = x;
    }
    public org.meta_environment.rascal.ast.FieldUpdate setKey (org.
							       meta_environment.
							       rascal.ast.
							       Name x)
    {
      org.meta_environment.rascal.ast.FieldUpdate z = new FieldUpdate ();
      z.$setKey (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression replacement;
    public org.meta_environment.rascal.ast.Expression getReplacement ()
    {
      return replacement;
    }
    private void $setReplacement (org.meta_environment.rascal.ast.
				  Expression x)
    {
      this.replacement = x;
    }
    public org.meta_environment.rascal.ast.FieldUpdate setReplacement (org.
								       meta_environment.
								       rascal.
								       ast.
								       Expression
								       x)
    {
      org.meta_environment.rascal.ast.FieldUpdate z = new FieldUpdate ();
      z.$setReplacement (x);
      return z;
    }
  }
  static public class FieldAccess extends Expression
  {
/* expression:Expression "." field:Name -> Expression {cons("FieldAccess")} */
    private FieldAccess ()
    {
    }
    /*package */ FieldAccess (ITree tree,
			      org.meta_environment.rascal.ast.
			      Expression expression,
			      org.meta_environment.rascal.ast.Name field)
    {
      this.tree = tree;
      this.expression = expression;
      this.field = field;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionFieldAccess (this);
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public org.meta_environment.rascal.ast.FieldAccess setExpression (org.
								      meta_environment.
								      rascal.
								      ast.
								      Expression
								      x)
    {
      org.meta_environment.rascal.ast.FieldAccess z = new FieldAccess ();
      z.$setExpression (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name field;
    public org.meta_environment.rascal.ast.Name getField ()
    {
      return field;
    }
    private void $setField (org.meta_environment.rascal.ast.Name x)
    {
      this.field = x;
    }
    public org.meta_environment.rascal.ast.FieldAccess setField (org.
								 meta_environment.
								 rascal.ast.
								 Name x)
    {
      org.meta_environment.rascal.ast.FieldAccess z = new FieldAccess ();
      z.$setField (x);
      return z;
    }
  }
  static public class Subscript extends Expression
  {
/* expression:Expression "[" subscript:Expression "]" -> Expression {cons("Subscript")} */
    private Subscript ()
    {
    }
    /*package */ Subscript (ITree tree,
			    org.meta_environment.rascal.ast.
			    Expression expression,
			    org.meta_environment.rascal.ast.
			    Expression subscript)
    {
      this.tree = tree;
      this.expression = expression;
      this.subscript = subscript;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionSubscript (this);
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public org.meta_environment.rascal.ast.Subscript setExpression (org.
								    meta_environment.
								    rascal.
								    ast.
								    Expression
								    x)
    {
      org.meta_environment.rascal.ast.Subscript z = new Subscript ();
      z.$setExpression (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression subscript;
    public org.meta_environment.rascal.ast.Expression getSubscript ()
    {
      return subscript;
    }
    private void $setSubscript (org.meta_environment.rascal.ast.Expression x)
    {
      this.subscript = x;
    }
    public org.meta_environment.rascal.ast.Subscript setSubscript (org.
								   meta_environment.
								   rascal.ast.
								   Expression
								   x)
    {
      org.meta_environment.rascal.ast.Subscript z = new Subscript ();
      z.$setSubscript (x);
      return z;
    }
  }
  static public class TransitiveReflexiveClosure extends Expression
  {
/* argument:Expression "*" -> Expression {cons("TransitiveReflexiveClosure")} */
    private TransitiveReflexiveClosure ()
    {
    }
    /*package */ TransitiveReflexiveClosure (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression argument)
    {
      this.tree = tree;
      this.argument = argument;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionTransitiveReflexiveClosure (this);
    }
    private org.meta_environment.rascal.ast.Expression argument;
    public org.meta_environment.rascal.ast.Expression getArgument ()
    {
      return argument;
    }
    private void $setArgument (org.meta_environment.rascal.ast.Expression x)
    {
      this.argument = x;
    }
    public org.meta_environment.rascal.ast.
      TransitiveReflexiveClosure setArgument (org.meta_environment.rascal.ast.
					      Expression x)
    {
      org.meta_environment.rascal.ast.TransitiveReflexiveClosure z =
	new TransitiveReflexiveClosure ();
      z.$setArgument (x);
      return z;
    }
  }
  static public class TransitiveClosure extends Expression
  {
/* argument:Expression "+" -> Expression {cons("TransitiveClosure")} */
    private TransitiveClosure ()
    {
    }
    /*package */ TransitiveClosure (ITree tree,
				    org.meta_environment.rascal.ast.
				    Expression argument)
    {
      this.tree = tree;
      this.argument = argument;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionTransitiveClosure (this);
    }
    private org.meta_environment.rascal.ast.Expression argument;
    public org.meta_environment.rascal.ast.Expression getArgument ()
    {
      return argument;
    }
    private void $setArgument (org.meta_environment.rascal.ast.Expression x)
    {
      this.argument = x;
    }
    public org.meta_environment.rascal.ast.TransitiveClosure setArgument (org.
									  meta_environment.
									  rascal.
									  ast.
									  Expression
									  x)
    {
      org.meta_environment.rascal.ast.TransitiveClosure z =
	new TransitiveClosure ();
      z.$setArgument (x);
      return z;
    }
  }
  static public class Annotation extends Expression
  {
/* expression:Expression "@" name:Name -> Expression {cons("Annotation")} */
    private Annotation ()
    {
    }
    /*package */ Annotation (ITree tree,
			     org.meta_environment.rascal.ast.
			     Expression expression,
			     org.meta_environment.rascal.ast.Name name)
    {
      this.tree = tree;
      this.expression = expression;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionAnnotation (this);
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public org.meta_environment.rascal.ast.Annotation setExpression (org.
								     meta_environment.
								     rascal.
								     ast.
								     Expression
								     x)
    {
      org.meta_environment.rascal.ast.Annotation z = new Annotation ();
      z.$setExpression (x);
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
    public org.meta_environment.rascal.ast.Annotation setName (org.
							       meta_environment.
							       rascal.ast.
							       Name x)
    {
      org.meta_environment.rascal.ast.Annotation z = new Annotation ();
      z.$setName (x);
      return z;
    }
  }
  static public class Negation extends Expression
  {
/* "!" argument:Expression -> Expression {cons("Negation")} */
    private Negation ()
    {
    }
    /*package */ Negation (ITree tree,
			   org.meta_environment.rascal.ast.
			   Expression argument)
    {
      this.tree = tree;
      this.argument = argument;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionNegation (this);
    }
    private org.meta_environment.rascal.ast.Expression argument;
    public org.meta_environment.rascal.ast.Expression getArgument ()
    {
      return argument;
    }
    private void $setArgument (org.meta_environment.rascal.ast.Expression x)
    {
      this.argument = x;
    }
    public org.meta_environment.rascal.ast.Negation setArgument (org.
								 meta_environment.
								 rascal.ast.
								 Expression x)
    {
      org.meta_environment.rascal.ast.Negation z = new Negation ();
      z.$setArgument (x);
      return z;
    }
  }
  static public class Product extends Expression
  {
/* lhs:Expression "*" rhs:Expression -> Expression {cons("Product"), left} */
    private Product ()
    {
    }
    /*package */ Product (ITree tree,
			  org.meta_environment.rascal.ast.Expression lhs,
			  org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionProduct (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.Product setLhs (org.
							   meta_environment.
							   rascal.ast.
							   Expression x)
    {
      org.meta_environment.rascal.ast.Product z = new Product ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.Product setRhs (org.
							   meta_environment.
							   rascal.ast.
							   Expression x)
    {
      org.meta_environment.rascal.ast.Product z = new Product ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class Intersection extends Expression
  {
/* lhs:Expression "&" rhs:Expression -> Expression {cons("Intersection"), left} */
    private Intersection ()
    {
    }
    /*package */ Intersection (ITree tree,
			       org.meta_environment.rascal.ast.Expression lhs,
			       org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionIntersection (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.Intersection setLhs (org.
								meta_environment.
								rascal.ast.
								Expression x)
    {
      org.meta_environment.rascal.ast.Intersection z = new Intersection ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.Intersection setRhs (org.
								meta_environment.
								rascal.ast.
								Expression x)
    {
      org.meta_environment.rascal.ast.Intersection z = new Intersection ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class Division extends Expression
  {
/* lhs:Expression "/" rhs:Expression -> Expression {cons("Division"), non-assoc} */
    private Division ()
    {
    }
    /*package */ Division (ITree tree,
			   org.meta_environment.rascal.ast.Expression lhs,
			   org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionDivision (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.Division setLhs (org.
							    meta_environment.
							    rascal.ast.
							    Expression x)
    {
      org.meta_environment.rascal.ast.Division z = new Division ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.Division setRhs (org.
							    meta_environment.
							    rascal.ast.
							    Expression x)
    {
      org.meta_environment.rascal.ast.Division z = new Division ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class Addition extends Expression
  {
/* lhs:Expression "+" rhs:Expression -> Expression {cons("Addition"), left} */
    private Addition ()
    {
    }
    /*package */ Addition (ITree tree,
			   org.meta_environment.rascal.ast.Expression lhs,
			   org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionAddition (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.Addition setLhs (org.
							    meta_environment.
							    rascal.ast.
							    Expression x)
    {
      org.meta_environment.rascal.ast.Addition z = new Addition ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.Addition setRhs (org.
							    meta_environment.
							    rascal.ast.
							    Expression x)
    {
      org.meta_environment.rascal.ast.Addition z = new Addition ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class Substraction extends Expression
  {
/* lhs:Expression "-" rhs:Expression -> Expression {cons("Substraction"), left} */
    private Substraction ()
    {
    }
    /*package */ Substraction (ITree tree,
			       org.meta_environment.rascal.ast.Expression lhs,
			       org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionSubstraction (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.Substraction setLhs (org.
								meta_environment.
								rascal.ast.
								Expression x)
    {
      org.meta_environment.rascal.ast.Substraction z = new Substraction ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.Substraction setRhs (org.
								meta_environment.
								rascal.ast.
								Expression x)
    {
      org.meta_environment.rascal.ast.Substraction z = new Substraction ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class RegExpMatch extends Expression
  {
/* lhs:Expression "=~" rhs:Expression -> Expression {non-assoc, cons("RegExpMatch")} */
    private RegExpMatch ()
    {
    }
    /*package */ RegExpMatch (ITree tree,
			      org.meta_environment.rascal.ast.Expression lhs,
			      org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionRegExpMatch (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.RegExpMatch setLhs (org.
							       meta_environment.
							       rascal.ast.
							       Expression x)
    {
      org.meta_environment.rascal.ast.RegExpMatch z = new RegExpMatch ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.RegExpMatch setRhs (org.
							       meta_environment.
							       rascal.ast.
							       Expression x)
    {
      org.meta_environment.rascal.ast.RegExpMatch z = new RegExpMatch ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class RegExpNoMatch extends Expression
  {
/* lhs:Expression "!~" rhs:Expression -> Expression {non-assoc, cons("RegExpNoMatch")} */
    private RegExpNoMatch ()
    {
    }
    /*package */ RegExpNoMatch (ITree tree,
				org.meta_environment.rascal.ast.
				Expression lhs,
				org.meta_environment.rascal.ast.
				Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionRegExpNoMatch (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.RegExpNoMatch setLhs (org.
								 meta_environment.
								 rascal.ast.
								 Expression x)
    {
      org.meta_environment.rascal.ast.RegExpNoMatch z = new RegExpNoMatch ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.RegExpNoMatch setRhs (org.
								 meta_environment.
								 rascal.ast.
								 Expression x)
    {
      org.meta_environment.rascal.ast.RegExpNoMatch z = new RegExpNoMatch ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class LessThan extends Expression
  {
/* lhs:Expression "<" rhs:Expression -> Expression {non-assoc, cons("LessThan")} */
    private LessThan ()
    {
    }
    /*package */ LessThan (ITree tree,
			   org.meta_environment.rascal.ast.Expression lhs,
			   org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionLessThan (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.LessThan setLhs (org.
							    meta_environment.
							    rascal.ast.
							    Expression x)
    {
      org.meta_environment.rascal.ast.LessThan z = new LessThan ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.LessThan setRhs (org.
							    meta_environment.
							    rascal.ast.
							    Expression x)
    {
      org.meta_environment.rascal.ast.LessThan z = new LessThan ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class LessThanOrEq extends Expression
  {
/* lhs:Expression "<=" rhs:Expression -> Expression {non-assoc, cons("LessThanOrEq")} */
    private LessThanOrEq ()
    {
    }
    /*package */ LessThanOrEq (ITree tree,
			       org.meta_environment.rascal.ast.Expression lhs,
			       org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionLessThanOrEq (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.LessThanOrEq setLhs (org.
								meta_environment.
								rascal.ast.
								Expression x)
    {
      org.meta_environment.rascal.ast.LessThanOrEq z = new LessThanOrEq ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.LessThanOrEq setRhs (org.
								meta_environment.
								rascal.ast.
								Expression x)
    {
      org.meta_environment.rascal.ast.LessThanOrEq z = new LessThanOrEq ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class GreaterThan extends Expression
  {
/* lhs:Expression ">" rhs:Expression -> Expression {non-assoc, cons("GreaterThan")} */
    private GreaterThan ()
    {
    }
    /*package */ GreaterThan (ITree tree,
			      org.meta_environment.rascal.ast.Expression lhs,
			      org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionGreaterThan (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.GreaterThan setLhs (org.
							       meta_environment.
							       rascal.ast.
							       Expression x)
    {
      org.meta_environment.rascal.ast.GreaterThan z = new GreaterThan ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.GreaterThan setRhs (org.
							       meta_environment.
							       rascal.ast.
							       Expression x)
    {
      org.meta_environment.rascal.ast.GreaterThan z = new GreaterThan ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class GreaterThanOrEq extends Expression
  {
/* lhs:Expression ">=" rhs:Expression -> Expression {non-assoc, cons("GreaterThanOrEq")} */
    private GreaterThanOrEq ()
    {
    }
    /*package */ GreaterThanOrEq (ITree tree,
				  org.meta_environment.rascal.ast.
				  Expression lhs,
				  org.meta_environment.rascal.ast.
				  Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionGreaterThanOrEq (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.GreaterThanOrEq setLhs (org.
								   meta_environment.
								   rascal.ast.
								   Expression
								   x)
    {
      org.meta_environment.rascal.ast.GreaterThanOrEq z =
	new GreaterThanOrEq ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.GreaterThanOrEq setRhs (org.
								   meta_environment.
								   rascal.ast.
								   Expression
								   x)
    {
      org.meta_environment.rascal.ast.GreaterThanOrEq z =
	new GreaterThanOrEq ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class Equals extends Expression
  {
/* lhs:Expression "==" rhs:Expression -> Expression {left, cons("Equals")} */
    private Equals ()
    {
    }
    /*package */ Equals (ITree tree,
			 org.meta_environment.rascal.ast.Expression lhs,
			 org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionEquals (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.Equals setLhs (org.
							  meta_environment.
							  rascal.ast.
							  Expression x)
    {
      org.meta_environment.rascal.ast.Equals z = new Equals ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.Equals setRhs (org.
							  meta_environment.
							  rascal.ast.
							  Expression x)
    {
      org.meta_environment.rascal.ast.Equals z = new Equals ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class NonEquals extends Expression
  {
/* lhs:Expression "!=" rhs:Expression -> Expression {left, cons("NonEquals")} */
    private NonEquals ()
    {
    }
    /*package */ NonEquals (ITree tree,
			    org.meta_environment.rascal.ast.Expression lhs,
			    org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionNonEquals (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.NonEquals setLhs (org.
							     meta_environment.
							     rascal.ast.
							     Expression x)
    {
      org.meta_environment.rascal.ast.NonEquals z = new NonEquals ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.NonEquals setRhs (org.
							     meta_environment.
							     rascal.ast.
							     Expression x)
    {
      org.meta_environment.rascal.ast.NonEquals z = new NonEquals ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class NotIn extends Expression
  {
/* lhs:Expression "notin" rhs:Expression -> Expression {non-assoc, cons("NotIn")} */
    private NotIn ()
    {
    }
    /*package */ NotIn (ITree tree,
			org.meta_environment.rascal.ast.Expression lhs,
			org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionNotIn (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.NotIn setLhs (org.meta_environment.
							 rascal.ast.
							 Expression x)
    {
      org.meta_environment.rascal.ast.NotIn z = new NotIn ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.NotIn setRhs (org.meta_environment.
							 rascal.ast.
							 Expression x)
    {
      org.meta_environment.rascal.ast.NotIn z = new NotIn ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class In extends Expression
  {
/* lhs:Expression "in" rhs:Expression -> Expression {non-assoc, cons("In")} */
    private In ()
    {
    }
    /*package */ In (ITree tree,
		     org.meta_environment.rascal.ast.Expression lhs,
		     org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionIn (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.In setLhs (org.meta_environment.
						      rascal.ast.Expression x)
    {
      org.meta_environment.rascal.ast.In z = new In ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.In setRhs (org.meta_environment.
						      rascal.ast.Expression x)
    {
      org.meta_environment.rascal.ast.In z = new In ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class And extends Expression
  {
/* lhs:Expression "&&" rhs:Expression -> Expression {left, cons("And")} */
    private And ()
    {
    }
    /*package */ And (ITree tree,
		      org.meta_environment.rascal.ast.Expression lhs,
		      org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionAnd (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.And setLhs (org.meta_environment.
						       rascal.ast.
						       Expression x)
    {
      org.meta_environment.rascal.ast.And z = new And ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.And setRhs (org.meta_environment.
						       rascal.ast.
						       Expression x)
    {
      org.meta_environment.rascal.ast.And z = new And ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class Or extends Expression
  {
/* lhs:Expression "||" rhs:Expression -> Expression {left, cons("Or")} */
    private Or ()
    {
    }
    /*package */ Or (ITree tree,
		     org.meta_environment.rascal.ast.Expression lhs,
		     org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionOr (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.Or setLhs (org.meta_environment.
						      rascal.ast.Expression x)
    {
      org.meta_environment.rascal.ast.Or z = new Or ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.Or setRhs (org.meta_environment.
						      rascal.ast.Expression x)
    {
      org.meta_environment.rascal.ast.Or z = new Or ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class IfDefined extends Expression
  {
/* lhs:Expression "?" rhs:Expression -> Expression {left, cons("IfDefined")} */
    private IfDefined ()
    {
    }
    /*package */ IfDefined (ITree tree,
			    org.meta_environment.rascal.ast.Expression lhs,
			    org.meta_environment.rascal.ast.Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionIfDefined (this);
    }
    private org.meta_environment.rascal.ast.Expression lhs;
    public org.meta_environment.rascal.ast.Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.lhs = x;
    }
    public org.meta_environment.rascal.ast.IfDefined setLhs (org.
							     meta_environment.
							     rascal.ast.
							     Expression x)
    {
      org.meta_environment.rascal.ast.IfDefined z = new IfDefined ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression rhs;
    public org.meta_environment.rascal.ast.Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Expression x)
    {
      this.rhs = x;
    }
    public org.meta_environment.rascal.ast.IfDefined setRhs (org.
							     meta_environment.
							     rascal.ast.
							     Expression x)
    {
      org.meta_environment.rascal.ast.IfDefined z = new IfDefined ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class IfThenElse extends Expression
  {
/* condition:Expression "?" thenExp:Expression ":" elseExp:Expression -> Expression {right, cons("IfThenElse")} */
    private IfThenElse ()
    {
    }
    /*package */ IfThenElse (ITree tree,
			     org.meta_environment.rascal.ast.
			     Expression condition,
			     org.meta_environment.rascal.ast.
			     Expression thenExp,
			     org.meta_environment.rascal.ast.
			     Expression elseExp)
    {
      this.tree = tree;
      this.condition = condition;
      this.thenExp = thenExp;
      this.elseExp = elseExp;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionIfThenElse (this);
    }
    private org.meta_environment.rascal.ast.Expression condition;
    public org.meta_environment.rascal.ast.Expression getCondition ()
    {
      return condition;
    }
    private void $setCondition (org.meta_environment.rascal.ast.Expression x)
    {
      this.condition = x;
    }
    public org.meta_environment.rascal.ast.IfThenElse setCondition (org.
								    meta_environment.
								    rascal.
								    ast.
								    Expression
								    x)
    {
      org.meta_environment.rascal.ast.IfThenElse z = new IfThenElse ();
      z.$setCondition (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression thenExp;
    public org.meta_environment.rascal.ast.Expression getThenExp ()
    {
      return thenExp;
    }
    private void $setThenExp (org.meta_environment.rascal.ast.Expression x)
    {
      this.thenExp = x;
    }
    public org.meta_environment.rascal.ast.IfThenElse setThenExp (org.
								  meta_environment.
								  rascal.ast.
								  Expression
								  x)
    {
      org.meta_environment.rascal.ast.IfThenElse z = new IfThenElse ();
      z.$setThenExp (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression elseExp;
    public org.meta_environment.rascal.ast.Expression getElseExp ()
    {
      return elseExp;
    }
    private void $setElseExp (org.meta_environment.rascal.ast.Expression x)
    {
      this.elseExp = x;
    }
    public org.meta_environment.rascal.ast.IfThenElse setElseExp (org.
								  meta_environment.
								  rascal.ast.
								  Expression
								  x)
    {
      org.meta_environment.rascal.ast.IfThenElse z = new IfThenElse ();
      z.$setElseExp (x);
      return z;
    }
  }
  static public class Operator extends Expression
  {
/* operator:StandardOperator -> Expression {cons("Operator")} */
    private Operator ()
    {
    }
    /*package */ Operator (ITree tree,
			   org.meta_environment.rascal.ast.
			   StandardOperator operator)
    {
      this.tree = tree;
      this.operator = operator;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionOperator (this);
    }
    private org.meta_environment.rascal.ast.StandardOperator operator;
    public org.meta_environment.rascal.ast.StandardOperator getOperator ()
    {
      return operator;
    }
    private void $setOperator (org.meta_environment.rascal.ast.
			       StandardOperator x)
    {
      this.operator = x;
    }
    public org.meta_environment.rascal.ast.Operator setOperator (org.
								 meta_environment.
								 rascal.ast.
								 StandardOperator
								 x)
    {
      org.meta_environment.rascal.ast.Operator z = new Operator ();
      z.$setOperator (x);
      return z;
    }
  }
  static public class Literal extends Expression
  {
/* literal:Literal -> Expression {cons("Literal")} */
    private Literal ()
    {
    }
    /*package */ Literal (ITree tree,
			  org.meta_environment.rascal.ast.Literal literal)
    {
      this.tree = tree;
      this.literal = literal;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionLiteral (this);
    }
    private org.meta_environment.rascal.ast.Literal literal;
    public org.meta_environment.rascal.ast.Literal getLiteral ()
    {
      return literal;
    }
    private void $setLiteral (org.meta_environment.rascal.ast.Literal x)
    {
      this.literal = x;
    }
    public org.meta_environment.rascal.ast.Literal setLiteral (org.
							       meta_environment.
							       rascal.ast.
							       Literal x)
    {
      org.meta_environment.rascal.ast.Literal z = new Literal ();
      z.$setLiteral (x);
      return z;
    }
  }
  static public class CallOrTree extends Expression
  {
/* name:Name "(" arguments:{Expression ","}* ")" -> Expression {cons("CallOrTree")} */
    private CallOrTree ()
    {
    }
    /*package */ CallOrTree (ITree tree,
			     org.meta_environment.rascal.ast.Name name,
			     java.util.List < Expression > arguments)
    {
      this.tree = tree;
      this.name = name;
      this.arguments = arguments;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionCallOrTree (this);
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
    public org.meta_environment.rascal.ast.CallOrTree setName (org.
							       meta_environment.
							       rascal.ast.
							       Name x)
    {
      org.meta_environment.rascal.ast.CallOrTree z = new CallOrTree ();
      z.$setName (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Expression >
      arguments;
    public java.util.List < org.meta_environment.rascal.ast.Expression >
      getArguments ()
    {
      return arguments;
    }
    private void $setArguments (java.util.List <
				org.meta_environment.rascal.ast.Expression >
				x)
    {
      this.arguments = x;
    }
    public org.meta_environment.rascal.ast.CallOrTree setArguments (java.util.
								    List <
								    org.
								    meta_environment.
								    rascal.
								    ast.
								    Expression
								    > x)
    {
      org.meta_environment.rascal.ast.CallOrTree z = new CallOrTree ();
      z.$setArguments (x);
      return z;
    }
  }
  static public class List extends Expression
  {
/* "[" elements:{Expression ","}* "]" -> Expression {cons("List")} */
    private List ()
    {
    }
    /*package */ List (ITree tree, java.util.List < Expression > elements)
    {
      this.tree = tree;
      this.elements = elements;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionList (this);
    }
    private java.util.List < org.meta_environment.rascal.ast.Expression >
      elements;
    public java.util.List < org.meta_environment.rascal.ast.Expression >
      getElements ()
    {
      return elements;
    }
    private void $setElements (java.util.List <
			       org.meta_environment.rascal.ast.Expression > x)
    {
      this.elements = x;
    }
    public org.meta_environment.rascal.ast.List setElements (java.util.List <
							     org.
							     meta_environment.
							     rascal.ast.
							     Expression > x)
    {
      org.meta_environment.rascal.ast.List z = new List ();
      z.$setElements (x);
      return z;
    }
  }
  static public class Set extends Expression
  {
/* "{" elements:{Expression ","}* "}" -> Expression {cons("Set")} */
    private Set ()
    {
    }
    /*package */ Set (ITree tree, java.util.List < Expression > elements)
    {
      this.tree = tree;
      this.elements = elements;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionSet (this);
    }
    private java.util.List < org.meta_environment.rascal.ast.Expression >
      elements;
    public java.util.List < org.meta_environment.rascal.ast.Expression >
      getElements ()
    {
      return elements;
    }
    private void $setElements (java.util.List <
			       org.meta_environment.rascal.ast.Expression > x)
    {
      this.elements = x;
    }
    public org.meta_environment.rascal.ast.Set setElements (java.util.List <
							    org.
							    meta_environment.
							    rascal.ast.
							    Expression > x)
    {
      org.meta_environment.rascal.ast.Set z = new Set ();
      z.$setElements (x);
      return z;
    }
  }
  static public class Tuple extends Expression
  {
/* "<" first:Expression "," rest:{Expression ","}+ ">" -> Expression {cons("Tuple")} */
    private Tuple ()
    {
    }
    /*package */ Tuple (ITree tree,
			org.meta_environment.rascal.ast.Expression first,
			java.util.List < Expression > rest)
    {
      this.tree = tree;
      this.first = first;
      this.rest = rest;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionTuple (this);
    }
    private org.meta_environment.rascal.ast.Expression first;
    public org.meta_environment.rascal.ast.Expression getFirst ()
    {
      return first;
    }
    private void $setFirst (org.meta_environment.rascal.ast.Expression x)
    {
      this.first = x;
    }
    public org.meta_environment.rascal.ast.Tuple setFirst (org.
							   meta_environment.
							   rascal.ast.
							   Expression x)
    {
      org.meta_environment.rascal.ast.Tuple z = new Tuple ();
      z.$setFirst (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Expression >
      rest;
    public java.util.List < org.meta_environment.rascal.ast.Expression >
      getRest ()
    {
      return rest;
    }
    private void $setRest (java.util.List <
			   org.meta_environment.rascal.ast.Expression > x)
    {
      this.rest = x;
    }
    public org.meta_environment.rascal.ast.Tuple setRest (java.util.List <
							  org.
							  meta_environment.
							  rascal.ast.
							  Expression > x)
    {
      org.meta_environment.rascal.ast.Tuple z = new Tuple ();
      z.$setRest (x);
      return z;
    }
  }
  static public class MapTuple extends Expression
  {
/* "<" from:Expression "->" to:Expression ">" -> Expression {cons("MapTuple")} */
    private MapTuple ()
    {
    }
    /*package */ MapTuple (ITree tree,
			   org.meta_environment.rascal.ast.Expression from,
			   org.meta_environment.rascal.ast.Expression to)
    {
      this.tree = tree;
      this.from = from;
      this.to = to;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionMapTuple (this);
    }
    private org.meta_environment.rascal.ast.Expression from;
    public org.meta_environment.rascal.ast.Expression getFrom ()
    {
      return from;
    }
    private void $setFrom (org.meta_environment.rascal.ast.Expression x)
    {
      this.from = x;
    }
    public org.meta_environment.rascal.ast.MapTuple setFrom (org.
							     meta_environment.
							     rascal.ast.
							     Expression x)
    {
      org.meta_environment.rascal.ast.MapTuple z = new MapTuple ();
      z.$setFrom (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression to;
    public org.meta_environment.rascal.ast.Expression getTo ()
    {
      return to;
    }
    private void $setTo (org.meta_environment.rascal.ast.Expression x)
    {
      this.to = x;
    }
    public org.meta_environment.rascal.ast.MapTuple setTo (org.
							   meta_environment.
							   rascal.ast.
							   Expression x)
    {
      org.meta_environment.rascal.ast.MapTuple z = new MapTuple ();
      z.$setTo (x);
      return z;
    }
  }
  static public class Location extends Expression
  {
/* Location -> Expression {cons("Location")} */
    private Location ()
    {
    }
    /*package */ Location (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionLocation (this);
    }
  }
  static public class Area extends Expression
  {
/* Area -> Expression {cons("Area")} */
    private Area ()
    {
    }
    /*package */ Area (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionArea (this);
    }
  }
  static public class FileLocation extends Expression
  {
/* "file" "(" filename:Expression ")" -> Expression {cons("FileLocation")} */
    private FileLocation ()
    {
    }
    /*package */ FileLocation (ITree tree,
			       org.meta_environment.rascal.ast.
			       Expression filename)
    {
      this.tree = tree;
      this.filename = filename;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionFileLocation (this);
    }
    private org.meta_environment.rascal.ast.Expression filename;
    public org.meta_environment.rascal.ast.Expression getFilename ()
    {
      return filename;
    }
    private void $setFilename (org.meta_environment.rascal.ast.Expression x)
    {
      this.filename = x;
    }
    public org.meta_environment.rascal.ast.FileLocation setFilename (org.
								     meta_environment.
								     rascal.
								     ast.
								     Expression
								     x)
    {
      org.meta_environment.rascal.ast.FileLocation z = new FileLocation ();
      z.$setFilename (x);
      return z;
    }
  }
  static public class AreaLocation extends Expression
  {
/* "area" "(" area:Expression ")" -> Expression {cons("AreaLocation")} */
    private AreaLocation ()
    {
    }
    /*package */ AreaLocation (ITree tree,
			       org.meta_environment.rascal.ast.
			       Expression area)
    {
      this.tree = tree;
      this.area = area;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionAreaLocation (this);
    }
    private org.meta_environment.rascal.ast.Expression area;
    public org.meta_environment.rascal.ast.Expression getArea ()
    {
      return area;
    }
    private void $setArea (org.meta_environment.rascal.ast.Expression x)
    {
      this.area = x;
    }
    public org.meta_environment.rascal.ast.AreaLocation setArea (org.
								 meta_environment.
								 rascal.ast.
								 Expression x)
    {
      org.meta_environment.rascal.ast.AreaLocation z = new AreaLocation ();
      z.$setArea (x);
      return z;
    }
  }
  static public class AreaInFileLocation extends Expression
  {
/* "area-in-file" "(" filename:Expression "," area:Expression ")" -> Expression {cons("AreaInFileLocation")} */
    private AreaInFileLocation ()
    {
    }
    /*package */ AreaInFileLocation (ITree tree,
				     org.meta_environment.rascal.ast.
				     Expression filename,
				     org.meta_environment.rascal.ast.
				     Expression area)
    {
      this.tree = tree;
      this.filename = filename;
      this.area = area;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionAreaInFileLocation (this);
    }
    private org.meta_environment.rascal.ast.Expression filename;
    public org.meta_environment.rascal.ast.Expression getFilename ()
    {
      return filename;
    }
    private void $setFilename (org.meta_environment.rascal.ast.Expression x)
    {
      this.filename = x;
    }
    public org.meta_environment.rascal.ast.
      AreaInFileLocation setFilename (org.meta_environment.rascal.ast.
				      Expression x)
    {
      org.meta_environment.rascal.ast.AreaInFileLocation z =
	new AreaInFileLocation ();
      z.$setFilename (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression area;
    public org.meta_environment.rascal.ast.Expression getArea ()
    {
      return area;
    }
    private void $setArea (org.meta_environment.rascal.ast.Expression x)
    {
      this.area = x;
    }
    public org.meta_environment.rascal.ast.AreaInFileLocation setArea (org.
								       meta_environment.
								       rascal.
								       ast.
								       Expression
								       x)
    {
      org.meta_environment.rascal.ast.AreaInFileLocation z =
	new AreaInFileLocation ();
      z.$setArea (x);
      return z;
    }
  }
  static public class QualifiedName extends Expression
  {
/* qualifiedName:QualifiedName -> Expression {cons("QualifiedName")} */
    private QualifiedName ()
    {
    }
    /*package */ QualifiedName (ITree tree,
				org.meta_environment.rascal.ast.
				QualifiedName qualifiedName)
    {
      this.tree = tree;
      this.qualifiedName = qualifiedName;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionQualifiedName (this);
    }
    private org.meta_environment.rascal.ast.QualifiedName qualifiedName;
    public org.meta_environment.rascal.ast.QualifiedName getQualifiedName ()
    {
      return qualifiedName;
    }
    private void $setQualifiedName (org.meta_environment.rascal.ast.
				    QualifiedName x)
    {
      this.qualifiedName = x;
    }
    public org.meta_environment.rascal.ast.
      QualifiedName setQualifiedName (org.meta_environment.rascal.ast.
				      QualifiedName x)
    {
      org.meta_environment.rascal.ast.QualifiedName z = new QualifiedName ();
      z.$setQualifiedName (x);
      return z;
    }
  }
  static public class TypedVariable extends Expression
  {
/* type:Type name:Name -> Expression {cons("TypedVariable")} */
    private TypedVariable ()
    {
    }
    /*package */ TypedVariable (ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionTypedVariable (this);
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
    public org.meta_environment.rascal.ast.TypedVariable setType (org.
								  meta_environment.
								  rascal.ast.
								  Type x)
    {
      org.meta_environment.rascal.ast.TypedVariable z = new TypedVariable ();
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
    public org.meta_environment.rascal.ast.TypedVariable setName (org.
								  meta_environment.
								  rascal.ast.
								  Name x)
    {
      org.meta_environment.rascal.ast.TypedVariable z = new TypedVariable ();
      z.$setName (x);
      return z;
    }
  }
  static public class Match extends Expression
  {
/* pattern:Expression ":=" expression:Expression -> Expression {cons("Match")} */
    private Match ()
    {
    }
    /*package */ Match (ITree tree,
			org.meta_environment.rascal.ast.Expression pattern,
			org.meta_environment.rascal.ast.Expression expression)
    {
      this.tree = tree;
      this.pattern = pattern;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionMatch (this);
    }
    private org.meta_environment.rascal.ast.Expression pattern;
    public org.meta_environment.rascal.ast.Expression getPattern ()
    {
      return pattern;
    }
    private void $setPattern (org.meta_environment.rascal.ast.Expression x)
    {
      this.pattern = x;
    }
    public org.meta_environment.rascal.ast.Match setPattern (org.
							     meta_environment.
							     rascal.ast.
							     Expression x)
    {
      org.meta_environment.rascal.ast.Match z = new Match ();
      z.$setPattern (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public org.meta_environment.rascal.ast.Match setExpression (org.
								meta_environment.
								rascal.ast.
								Expression x)
    {
      org.meta_environment.rascal.ast.Match z = new Match ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class NoMatch extends Expression
  {
/* pattern:Expression "!:=" expression:Expression -> Expression {cons("NoMatch")} */
    private NoMatch ()
    {
    }
    /*package */ NoMatch (ITree tree,
			  org.meta_environment.rascal.ast.Expression pattern,
			  org.meta_environment.rascal.ast.
			  Expression expression)
    {
      this.tree = tree;
      this.pattern = pattern;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionNoMatch (this);
    }
    private org.meta_environment.rascal.ast.Expression pattern;
    public org.meta_environment.rascal.ast.Expression getPattern ()
    {
      return pattern;
    }
    private void $setPattern (org.meta_environment.rascal.ast.Expression x)
    {
      this.pattern = x;
    }
    public org.meta_environment.rascal.ast.NoMatch setPattern (org.
							       meta_environment.
							       rascal.ast.
							       Expression x)
    {
      org.meta_environment.rascal.ast.NoMatch z = new NoMatch ();
      z.$setPattern (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public org.meta_environment.rascal.ast.NoMatch setExpression (org.
								  meta_environment.
								  rascal.ast.
								  Expression
								  x)
    {
      org.meta_environment.rascal.ast.NoMatch z = new NoMatch ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class Comprehension extends Expression
  {
/* comprehension:Comprehension -> Expression {cons("Comprehension")} */
    private Comprehension ()
    {
    }
    /*package */ Comprehension (ITree tree,
				org.meta_environment.rascal.ast.
				Comprehension comprehension)
    {
      this.tree = tree;
      this.comprehension = comprehension;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionComprehension (this);
    }
    private org.meta_environment.rascal.ast.Comprehension comprehension;
    public org.meta_environment.rascal.ast.Comprehension getComprehension ()
    {
      return comprehension;
    }
    private void $setComprehension (org.meta_environment.rascal.ast.
				    Comprehension x)
    {
      this.comprehension = x;
    }
    public org.meta_environment.rascal.ast.
      Comprehension setComprehension (org.meta_environment.rascal.ast.
				      Comprehension x)
    {
      org.meta_environment.rascal.ast.Comprehension z = new Comprehension ();
      z.$setComprehension (x);
      return z;
    }
  }
  static public class ForAll extends Expression
  {
/* "forall" "(" producer:ValueProducer "|" expression:Expression ")" -> Expression {cons("ForAll")} */
    private ForAll ()
    {
    }
    /*package */ ForAll (ITree tree,
			 org.meta_environment.rascal.ast.
			 ValueProducer producer,
			 org.meta_environment.rascal.ast.
			 Expression expression)
    {
      this.tree = tree;
      this.producer = producer;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionForAll (this);
    }
    private org.meta_environment.rascal.ast.ValueProducer producer;
    public org.meta_environment.rascal.ast.ValueProducer getProducer ()
    {
      return producer;
    }
    private void $setProducer (org.meta_environment.rascal.ast.
			       ValueProducer x)
    {
      this.producer = x;
    }
    public org.meta_environment.rascal.ast.ForAll setProducer (org.
							       meta_environment.
							       rascal.ast.
							       ValueProducer
							       x)
    {
      org.meta_environment.rascal.ast.ForAll z = new ForAll ();
      z.$setProducer (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public org.meta_environment.rascal.ast.ForAll setExpression (org.
								 meta_environment.
								 rascal.ast.
								 Expression x)
    {
      org.meta_environment.rascal.ast.ForAll z = new ForAll ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class Exists extends Expression
  {
/* "exists" "(" producer:ValueProducer "|" expression:Expression ")" -> Expression {cons("Exists")} */
    private Exists ()
    {
    }
    /*package */ Exists (ITree tree,
			 org.meta_environment.rascal.ast.
			 ValueProducer producer,
			 org.meta_environment.rascal.ast.
			 Expression expression)
    {
      this.tree = tree;
      this.producer = producer;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionExists (this);
    }
    private org.meta_environment.rascal.ast.ValueProducer producer;
    public org.meta_environment.rascal.ast.ValueProducer getProducer ()
    {
      return producer;
    }
    private void $setProducer (org.meta_environment.rascal.ast.
			       ValueProducer x)
    {
      this.producer = x;
    }
    public org.meta_environment.rascal.ast.Exists setProducer (org.
							       meta_environment.
							       rascal.ast.
							       ValueProducer
							       x)
    {
      org.meta_environment.rascal.ast.Exists z = new Exists ();
      z.$setProducer (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public org.meta_environment.rascal.ast.Exists setExpression (org.
								 meta_environment.
								 rascal.ast.
								 Expression x)
    {
      org.meta_environment.rascal.ast.Exists z = new Exists ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class Visit extends Expression
  {
/* visit:Visit -> Expression {cons("Visit")} */
    private Visit ()
    {
    }
    /*package */ Visit (ITree tree,
			org.meta_environment.rascal.ast.Visit visit)
    {
      this.tree = tree;
      this.visit = visit;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionVisit (this);
    }
    private org.meta_environment.rascal.ast.Visit visit;
    public org.meta_environment.rascal.ast.Visit getVisit ()
    {
      return visit;
    }
    private void $setVisit (org.meta_environment.rascal.ast.Visit x)
    {
      this.visit = x;
    }
    public org.meta_environment.rascal.ast.Visit setVisit (org.
							   meta_environment.
							   rascal.ast.Visit x)
    {
      org.meta_environment.rascal.ast.Visit z = new Visit ();
      z.$setVisit (x);
      return z;
    }
  }
}
