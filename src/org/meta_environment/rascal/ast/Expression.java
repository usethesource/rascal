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
    /*package */ Closure (ITree tree, Type type,
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
    private Type type;
    public Type getType ()
    {
      return type;
    }
    private void $setType (Type x)
    {
      this.type = x;
    }
    public Closure setType (Type x)
    {
      Closure z = new Closure ();
      z.$setType (x);
      return z;
    }
    private java.util.List < Statement > statements;
    public java.util.List < Statement > getStatements ()
    {
      return statements;
    }
    private void $setStatements (java.util.List < Statement > x)
    {
      this.statements = x;
    }
    public Closure setStatements (java.util.List < Statement > x)
    {
      Closure z = new Closure ();
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
    /*package */ ClosureCall (ITree tree, Expression closure,
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
    private Expression closure;
    public Expression getClosure ()
    {
      return closure;
    }
    private void $setClosure (Expression x)
    {
      this.closure = x;
    }
    public ClosureCall setClosure (Expression x)
    {
      ClosureCall z = new ClosureCall ();
      z.$setClosure (x);
      return z;
    }
    private java.util.List < Expression > arguments;
    public java.util.List < Expression > getArguments ()
    {
      return arguments;
    }
    private void $setArguments (java.util.List < Expression > x)
    {
      this.arguments = x;
    }
    public ClosureCall setArguments (java.util.List < Expression > x)
    {
      ClosureCall z = new ClosureCall ();
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
    /*package */ Range (ITree tree, Expression from, Expression to)
    {
      this.tree = tree;
      this.from = from;
      this.to = to;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionRange (this);
    }
    private Expression from;
    public Expression getFrom ()
    {
      return from;
    }
    private void $setFrom (Expression x)
    {
      this.from = x;
    }
    public Range setFrom (Expression x)
    {
      Range z = new Range ();
      z.$setFrom (x);
      return z;
    }
    private Expression to;
    public Expression getTo ()
    {
      return to;
    }
    private void $setTo (Expression x)
    {
      this.to = x;
    }
    public Range setTo (Expression x)
    {
      Range z = new Range ();
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
    /*package */ StepRange (ITree tree, Expression from, Expression by,
			    Expression to)
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
    private Expression from;
    public Expression getFrom ()
    {
      return from;
    }
    private void $setFrom (Expression x)
    {
      this.from = x;
    }
    public StepRange setFrom (Expression x)
    {
      StepRange z = new StepRange ();
      z.$setFrom (x);
      return z;
    }
    private Expression by;
    public Expression getBy ()
    {
      return by;
    }
    private void $setBy (Expression x)
    {
      this.by = x;
    }
    public StepRange setBy (Expression x)
    {
      StepRange z = new StepRange ();
      z.$setBy (x);
      return z;
    }
    private Expression to;
    public Expression getTo ()
    {
      return to;
    }
    private void $setTo (Expression x)
    {
      this.to = x;
    }
    public StepRange setTo (Expression x)
    {
      StepRange z = new StepRange ();
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
    /*package */ FieldUpdate (ITree tree, Expression expression, Name key,
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
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public FieldUpdate setExpression (Expression x)
    {
      FieldUpdate z = new FieldUpdate ();
      z.$setExpression (x);
      return z;
    }
    private Name key;
    public Name getKey ()
    {
      return key;
    }
    private void $setKey (Name x)
    {
      this.key = x;
    }
    public FieldUpdate setKey (Name x)
    {
      FieldUpdate z = new FieldUpdate ();
      z.$setKey (x);
      return z;
    }
    private Expression replacement;
    public Expression getReplacement ()
    {
      return replacement;
    }
    private void $setReplacement (Expression x)
    {
      this.replacement = x;
    }
    public FieldUpdate setReplacement (Expression x)
    {
      FieldUpdate z = new FieldUpdate ();
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
    /*package */ FieldAccess (ITree tree, Expression expression, Name field)
    {
      this.tree = tree;
      this.expression = expression;
      this.field = field;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionFieldAccess (this);
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public FieldAccess setExpression (Expression x)
    {
      FieldAccess z = new FieldAccess ();
      z.$setExpression (x);
      return z;
    }
    private Name field;
    public Name getField ()
    {
      return field;
    }
    private void $setField (Name x)
    {
      this.field = x;
    }
    public FieldAccess setField (Name x)
    {
      FieldAccess z = new FieldAccess ();
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
    /*package */ Subscript (ITree tree, Expression expression,
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
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public Subscript setExpression (Expression x)
    {
      Subscript z = new Subscript ();
      z.$setExpression (x);
      return z;
    }
    private Expression subscript;
    public Expression getSubscript ()
    {
      return subscript;
    }
    private void $setSubscript (Expression x)
    {
      this.subscript = x;
    }
    public Subscript setSubscript (Expression x)
    {
      Subscript z = new Subscript ();
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
    /*package */ TransitiveReflexiveClosure (ITree tree, Expression argument)
    {
      this.tree = tree;
      this.argument = argument;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionTransitiveReflexiveClosure (this);
    }
    private Expression argument;
    public Expression getArgument ()
    {
      return argument;
    }
    private void $setArgument (Expression x)
    {
      this.argument = x;
    }
    public TransitiveReflexiveClosure setArgument (Expression x)
    {
      TransitiveReflexiveClosure z = new TransitiveReflexiveClosure ();
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
    /*package */ TransitiveClosure (ITree tree, Expression argument)
    {
      this.tree = tree;
      this.argument = argument;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionTransitiveClosure (this);
    }
    private Expression argument;
    public Expression getArgument ()
    {
      return argument;
    }
    private void $setArgument (Expression x)
    {
      this.argument = x;
    }
    public TransitiveClosure setArgument (Expression x)
    {
      TransitiveClosure z = new TransitiveClosure ();
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
    /*package */ Annotation (ITree tree, Expression expression, Name name)
    {
      this.tree = tree;
      this.expression = expression;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionAnnotation (this);
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public Annotation setExpression (Expression x)
    {
      Annotation z = new Annotation ();
      z.$setExpression (x);
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
  }
  static public class Negation extends Expression
  {
/* "!" argument:Expression -> Expression {cons("Negation")} */
    private Negation ()
    {
    }
    /*package */ Negation (ITree tree, Expression argument)
    {
      this.tree = tree;
      this.argument = argument;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionNegation (this);
    }
    private Expression argument;
    public Expression getArgument ()
    {
      return argument;
    }
    private void $setArgument (Expression x)
    {
      this.argument = x;
    }
    public Negation setArgument (Expression x)
    {
      Negation z = new Negation ();
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
    /*package */ Product (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionProduct (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public Product setLhs (Expression x)
    {
      Product z = new Product ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public Product setRhs (Expression x)
    {
      Product z = new Product ();
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
    /*package */ Intersection (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionIntersection (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public Intersection setLhs (Expression x)
    {
      Intersection z = new Intersection ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public Intersection setRhs (Expression x)
    {
      Intersection z = new Intersection ();
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
    /*package */ Division (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionDivision (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public Division setLhs (Expression x)
    {
      Division z = new Division ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public Division setRhs (Expression x)
    {
      Division z = new Division ();
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
    /*package */ Addition (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionAddition (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public Addition setLhs (Expression x)
    {
      Addition z = new Addition ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public Addition setRhs (Expression x)
    {
      Addition z = new Addition ();
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
    /*package */ Substraction (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionSubstraction (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public Substraction setLhs (Expression x)
    {
      Substraction z = new Substraction ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public Substraction setRhs (Expression x)
    {
      Substraction z = new Substraction ();
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
    /*package */ RegExpMatch (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionRegExpMatch (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public RegExpMatch setLhs (Expression x)
    {
      RegExpMatch z = new RegExpMatch ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public RegExpMatch setRhs (Expression x)
    {
      RegExpMatch z = new RegExpMatch ();
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
    /*package */ RegExpNoMatch (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionRegExpNoMatch (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public RegExpNoMatch setLhs (Expression x)
    {
      RegExpNoMatch z = new RegExpNoMatch ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public RegExpNoMatch setRhs (Expression x)
    {
      RegExpNoMatch z = new RegExpNoMatch ();
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
    /*package */ LessThan (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionLessThan (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public LessThan setLhs (Expression x)
    {
      LessThan z = new LessThan ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public LessThan setRhs (Expression x)
    {
      LessThan z = new LessThan ();
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
    /*package */ LessThanOrEq (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionLessThanOrEq (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public LessThanOrEq setLhs (Expression x)
    {
      LessThanOrEq z = new LessThanOrEq ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public LessThanOrEq setRhs (Expression x)
    {
      LessThanOrEq z = new LessThanOrEq ();
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
    /*package */ GreaterThan (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionGreaterThan (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public GreaterThan setLhs (Expression x)
    {
      GreaterThan z = new GreaterThan ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public GreaterThan setRhs (Expression x)
    {
      GreaterThan z = new GreaterThan ();
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
    /*package */ GreaterThanOrEq (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionGreaterThanOrEq (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public GreaterThanOrEq setLhs (Expression x)
    {
      GreaterThanOrEq z = new GreaterThanOrEq ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public GreaterThanOrEq setRhs (Expression x)
    {
      GreaterThanOrEq z = new GreaterThanOrEq ();
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
    /*package */ Equals (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionEquals (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public Equals setLhs (Expression x)
    {
      Equals z = new Equals ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public Equals setRhs (Expression x)
    {
      Equals z = new Equals ();
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
    /*package */ NonEquals (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionNonEquals (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public NonEquals setLhs (Expression x)
    {
      NonEquals z = new NonEquals ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public NonEquals setRhs (Expression x)
    {
      NonEquals z = new NonEquals ();
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
    /*package */ NotIn (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionNotIn (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public NotIn setLhs (Expression x)
    {
      NotIn z = new NotIn ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public NotIn setRhs (Expression x)
    {
      NotIn z = new NotIn ();
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
    /*package */ In (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionIn (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public In setLhs (Expression x)
    {
      In z = new In ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public In setRhs (Expression x)
    {
      In z = new In ();
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
    /*package */ And (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionAnd (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public And setLhs (Expression x)
    {
      And z = new And ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public And setRhs (Expression x)
    {
      And z = new And ();
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
    /*package */ Or (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionOr (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public Or setLhs (Expression x)
    {
      Or z = new Or ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public Or setRhs (Expression x)
    {
      Or z = new Or ();
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
    /*package */ IfDefined (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionIfDefined (this);
    }
    private Expression lhs;
    public Expression getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Expression x)
    {
      this.lhs = x;
    }
    public IfDefined setLhs (Expression x)
    {
      IfDefined z = new IfDefined ();
      z.$setLhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Expression x)
    {
      this.rhs = x;
    }
    public IfDefined setRhs (Expression x)
    {
      IfDefined z = new IfDefined ();
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
    /*package */ IfThenElse (ITree tree, Expression condition,
			     Expression thenExp, Expression elseExp)
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
    private Expression condition;
    public Expression getCondition ()
    {
      return condition;
    }
    private void $setCondition (Expression x)
    {
      this.condition = x;
    }
    public IfThenElse setCondition (Expression x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setCondition (x);
      return z;
    }
    private Expression thenExp;
    public Expression getThenExp ()
    {
      return thenExp;
    }
    private void $setThenExp (Expression x)
    {
      this.thenExp = x;
    }
    public IfThenElse setThenExp (Expression x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setThenExp (x);
      return z;
    }
    private Expression elseExp;
    public Expression getElseExp ()
    {
      return elseExp;
    }
    private void $setElseExp (Expression x)
    {
      this.elseExp = x;
    }
    public IfThenElse setElseExp (Expression x)
    {
      IfThenElse z = new IfThenElse ();
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
    /*package */ Operator (ITree tree, StandardOperator operator)
    {
      this.tree = tree;
      this.operator = operator;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionOperator (this);
    }
    private StandardOperator operator;
    public StandardOperator getOperator ()
    {
      return operator;
    }
    private void $setOperator (StandardOperator x)
    {
      this.operator = x;
    }
    public Operator setOperator (StandardOperator x)
    {
      Operator z = new Operator ();
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
    /*package */ Literal (ITree tree, Literal literal)
    {
      this.tree = tree;
      this.literal = literal;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionLiteral (this);
    }
    private Literal literal;
    public Literal getLiteral ()
    {
      return literal;
    }
    private void $setLiteral (Literal x)
    {
      this.literal = x;
    }
    public Literal setLiteral (Literal x)
    {
      Literal z = new Literal ();
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
    /*package */ CallOrTree (ITree tree, Name name,
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
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public CallOrTree setName (Name x)
    {
      CallOrTree z = new CallOrTree ();
      z.$setName (x);
      return z;
    }
    private java.util.List < Expression > arguments;
    public java.util.List < Expression > getArguments ()
    {
      return arguments;
    }
    private void $setArguments (java.util.List < Expression > x)
    {
      this.arguments = x;
    }
    public CallOrTree setArguments (java.util.List < Expression > x)
    {
      CallOrTree z = new CallOrTree ();
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
    private java.util.List < Expression > elements;
    public java.util.List < Expression > getElements ()
    {
      return elements;
    }
    private void $setElements (java.util.List < Expression > x)
    {
      this.elements = x;
    }
    public List setElements (java.util.List < Expression > x)
    {
      List z = new List ();
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
    private java.util.List < Expression > elements;
    public java.util.List < Expression > getElements ()
    {
      return elements;
    }
    private void $setElements (java.util.List < Expression > x)
    {
      this.elements = x;
    }
    public Set setElements (java.util.List < Expression > x)
    {
      Set z = new Set ();
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
    /*package */ Tuple (ITree tree, Expression first,
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
    private Expression first;
    public Expression getFirst ()
    {
      return first;
    }
    private void $setFirst (Expression x)
    {
      this.first = x;
    }
    public Tuple setFirst (Expression x)
    {
      Tuple z = new Tuple ();
      z.$setFirst (x);
      return z;
    }
    private java.util.List < Expression > rest;
    public java.util.List < Expression > getRest ()
    {
      return rest;
    }
    private void $setRest (java.util.List < Expression > x)
    {
      this.rest = x;
    }
    public Tuple setRest (java.util.List < Expression > x)
    {
      Tuple z = new Tuple ();
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
    /*package */ MapTuple (ITree tree, Expression from, Expression to)
    {
      this.tree = tree;
      this.from = from;
      this.to = to;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionMapTuple (this);
    }
    private Expression from;
    public Expression getFrom ()
    {
      return from;
    }
    private void $setFrom (Expression x)
    {
      this.from = x;
    }
    public MapTuple setFrom (Expression x)
    {
      MapTuple z = new MapTuple ();
      z.$setFrom (x);
      return z;
    }
    private Expression to;
    public Expression getTo ()
    {
      return to;
    }
    private void $setTo (Expression x)
    {
      this.to = x;
    }
    public MapTuple setTo (Expression x)
    {
      MapTuple z = new MapTuple ();
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
    /*package */ FileLocation (ITree tree, Expression filename)
    {
      this.tree = tree;
      this.filename = filename;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionFileLocation (this);
    }
    private Expression filename;
    public Expression getFilename ()
    {
      return filename;
    }
    private void $setFilename (Expression x)
    {
      this.filename = x;
    }
    public FileLocation setFilename (Expression x)
    {
      FileLocation z = new FileLocation ();
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
    /*package */ AreaLocation (ITree tree, Expression area)
    {
      this.tree = tree;
      this.area = area;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionAreaLocation (this);
    }
    private Expression area;
    public Expression getArea ()
    {
      return area;
    }
    private void $setArea (Expression x)
    {
      this.area = x;
    }
    public AreaLocation setArea (Expression x)
    {
      AreaLocation z = new AreaLocation ();
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
    /*package */ AreaInFileLocation (ITree tree, Expression filename,
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
    private Expression filename;
    public Expression getFilename ()
    {
      return filename;
    }
    private void $setFilename (Expression x)
    {
      this.filename = x;
    }
    public AreaInFileLocation setFilename (Expression x)
    {
      AreaInFileLocation z = new AreaInFileLocation ();
      z.$setFilename (x);
      return z;
    }
    private Expression area;
    public Expression getArea ()
    {
      return area;
    }
    private void $setArea (Expression x)
    {
      this.area = x;
    }
    public AreaInFileLocation setArea (Expression x)
    {
      AreaInFileLocation z = new AreaInFileLocation ();
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
    /*package */ QualifiedName (ITree tree, QualifiedName qualifiedName)
    {
      this.tree = tree;
      this.qualifiedName = qualifiedName;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionQualifiedName (this);
    }
    private QualifiedName qualifiedName;
    public QualifiedName getQualifiedName ()
    {
      return qualifiedName;
    }
    private void $setQualifiedName (QualifiedName x)
    {
      this.qualifiedName = x;
    }
    public QualifiedName setQualifiedName (QualifiedName x)
    {
      QualifiedName z = new QualifiedName ();
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
    /*package */ TypedVariable (ITree tree, Type type, Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionTypedVariable (this);
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
    public TypedVariable setType (Type x)
    {
      TypedVariable z = new TypedVariable ();
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
    public TypedVariable setName (Name x)
    {
      TypedVariable z = new TypedVariable ();
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
    /*package */ Match (ITree tree, Expression pattern, Expression expression)
    {
      this.tree = tree;
      this.pattern = pattern;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionMatch (this);
    }
    private Expression pattern;
    public Expression getPattern ()
    {
      return pattern;
    }
    private void $setPattern (Expression x)
    {
      this.pattern = x;
    }
    public Match setPattern (Expression x)
    {
      Match z = new Match ();
      z.$setPattern (x);
      return z;
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public Match setExpression (Expression x)
    {
      Match z = new Match ();
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
    /*package */ NoMatch (ITree tree, Expression pattern,
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
    private Expression pattern;
    public Expression getPattern ()
    {
      return pattern;
    }
    private void $setPattern (Expression x)
    {
      this.pattern = x;
    }
    public NoMatch setPattern (Expression x)
    {
      NoMatch z = new NoMatch ();
      z.$setPattern (x);
      return z;
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public NoMatch setExpression (Expression x)
    {
      NoMatch z = new NoMatch ();
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
    /*package */ Comprehension (ITree tree, Comprehension comprehension)
    {
      this.tree = tree;
      this.comprehension = comprehension;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionComprehension (this);
    }
    private Comprehension comprehension;
    public Comprehension getComprehension ()
    {
      return comprehension;
    }
    private void $setComprehension (Comprehension x)
    {
      this.comprehension = x;
    }
    public Comprehension setComprehension (Comprehension x)
    {
      Comprehension z = new Comprehension ();
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
    /*package */ ForAll (ITree tree, ValueProducer producer,
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
    private ValueProducer producer;
    public ValueProducer getProducer ()
    {
      return producer;
    }
    private void $setProducer (ValueProducer x)
    {
      this.producer = x;
    }
    public ForAll setProducer (ValueProducer x)
    {
      ForAll z = new ForAll ();
      z.$setProducer (x);
      return z;
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public ForAll setExpression (Expression x)
    {
      ForAll z = new ForAll ();
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
    /*package */ Exists (ITree tree, ValueProducer producer,
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
    private ValueProducer producer;
    public ValueProducer getProducer ()
    {
      return producer;
    }
    private void $setProducer (ValueProducer x)
    {
      this.producer = x;
    }
    public Exists setProducer (ValueProducer x)
    {
      Exists z = new Exists ();
      z.$setProducer (x);
      return z;
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public Exists setExpression (Expression x)
    {
      Exists z = new Exists ();
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
    /*package */ Visit (ITree tree, Visit visit)
    {
      this.tree = tree;
      this.visit = visit;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionVisit (this);
    }
    private Visit visit;
    public Visit getVisit ()
    {
      return visit;
    }
    private void $setVisit (Visit x)
    {
      this.visit = x;
    }
    public Visit setVisit (Visit x)
    {
      Visit z = new Visit ();
      z.$setVisit (x);
      return z;
    }
  }
}
