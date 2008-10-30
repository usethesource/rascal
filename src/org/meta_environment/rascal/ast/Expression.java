package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Expression extends AbstractAST
{
  public class Closure extends Expression
  {
/* "fun" type:Type Parameters "{" statements:Statement* "}" -> Expression {cons("Closure")} */
    private Closure ()
    {
    }
    /*package */ Closure (ITree tree, Type type,
			  List < Statement > statements)
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
    public Type gettype ()
    {
      return type;
    }
    private void $settype (Type x)
    {
      this.type = x;
    }
    public Closure settype (Type x)
    {
      Closure z = new Closure ();
      z.$settype (x);
      return z;
    }
    private List < Statement > statements;
    public List < Statement > getstatements ()
    {
      return statements;
    }
    private void $setstatements (List < Statement > x)
    {
      this.statements = x;
    }
    public Closure setstatements (List < Statement > x)
    {
      Closure z = new Closure ();
      z.$setstatements (x);
      return z;
    }
  }
  public class Ambiguity extends Expression
  {
    private final List < Expression > alternatives;
    public Ambiguity (List < Expression > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Expression > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends Expression
  {
    /* "(" Expression ")" -> Expression {bracket} */
  }
  public class ClosureCall extends Expression
  {
/* "(" closure:Expression ")" "(" arguments:{Expression ","}* ")" -> Expression {cons("ClosureCall")} */
    private ClosureCall ()
    {
    }
    /*package */ ClosureCall (ITree tree, Expression closure,
			      List < Expression > arguments)
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
    public Expression getclosure ()
    {
      return closure;
    }
    private void $setclosure (Expression x)
    {
      this.closure = x;
    }
    public ClosureCall setclosure (Expression x)
    {
      ClosureCall z = new ClosureCall ();
      z.$setclosure (x);
      return z;
    }
    private List < Expression > arguments;
    public List < Expression > getarguments ()
    {
      return arguments;
    }
    private void $setarguments (List < Expression > x)
    {
      this.arguments = x;
    }
    public ClosureCall setarguments (List < Expression > x)
    {
      ClosureCall z = new ClosureCall ();
      z.$setarguments (x);
      return z;
    }
  }
  public class FieldUpdate extends Expression
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
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public FieldUpdate setexpression (Expression x)
    {
      FieldUpdate z = new FieldUpdate ();
      z.$setexpression (x);
      return z;
    }
    private Name key;
    public Name getkey ()
    {
      return key;
    }
    private void $setkey (Name x)
    {
      this.key = x;
    }
    public FieldUpdate setkey (Name x)
    {
      FieldUpdate z = new FieldUpdate ();
      z.$setkey (x);
      return z;
    }
    private Expression replacement;
    public Expression getreplacement ()
    {
      return replacement;
    }
    private void $setreplacement (Expression x)
    {
      this.replacement = x;
    }
    public FieldUpdate setreplacement (Expression x)
    {
      FieldUpdate z = new FieldUpdate ();
      z.$setreplacement (x);
      return z;
    }
  }
  public class FieldAccess extends Expression
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
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public FieldAccess setexpression (Expression x)
    {
      FieldAccess z = new FieldAccess ();
      z.$setexpression (x);
      return z;
    }
    private Name field;
    public Name getfield ()
    {
      return field;
    }
    private void $setfield (Name x)
    {
      this.field = x;
    }
    public FieldAccess setfield (Name x)
    {
      FieldAccess z = new FieldAccess ();
      z.$setfield (x);
      return z;
    }
  }
  public class Subscript extends Expression
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
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Subscript setexpression (Expression x)
    {
      Subscript z = new Subscript ();
      z.$setexpression (x);
      return z;
    }
    private Expression subscript;
    public Expression getsubscript ()
    {
      return subscript;
    }
    private void $setsubscript (Expression x)
    {
      this.subscript = x;
    }
    public Subscript setsubscript (Expression x)
    {
      Subscript z = new Subscript ();
      z.$setsubscript (x);
      return z;
    }
  }
  public class TransitiveReflexiveClosure extends Expression
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
    public Expression getargument ()
    {
      return argument;
    }
    private void $setargument (Expression x)
    {
      this.argument = x;
    }
    public TransitiveReflexiveClosure setargument (Expression x)
    {
      TransitiveReflexiveClosure z = new TransitiveReflexiveClosure ();
      z.$setargument (x);
      return z;
    }
  }
  public class TransitiveClosure extends Expression
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
    public Expression getargument ()
    {
      return argument;
    }
    private void $setargument (Expression x)
    {
      this.argument = x;
    }
    public TransitiveClosure setargument (Expression x)
    {
      TransitiveClosure z = new TransitiveClosure ();
      z.$setargument (x);
      return z;
    }
  }
  public class Annotation extends Expression
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
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Annotation setexpression (Expression x)
    {
      Annotation z = new Annotation ();
      z.$setexpression (x);
      return z;
    }
    private Name name;
    public Name getname ()
    {
      return name;
    }
    private void $setname (Name x)
    {
      this.name = x;
    }
    public Annotation setname (Name x)
    {
      Annotation z = new Annotation ();
      z.$setname (x);
      return z;
    }
  }
  public class Negation extends Expression
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
    public Expression getargument ()
    {
      return argument;
    }
    private void $setargument (Expression x)
    {
      this.argument = x;
    }
    public Negation setargument (Expression x)
    {
      Negation z = new Negation ();
      z.$setargument (x);
      return z;
    }
  }
  public class Product extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public Product setlhs (Expression x)
    {
      Product z = new Product ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public Product setrhs (Expression x)
    {
      Product z = new Product ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Intersection extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public Intersection setlhs (Expression x)
    {
      Intersection z = new Intersection ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public Intersection setrhs (Expression x)
    {
      Intersection z = new Intersection ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Division extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public Division setlhs (Expression x)
    {
      Division z = new Division ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public Division setrhs (Expression x)
    {
      Division z = new Division ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Addition extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public Addition setlhs (Expression x)
    {
      Addition z = new Addition ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public Addition setrhs (Expression x)
    {
      Addition z = new Addition ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Substraction extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public Substraction setlhs (Expression x)
    {
      Substraction z = new Substraction ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public Substraction setrhs (Expression x)
    {
      Substraction z = new Substraction ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Match extends Expression
  {
/* lhs:Expression "=~" rhs:Expression -> Expression {non-assoc, cons("Match")} */
    private Match ()
    {
    }
    /*package */ Match (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionMatch (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public Match setlhs (Expression x)
    {
      Match z = new Match ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public Match setrhs (Expression x)
    {
      Match z = new Match ();
      z.$setrhs (x);
      return z;
    }
  }
  public class NoMatch extends Expression
  {
/* lhs:Expression "!~" rhs:Expression -> Expression {non-assoc, cons("NoMatch")} */
    private NoMatch ()
    {
    }
    /*package */ NoMatch (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionNoMatch (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public NoMatch setlhs (Expression x)
    {
      NoMatch z = new NoMatch ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public NoMatch setrhs (Expression x)
    {
      NoMatch z = new NoMatch ();
      z.$setrhs (x);
      return z;
    }
  }
  public class LessThan extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public LessThan setlhs (Expression x)
    {
      LessThan z = new LessThan ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public LessThan setrhs (Expression x)
    {
      LessThan z = new LessThan ();
      z.$setrhs (x);
      return z;
    }
  }
  public class LessThanOrEq extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public LessThanOrEq setlhs (Expression x)
    {
      LessThanOrEq z = new LessThanOrEq ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public LessThanOrEq setrhs (Expression x)
    {
      LessThanOrEq z = new LessThanOrEq ();
      z.$setrhs (x);
      return z;
    }
  }
  public class GreaterThan extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public GreaterThan setlhs (Expression x)
    {
      GreaterThan z = new GreaterThan ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public GreaterThan setrhs (Expression x)
    {
      GreaterThan z = new GreaterThan ();
      z.$setrhs (x);
      return z;
    }
  }
  public class GreaterThanOrEq extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public GreaterThanOrEq setlhs (Expression x)
    {
      GreaterThanOrEq z = new GreaterThanOrEq ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public GreaterThanOrEq setrhs (Expression x)
    {
      GreaterThanOrEq z = new GreaterThanOrEq ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Equals extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public Equals setlhs (Expression x)
    {
      Equals z = new Equals ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public Equals setrhs (Expression x)
    {
      Equals z = new Equals ();
      z.$setrhs (x);
      return z;
    }
  }
  public class NonEquals extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public NonEquals setlhs (Expression x)
    {
      NonEquals z = new NonEquals ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public NonEquals setrhs (Expression x)
    {
      NonEquals z = new NonEquals ();
      z.$setrhs (x);
      return z;
    }
  }
  public class NotIn extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public NotIn setlhs (Expression x)
    {
      NotIn z = new NotIn ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public NotIn setrhs (Expression x)
    {
      NotIn z = new NotIn ();
      z.$setrhs (x);
      return z;
    }
  }
  public class In extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public In setlhs (Expression x)
    {
      In z = new In ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public In setrhs (Expression x)
    {
      In z = new In ();
      z.$setrhs (x);
      return z;
    }
  }
  public class And extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public And setlhs (Expression x)
    {
      And z = new And ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public And setrhs (Expression x)
    {
      And z = new And ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Or extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public Or setlhs (Expression x)
    {
      Or z = new Or ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public Or setrhs (Expression x)
    {
      Or z = new Or ();
      z.$setrhs (x);
      return z;
    }
  }
  public class IfDefined extends Expression
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
    public Expression getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Expression x)
    {
      this.lhs = x;
    }
    public IfDefined setlhs (Expression x)
    {
      IfDefined z = new IfDefined ();
      z.$setlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Expression x)
    {
      this.rhs = x;
    }
    public IfDefined setrhs (Expression x)
    {
      IfDefined z = new IfDefined ();
      z.$setrhs (x);
      return z;
    }
  }
  public class IfThenElse extends Expression
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
    public Expression getcondition ()
    {
      return condition;
    }
    private void $setcondition (Expression x)
    {
      this.condition = x;
    }
    public IfThenElse setcondition (Expression x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setcondition (x);
      return z;
    }
    private Expression thenExp;
    public Expression getthenExp ()
    {
      return thenExp;
    }
    private void $setthenExp (Expression x)
    {
      this.thenExp = x;
    }
    public IfThenElse setthenExp (Expression x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setthenExp (x);
      return z;
    }
    private Expression elseExp;
    public Expression getelseExp ()
    {
      return elseExp;
    }
    private void $setelseExp (Expression x)
    {
      this.elseExp = x;
    }
    public IfThenElse setelseExp (Expression x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setelseExp (x);
      return z;
    }
  }
  public class Operator extends Expression
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
    public StandardOperator getoperator ()
    {
      return operator;
    }
    private void $setoperator (StandardOperator x)
    {
      this.operator = x;
    }
    public Operator setoperator (StandardOperator x)
    {
      Operator z = new Operator ();
      z.$setoperator (x);
      return z;
    }
  }
  public class Literal extends Expression
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
    public Literal getliteral ()
    {
      return literal;
    }
    private void $setliteral (Literal x)
    {
      this.literal = x;
    }
    public Literal setliteral (Literal x)
    {
      Literal z = new Literal ();
      z.$setliteral (x);
      return z;
    }
  }
  public class CallOrTree extends Expression
  {
/* name:Name "(" arguments:{Expression ","}* ")" -> Expression {cons("CallOrTree")} */
    private CallOrTree ()
    {
    }
    /*package */ CallOrTree (ITree tree, Name name,
			     List < Expression > arguments)
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
    public Name getname ()
    {
      return name;
    }
    private void $setname (Name x)
    {
      this.name = x;
    }
    public CallOrTree setname (Name x)
    {
      CallOrTree z = new CallOrTree ();
      z.$setname (x);
      return z;
    }
    private List < Expression > arguments;
    public List < Expression > getarguments ()
    {
      return arguments;
    }
    private void $setarguments (List < Expression > x)
    {
      this.arguments = x;
    }
    public CallOrTree setarguments (List < Expression > x)
    {
      CallOrTree z = new CallOrTree ();
      z.$setarguments (x);
      return z;
    }
  }
  public class List extends Expression
  {
/* "[" elements:{Expression ","}* "]" -> Expression {cons("List")} */
    private List ()
    {
    }
    /*package */ List (ITree tree, List < Expression > elements)
    {
      this.tree = tree;
      this.elements = elements;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionList (this);
    }
    private List < Expression > elements;
    public List < Expression > getelements ()
    {
      return elements;
    }
    private void $setelements (List < Expression > x)
    {
      this.elements = x;
    }
    public List setelements (List < Expression > x)
    {
      List z = new List ();
      z.$setelements (x);
      return z;
    }
  }
  public class Range extends Expression
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
    public Expression getfrom ()
    {
      return from;
    }
    private void $setfrom (Expression x)
    {
      this.from = x;
    }
    public Range setfrom (Expression x)
    {
      Range z = new Range ();
      z.$setfrom (x);
      return z;
    }
    private Expression to;
    public Expression getto ()
    {
      return to;
    }
    private void $setto (Expression x)
    {
      this.to = x;
    }
    public Range setto (Expression x)
    {
      Range z = new Range ();
      z.$setto (x);
      return z;
    }
  }
  public class StepRange extends Expression
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
    public Expression getfrom ()
    {
      return from;
    }
    private void $setfrom (Expression x)
    {
      this.from = x;
    }
    public StepRange setfrom (Expression x)
    {
      StepRange z = new StepRange ();
      z.$setfrom (x);
      return z;
    }
    private Expression by;
    public Expression getby ()
    {
      return by;
    }
    private void $setby (Expression x)
    {
      this.by = x;
    }
    public StepRange setby (Expression x)
    {
      StepRange z = new StepRange ();
      z.$setby (x);
      return z;
    }
    private Expression to;
    public Expression getto ()
    {
      return to;
    }
    private void $setto (Expression x)
    {
      this.to = x;
    }
    public StepRange setto (Expression x)
    {
      StepRange z = new StepRange ();
      z.$setto (x);
      return z;
    }
  }
  public class Set extends Expression
  {
/* "{" elements:{Expression ","}* "}" -> Expression {cons("Set")} */
    private Set ()
    {
    }
    /*package */ Set (ITree tree, List < Expression > elements)
    {
      this.tree = tree;
      this.elements = elements;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionSet (this);
    }
    private List < Expression > elements;
    public List < Expression > getelements ()
    {
      return elements;
    }
    private void $setelements (List < Expression > x)
    {
      this.elements = x;
    }
    public Set setelements (List < Expression > x)
    {
      Set z = new Set ();
      z.$setelements (x);
      return z;
    }
  }
  public class Tuple extends Expression
  {
/* "<" first:Expression "," rest:{Expression ","}+ ">" -> Expression {cons("Tuple")} */
    private Tuple ()
    {
    }
    /*package */ Tuple (ITree tree, Expression first,
			List < Expression > rest)
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
    public Expression getfirst ()
    {
      return first;
    }
    private void $setfirst (Expression x)
    {
      this.first = x;
    }
    public Tuple setfirst (Expression x)
    {
      Tuple z = new Tuple ();
      z.$setfirst (x);
      return z;
    }
    private List < Expression > rest;
    public List < Expression > getrest ()
    {
      return rest;
    }
    private void $setrest (List < Expression > x)
    {
      this.rest = x;
    }
    public Tuple setrest (List < Expression > x)
    {
      Tuple z = new Tuple ();
      z.$setrest (x);
      return z;
    }
  }
  public class MapTuple extends Expression
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
    public Expression getfrom ()
    {
      return from;
    }
    private void $setfrom (Expression x)
    {
      this.from = x;
    }
    public MapTuple setfrom (Expression x)
    {
      MapTuple z = new MapTuple ();
      z.$setfrom (x);
      return z;
    }
    private Expression to;
    public Expression getto ()
    {
      return to;
    }
    private void $setto (Expression x)
    {
      this.to = x;
    }
    public MapTuple setto (Expression x)
    {
      MapTuple z = new MapTuple ();
      z.$setto (x);
      return z;
    }
  }
  public class Location extends Expression
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
  public class Area extends Expression
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
  public class FileLocation extends Expression
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
    public Expression getfilename ()
    {
      return filename;
    }
    private void $setfilename (Expression x)
    {
      this.filename = x;
    }
    public FileLocation setfilename (Expression x)
    {
      FileLocation z = new FileLocation ();
      z.$setfilename (x);
      return z;
    }
  }
  public class AreaLocation extends Expression
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
    public Expression getarea ()
    {
      return area;
    }
    private void $setarea (Expression x)
    {
      this.area = x;
    }
    public AreaLocation setarea (Expression x)
    {
      AreaLocation z = new AreaLocation ();
      z.$setarea (x);
      return z;
    }
  }
  public class AreaInFileLocation extends Expression
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
    public Expression getfilename ()
    {
      return filename;
    }
    private void $setfilename (Expression x)
    {
      this.filename = x;
    }
    public AreaInFileLocation setfilename (Expression x)
    {
      AreaInFileLocation z = new AreaInFileLocation ();
      z.$setfilename (x);
      return z;
    }
    private Expression area;
    public Expression getarea ()
    {
      return area;
    }
    private void $setarea (Expression x)
    {
      this.area = x;
    }
    public AreaInFileLocation setarea (Expression x)
    {
      AreaInFileLocation z = new AreaInFileLocation ();
      z.$setarea (x);
      return z;
    }
  }
  public class QualifiedName extends Expression
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
    public QualifiedName getqualifiedName ()
    {
      return qualifiedName;
    }
    private void $setqualifiedName (QualifiedName x)
    {
      this.qualifiedName = x;
    }
    public QualifiedName setqualifiedName (QualifiedName x)
    {
      QualifiedName z = new QualifiedName ();
      z.$setqualifiedName (x);
      return z;
    }
  }
  public class TypedVariablePattern extends Expression
  {
/* type:Type name:Name -> Expression {cons("TypedVariablePattern")} */
    private TypedVariablePattern ()
    {
    }
    /*package */ TypedVariablePattern (ITree tree, Type type, Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionTypedVariablePattern (this);
    }
    private Type type;
    public Type gettype ()
    {
      return type;
    }
    private void $settype (Type x)
    {
      this.type = x;
    }
    public TypedVariablePattern settype (Type x)
    {
      TypedVariablePattern z = new TypedVariablePattern ();
      z.$settype (x);
      return z;
    }
    private Name name;
    public Name getname ()
    {
      return name;
    }
    private void $setname (Name x)
    {
      this.name = x;
    }
    public TypedVariablePattern setname (Name x)
    {
      TypedVariablePattern z = new TypedVariablePattern ();
      z.$setname (x);
      return z;
    }
  }
  public class Comprehension extends Expression
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
    public Comprehension getcomprehension ()
    {
      return comprehension;
    }
    private void $setcomprehension (Comprehension x)
    {
      this.comprehension = x;
    }
    public Comprehension setcomprehension (Comprehension x)
    {
      Comprehension z = new Comprehension ();
      z.$setcomprehension (x);
      return z;
    }
  }
  public class Exists extends Expression
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
    public ValueProducer getproducer ()
    {
      return producer;
    }
    private void $setproducer (ValueProducer x)
    {
      this.producer = x;
    }
    public Exists setproducer (ValueProducer x)
    {
      Exists z = new Exists ();
      z.$setproducer (x);
      return z;
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Exists setexpression (Expression x)
    {
      Exists z = new Exists ();
      z.$setexpression (x);
      return z;
    }
  }
  public class Forall extends Expression
  {
/* "forall" "(" producers:ValueProducer "|" expression:Expression ")" -> Expression {cons("Forall")} */
    private Forall ()
    {
    }
    /*package */ Forall (ITree tree, ValueProducer producers,
			 Expression expression)
    {
      this.tree = tree;
      this.producers = producers;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitExpressionForall (this);
    }
    private ValueProducer producers;
    public ValueProducer getproducers ()
    {
      return producers;
    }
    private void $setproducers (ValueProducer x)
    {
      this.producers = x;
    }
    public Forall setproducers (ValueProducer x)
    {
      Forall z = new Forall ();
      z.$setproducers (x);
      return z;
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Forall setexpression (Expression x)
    {
      Forall z = new Forall ();
      z.$setexpression (x);
      return z;
    }
  }
  public class Visit extends Expression
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
    public Visit getvisit ()
    {
      return visit;
    }
    private void $setvisit (Visit x)
    {
      this.visit = x;
    }
    public Visit setvisit (Visit x)
    {
      Visit z = new Visit ();
      z.$setvisit (x);
      return z;
    }
  }
}
