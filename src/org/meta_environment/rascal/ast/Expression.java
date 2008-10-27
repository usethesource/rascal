package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
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
      return visitor.visitClosureExpression (this);
    }
    private Type type;
    public Type gettype ()
    {
      return type;
    }
    private void privateSettype (Type x)
    {
      this.type = x;
    }
    public Closure settype (Type x)
    {
      Closure z = new Closure ();
      z.privateSettype (x);
      return z;
    }
    private List < Statement > statements;
    public List < Statement > getstatements ()
    {
      return statements;
    }
    private void privateSetstatements (List < Statement > x)
    {
      this.statements = x;
    }
    public Closure setstatements (List < Statement > x)
    {
      Closure z = new Closure ();
      z.privateSetstatements (x);
      return z;
    }
  }
  prod2class ("(" Expression ")"->Expression
	      {
	      bracket}
  )public class ClosureCall extends Expression
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
      return visitor.visitClosureCallExpression (this);
    }
    private Expression closure;
    public Expression getclosure ()
    {
      return closure;
    }
    private void privateSetclosure (Expression x)
    {
      this.closure = x;
    }
    public ClosureCall setclosure (Expression x)
    {
      ClosureCall z = new ClosureCall ();
      z.privateSetclosure (x);
      return z;
    }
    private List < Expression > arguments;
    public List < Expression > getarguments ()
    {
      return arguments;
    }
    private void privateSetarguments (List < Expression > x)
    {
      this.arguments = x;
    }
    public ClosureCall setarguments (List < Expression > x)
    {
      ClosureCall z = new ClosureCall ();
      z.privateSetarguments (x);
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
      return visitor.visitFieldAccessExpression (this);
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public FieldAccess setexpression (Expression x)
    {
      FieldAccess z = new FieldAccess ();
      z.privateSetexpression (x);
      return z;
    }
    private Name field;
    public Name getfield ()
    {
      return field;
    }
    private void privateSetfield (Name x)
    {
      this.field = x;
    }
    public FieldAccess setfield (Name x)
    {
      FieldAccess z = new FieldAccess ();
      z.privateSetfield (x);
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
      return visitor.visitSubscriptExpression (this);
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Subscript setexpression (Expression x)
    {
      Subscript z = new Subscript ();
      z.privateSetexpression (x);
      return z;
    }
    private Expression subscript;
    public Expression getsubscript ()
    {
      return subscript;
    }
    private void privateSetsubscript (Expression x)
    {
      this.subscript = x;
    }
    public Subscript setsubscript (Expression x)
    {
      Subscript z = new Subscript ();
      z.privateSetsubscript (x);
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
      return visitor.visitTransitiveReflexiveClosureExpression (this);
    }
    private Expression argument;
    public Expression getargument ()
    {
      return argument;
    }
    private void privateSetargument (Expression x)
    {
      this.argument = x;
    }
    public TransitiveReflexiveClosure setargument (Expression x)
    {
      TransitiveReflexiveClosure z = new TransitiveReflexiveClosure ();
      z.privateSetargument (x);
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
      return visitor.visitTransitiveClosureExpression (this);
    }
    private Expression argument;
    public Expression getargument ()
    {
      return argument;
    }
    private void privateSetargument (Expression x)
    {
      this.argument = x;
    }
    public TransitiveClosure setargument (Expression x)
    {
      TransitiveClosure z = new TransitiveClosure ();
      z.privateSetargument (x);
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
      return visitor.visitAnnotationExpression (this);
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Annotation setexpression (Expression x)
    {
      Annotation z = new Annotation ();
      z.privateSetexpression (x);
      return z;
    }
    private Name name;
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
      Annotation z = new Annotation ();
      z.privateSetname (x);
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
      return visitor.visitNegationExpression (this);
    }
    private Expression argument;
    public Expression getargument ()
    {
      return argument;
    }
    private void privateSetargument (Expression x)
    {
      this.argument = x;
    }
    public Negation setargument (Expression x)
    {
      Negation z = new Negation ();
      z.privateSetargument (x);
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
      return visitor.visitProductExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Product setlhs (Expression x)
    {
      Product z = new Product ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Product setrhs (Expression x)
    {
      Product z = new Product ();
      z.privateSetrhs (x);
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
      return visitor.visitIntersectionExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Intersection setlhs (Expression x)
    {
      Intersection z = new Intersection ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Intersection setrhs (Expression x)
    {
      Intersection z = new Intersection ();
      z.privateSetrhs (x);
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
      return visitor.visitDivisionExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Division setlhs (Expression x)
    {
      Division z = new Division ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Division setrhs (Expression x)
    {
      Division z = new Division ();
      z.privateSetrhs (x);
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
      return visitor.visitAdditionExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Addition setlhs (Expression x)
    {
      Addition z = new Addition ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Addition setrhs (Expression x)
    {
      Addition z = new Addition ();
      z.privateSetrhs (x);
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
      return visitor.visitSubstractionExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Substraction setlhs (Expression x)
    {
      Substraction z = new Substraction ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Substraction setrhs (Expression x)
    {
      Substraction z = new Substraction ();
      z.privateSetrhs (x);
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
      return visitor.visitMatchExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Match setlhs (Expression x)
    {
      Match z = new Match ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Match setrhs (Expression x)
    {
      Match z = new Match ();
      z.privateSetrhs (x);
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
      return visitor.visitNoMatchExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public NoMatch setlhs (Expression x)
    {
      NoMatch z = new NoMatch ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public NoMatch setrhs (Expression x)
    {
      NoMatch z = new NoMatch ();
      z.privateSetrhs (x);
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
      return visitor.visitLessThanExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public LessThan setlhs (Expression x)
    {
      LessThan z = new LessThan ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public LessThan setrhs (Expression x)
    {
      LessThan z = new LessThan ();
      z.privateSetrhs (x);
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
      return visitor.visitLessThanOrEqExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public LessThanOrEq setlhs (Expression x)
    {
      LessThanOrEq z = new LessThanOrEq ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public LessThanOrEq setrhs (Expression x)
    {
      LessThanOrEq z = new LessThanOrEq ();
      z.privateSetrhs (x);
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
      return visitor.visitGreaterThanExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public GreaterThan setlhs (Expression x)
    {
      GreaterThan z = new GreaterThan ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public GreaterThan setrhs (Expression x)
    {
      GreaterThan z = new GreaterThan ();
      z.privateSetrhs (x);
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
      return visitor.visitGreaterThanOrEqExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public GreaterThanOrEq setlhs (Expression x)
    {
      GreaterThanOrEq z = new GreaterThanOrEq ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public GreaterThanOrEq setrhs (Expression x)
    {
      GreaterThanOrEq z = new GreaterThanOrEq ();
      z.privateSetrhs (x);
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
      return visitor.visitEqualsExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Equals setlhs (Expression x)
    {
      Equals z = new Equals ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Equals setrhs (Expression x)
    {
      Equals z = new Equals ();
      z.privateSetrhs (x);
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
      return visitor.visitNonEqualsExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public NonEquals setlhs (Expression x)
    {
      NonEquals z = new NonEquals ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public NonEquals setrhs (Expression x)
    {
      NonEquals z = new NonEquals ();
      z.privateSetrhs (x);
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
      return visitor.visitNotInExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public NotIn setlhs (Expression x)
    {
      NotIn z = new NotIn ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public NotIn setrhs (Expression x)
    {
      NotIn z = new NotIn ();
      z.privateSetrhs (x);
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
      return visitor.visitInExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public In setlhs (Expression x)
    {
      In z = new In ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public In setrhs (Expression x)
    {
      In z = new In ();
      z.privateSetrhs (x);
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
      return visitor.visitAndExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public And setlhs (Expression x)
    {
      And z = new And ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public And setrhs (Expression x)
    {
      And z = new And ();
      z.privateSetrhs (x);
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
      return visitor.visitOrExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Or setlhs (Expression x)
    {
      Or z = new Or ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Or setrhs (Expression x)
    {
      Or z = new Or ();
      z.privateSetrhs (x);
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
      return visitor.visitIfDefinedExpression (this);
    }
    private Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public IfDefined setlhs (Expression x)
    {
      IfDefined z = new IfDefined ();
      z.privateSetlhs (x);
      return z;
    }
    private Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public IfDefined setrhs (Expression x)
    {
      IfDefined z = new IfDefined ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class IfThenElse extends Expression
  {
/* condition:Expression "?" then:Expression ":" else:Expression -> Expression {right, cons("IfThenElse")} */
    private IfThenElse ()
    {
    }
    /*package */ IfThenElse (ITree tree, Expression condition,
			     Expression then, Expression
			     else
    )
    {
      this.tree = tree;
      this.condition = condition;
      this.then = then;
      this.
      else
      =
      else;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitIfThenElseExpression (this);
    }
    private Expression condition;
    public Expression getcondition ()
    {
      return condition;
    }
    private void privateSetcondition (Expression x)
    {
      this.condition = x;
    }
    public IfThenElse setcondition (Expression x)
    {
      IfThenElse z = new IfThenElse ();
      z.privateSetcondition (x);
      return z;
    }
    private Expression then;
    public Expression getthen ()
    {
      return then;
    }
    private void privateSetthen (Expression x)
    {
      this.then = x;
    }
    public IfThenElse setthen (Expression x)
    {
      IfThenElse z = new IfThenElse ();
      z.privateSetthen (x);
      return z;
    }
    private Expression
    else;
    public Expression getelse ()
    {
      return
      else;
    }
    private void privateSetelse (Expression x)
    {
      this.
      else
      = x;
    }
    public IfThenElse setelse (Expression x)
    {
      IfThenElse z = new IfThenElse ();
      z.privateSetelse (x);
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
      return visitor.visitOperatorExpression (this);
    }
    private StandardOperator operator;
    public StandardOperator getoperator ()
    {
      return operator;
    }
    private void privateSetoperator (StandardOperator x)
    {
      this.operator = x;
    }
    public Operator setoperator (StandardOperator x)
    {
      Operator z = new Operator ();
      z.privateSetoperator (x);
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
      return visitor.visitLiteralExpression (this);
    }
    private Literal literal;
    public Literal getliteral ()
    {
      return literal;
    }
    private void privateSetliteral (Literal x)
    {
      this.literal = x;
    }
    public Literal setliteral (Literal x)
    {
      Literal z = new Literal ();
      z.privateSetliteral (x);
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
      return visitor.visitCallOrTreeExpression (this);
    }
    private Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public CallOrTree setname (Name x)
    {
      CallOrTree z = new CallOrTree ();
      z.privateSetname (x);
      return z;
    }
    private List < Expression > arguments;
    public List < Expression > getarguments ()
    {
      return arguments;
    }
    private void privateSetarguments (List < Expression > x)
    {
      this.arguments = x;
    }
    public CallOrTree setarguments (List < Expression > x)
    {
      CallOrTree z = new CallOrTree ();
      z.privateSetarguments (x);
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
      return visitor.visitListExpression (this);
    }
    private List < Expression > elements;
    public List < Expression > getelements ()
    {
      return elements;
    }
    private void privateSetelements (List < Expression > x)
    {
      this.elements = x;
    }
    public List setelements (List < Expression > x)
    {
      List z = new List ();
      z.privateSetelements (x);
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
      return visitor.visitRangeExpression (this);
    }
    private Expression from;
    public Expression getfrom ()
    {
      return from;
    }
    private void privateSetfrom (Expression x)
    {
      this.from = x;
    }
    public Range setfrom (Expression x)
    {
      Range z = new Range ();
      z.privateSetfrom (x);
      return z;
    }
    private Expression to;
    public Expression getto ()
    {
      return to;
    }
    private void privateSetto (Expression x)
    {
      this.to = x;
    }
    public Range setto (Expression x)
    {
      Range z = new Range ();
      z.privateSetto (x);
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
      return visitor.visitStepRangeExpression (this);
    }
    private Expression from;
    public Expression getfrom ()
    {
      return from;
    }
    private void privateSetfrom (Expression x)
    {
      this.from = x;
    }
    public StepRange setfrom (Expression x)
    {
      StepRange z = new StepRange ();
      z.privateSetfrom (x);
      return z;
    }
    private Expression by;
    public Expression getby ()
    {
      return by;
    }
    private void privateSetby (Expression x)
    {
      this.by = x;
    }
    public StepRange setby (Expression x)
    {
      StepRange z = new StepRange ();
      z.privateSetby (x);
      return z;
    }
    private Expression to;
    public Expression getto ()
    {
      return to;
    }
    private void privateSetto (Expression x)
    {
      this.to = x;
    }
    public StepRange setto (Expression x)
    {
      StepRange z = new StepRange ();
      z.privateSetto (x);
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
      return visitor.visitSetExpression (this);
    }
    private List < Expression > elements;
    public List < Expression > getelements ()
    {
      return elements;
    }
    private void privateSetelements (List < Expression > x)
    {
      this.elements = x;
    }
    public Set setelements (List < Expression > x)
    {
      Set z = new Set ();
      z.privateSetelements (x);
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
      return visitor.visitTupleExpression (this);
    }
    private Expression first;
    public Expression getfirst ()
    {
      return first;
    }
    private void privateSetfirst (Expression x)
    {
      this.first = x;
    }
    public Tuple setfirst (Expression x)
    {
      Tuple z = new Tuple ();
      z.privateSetfirst (x);
      return z;
    }
    private List < Expression > rest;
    public List < Expression > getrest ()
    {
      return rest;
    }
    private void privateSetrest (List < Expression > x)
    {
      this.rest = x;
    }
    public Tuple setrest (List < Expression > x)
    {
      Tuple z = new Tuple ();
      z.privateSetrest (x);
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
      return visitor.visitMapTupleExpression (this);
    }
    private Expression from;
    public Expression getfrom ()
    {
      return from;
    }
    private void privateSetfrom (Expression x)
    {
      this.from = x;
    }
    public MapTuple setfrom (Expression x)
    {
      MapTuple z = new MapTuple ();
      z.privateSetfrom (x);
      return z;
    }
    private Expression to;
    public Expression getto ()
    {
      return to;
    }
    private void privateSetto (Expression x)
    {
      this.to = x;
    }
    public MapTuple setto (Expression x)
    {
      MapTuple z = new MapTuple ();
      z.privateSetto (x);
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
      return visitor.visitLocationExpression (this);
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
      return visitor.visitAreaExpression (this);
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
      return visitor.visitFileLocationExpression (this);
    }
    private Expression filename;
    public Expression getfilename ()
    {
      return filename;
    }
    private void privateSetfilename (Expression x)
    {
      this.filename = x;
    }
    public FileLocation setfilename (Expression x)
    {
      FileLocation z = new FileLocation ();
      z.privateSetfilename (x);
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
      return visitor.visitAreaLocationExpression (this);
    }
    private Expression area;
    public Expression getarea ()
    {
      return area;
    }
    private void privateSetarea (Expression x)
    {
      this.area = x;
    }
    public AreaLocation setarea (Expression x)
    {
      AreaLocation z = new AreaLocation ();
      z.privateSetarea (x);
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
      return visitor.visitAreaInFileLocationExpression (this);
    }
    private Expression filename;
    public Expression getfilename ()
    {
      return filename;
    }
    private void privateSetfilename (Expression x)
    {
      this.filename = x;
    }
    public AreaInFileLocation setfilename (Expression x)
    {
      AreaInFileLocation z = new AreaInFileLocation ();
      z.privateSetfilename (x);
      return z;
    }
    private Expression area;
    public Expression getarea ()
    {
      return area;
    }
    private void privateSetarea (Expression x)
    {
      this.area = x;
    }
    public AreaInFileLocation setarea (Expression x)
    {
      AreaInFileLocation z = new AreaInFileLocation ();
      z.privateSetarea (x);
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
      return visitor.visitQualifiedNameExpression (this);
    }
    private QualifiedName qualifiedName;
    public QualifiedName getqualifiedName ()
    {
      return qualifiedName;
    }
    private void privateSetqualifiedName (QualifiedName x)
    {
      this.qualifiedName = x;
    }
    public QualifiedName setqualifiedName (QualifiedName x)
    {
      QualifiedName z = new QualifiedName ();
      z.privateSetqualifiedName (x);
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
      return visitor.visitComprehensionExpression (this);
    }
    private Comprehension comprehension;
    public Comprehension getcomprehension ()
    {
      return comprehension;
    }
    private void privateSetcomprehension (Comprehension x)
    {
      this.comprehension = x;
    }
    public Comprehension setcomprehension (Comprehension x)
    {
      Comprehension z = new Comprehension ();
      z.privateSetcomprehension (x);
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
      return visitor.visitExistsExpression (this);
    }
    private ValueProducer producer;
    public ValueProducer getproducer ()
    {
      return producer;
    }
    private void privateSetproducer (ValueProducer x)
    {
      this.producer = x;
    }
    public Exists setproducer (ValueProducer x)
    {
      Exists z = new Exists ();
      z.privateSetproducer (x);
      return z;
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Exists setexpression (Expression x)
    {
      Exists z = new Exists ();
      z.privateSetexpression (x);
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
      return visitor.visitForallExpression (this);
    }
    private ValueProducer producers;
    public ValueProducer getproducers ()
    {
      return producers;
    }
    private void privateSetproducers (ValueProducer x)
    {
      this.producers = x;
    }
    public Forall setproducers (ValueProducer x)
    {
      Forall z = new Forall ();
      z.privateSetproducers (x);
      return z;
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Forall setexpression (Expression x)
    {
      Forall z = new Forall ();
      z.privateSetexpression (x);
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
      return visitor.visitVisitExpression (this);
    }
    private Visit visit;
    public Visit getvisit ()
    {
      return visit;
    }
    private void privateSetvisit (Visit x)
    {
      this.visit = x;
    }
    public Visit setvisit (Visit x)
    {
      Visit z = new Visit ();
      z.privateSetvisit (x);
      return z;
    }
  }
}
