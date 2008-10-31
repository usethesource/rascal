package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public class ASTFactory
{
  java.util.Map < AbstractAST, AbstractAST > table =
    new java.util.Hashtable < AbstractAST, AbstractAST > ();

  public Body.Toplevels makeBodyToplevels (ITree tree,
					   java.util.List < Toplevel >
					   toplevels)
  {
    org.meta_environment.rascal.ast.Body.Toplevels x =
      new org.meta_environment.rascal.ast.Body.Toplevels (tree, toplevels);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Body.Toplevels) table.get (x);
  }
  public Formal.TypeName makeFormalTypeName (ITree tree,
					     org.meta_environment.rascal.ast.
					     Type type,
					     org.meta_environment.rascal.ast.
					     Name name)
  {
    org.meta_environment.rascal.ast.Formal.TypeName x =
      new org.meta_environment.rascal.ast.Formal.TypeName (tree, type, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Formal.TypeName) table.get (x);
  }
  public Formals.Default makeFormalsDefault (ITree tree,
					     java.util.List < Formal >
					     formals)
  {
    org.meta_environment.rascal.ast.Formals.Default x =
      new org.meta_environment.rascal.ast.Formals.Default (tree, formals);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Formals.Default) table.get (x);
  }
  public Parameters.VarArgs makeParametersVarArgs (ITree tree,
						   org.meta_environment.
						   rascal.ast.Formals formals)
  {
    org.meta_environment.rascal.ast.Parameters.VarArgs x =
      new org.meta_environment.rascal.ast.Parameters.VarArgs (tree, formals);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Parameters.VarArgs) table.get (x);
  }
  public Parameters.Default makeParametersDefault (ITree tree,
						   org.meta_environment.
						   rascal.ast.Formals formals)
  {
    org.meta_environment.rascal.ast.Parameters.Default x =
      new org.meta_environment.rascal.ast.Parameters.Default (tree, formals);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Parameters.Default) table.get (x);
  }
  public Expression.Visit makeExpressionVisit (ITree tree,
					       org.meta_environment.rascal.
					       ast.Visit visit)
  {
    org.meta_environment.rascal.ast.Expression.Visit x =
      new org.meta_environment.rascal.ast.Expression.Visit (tree, visit);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Visit) table.get (x);
  }
  public Expression.Exists makeExpressionExists (ITree tree,
						 org.meta_environment.rascal.
						 ast.ValueProducer producer,
						 org.meta_environment.rascal.
						 ast.Expression expression)
  {
    org.meta_environment.rascal.ast.Expression.Exists x =
      new org.meta_environment.rascal.ast.Expression.Exists (tree, producer,
							     expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Exists) table.get (x);
  }
  public Expression.ForAll makeExpressionForAll (ITree tree,
						 org.meta_environment.rascal.
						 ast.ValueProducer producer,
						 org.meta_environment.rascal.
						 ast.Expression expression)
  {
    org.meta_environment.rascal.ast.Expression.ForAll x =
      new org.meta_environment.rascal.ast.Expression.ForAll (tree, producer,
							     expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.ForAll) table.get (x);
  }
  public Expression.Comprehension makeExpressionComprehension (ITree tree,
							       org.
							       meta_environment.
							       rascal.ast.
							       Comprehension
							       comprehension)
  {
    org.meta_environment.rascal.ast.Expression.Comprehension x =
      new org.meta_environment.rascal.ast.Expression.Comprehension (tree,
								    comprehension);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Comprehension) table.
      get (x);
  }
  public Expression.NoMatch makeExpressionNoMatch (ITree tree,
						   org.meta_environment.
						   rascal.ast.
						   Expression pattern,
						   org.meta_environment.
						   rascal.ast.
						   Expression expression)
  {
    org.meta_environment.rascal.ast.Expression.NoMatch x =
      new org.meta_environment.rascal.ast.Expression.NoMatch (tree, pattern,
							      expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.NoMatch) table.get (x);
  }
  public Expression.Match makeExpressionMatch (ITree tree,
					       org.meta_environment.rascal.
					       ast.Expression pattern,
					       org.meta_environment.rascal.
					       ast.Expression expression)
  {
    org.meta_environment.rascal.ast.Expression.Match x =
      new org.meta_environment.rascal.ast.Expression.Match (tree, pattern,
							    expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Match) table.get (x);
  }
  public Expression.TypedVariable makeExpressionTypedVariable (ITree tree,
							       org.
							       meta_environment.
							       rascal.ast.
							       Type type,
							       org.
							       meta_environment.
							       rascal.ast.
							       Name name)
  {
    org.meta_environment.rascal.ast.Expression.TypedVariable x =
      new org.meta_environment.rascal.ast.Expression.TypedVariable (tree,
								    type,
								    name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.TypedVariable) table.
      get (x);
  }
  public Expression.QualifiedName makeExpressionQualifiedName (ITree tree,
							       org.
							       meta_environment.
							       rascal.ast.
							       QualifiedName
							       qualifiedName)
  {
    org.meta_environment.rascal.ast.Expression.QualifiedName x =
      new org.meta_environment.rascal.ast.Expression.QualifiedName (tree,
								    qualifiedName);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.QualifiedName) table.
      get (x);
  }
  public Expression.
    AreaInFileLocation makeExpressionAreaInFileLocation (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 Expression filename,
							 org.meta_environment.
							 rascal.ast.
							 Expression area)
  {
    org.meta_environment.rascal.ast.Expression.AreaInFileLocation x =
      new org.meta_environment.rascal.ast.Expression.AreaInFileLocation (tree,
									 filename,
									 area);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.
	    AreaInFileLocation) table.get (x);
  }
  public Expression.AreaLocation makeExpressionAreaLocation (ITree tree,
							     org.
							     meta_environment.
							     rascal.ast.
							     Expression area)
  {
    org.meta_environment.rascal.ast.Expression.AreaLocation x =
      new org.meta_environment.rascal.ast.Expression.AreaLocation (tree,
								   area);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.AreaLocation) table.
      get (x);
  }
  public Expression.FileLocation makeExpressionFileLocation (ITree tree,
							     org.
							     meta_environment.
							     rascal.ast.
							     Expression
							     filename)
  {
    org.meta_environment.rascal.ast.Expression.FileLocation x =
      new org.meta_environment.rascal.ast.Expression.FileLocation (tree,
								   filename);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.FileLocation) table.
      get (x);
  }
  public Expression.Area makeExpressionArea (ITree tree)
  {
    org.meta_environment.rascal.ast.Expression.Area x =
      new org.meta_environment.rascal.ast.Expression.Area (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Area) table.get (x);
  }
  public Expression.Location makeExpressionLocation (ITree tree)
  {
    org.meta_environment.rascal.ast.Expression.Location x =
      new org.meta_environment.rascal.ast.Expression.Location (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Location) table.
      get (x);
  }
  public Expression.MapTuple makeExpressionMapTuple (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     Expression from,
						     org.meta_environment.
						     rascal.ast.Expression to)
  {
    org.meta_environment.rascal.ast.Expression.MapTuple x =
      new org.meta_environment.rascal.ast.Expression.MapTuple (tree, from,
							       to);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.MapTuple) table.
      get (x);
  }
  public Expression.Tuple makeExpressionTuple (ITree tree,
					       org.meta_environment.rascal.
					       ast.Expression first,
					       java.util.List < Expression >
					       rest)
  {
    org.meta_environment.rascal.ast.Expression.Tuple x =
      new org.meta_environment.rascal.ast.Expression.Tuple (tree, first,
							    rest);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Tuple) table.get (x);
  }
  public Expression.Set makeExpressionSet (ITree tree,
					   java.util.List < Expression >
					   elements)
  {
    org.meta_environment.rascal.ast.Expression.Set x =
      new org.meta_environment.rascal.ast.Expression.Set (tree, elements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Set) table.get (x);
  }
  public Expression.List makeExpressionList (ITree tree,
					     java.util.List < Expression >
					     elements)
  {
    org.meta_environment.rascal.ast.Expression.List x =
      new org.meta_environment.rascal.ast.Expression.List (tree, elements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.List) table.get (x);
  }
  public Expression.CallOrTree makeExpressionCallOrTree (ITree tree,
							 org.meta_environment.
							 rascal.ast.Name name,
							 java.util.List <
							 Expression >
							 arguments)
  {
    org.meta_environment.rascal.ast.Expression.CallOrTree x =
      new org.meta_environment.rascal.ast.Expression.CallOrTree (tree, name,
								 arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.CallOrTree) table.
      get (x);
  }
  public Expression.Literal makeExpressionLiteral (ITree tree,
						   org.meta_environment.
						   rascal.ast.Literal literal)
  {
    org.meta_environment.rascal.ast.Expression.Literal x =
      new org.meta_environment.rascal.ast.Expression.Literal (tree, literal);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Literal) table.get (x);
  }
  public Expression.Operator makeExpressionOperator (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     StandardOperator
						     operator)
  {
    org.meta_environment.rascal.ast.Expression.Operator x =
      new org.meta_environment.rascal.ast.Expression.Operator (tree,
							       operator);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Operator) table.
      get (x);
  }
  public Expression.IfThenElse makeExpressionIfThenElse (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 Expression condition,
							 org.meta_environment.
							 rascal.ast.
							 Expression thenExp,
							 org.meta_environment.
							 rascal.ast.
							 Expression elseExp)
  {
    org.meta_environment.rascal.ast.Expression.IfThenElse x =
      new org.meta_environment.rascal.ast.Expression.IfThenElse (tree,
								 condition,
								 thenExp,
								 elseExp);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.IfThenElse) table.
      get (x);
  }
  public Expression.IfDefined makeExpressionIfDefined (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Expression lhs,
						       org.meta_environment.
						       rascal.ast.
						       Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.IfDefined x =
      new org.meta_environment.rascal.ast.Expression.IfDefined (tree, lhs,
								rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.IfDefined) table.
      get (x);
  }
  public Expression.Or makeExpressionOr (ITree tree,
					 org.meta_environment.rascal.ast.
					 Expression lhs,
					 org.meta_environment.rascal.ast.
					 Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Or x =
      new org.meta_environment.rascal.ast.Expression.Or (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Or) table.get (x);
  }
  public Expression.And makeExpressionAnd (ITree tree,
					   org.meta_environment.rascal.ast.
					   Expression lhs,
					   org.meta_environment.rascal.ast.
					   Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.And x =
      new org.meta_environment.rascal.ast.Expression.And (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.And) table.get (x);
  }
  public Expression.In makeExpressionIn (ITree tree,
					 org.meta_environment.rascal.ast.
					 Expression lhs,
					 org.meta_environment.rascal.ast.
					 Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.In x =
      new org.meta_environment.rascal.ast.Expression.In (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.In) table.get (x);
  }
  public Expression.NotIn makeExpressionNotIn (ITree tree,
					       org.meta_environment.rascal.
					       ast.Expression lhs,
					       org.meta_environment.rascal.
					       ast.Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.NotIn x =
      new org.meta_environment.rascal.ast.Expression.NotIn (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.NotIn) table.get (x);
  }
  public Expression.NonEquals makeExpressionNonEquals (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Expression lhs,
						       org.meta_environment.
						       rascal.ast.
						       Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.NonEquals x =
      new org.meta_environment.rascal.ast.Expression.NonEquals (tree, lhs,
								rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.NonEquals) table.
      get (x);
  }
  public Expression.Equals makeExpressionEquals (ITree tree,
						 org.meta_environment.rascal.
						 ast.Expression lhs,
						 org.meta_environment.rascal.
						 ast.Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Equals x =
      new org.meta_environment.rascal.ast.Expression.Equals (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Equals) table.get (x);
  }
  public Expression.GreaterThanOrEq makeExpressionGreaterThanOrEq (ITree tree,
								   org.
								   meta_environment.
								   rascal.ast.
								   Expression
								   lhs,
								   org.
								   meta_environment.
								   rascal.ast.
								   Expression
								   rhs)
  {
    org.meta_environment.rascal.ast.Expression.GreaterThanOrEq x =
      new org.meta_environment.rascal.ast.Expression.GreaterThanOrEq (tree,
								      lhs,
								      rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.GreaterThanOrEq) table.
      get (x);
  }
  public Expression.GreaterThan makeExpressionGreaterThan (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   Expression lhs,
							   org.
							   meta_environment.
							   rascal.ast.
							   Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.GreaterThan x =
      new org.meta_environment.rascal.ast.Expression.GreaterThan (tree, lhs,
								  rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.GreaterThan) table.
      get (x);
  }
  public Expression.LessThanOrEq makeExpressionLessThanOrEq (ITree tree,
							     org.
							     meta_environment.
							     rascal.ast.
							     Expression lhs,
							     org.
							     meta_environment.
							     rascal.ast.
							     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.LessThanOrEq x =
      new org.meta_environment.rascal.ast.Expression.LessThanOrEq (tree, lhs,
								   rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.LessThanOrEq) table.
      get (x);
  }
  public Expression.LessThan makeExpressionLessThan (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     Expression lhs,
						     org.meta_environment.
						     rascal.ast.
						     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.LessThan x =
      new org.meta_environment.rascal.ast.Expression.LessThan (tree, lhs,
							       rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.LessThan) table.
      get (x);
  }
  public Expression.RegExpNoMatch makeExpressionRegExpNoMatch (ITree tree,
							       org.
							       meta_environment.
							       rascal.ast.
							       Expression lhs,
							       org.
							       meta_environment.
							       rascal.ast.
							       Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.RegExpNoMatch x =
      new org.meta_environment.rascal.ast.Expression.RegExpNoMatch (tree, lhs,
								    rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.RegExpNoMatch) table.
      get (x);
  }
  public Expression.RegExpMatch makeExpressionRegExpMatch (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   Expression lhs,
							   org.
							   meta_environment.
							   rascal.ast.
							   Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.RegExpMatch x =
      new org.meta_environment.rascal.ast.Expression.RegExpMatch (tree, lhs,
								  rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.RegExpMatch) table.
      get (x);
  }
  public Expression.Substraction makeExpressionSubstraction (ITree tree,
							     org.
							     meta_environment.
							     rascal.ast.
							     Expression lhs,
							     org.
							     meta_environment.
							     rascal.ast.
							     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Substraction x =
      new org.meta_environment.rascal.ast.Expression.Substraction (tree, lhs,
								   rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Substraction) table.
      get (x);
  }
  public Expression.Addition makeExpressionAddition (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     Expression lhs,
						     org.meta_environment.
						     rascal.ast.
						     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Addition x =
      new org.meta_environment.rascal.ast.Expression.Addition (tree, lhs,
							       rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Addition) table.
      get (x);
  }
  public Expression.Division makeExpressionDivision (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     Expression lhs,
						     org.meta_environment.
						     rascal.ast.
						     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Division x =
      new org.meta_environment.rascal.ast.Expression.Division (tree, lhs,
							       rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Division) table.
      get (x);
  }
  public Expression.Intersection makeExpressionIntersection (ITree tree,
							     org.
							     meta_environment.
							     rascal.ast.
							     Expression lhs,
							     org.
							     meta_environment.
							     rascal.ast.
							     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Intersection x =
      new org.meta_environment.rascal.ast.Expression.Intersection (tree, lhs,
								   rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Intersection) table.
      get (x);
  }
  public Expression.Product makeExpressionProduct (ITree tree,
						   org.meta_environment.
						   rascal.ast.Expression lhs,
						   org.meta_environment.
						   rascal.ast.Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Product x =
      new org.meta_environment.rascal.ast.Expression.Product (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Product) table.get (x);
  }
  public Expression.Negation makeExpressionNegation (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     Expression argument)
  {
    org.meta_environment.rascal.ast.Expression.Negation x =
      new org.meta_environment.rascal.ast.Expression.Negation (tree,
							       argument);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Negation) table.
      get (x);
  }
  public Expression.Annotation makeExpressionAnnotation (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 Expression
							 expression,
							 org.meta_environment.
							 rascal.ast.Name name)
  {
    org.meta_environment.rascal.ast.Expression.Annotation x =
      new org.meta_environment.rascal.ast.Expression.Annotation (tree,
								 expression,
								 name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Annotation) table.
      get (x);
  }
  public Expression.
    TransitiveClosure makeExpressionTransitiveClosure (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Expression argument)
  {
    org.meta_environment.rascal.ast.Expression.TransitiveClosure x =
      new org.meta_environment.rascal.ast.Expression.TransitiveClosure (tree,
									argument);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.
	    TransitiveClosure) table.get (x);
  }
  public Expression.
    TransitiveReflexiveClosure makeExpressionTransitiveReflexiveClosure (ITree
									 tree,
									 org.
									 meta_environment.
									 rascal.
									 ast.
									 Expression
									 argument)
  {
    org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure x =
      new org.meta_environment.rascal.ast.Expression.
      TransitiveReflexiveClosure (tree, argument);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.
	    TransitiveReflexiveClosure) table.get (x);
  }
  public Expression.Subscript makeExpressionSubscript (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Expression expression,
						       org.meta_environment.
						       rascal.ast.
						       Expression subscript)
  {
    org.meta_environment.rascal.ast.Expression.Subscript x =
      new org.meta_environment.rascal.ast.Expression.Subscript (tree,
								expression,
								subscript);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Subscript) table.
      get (x);
  }
  public Expression.FieldAccess makeExpressionFieldAccess (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   Expression
							   expression,
							   org.
							   meta_environment.
							   rascal.ast.
							   Name field)
  {
    org.meta_environment.rascal.ast.Expression.FieldAccess x =
      new org.meta_environment.rascal.ast.Expression.FieldAccess (tree,
								  expression,
								  field);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.FieldAccess) table.
      get (x);
  }
  public Expression.FieldUpdate makeExpressionFieldUpdate (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   Expression
							   expression,
							   org.
							   meta_environment.
							   rascal.ast.
							   Name key,
							   org.
							   meta_environment.
							   rascal.ast.
							   Expression
							   replacement)
  {
    org.meta_environment.rascal.ast.Expression.FieldUpdate x =
      new org.meta_environment.rascal.ast.Expression.FieldUpdate (tree,
								  expression,
								  key,
								  replacement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.FieldUpdate) table.
      get (x);
  }
  public Expression.StepRange makeExpressionStepRange (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Expression from,
						       org.meta_environment.
						       rascal.ast.
						       Expression by,
						       org.meta_environment.
						       rascal.ast.
						       Expression to)
  {
    org.meta_environment.rascal.ast.Expression.StepRange x =
      new org.meta_environment.rascal.ast.Expression.StepRange (tree, from,
								by, to);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.StepRange) table.
      get (x);
  }
  public Expression.Range makeExpressionRange (ITree tree,
					       org.meta_environment.rascal.
					       ast.Expression from,
					       org.meta_environment.rascal.
					       ast.Expression to)
  {
    org.meta_environment.rascal.ast.Expression.Range x =
      new org.meta_environment.rascal.ast.Expression.Range (tree, from, to);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Range) table.get (x);
  }
  public Expression.ClosureCall makeExpressionClosureCall (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   Expression closure,
							   java.util.List <
							   Expression >
							   arguments)
  {
    org.meta_environment.rascal.ast.Expression.ClosureCall x =
      new org.meta_environment.rascal.ast.Expression.ClosureCall (tree,
								  closure,
								  arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.ClosureCall) table.
      get (x);
  }
  public Expression.Closure makeExpressionClosure (ITree tree,
						   org.meta_environment.
						   rascal.ast.Type type,
						   java.util.List <
						   Statement > statements)
  {
    org.meta_environment.rascal.ast.Expression.Closure x =
      new org.meta_environment.rascal.ast.Expression.Closure (tree, type,
							      statements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Closure) table.get (x);
  }
  public Literal.String makeLiteralString (ITree tree,
					   org.meta_environment.rascal.ast.
					   StringLiteral stringLiteral)
  {
    org.meta_environment.rascal.ast.Literal.String x =
      new org.meta_environment.rascal.ast.Literal.String (tree,
							  stringLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.String) table.get (x);
  }
  public Literal.Double makeLiteralDouble (ITree tree,
					   org.meta_environment.rascal.ast.
					   FloatingPointLiteral doubleLiteral)
  {
    org.meta_environment.rascal.ast.Literal.Double x =
      new org.meta_environment.rascal.ast.Literal.Double (tree,
							  doubleLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.Double) table.get (x);
  }
  public Literal.Integer makeLiteralInteger (ITree tree,
					     org.meta_environment.rascal.ast.
					     IntegerLiteral integerLiteral)
  {
    org.meta_environment.rascal.ast.Literal.Integer x =
      new org.meta_environment.rascal.ast.Literal.Integer (tree,
							   integerLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.Integer) table.get (x);
  }
  public Literal.Boolean makeLiteralBoolean (ITree tree,
					     org.meta_environment.rascal.ast.
					     BooleanLiteral booleanLiteral)
  {
    org.meta_environment.rascal.ast.Literal.Boolean x =
      new org.meta_environment.rascal.ast.Literal.Boolean (tree,
							   booleanLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.Boolean) table.get (x);
  }
  public Literal.Symbol makeLiteralSymbol (ITree tree,
					   org.meta_environment.rascal.ast.
					   SymbolLiteral symbolLiteral)
  {
    org.meta_environment.rascal.ast.Literal.Symbol x =
      new org.meta_environment.rascal.ast.Literal.Symbol (tree,
							  symbolLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.Symbol) table.get (x);
  }
  public Literal.RegExp makeLiteralRegExp (ITree tree,
					   org.meta_environment.rascal.ast.
					   RegExpLiteral regExp)
  {
    org.meta_environment.rascal.ast.Literal.RegExp x =
      new org.meta_environment.rascal.ast.Literal.RegExp (tree, regExp);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.RegExp) table.get (x);
  }
  public Bound.Default makeBoundDefault (ITree tree,
					 org.meta_environment.rascal.ast.
					 Expression expression)
  {
    org.meta_environment.rascal.ast.Bound.Default x =
      new org.meta_environment.rascal.ast.Bound.Default (tree, expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Bound.Default) table.get (x);
  }
  public Bound.Empty makeBoundEmpty (ITree tree)
  {
    org.meta_environment.rascal.ast.Bound.Empty x =
      new org.meta_environment.rascal.ast.Bound.Empty (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Bound.Empty) table.get (x);
  }
  public Statement.GlobalDirective makeStatementGlobalDirective (ITree tree,
								 org.
								 meta_environment.
								 rascal.ast.
								 Type type,
								 java.util.
								 List <
								 QualifiedName
								 > names)
  {
    org.meta_environment.rascal.ast.Statement.GlobalDirective x =
      new org.meta_environment.rascal.ast.Statement.GlobalDirective (tree,
								     type,
								     names);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.GlobalDirective) table.
      get (x);
  }
  public Statement.
    VariableDeclaration makeStatementVariableDeclaration (ITree tree,
							  org.
							  meta_environment.
							  rascal.ast.
							  LocalVariableDeclaration
							  declaration)
  {
    org.meta_environment.rascal.ast.Statement.VariableDeclaration x =
      new org.meta_environment.rascal.ast.Statement.VariableDeclaration (tree,
									 declaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.
	    VariableDeclaration) table.get (x);
  }
  public Statement.
    FunctionDeclaration makeStatementFunctionDeclaration (ITree tree,
							  org.
							  meta_environment.
							  rascal.ast.
							  FunctionDeclaration
							  functionDeclaration)
  {
    org.meta_environment.rascal.ast.Statement.FunctionDeclaration x =
      new org.meta_environment.rascal.ast.Statement.FunctionDeclaration (tree,
									 functionDeclaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.
	    FunctionDeclaration) table.get (x);
  }
  public Statement.Block makeStatementBlock (ITree tree,
					     org.meta_environment.rascal.ast.
					     Label label,
					     java.util.List < Statement >
					     statements)
  {
    org.meta_environment.rascal.ast.Statement.Block x =
      new org.meta_environment.rascal.ast.Statement.Block (tree, label,
							   statements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Block) table.get (x);
  }
  public Statement.TryFinally makeStatementTryFinally (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Statement body,
						       java.util.List <
						       Catch > handlers,
						       org.meta_environment.
						       rascal.ast.
						       Statement finallyBody)
  {
    org.meta_environment.rascal.ast.Statement.TryFinally x =
      new org.meta_environment.rascal.ast.Statement.TryFinally (tree, body,
								handlers,
								finallyBody);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.TryFinally) table.
      get (x);
  }
  public Statement.Try makeStatementTry (ITree tree,
					 org.meta_environment.rascal.ast.
					 Statement body,
					 java.util.List < Catch > handlers)
  {
    org.meta_environment.rascal.ast.Statement.Try x =
      new org.meta_environment.rascal.ast.Statement.Try (tree, body,
							 handlers);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Try) table.get (x);
  }
  public Statement.Throw makeStatementThrow (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression expression)
  {
    org.meta_environment.rascal.ast.Statement.Throw x =
      new org.meta_environment.rascal.ast.Statement.Throw (tree, expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Throw) table.get (x);
  }
  public Statement.Insert makeStatementInsert (ITree tree,
					       org.meta_environment.rascal.
					       ast.Expression expression)
  {
    org.meta_environment.rascal.ast.Statement.Insert x =
      new org.meta_environment.rascal.ast.Statement.Insert (tree, expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Insert) table.get (x);
  }
  public Statement.Assert makeStatementAssert (ITree tree,
					       org.meta_environment.rascal.
					       ast.StringLiteral label,
					       org.meta_environment.rascal.
					       ast.Expression expression)
  {
    org.meta_environment.rascal.ast.Statement.Assert x =
      new org.meta_environment.rascal.ast.Statement.Assert (tree, label,
							    expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Assert) table.get (x);
  }
  public Statement.Continue makeStatementContinue (ITree tree)
  {
    org.meta_environment.rascal.ast.Statement.Continue x =
      new org.meta_environment.rascal.ast.Statement.Continue (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Continue) table.get (x);
  }
  public Statement.Return makeStatementReturn (ITree tree)
  {
    org.meta_environment.rascal.ast.Statement.Return x =
      new org.meta_environment.rascal.ast.Statement.Return (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Return) table.get (x);
  }
  public Statement.Fail makeStatementFail (ITree tree)
  {
    org.meta_environment.rascal.ast.Statement.Fail x =
      new org.meta_environment.rascal.ast.Statement.Fail (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Fail) table.get (x);
  }
  public Statement.Break makeStatementBreak (ITree tree)
  {
    org.meta_environment.rascal.ast.Statement.Break x =
      new org.meta_environment.rascal.ast.Statement.Break (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Break) table.get (x);
  }
  public Statement.Assignment makeStatementAssignment (ITree tree,
						       java.util.List <
						       Assignable >
						       assignables,
						       org.meta_environment.
						       rascal.ast.
						       Assignment operator,
						       java.util.List <
						       Expression >
						       expressions)
  {
    org.meta_environment.rascal.ast.Statement.Assignment x =
      new org.meta_environment.rascal.ast.Statement.Assignment (tree,
								assignables,
								operator,
								expressions);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Assignment) table.
      get (x);
  }
  public Statement.Visit makeStatementVisit (ITree tree,
					     org.meta_environment.rascal.ast.
					     Visit visit)
  {
    org.meta_environment.rascal.ast.Statement.Visit x =
      new org.meta_environment.rascal.ast.Statement.Visit (tree, visit);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Visit) table.get (x);
  }
  public Statement.Expression makeStatementExpression (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Expression expression)
  {
    org.meta_environment.rascal.ast.Statement.Expression x =
      new org.meta_environment.rascal.ast.Statement.Expression (tree,
								expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Expression) table.
      get (x);
  }
  public Statement.First makeStatementFirst (ITree tree,
					     org.meta_environment.rascal.ast.
					     Label label,
					     java.util.List < Expression >
					     conditions,
					     org.meta_environment.rascal.ast.
					     Statement body)
  {
    org.meta_environment.rascal.ast.Statement.First x =
      new org.meta_environment.rascal.ast.Statement.First (tree, label,
							   conditions, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.First) table.get (x);
  }
  public Statement.All makeStatementAll (ITree tree,
					 org.meta_environment.rascal.ast.
					 Label label,
					 java.util.List < Expression >
					 conditions,
					 org.meta_environment.rascal.ast.
					 Statement body)
  {
    org.meta_environment.rascal.ast.Statement.All x =
      new org.meta_environment.rascal.ast.Statement.All (tree, label,
							 conditions, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.All) table.get (x);
  }
  public Statement.Switch makeStatementSwitch (ITree tree,
					       org.meta_environment.rascal.
					       ast.Label label,
					       org.meta_environment.rascal.
					       ast.Expression expression,
					       java.util.List < Case > cases)
  {
    org.meta_environment.rascal.ast.Statement.Switch x =
      new org.meta_environment.rascal.ast.Statement.Switch (tree, label,
							    expression,
							    cases);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Switch) table.get (x);
  }
  public Statement.IfThen makeStatementIfThen (ITree tree,
					       org.meta_environment.rascal.
					       ast.Label label,
					       java.util.List < Expression >
					       conditions,
					       org.meta_environment.rascal.
					       ast.Statement thenStatement)
  {
    org.meta_environment.rascal.ast.Statement.IfThen x =
      new org.meta_environment.rascal.ast.Statement.IfThen (tree, label,
							    conditions,
							    thenStatement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.IfThen) table.get (x);
  }
  public Statement.IfThenElse makeStatementIfThenElse (ITree tree,
						       org.meta_environment.
						       rascal.ast.Label label,
						       java.util.List <
						       Expression >
						       conditions,
						       org.meta_environment.
						       rascal.ast.
						       Statement
						       thenStatement,
						       org.meta_environment.
						       rascal.ast.
						       Statement
						       elseStatement)
  {
    org.meta_environment.rascal.ast.Statement.IfThenElse x =
      new org.meta_environment.rascal.ast.Statement.IfThenElse (tree, label,
								conditions,
								thenStatement,
								elseStatement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.IfThenElse) table.
      get (x);
  }
  public Statement.DoWhile makeStatementDoWhile (ITree tree,
						 org.meta_environment.rascal.
						 ast.Label label,
						 org.meta_environment.rascal.
						 ast.Statement body,
						 org.meta_environment.rascal.
						 ast.Expression condition)
  {
    org.meta_environment.rascal.ast.Statement.DoWhile x =
      new org.meta_environment.rascal.ast.Statement.DoWhile (tree, label,
							     body, condition);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.DoWhile) table.get (x);
  }
  public Statement.While makeStatementWhile (ITree tree,
					     org.meta_environment.rascal.ast.
					     Label label,
					     org.meta_environment.rascal.ast.
					     Expression condition,
					     org.meta_environment.rascal.ast.
					     Statement body)
  {
    org.meta_environment.rascal.ast.Statement.While x =
      new org.meta_environment.rascal.ast.Statement.While (tree, label,
							   condition, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.While) table.get (x);
  }
  public Statement.For makeStatementFor (ITree tree,
					 org.meta_environment.rascal.ast.
					 Label label,
					 java.util.List < Generator >
					 generators,
					 org.meta_environment.rascal.ast.
					 Statement body)
  {
    org.meta_environment.rascal.ast.Statement.For x =
      new org.meta_environment.rascal.ast.Statement.For (tree, label,
							 generators, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.For) table.get (x);
  }
  public Statement.Solve makeStatementSolve (ITree tree,
					     java.util.List < Declarator >
					     declarations,
					     org.meta_environment.rascal.ast.
					     Statement body)
  {
    org.meta_environment.rascal.ast.Statement.Solve x =
      new org.meta_environment.rascal.ast.Statement.Solve (tree, declarations,
							   body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Solve) table.get (x);
  }
  public Assignable.Constructor makeAssignableConstructor (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   Name name,
							   java.util.List <
							   Assignable >
							   arguments)
  {
    org.meta_environment.rascal.ast.Assignable.Constructor x =
      new org.meta_environment.rascal.ast.Assignable.Constructor (tree, name,
								  arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Constructor) table.
      get (x);
  }
  public Assignable.Tuple makeAssignableTuple (ITree tree,
					       org.meta_environment.rascal.
					       ast.Assignable first,
					       java.util.List < Assignable >
					       rest)
  {
    org.meta_environment.rascal.ast.Assignable.Tuple x =
      new org.meta_environment.rascal.ast.Assignable.Tuple (tree, first,
							    rest);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Tuple) table.get (x);
  }
  public Assignable.Annotation makeAssignableAnnotation (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 Assignable receiver,
							 org.meta_environment.
							 rascal.ast.
							 Expression
							 annotation)
  {
    org.meta_environment.rascal.ast.Assignable.Annotation x =
      new org.meta_environment.rascal.ast.Assignable.Annotation (tree,
								 receiver,
								 annotation);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Annotation) table.
      get (x);
  }
  public Assignable.IfDefined makeAssignableIfDefined (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Assignable receiver,
						       org.meta_environment.
						       rascal.ast.
						       Expression condition)
  {
    org.meta_environment.rascal.ast.Assignable.IfDefined x =
      new org.meta_environment.rascal.ast.Assignable.IfDefined (tree,
								receiver,
								condition);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.IfDefined) table.
      get (x);
  }
  public Assignable.FieldAccess makeAssignableFieldAccess (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   Assignable
							   receiver,
							   org.
							   meta_environment.
							   rascal.ast.
							   Name field)
  {
    org.meta_environment.rascal.ast.Assignable.FieldAccess x =
      new org.meta_environment.rascal.ast.Assignable.FieldAccess (tree,
								  receiver,
								  field);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.FieldAccess) table.
      get (x);
  }
  public Assignable.Subscript makeAssignableSubscript (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Assignable receiver,
						       org.meta_environment.
						       rascal.ast.
						       Expression subscript)
  {
    org.meta_environment.rascal.ast.Assignable.Subscript x =
      new org.meta_environment.rascal.ast.Assignable.Subscript (tree,
								receiver,
								subscript);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Subscript) table.
      get (x);
  }
  public Assignable.Variable makeAssignableVariable (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     QualifiedName
						     qualifiedName)
  {
    org.meta_environment.rascal.ast.Assignable.Variable x =
      new org.meta_environment.rascal.ast.Assignable.Variable (tree,
							       qualifiedName);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Variable) table.
      get (x);
  }
  public Assignment.Interesection makeAssignmentInteresection (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Interesection x =
      new org.meta_environment.rascal.ast.Assignment.Interesection (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Interesection) table.
      get (x);
  }
  public Assignment.Division makeAssignmentDivision (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Division x =
      new org.meta_environment.rascal.ast.Assignment.Division (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Division) table.
      get (x);
  }
  public Assignment.Product makeAssignmentProduct (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Product x =
      new org.meta_environment.rascal.ast.Assignment.Product (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Product) table.get (x);
  }
  public Assignment.Substraction makeAssignmentSubstraction (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Substraction x =
      new org.meta_environment.rascal.ast.Assignment.Substraction (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Substraction) table.
      get (x);
  }
  public Assignment.Addition makeAssignmentAddition (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Addition x =
      new org.meta_environment.rascal.ast.Assignment.Addition (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Addition) table.
      get (x);
  }
  public Assignment.Default makeAssignmentDefault (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Default x =
      new org.meta_environment.rascal.ast.Assignment.Default (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Default) table.get (x);
  }
  public Label.Default makeLabelDefault (ITree tree,
					 org.meta_environment.rascal.ast.
					 Name name)
  {
    org.meta_environment.rascal.ast.Label.Default x =
      new org.meta_environment.rascal.ast.Label.Default (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Label.Default) table.get (x);
  }
  public Label.Empty makeLabelEmpty (ITree tree)
  {
    org.meta_environment.rascal.ast.Label.Empty x =
      new org.meta_environment.rascal.ast.Label.Empty (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Label.Empty) table.get (x);
  }
  public Break.NoLabel makeBreakNoLabel (ITree tree)
  {
    org.meta_environment.rascal.ast.Break.NoLabel x =
      new org.meta_environment.rascal.ast.Break.NoLabel (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Break.NoLabel) table.get (x);
  }
  public Break.WithLabel makeBreakWithLabel (ITree tree,
					     org.meta_environment.rascal.ast.
					     Name label)
  {
    org.meta_environment.rascal.ast.Break.WithLabel x =
      new org.meta_environment.rascal.ast.Break.WithLabel (tree, label);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Break.WithLabel) table.get (x);
  }
  public Fail.NoLabel makeFailNoLabel (ITree tree)
  {
    org.meta_environment.rascal.ast.Fail.NoLabel x =
      new org.meta_environment.rascal.ast.Fail.NoLabel (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Fail.NoLabel) table.get (x);
  }
  public Fail.WithLabel makeFailWithLabel (ITree tree,
					   org.meta_environment.rascal.ast.
					   Name label)
  {
    org.meta_environment.rascal.ast.Fail.WithLabel x =
      new org.meta_environment.rascal.ast.Fail.WithLabel (tree, label);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Fail.WithLabel) table.get (x);
  }
  public Return.NoExpression makeReturnNoExpression (ITree tree)
  {
    org.meta_environment.rascal.ast.Return.NoExpression x =
      new org.meta_environment.rascal.ast.Return.NoExpression (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Return.NoExpression) table.
      get (x);
  }
  public Return.WithExpression makeReturnWithExpression (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 Expression
							 expression)
  {
    org.meta_environment.rascal.ast.Return.WithExpression x =
      new org.meta_environment.rascal.ast.Return.WithExpression (tree,
								 expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Return.WithExpression) table.
      get (x);
  }
  public Catch.Binding makeCatchBinding (ITree tree,
					 org.meta_environment.rascal.ast.
					 Type type,
					 org.meta_environment.rascal.ast.
					 Name name,
					 org.meta_environment.rascal.ast.
					 Statement body)
  {
    org.meta_environment.rascal.ast.Catch.Binding x =
      new org.meta_environment.rascal.ast.Catch.Binding (tree, type, name,
							 body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Catch.Binding) table.get (x);
  }
  public Catch.Default makeCatchDefault (ITree tree,
					 org.meta_environment.rascal.ast.
					 Statement body)
  {
    org.meta_environment.rascal.ast.Catch.Default x =
      new org.meta_environment.rascal.ast.Catch.Default (tree, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Catch.Default) table.get (x);
  }
  public Declarator.Default makeDeclaratorDefault (ITree tree,
						   org.meta_environment.
						   rascal.ast.Type type,
						   java.util.List < Variable >
						   variables)
  {
    org.meta_environment.rascal.ast.Declarator.Default x =
      new org.meta_environment.rascal.ast.Declarator.Default (tree, type,
							      variables);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declarator.Default) table.get (x);
  }
  public LocalVariableDeclaration.
    Dynamic makeLocalVariableDeclarationDynamic (ITree tree,
						 org.meta_environment.rascal.
						 ast.Declarator declarator)
  {
    org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic x =
      new org.meta_environment.rascal.ast.LocalVariableDeclaration.
      Dynamic (tree, declarator);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LocalVariableDeclaration.
	    Dynamic) table.get (x);
  }
  public LocalVariableDeclaration.
    Default makeLocalVariableDeclarationDefault (ITree tree,
						 org.meta_environment.rascal.
						 ast.Declarator declarator)
  {
    org.meta_environment.rascal.ast.LocalVariableDeclaration.Default x =
      new org.meta_environment.rascal.ast.LocalVariableDeclaration.
      Default (tree, declarator);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LocalVariableDeclaration.
	    Default) table.get (x);
  }
  public Visibility.Private makeVisibilityPrivate (ITree tree)
  {
    org.meta_environment.rascal.ast.Visibility.Private x =
      new org.meta_environment.rascal.ast.Visibility.Private (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Visibility.Private) table.get (x);
  }
  public Visibility.Public makeVisibilityPublic (ITree tree)
  {
    org.meta_environment.rascal.ast.Visibility.Public x =
      new org.meta_environment.rascal.ast.Visibility.Public (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Visibility.Public) table.get (x);
  }
  public Toplevel.DefaultVisibility makeToplevelDefaultVisibility (ITree tree,
								   org.
								   meta_environment.
								   rascal.ast.
								   Declaration
								   declaration)
  {
    org.meta_environment.rascal.ast.Toplevel.DefaultVisibility x =
      new org.meta_environment.rascal.ast.Toplevel.DefaultVisibility (tree,
								      declaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Toplevel.DefaultVisibility) table.
      get (x);
  }
  public Toplevel.GivenVisibility makeToplevelGivenVisibility (ITree tree,
							       org.
							       meta_environment.
							       rascal.ast.
							       Visibility
							       visibility,
							       org.
							       meta_environment.
							       rascal.ast.
							       Declaration
							       declaration)
  {
    org.meta_environment.rascal.ast.Toplevel.GivenVisibility x =
      new org.meta_environment.rascal.ast.Toplevel.GivenVisibility (tree,
								    visibility,
								    declaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Toplevel.GivenVisibility) table.
      get (x);
  }
  public Declaration.Tag makeDeclarationTag (ITree tree,
					     org.meta_environment.rascal.ast.
					     Kind kind,
					     org.meta_environment.rascal.ast.
					     Name name,
					     org.meta_environment.rascal.ast.
					     Tags tags,
					     java.util.List < Type > types)
  {
    org.meta_environment.rascal.ast.Declaration.Tag x =
      new org.meta_environment.rascal.ast.Declaration.Tag (tree, kind, name,
							   tags, types);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Tag) table.get (x);
  }
  public Declaration.Annotation makeDeclarationAnnotation (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   Type type,
							   org.
							   meta_environment.
							   rascal.ast.
							   Name name,
							   org.
							   meta_environment.
							   rascal.ast.
							   Tags tags,
							   java.util.List <
							   Type > types)
  {
    org.meta_environment.rascal.ast.Declaration.Annotation x =
      new org.meta_environment.rascal.ast.Declaration.Annotation (tree, type,
								  name, tags,
								  types);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Annotation) table.
      get (x);
  }
  public Declaration.Rule makeDeclarationRule (ITree tree,
					       org.meta_environment.rascal.
					       ast.Name name,
					       org.meta_environment.rascal.
					       ast.Tags tags,
					       org.meta_environment.rascal.
					       ast.Rule rule)
  {
    org.meta_environment.rascal.ast.Declaration.Rule x =
      new org.meta_environment.rascal.ast.Declaration.Rule (tree, name, tags,
							    rule);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Rule) table.get (x);
  }
  public Declaration.Variable makeDeclarationVariable (ITree tree,
						       org.meta_environment.
						       rascal.ast.Type type,
						       java.util.List <
						       Variable > variables)
  {
    org.meta_environment.rascal.ast.Declaration.Variable x =
      new org.meta_environment.rascal.ast.Declaration.Variable (tree, type,
								variables);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Variable) table.
      get (x);
  }
  public Declaration.Function makeDeclarationFunction (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       FunctionDeclaration
						       functionDeclaration)
  {
    org.meta_environment.rascal.ast.Declaration.Function x =
      new org.meta_environment.rascal.ast.Declaration.Function (tree,
								functionDeclaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Function) table.
      get (x);
  }
  public Declaration.Data makeDeclarationData (ITree tree,
					       org.meta_environment.rascal.
					       ast.UserType user,
					       org.meta_environment.rascal.
					       ast.Tags tags,
					       java.util.List < Variant >
					       variants)
  {
    org.meta_environment.rascal.ast.Declaration.Data x =
      new org.meta_environment.rascal.ast.Declaration.Data (tree, user, tags,
							    variants);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Data) table.get (x);
  }
  public Declaration.Type makeDeclarationType (ITree tree,
					       org.meta_environment.rascal.
					       ast.Type base,
					       org.meta_environment.rascal.
					       ast.UserType user,
					       org.meta_environment.rascal.
					       ast.Tags tags)
  {
    org.meta_environment.rascal.ast.Declaration.Type x =
      new org.meta_environment.rascal.ast.Declaration.Type (tree, base, user,
							    tags);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Type) table.get (x);
  }
  public Declaration.View makeDeclarationView (ITree tree,
					       org.meta_environment.rascal.
					       ast.Name view,
					       org.meta_environment.rascal.
					       ast.Name type,
					       org.meta_environment.rascal.
					       ast.Tags tags,
					       java.util.List < Alternative >
					       alternatives)
  {
    org.meta_environment.rascal.ast.Declaration.View x =
      new org.meta_environment.rascal.ast.Declaration.View (tree, view, type,
							    tags,
							    alternatives);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.View) table.get (x);
  }
  public Alternative.NamedType makeAlternativeNamedType (ITree tree,
							 org.meta_environment.
							 rascal.ast.Name name,
							 org.meta_environment.
							 rascal.ast.Type type)
  {
    org.meta_environment.rascal.ast.Alternative.NamedType x =
      new org.meta_environment.rascal.ast.Alternative.NamedType (tree, name,
								 type);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Alternative.NamedType) table.
      get (x);
  }
  public Variant.NillaryConstructor makeVariantNillaryConstructor (ITree tree,
								   org.
								   meta_environment.
								   rascal.ast.
								   Name name)
  {
    org.meta_environment.rascal.ast.Variant.NillaryConstructor x =
      new org.meta_environment.rascal.ast.Variant.NillaryConstructor (tree,
								      name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Variant.NillaryConstructor) table.
      get (x);
  }
  public Variant.NAryConstructor makeVariantNAryConstructor (ITree tree,
							     org.
							     meta_environment.
							     rascal.ast.
							     Name name,
							     java.util.List <
							     TypeArg >
							     arguments)
  {
    org.meta_environment.rascal.ast.Variant.NAryConstructor x =
      new org.meta_environment.rascal.ast.Variant.NAryConstructor (tree, name,
								   arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Variant.NAryConstructor) table.
      get (x);
  }
  public Variant.Type makeVariantType (ITree tree,
				       org.meta_environment.rascal.ast.
				       Type type,
				       org.meta_environment.rascal.ast.
				       Name name)
  {
    org.meta_environment.rascal.ast.Variant.Type x =
      new org.meta_environment.rascal.ast.Variant.Type (tree, type, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Variant.Type) table.get (x);
  }
  public StandardOperator.NotIn makeStandardOperatorNotIn (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.NotIn x =
      new org.meta_environment.rascal.ast.StandardOperator.NotIn (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.NotIn) table.
      get (x);
  }
  public StandardOperator.In makeStandardOperatorIn (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.In x =
      new org.meta_environment.rascal.ast.StandardOperator.In (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.In) table.
      get (x);
  }
  public StandardOperator.Not makeStandardOperatorNot (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Not x =
      new org.meta_environment.rascal.ast.StandardOperator.Not (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Not) table.
      get (x);
  }
  public StandardOperator.Or makeStandardOperatorOr (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Or x =
      new org.meta_environment.rascal.ast.StandardOperator.Or (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Or) table.
      get (x);
  }
  public StandardOperator.And makeStandardOperatorAnd (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.And x =
      new org.meta_environment.rascal.ast.StandardOperator.And (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.And) table.
      get (x);
  }
  public StandardOperator.
    GreaterThanOrEq makeStandardOperatorGreaterThanOrEq (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.GreaterThanOrEq x =
      new org.meta_environment.rascal.ast.StandardOperator.
      GreaterThanOrEq (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    GreaterThanOrEq) table.get (x);
  }
  public StandardOperator.
    GreaterThan makeStandardOperatorGreaterThan (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.GreaterThan x =
      new org.meta_environment.rascal.ast.StandardOperator.GreaterThan (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    GreaterThan) table.get (x);
  }
  public StandardOperator.
    LessThanOrEq makeStandardOperatorLessThanOrEq (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.LessThanOrEq x =
      new org.meta_environment.rascal.ast.StandardOperator.
      LessThanOrEq (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    LessThanOrEq) table.get (x);
  }
  public StandardOperator.LessThan makeStandardOperatorLessThan (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.LessThan x =
      new org.meta_environment.rascal.ast.StandardOperator.LessThan (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.LessThan) table.
      get (x);
  }
  public StandardOperator.NotEquals makeStandardOperatorNotEquals (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.NotEquals x =
      new org.meta_environment.rascal.ast.StandardOperator.NotEquals (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.NotEquals) table.
      get (x);
  }
  public StandardOperator.Equals makeStandardOperatorEquals (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Equals x =
      new org.meta_environment.rascal.ast.StandardOperator.Equals (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Equals) table.
      get (x);
  }
  public StandardOperator.
    Intersection makeStandardOperatorIntersection (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Intersection x =
      new org.meta_environment.rascal.ast.StandardOperator.
      Intersection (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    Intersection) table.get (x);
  }
  public StandardOperator.Division makeStandardOperatorDivision (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Division x =
      new org.meta_environment.rascal.ast.StandardOperator.Division (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Division) table.
      get (x);
  }
  public StandardOperator.Product makeStandardOperatorProduct (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Product x =
      new org.meta_environment.rascal.ast.StandardOperator.Product (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Product) table.
      get (x);
  }
  public StandardOperator.
    Substraction makeStandardOperatorSubstraction (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Substraction x =
      new org.meta_environment.rascal.ast.StandardOperator.
      Substraction (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    Substraction) table.get (x);
  }
  public StandardOperator.Addition makeStandardOperatorAddition (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Addition x =
      new org.meta_environment.rascal.ast.StandardOperator.Addition (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Addition) table.
      get (x);
  }
  public FunctionName.Operator makeFunctionNameOperator (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 StandardOperator
							 operator)
  {
    org.meta_environment.rascal.ast.FunctionName.Operator x =
      new org.meta_environment.rascal.ast.FunctionName.Operator (tree,
								 operator);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionName.Operator) table.
      get (x);
  }
  public FunctionName.Name makeFunctionNameName (ITree tree,
						 org.meta_environment.rascal.
						 ast.Name name)
  {
    org.meta_environment.rascal.ast.FunctionName.Name x =
      new org.meta_environment.rascal.ast.FunctionName.Name (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionName.Name) table.get (x);
  }
  public FunctionModifier.Java makeFunctionModifierJava (ITree tree)
  {
    org.meta_environment.rascal.ast.FunctionModifier.Java x =
      new org.meta_environment.rascal.ast.FunctionModifier.Java (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionModifier.Java) table.
      get (x);
  }
  public FunctionModifiers.List makeFunctionModifiersList (ITree tree,
							   java.util.List <
							   FunctionModifier >
							   modifiers)
  {
    org.meta_environment.rascal.ast.FunctionModifiers.List x =
      new org.meta_environment.rascal.ast.FunctionModifiers.List (tree,
								  modifiers);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionModifiers.List) table.
      get (x);
  }
  public Signature.WithThrows makeSignatureWithThrows (ITree tree,
						       org.meta_environment.
						       rascal.ast.Type type,
						       org.meta_environment.
						       rascal.ast.
						       FunctionModifiers
						       modifiers,
						       org.meta_environment.
						       rascal.ast.
						       FunctionName name,
						       org.meta_environment.
						       rascal.ast.
						       Parameters parameters,
						       java.util.List < Type >
						       exceptions)
  {
    org.meta_environment.rascal.ast.Signature.WithThrows x =
      new org.meta_environment.rascal.ast.Signature.WithThrows (tree, type,
								modifiers,
								name,
								parameters,
								exceptions);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Signature.WithThrows) table.
      get (x);
  }
  public Signature.NoThrows makeSignatureNoThrows (ITree tree,
						   org.meta_environment.
						   rascal.ast.Type type,
						   org.meta_environment.
						   rascal.ast.
						   FunctionModifiers
						   modifiers,
						   org.meta_environment.
						   rascal.ast.
						   FunctionName name,
						   org.meta_environment.
						   rascal.ast.
						   Parameters parameters)
  {
    org.meta_environment.rascal.ast.Signature.NoThrows x =
      new org.meta_environment.rascal.ast.Signature.NoThrows (tree, type,
							      modifiers, name,
							      parameters);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Signature.NoThrows) table.get (x);
  }
  public FunctionDeclaration.
    Abstract makeFunctionDeclarationAbstract (ITree tree,
					      org.meta_environment.rascal.ast.
					      Signature signature,
					      org.meta_environment.rascal.ast.
					      Tags tags)
  {
    org.meta_environment.rascal.ast.FunctionDeclaration.Abstract x =
      new org.meta_environment.rascal.ast.FunctionDeclaration.Abstract (tree,
									signature,
									tags);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionDeclaration.
	    Abstract) table.get (x);
  }
  public FunctionDeclaration.
    Default makeFunctionDeclarationDefault (ITree tree,
					    org.meta_environment.rascal.ast.
					    Signature signature,
					    org.meta_environment.rascal.ast.
					    Tags tags,
					    org.meta_environment.rascal.ast.
					    FunctionBody body)
  {
    org.meta_environment.rascal.ast.FunctionDeclaration.Default x =
      new org.meta_environment.rascal.ast.FunctionDeclaration.Default (tree,
								       signature,
								       tags,
								       body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionDeclaration.
	    Default) table.get (x);
  }
  public FunctionBody.Default makeFunctionBodyDefault (ITree tree,
						       java.util.List <
						       Statement > statements)
  {
    org.meta_environment.rascal.ast.FunctionBody.Default x =
      new org.meta_environment.rascal.ast.FunctionBody.Default (tree,
								statements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionBody.Default) table.
      get (x);
  }
  public Variable.
    GivenInitialization makeVariableGivenInitialization (ITree tree,
							 org.meta_environment.
							 rascal.ast.Name name,
							 org.meta_environment.
							 rascal.ast.Tags tags,
							 org.meta_environment.
							 rascal.ast.
							 Expression initial)
  {
    org.meta_environment.rascal.ast.Variable.GivenInitialization x =
      new org.meta_environment.rascal.ast.Variable.GivenInitialization (tree,
									name,
									tags,
									initial);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Variable.
	    GivenInitialization) table.get (x);
  }
  public Kind.All makeKindAll (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.All x =
      new org.meta_environment.rascal.ast.Kind.All (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.All) table.get (x);
  }
  public Kind.Tag makeKindTag (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Tag x =
      new org.meta_environment.rascal.ast.Kind.Tag (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Tag) table.get (x);
  }
  public Kind.Anno makeKindAnno (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Anno x =
      new org.meta_environment.rascal.ast.Kind.Anno (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Anno) table.get (x);
  }
  public Kind.Type makeKindType (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Type x =
      new org.meta_environment.rascal.ast.Kind.Type (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Type) table.get (x);
  }
  public Kind.View makeKindView (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.View x =
      new org.meta_environment.rascal.ast.Kind.View (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.View) table.get (x);
  }
  public Kind.Data makeKindData (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Data x =
      new org.meta_environment.rascal.ast.Kind.Data (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Data) table.get (x);
  }
  public Kind.Variable makeKindVariable (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Variable x =
      new org.meta_environment.rascal.ast.Kind.Variable (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Variable) table.get (x);
  }
  public Kind.Function makeKindFunction (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Function x =
      new org.meta_environment.rascal.ast.Kind.Function (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Function) table.get (x);
  }
  public Kind.Module makeKindModule (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Module x =
      new org.meta_environment.rascal.ast.Kind.Module (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Module) table.get (x);
  }
  public BasicType.Loc makeBasicTypeLoc (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Loc x =
      new org.meta_environment.rascal.ast.BasicType.Loc (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Loc) table.get (x);
  }
  public BasicType.Void makeBasicTypeVoid (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Void x =
      new org.meta_environment.rascal.ast.BasicType.Void (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Void) table.get (x);
  }
  public BasicType.Term makeBasicTypeTerm (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Term x =
      new org.meta_environment.rascal.ast.BasicType.Term (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Term) table.get (x);
  }
  public BasicType.Value makeBasicTypeValue (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Value x =
      new org.meta_environment.rascal.ast.BasicType.Value (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Value) table.get (x);
  }
  public BasicType.String makeBasicTypeString (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.String x =
      new org.meta_environment.rascal.ast.BasicType.String (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.String) table.get (x);
  }
  public BasicType.Double makeBasicTypeDouble (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Double x =
      new org.meta_environment.rascal.ast.BasicType.Double (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Double) table.get (x);
  }
  public BasicType.Int makeBasicTypeInt (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Int x =
      new org.meta_environment.rascal.ast.BasicType.Int (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Int) table.get (x);
  }
  public BasicType.Bool makeBasicTypeBool (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Bool x =
      new org.meta_environment.rascal.ast.BasicType.Bool (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Bool) table.get (x);
  }
  public TypeArg.Named makeTypeArgNamed (ITree tree,
					 org.meta_environment.rascal.ast.
					 Type type,
					 org.meta_environment.rascal.ast.
					 Name name)
  {
    org.meta_environment.rascal.ast.TypeArg.Named x =
      new org.meta_environment.rascal.ast.TypeArg.Named (tree, type, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TypeArg.Named) table.get (x);
  }
  public TypeArg.Default makeTypeArgDefault (ITree tree,
					     org.meta_environment.rascal.ast.
					     Type type)
  {
    org.meta_environment.rascal.ast.TypeArg.Default x =
      new org.meta_environment.rascal.ast.TypeArg.Default (tree, type);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TypeArg.Default) table.get (x);
  }
  public StructuredType.Tuple makeStructuredTypeTuple (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       TypeArg first,
						       java.util.List <
						       TypeArg > rest)
  {
    org.meta_environment.rascal.ast.StructuredType.Tuple x =
      new org.meta_environment.rascal.ast.StructuredType.Tuple (tree, first,
								rest);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.Tuple) table.
      get (x);
  }
  public StructuredType.Relation makeStructuredTypeRelation (ITree tree,
							     org.
							     meta_environment.
							     rascal.ast.
							     TypeArg first,
							     java.util.List <
							     TypeArg > rest)
  {
    org.meta_environment.rascal.ast.StructuredType.Relation x =
      new org.meta_environment.rascal.ast.StructuredType.Relation (tree,
								   first,
								   rest);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.Relation) table.
      get (x);
  }
  public StructuredType.Map makeStructuredTypeMap (ITree tree,
						   org.meta_environment.
						   rascal.ast.TypeArg first,
						   org.meta_environment.
						   rascal.ast.TypeArg second)
  {
    org.meta_environment.rascal.ast.StructuredType.Map x =
      new org.meta_environment.rascal.ast.StructuredType.Map (tree, first,
							      second);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.Map) table.get (x);
  }
  public StructuredType.Set makeStructuredTypeSet (ITree tree,
						   org.meta_environment.
						   rascal.ast.TypeArg typeArg)
  {
    org.meta_environment.rascal.ast.StructuredType.Set x =
      new org.meta_environment.rascal.ast.StructuredType.Set (tree, typeArg);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.Set) table.get (x);
  }
  public StructuredType.List makeStructuredTypeList (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     TypeArg typeArg)
  {
    org.meta_environment.rascal.ast.StructuredType.List x =
      new org.meta_environment.rascal.ast.StructuredType.List (tree, typeArg);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.List) table.
      get (x);
  }
  public FunctionType.TypeArguments makeFunctionTypeTypeArguments (ITree tree,
								   org.
								   meta_environment.
								   rascal.ast.
								   Type type,
								   java.util.
								   List <
								   TypeArg >
								   arguments)
  {
    org.meta_environment.rascal.ast.FunctionType.TypeArguments x =
      new org.meta_environment.rascal.ast.FunctionType.TypeArguments (tree,
								      type,
								      arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionType.TypeArguments) table.
      get (x);
  }
  public TypeVar.Bounded makeTypeVarBounded (ITree tree,
					     org.meta_environment.rascal.ast.
					     Name name,
					     org.meta_environment.rascal.ast.
					     Type bound)
  {
    org.meta_environment.rascal.ast.TypeVar.Bounded x =
      new org.meta_environment.rascal.ast.TypeVar.Bounded (tree, name, bound);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TypeVar.Bounded) table.get (x);
  }
  public TypeVar.Free makeTypeVarFree (ITree tree,
				       org.meta_environment.rascal.ast.
				       Name name)
  {
    org.meta_environment.rascal.ast.TypeVar.Free x =
      new org.meta_environment.rascal.ast.TypeVar.Free (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TypeVar.Free) table.get (x);
  }
  public UserType.Parametric makeUserTypeParametric (ITree tree,
						     org.meta_environment.
						     rascal.ast.Name name,
						     java.util.List <
						     TypeVar > parameters)
  {
    org.meta_environment.rascal.ast.UserType.Parametric x =
      new org.meta_environment.rascal.ast.UserType.Parametric (tree, name,
							       parameters);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.UserType.Parametric) table.
      get (x);
  }
  public UserType.Name makeUserTypeName (ITree tree,
					 org.meta_environment.rascal.ast.
					 Name name)
  {
    org.meta_environment.rascal.ast.UserType.Name x =
      new org.meta_environment.rascal.ast.UserType.Name (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.UserType.Name) table.get (x);
  }
  public DataTypeSelector.Selector makeDataTypeSelectorSelector (ITree tree,
								 org.
								 meta_environment.
								 rascal.ast.
								 Name sort,
								 org.
								 meta_environment.
								 rascal.ast.
								 Name
								 production)
  {
    org.meta_environment.rascal.ast.DataTypeSelector.Selector x =
      new org.meta_environment.rascal.ast.DataTypeSelector.Selector (tree,
								     sort,
								     production);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.DataTypeSelector.Selector) table.
      get (x);
  }
  public Type.Selector makeTypeSelector (ITree tree,
					 org.meta_environment.rascal.ast.
					 DataTypeSelector selector)
  {
    org.meta_environment.rascal.ast.Type.Selector x =
      new org.meta_environment.rascal.ast.Type.Selector (tree, selector);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Selector) table.get (x);
  }
  public Type.Symbol makeTypeSymbol (ITree tree,
				     org.meta_environment.rascal.ast.
				     Symbol symbol)
  {
    org.meta_environment.rascal.ast.Type.Symbol x =
      new org.meta_environment.rascal.ast.Type.Symbol (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Symbol) table.get (x);
  }
  public Type.User makeTypeUser (ITree tree,
				 org.meta_environment.rascal.ast.
				 UserType user)
  {
    org.meta_environment.rascal.ast.Type.User x =
      new org.meta_environment.rascal.ast.Type.User (tree, user);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.User) table.get (x);
  }
  public Type.Variable makeTypeVariable (ITree tree,
					 org.meta_environment.rascal.ast.
					 TypeVar typeVar)
  {
    org.meta_environment.rascal.ast.Type.Variable x =
      new org.meta_environment.rascal.ast.Type.Variable (tree, typeVar);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Variable) table.get (x);
  }
  public Type.Function makeTypeFunction (ITree tree,
					 org.meta_environment.rascal.ast.
					 FunctionType function)
  {
    org.meta_environment.rascal.ast.Type.Function x =
      new org.meta_environment.rascal.ast.Type.Function (tree, function);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Function) table.get (x);
  }
  public Type.Structured makeTypeStructured (ITree tree,
					     org.meta_environment.rascal.ast.
					     StructuredType structured)
  {
    org.meta_environment.rascal.ast.Type.Structured x =
      new org.meta_environment.rascal.ast.Type.Structured (tree, structured);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Structured) table.get (x);
  }
  public Type.Basic makeTypeBasic (ITree tree,
				   org.meta_environment.rascal.ast.
				   BasicType basic)
  {
    org.meta_environment.rascal.ast.Type.Basic x =
      new org.meta_environment.rascal.ast.Type.Basic (tree, basic);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Basic) table.get (x);
  }
  public StrChar.newline makeStrCharnewline (ITree tree)
  {
    org.meta_environment.rascal.ast.StrChar.newline x =
      new org.meta_environment.rascal.ast.StrChar.newline (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StrChar.newline) table.get (x);
  }
  public Symbol.
    CaseInsensitiveLiteral makeSymbolCaseInsensitiveLiteral (ITree tree,
							     org.
							     meta_environment.
							     rascal.ast.
							     SingleQuotedStrCon
							     singelQuotedString)
  {
    org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral x =
      new org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral (tree,
									 singelQuotedString);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.
	    CaseInsensitiveLiteral) table.get (x);
  }
  public Symbol.Literal makeSymbolLiteral (ITree tree,
					   org.meta_environment.rascal.ast.
					   StrCon string)
  {
    org.meta_environment.rascal.ast.Symbol.Literal x =
      new org.meta_environment.rascal.ast.Symbol.Literal (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Literal) table.get (x);
  }
  public Symbol.LiftedSymbol makeSymbolLiftedSymbol (ITree tree,
						     org.meta_environment.
						     rascal.ast.Symbol symbol)
  {
    org.meta_environment.rascal.ast.Symbol.LiftedSymbol x =
      new org.meta_environment.rascal.ast.Symbol.LiftedSymbol (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.LiftedSymbol) table.
      get (x);
  }
  public Symbol.CharacterClass makeSymbolCharacterClass (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 CharClass charClass)
  {
    org.meta_environment.rascal.ast.Symbol.CharacterClass x =
      new org.meta_environment.rascal.ast.Symbol.CharacterClass (tree,
								 charClass);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.CharacterClass) table.
      get (x);
  }
  public Symbol.Alternative makeSymbolAlternative (ITree tree,
						   org.meta_environment.
						   rascal.ast.Symbol lhs,
						   org.meta_environment.
						   rascal.ast.Symbol rhs)
  {
    org.meta_environment.rascal.ast.Symbol.Alternative x =
      new org.meta_environment.rascal.ast.Symbol.Alternative (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Alternative) table.get (x);
  }
  public Symbol.IterStarSep makeSymbolIterStarSep (ITree tree,
						   org.meta_environment.
						   rascal.ast.Symbol symbol,
						   org.meta_environment.
						   rascal.ast.StrCon sep)
  {
    org.meta_environment.rascal.ast.Symbol.IterStarSep x =
      new org.meta_environment.rascal.ast.Symbol.IterStarSep (tree, symbol,
							      sep);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.IterStarSep) table.get (x);
  }
  public Symbol.IterSep makeSymbolIterSep (ITree tree,
					   org.meta_environment.rascal.ast.
					   Symbol symbol,
					   org.meta_environment.rascal.ast.
					   StrCon sep)
  {
    org.meta_environment.rascal.ast.Symbol.IterSep x =
      new org.meta_environment.rascal.ast.Symbol.IterSep (tree, symbol, sep);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.IterSep) table.get (x);
  }
  public Symbol.IterStar makeSymbolIterStar (ITree tree,
					     org.meta_environment.rascal.ast.
					     Symbol symbol)
  {
    org.meta_environment.rascal.ast.Symbol.IterStar x =
      new org.meta_environment.rascal.ast.Symbol.IterStar (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.IterStar) table.get (x);
  }
  public Symbol.Iter makeSymbolIter (ITree tree,
				     org.meta_environment.rascal.ast.
				     Symbol symbol)
  {
    org.meta_environment.rascal.ast.Symbol.Iter x =
      new org.meta_environment.rascal.ast.Symbol.Iter (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Iter) table.get (x);
  }
  public Symbol.Optional makeSymbolOptional (ITree tree,
					     org.meta_environment.rascal.ast.
					     Symbol symbol)
  {
    org.meta_environment.rascal.ast.Symbol.Optional x =
      new org.meta_environment.rascal.ast.Symbol.Optional (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Optional) table.get (x);
  }
  public Symbol.Sequence makeSymbolSequence (ITree tree,
					     org.meta_environment.rascal.ast.
					     Symbol head,
					     java.util.List < Symbol > tail)
  {
    org.meta_environment.rascal.ast.Symbol.Sequence x =
      new org.meta_environment.rascal.ast.Symbol.Sequence (tree, head, tail);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Sequence) table.get (x);
  }
  public Symbol.Empty makeSymbolEmpty (ITree tree)
  {
    org.meta_environment.rascal.ast.Symbol.Empty x =
      new org.meta_environment.rascal.ast.Symbol.Empty (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Empty) table.get (x);
  }
  public Symbol.ParameterizedSort makeSymbolParameterizedSort (ITree tree,
							       org.
							       meta_environment.
							       rascal.ast.
							       Sort sort,
							       java.util.
							       List < Symbol >
							       parameters)
  {
    org.meta_environment.rascal.ast.Symbol.ParameterizedSort x =
      new org.meta_environment.rascal.ast.Symbol.ParameterizedSort (tree,
								    sort,
								    parameters);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.ParameterizedSort) table.
      get (x);
  }
  public Symbol.Sort makeSymbolSort (ITree tree,
				     org.meta_environment.rascal.ast.
				     Sort sort)
  {
    org.meta_environment.rascal.ast.Symbol.Sort x =
      new org.meta_environment.rascal.ast.Symbol.Sort (tree, sort);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Sort) table.get (x);
  }
  public CharRange.Range makeCharRangeRange (ITree tree,
					     org.meta_environment.rascal.ast.
					     Character start,
					     org.meta_environment.rascal.ast.
					     Character end)
  {
    org.meta_environment.rascal.ast.CharRange.Range x =
      new org.meta_environment.rascal.ast.CharRange.Range (tree, start, end);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharRange.Range) table.get (x);
  }
  public CharRange.Character makeCharRangeCharacter (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     Character character)
  {
    org.meta_environment.rascal.ast.CharRange.Character x =
      new org.meta_environment.rascal.ast.CharRange.Character (tree,
							       character);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharRange.Character) table.
      get (x);
  }
  public CharRanges.Concatenate makeCharRangesConcatenate (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   CharRanges lhs,
							   org.
							   meta_environment.
							   rascal.ast.
							   CharRanges rhs)
  {
    org.meta_environment.rascal.ast.CharRanges.Concatenate x =
      new org.meta_environment.rascal.ast.CharRanges.Concatenate (tree, lhs,
								  rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharRanges.Concatenate) table.
      get (x);
  }
  public CharRanges.Range makeCharRangesRange (ITree tree,
					       org.meta_environment.rascal.
					       ast.CharRange range)
  {
    org.meta_environment.rascal.ast.CharRanges.Range x =
      new org.meta_environment.rascal.ast.CharRanges.Range (tree, range);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharRanges.Range) table.get (x);
  }
  public OptCharRanges.Present makeOptCharRangesPresent (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 CharRanges ranges)
  {
    org.meta_environment.rascal.ast.OptCharRanges.Present x =
      new org.meta_environment.rascal.ast.OptCharRanges.Present (tree,
								 ranges);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.OptCharRanges.Present) table.
      get (x);
  }
  public OptCharRanges.Absent makeOptCharRangesAbsent (ITree tree)
  {
    org.meta_environment.rascal.ast.OptCharRanges.Absent x =
      new org.meta_environment.rascal.ast.OptCharRanges.Absent (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.OptCharRanges.Absent) table.
      get (x);
  }
  public CharClass.Union makeCharClassUnion (ITree tree,
					     org.meta_environment.rascal.ast.
					     CharClass lhs,
					     org.meta_environment.rascal.ast.
					     CharClass rhs)
  {
    org.meta_environment.rascal.ast.CharClass.Union x =
      new org.meta_environment.rascal.ast.CharClass.Union (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.Union) table.get (x);
  }
  public CharClass.Intersection makeCharClassIntersection (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   CharClass lhs,
							   org.
							   meta_environment.
							   rascal.ast.
							   CharClass rhs)
  {
    org.meta_environment.rascal.ast.CharClass.Intersection x =
      new org.meta_environment.rascal.ast.CharClass.Intersection (tree, lhs,
								  rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.Intersection) table.
      get (x);
  }
  public CharClass.Difference makeCharClassDifference (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       CharClass lhs,
						       org.meta_environment.
						       rascal.ast.
						       CharClass rhs)
  {
    org.meta_environment.rascal.ast.CharClass.Difference x =
      new org.meta_environment.rascal.ast.CharClass.Difference (tree, lhs,
								rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.Difference) table.
      get (x);
  }
  public CharClass.Complement makeCharClassComplement (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       CharClass charClass)
  {
    org.meta_environment.rascal.ast.CharClass.Complement x =
      new org.meta_environment.rascal.ast.CharClass.Complement (tree,
								charClass);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.Complement) table.
      get (x);
  }
  public CharClass.SimpleCharclass makeCharClassSimpleCharclass (ITree tree,
								 org.
								 meta_environment.
								 rascal.ast.
								 OptCharRanges
								 optionalCharRanges)
  {
    org.meta_environment.rascal.ast.CharClass.SimpleCharclass x =
      new org.meta_environment.rascal.ast.CharClass.SimpleCharclass (tree,
								     optionalCharRanges);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.SimpleCharclass) table.
      get (x);
  }
  public Character.LabelStart makeCharacterLabelStart (ITree tree)
  {
    org.meta_environment.rascal.ast.Character.LabelStart x =
      new org.meta_environment.rascal.ast.Character.LabelStart (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.LabelStart) table.
      get (x);
  }
  public Character.Bottom makeCharacterBottom (ITree tree)
  {
    org.meta_environment.rascal.ast.Character.Bottom x =
      new org.meta_environment.rascal.ast.Character.Bottom (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.Bottom) table.get (x);
  }
  public Character.EOF makeCharacterEOF (ITree tree)
  {
    org.meta_environment.rascal.ast.Character.EOF x =
      new org.meta_environment.rascal.ast.Character.EOF (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.EOF) table.get (x);
  }
  public Character.Top makeCharacterTop (ITree tree)
  {
    org.meta_environment.rascal.ast.Character.Top x =
      new org.meta_environment.rascal.ast.Character.Top (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.Top) table.get (x);
  }
  public Character.Short makeCharacterShort (ITree tree,
					     org.meta_environment.rascal.ast.
					     ShortChar shortChar)
  {
    org.meta_environment.rascal.ast.Character.Short x =
      new org.meta_environment.rascal.ast.Character.Short (tree, shortChar);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.Short) table.get (x);
  }
  public Character.Numeric makeCharacterNumeric (ITree tree,
						 org.meta_environment.rascal.
						 ast.NumChar numChar)
  {
    org.meta_environment.rascal.ast.Character.Numeric x =
      new org.meta_environment.rascal.ast.Character.Numeric (tree, numChar);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.Numeric) table.get (x);
  }
  public Module.Default makeModuleDefault (ITree tree,
					   org.meta_environment.rascal.ast.
					   Header header,
					   org.meta_environment.rascal.ast.
					   Body body)
  {
    org.meta_environment.rascal.ast.Module.Default x =
      new org.meta_environment.rascal.ast.Module.Default (tree, header, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Module.Default) table.get (x);
  }
  public ModuleActuals.Default makeModuleActualsDefault (ITree tree,
							 java.util.List <
							 Type > types)
  {
    org.meta_environment.rascal.ast.ModuleActuals.Default x =
      new org.meta_environment.rascal.ast.ModuleActuals.Default (tree, types);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ModuleActuals.Default) table.
      get (x);
  }
  public ImportedModule.Default makeImportedModuleDefault (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   ModuleName name)
  {
    org.meta_environment.rascal.ast.ImportedModule.Default x =
      new org.meta_environment.rascal.ast.ImportedModule.Default (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ImportedModule.Default) table.
      get (x);
  }
  public ImportedModule.Renamings makeImportedModuleRenamings (ITree tree,
							       org.
							       meta_environment.
							       rascal.ast.
							       ModuleName
							       name,
							       org.
							       meta_environment.
							       rascal.ast.
							       Renamings
							       renamings)
  {
    org.meta_environment.rascal.ast.ImportedModule.Renamings x =
      new org.meta_environment.rascal.ast.ImportedModule.Renamings (tree,
								    name,
								    renamings);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ImportedModule.Renamings) table.
      get (x);
  }
  public ImportedModule.Actuals makeImportedModuleActuals (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   ModuleName name,
							   org.
							   meta_environment.
							   rascal.ast.
							   ModuleActuals
							   actuals)
  {
    org.meta_environment.rascal.ast.ImportedModule.Actuals x =
      new org.meta_environment.rascal.ast.ImportedModule.Actuals (tree, name,
								  actuals);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ImportedModule.Actuals) table.
      get (x);
  }
  public ImportedModule.
    ActualsRenaming makeImportedModuleActualsRenaming (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       ModuleName name,
						       org.meta_environment.
						       rascal.ast.
						       ModuleActuals actuals,
						       org.meta_environment.
						       rascal.ast.
						       Renamings renamings)
  {
    org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming x =
      new org.meta_environment.rascal.ast.ImportedModule.
      ActualsRenaming (tree, name, actuals, renamings);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ImportedModule.
	    ActualsRenaming) table.get (x);
  }
  public Renaming.Default makeRenamingDefault (ITree tree,
					       org.meta_environment.rascal.
					       ast.Name from,
					       org.meta_environment.rascal.
					       ast.Name to)
  {
    org.meta_environment.rascal.ast.Renaming.Default x =
      new org.meta_environment.rascal.ast.Renaming.Default (tree, from, to);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Renaming.Default) table.get (x);
  }
  public Renamings.Default makeRenamingsDefault (ITree tree,
						 java.util.List < Renaming >
						 renamings)
  {
    org.meta_environment.rascal.ast.Renamings.Default x =
      new org.meta_environment.rascal.ast.Renamings.Default (tree, renamings);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Renamings.Default) table.get (x);
  }
  public Import.Extend makeImportExtend (ITree tree,
					 org.meta_environment.rascal.ast.
					 ImportedModule module)
  {
    org.meta_environment.rascal.ast.Import.Extend x =
      new org.meta_environment.rascal.ast.Import.Extend (tree, module);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Import.Extend) table.get (x);
  }
  public Import.Default makeImportDefault (ITree tree,
					   org.meta_environment.rascal.ast.
					   ImportedModule module)
  {
    org.meta_environment.rascal.ast.Import.Default x =
      new org.meta_environment.rascal.ast.Import.Default (tree, module);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Import.Default) table.get (x);
  }
  public ModuleParameters.Default makeModuleParametersDefault (ITree tree,
							       java.util.
							       List <
							       TypeVar >
							       parameters)
  {
    org.meta_environment.rascal.ast.ModuleParameters.Default x =
      new org.meta_environment.rascal.ast.ModuleParameters.Default (tree,
								    parameters);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ModuleParameters.Default) table.
      get (x);
  }
  public Header.Parameters makeHeaderParameters (ITree tree,
						 org.meta_environment.rascal.
						 ast.ModuleName name,
						 org.meta_environment.rascal.
						 ast.ModuleParameters params,
						 org.meta_environment.rascal.
						 ast.Tags tags,
						 java.util.List < Import >
						 imports)
  {
    org.meta_environment.rascal.ast.Header.Parameters x =
      new org.meta_environment.rascal.ast.Header.Parameters (tree, name,
							     params, tags,
							     imports);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Header.Parameters) table.get (x);
  }
  public Header.Default makeHeaderDefault (ITree tree,
					   org.meta_environment.rascal.ast.
					   ModuleName name,
					   org.meta_environment.rascal.ast.
					   Tags tags,
					   java.util.List < Import > imports)
  {
    org.meta_environment.rascal.ast.Header.Default x =
      new org.meta_environment.rascal.ast.Header.Default (tree, name, tags,
							  imports);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Header.Default) table.get (x);
  }
  public QualifiedName.Default makeQualifiedNameDefault (ITree tree,
							 java.util.List <
							 Name > names)
  {
    org.meta_environment.rascal.ast.QualifiedName.Default x =
      new org.meta_environment.rascal.ast.QualifiedName.Default (tree, names);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.QualifiedName.Default) table.
      get (x);
  }
  public Area.Default makeAreaDefault (ITree tree,
				       org.meta_environment.rascal.ast.
				       Expression beginLine,
				       org.meta_environment.rascal.ast.
				       Expression beginColumn,
				       org.meta_environment.rascal.ast.
				       Expression endLine,
				       org.meta_environment.rascal.ast.
				       Expression endColumn,
				       org.meta_environment.rascal.ast.
				       Expression offset,
				       org.meta_environment.rascal.ast.
				       Expression length)
  {
    org.meta_environment.rascal.ast.Area.Default x =
      new org.meta_environment.rascal.ast.Area.Default (tree, beginLine,
							beginColumn, endLine,
							endColumn, offset,
							length);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Area.Default) table.get (x);
  }
  public Tag.Default makeTagDefault (ITree tree,
				     org.meta_environment.rascal.ast.
				     Name name)
  {
    org.meta_environment.rascal.ast.Tag.Default x =
      new org.meta_environment.rascal.ast.Tag.Default (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Tag.Default) table.get (x);
  }
  public Tags.Default makeTagsDefault (ITree tree,
				       java.util.List < Tag > annotations)
  {
    org.meta_environment.rascal.ast.Tags.Default x =
      new org.meta_environment.rascal.ast.Tags.Default (tree, annotations);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Tags.Default) table.get (x);
  }
  public ValueProducer.
    GivenStrategy makeValueProducerGivenStrategy (ITree tree,
						  org.meta_environment.rascal.
						  ast.Strategy strategy,
						  org.meta_environment.rascal.
						  ast.Expression pattern,
						  org.meta_environment.rascal.
						  ast.Expression expression)
  {
    org.meta_environment.rascal.ast.ValueProducer.GivenStrategy x =
      new org.meta_environment.rascal.ast.ValueProducer.GivenStrategy (tree,
								       strategy,
								       pattern,
								       expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ValueProducer.
	    GivenStrategy) table.get (x);
  }
  public ValueProducer.
    DefaultStrategy makeValueProducerDefaultStrategy (ITree tree,
						      org.meta_environment.
						      rascal.ast.
						      Expression pattern,
						      org.meta_environment.
						      rascal.ast.
						      Expression expression)
  {
    org.meta_environment.rascal.ast.ValueProducer.DefaultStrategy x =
      new org.meta_environment.rascal.ast.ValueProducer.DefaultStrategy (tree,
									 pattern,
									 expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ValueProducer.
	    DefaultStrategy) table.get (x);
  }
  public Generator.Producer makeGeneratorProducer (ITree tree,
						   org.meta_environment.
						   rascal.ast.
						   ValueProducer producer)
  {
    org.meta_environment.rascal.ast.Generator.Producer x =
      new org.meta_environment.rascal.ast.Generator.Producer (tree, producer);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Generator.Producer) table.get (x);
  }
  public Generator.Expression makeGeneratorExpression (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Expression expression)
  {
    org.meta_environment.rascal.ast.Generator.Expression x =
      new org.meta_environment.rascal.ast.Generator.Expression (tree,
								expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Generator.Expression) table.
      get (x);
  }
  public Strategy.Innermost makeStrategyInnermost (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.Innermost x =
      new org.meta_environment.rascal.ast.Strategy.Innermost (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.Innermost) table.get (x);
  }
  public Strategy.Outermost makeStrategyOutermost (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.Outermost x =
      new org.meta_environment.rascal.ast.Strategy.Outermost (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.Outermost) table.get (x);
  }
  public Strategy.BottomUpBreak makeStrategyBottomUpBreak (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.BottomUpBreak x =
      new org.meta_environment.rascal.ast.Strategy.BottomUpBreak (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.BottomUpBreak) table.
      get (x);
  }
  public Strategy.BottomUp makeStrategyBottomUp (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.BottomUp x =
      new org.meta_environment.rascal.ast.Strategy.BottomUp (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.BottomUp) table.get (x);
  }
  public Strategy.TopDownBreak makeStrategyTopDownBreak (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.TopDownBreak x =
      new org.meta_environment.rascal.ast.Strategy.TopDownBreak (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.TopDownBreak) table.
      get (x);
  }
  public Strategy.TopDown makeStrategyTopDown (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.TopDown x =
      new org.meta_environment.rascal.ast.Strategy.TopDown (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.TopDown) table.get (x);
  }
  public Comprehension.List makeComprehensionList (ITree tree,
						   org.meta_environment.
						   rascal.ast.
						   Expression result,
						   java.util.List <
						   Generator > generators)
  {
    org.meta_environment.rascal.ast.Comprehension.List x =
      new org.meta_environment.rascal.ast.Comprehension.List (tree, result,
							      generators);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Comprehension.List) table.get (x);
  }
  public Comprehension.Set makeComprehensionSet (ITree tree,
						 org.meta_environment.rascal.
						 ast.Expression result,
						 java.util.List < Generator >
						 generators)
  {
    org.meta_environment.rascal.ast.Comprehension.Set x =
      new org.meta_environment.rascal.ast.Comprehension.Set (tree, result,
							     generators);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Comprehension.Set) table.get (x);
  }
  public Match.Arbitrary makeMatchArbitrary (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression match,
					     org.meta_environment.rascal.ast.
					     Statement statement)
  {
    org.meta_environment.rascal.ast.Match.Arbitrary x =
      new org.meta_environment.rascal.ast.Match.Arbitrary (tree, match,
							   statement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Match.Arbitrary) table.get (x);
  }
  public Match.Replacing makeMatchReplacing (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression match,
					     org.meta_environment.rascal.ast.
					     Expression replacement)
  {
    org.meta_environment.rascal.ast.Match.Replacing x =
      new org.meta_environment.rascal.ast.Match.Replacing (tree, match,
							   replacement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Match.Replacing) table.get (x);
  }
  public Rule.NoGuard makeRuleNoGuard (ITree tree,
				       org.meta_environment.rascal.ast.
				       Match match)
  {
    org.meta_environment.rascal.ast.Rule.NoGuard x =
      new org.meta_environment.rascal.ast.Rule.NoGuard (tree, match);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Rule.NoGuard) table.get (x);
  }
  public Rule.WithGuard makeRuleWithGuard (ITree tree,
					   org.meta_environment.rascal.ast.
					   Type type,
					   org.meta_environment.rascal.ast.
					   Match match)
  {
    org.meta_environment.rascal.ast.Rule.WithGuard x =
      new org.meta_environment.rascal.ast.Rule.WithGuard (tree, type, match);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Rule.WithGuard) table.get (x);
  }
  public Case.Default makeCaseDefault (ITree tree,
				       org.meta_environment.rascal.ast.
				       Statement statement)
  {
    org.meta_environment.rascal.ast.Case.Default x =
      new org.meta_environment.rascal.ast.Case.Default (tree, statement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Case.Default) table.get (x);
  }
  public Case.Rule makeCaseRule (ITree tree,
				 org.meta_environment.rascal.ast.Rule rule)
  {
    org.meta_environment.rascal.ast.Case.Rule x =
      new org.meta_environment.rascal.ast.Case.Rule (tree, rule);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Case.Rule) table.get (x);
  }
  public Visit.GivenStrategy makeVisitGivenStrategy (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     Strategy strategy,
						     org.meta_environment.
						     rascal.ast.
						     Expression subject,
						     java.util.List < Case >
						     cases)
  {
    org.meta_environment.rascal.ast.Visit.GivenStrategy x =
      new org.meta_environment.rascal.ast.Visit.GivenStrategy (tree, strategy,
							       subject,
							       cases);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Visit.GivenStrategy) table.
      get (x);
  }
  public Visit.DefaultStrategy makeVisitDefaultStrategy (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 Expression subject,
							 java.util.List <
							 Case > cases)
  {
    org.meta_environment.rascal.ast.Visit.DefaultStrategy x =
      new org.meta_environment.rascal.ast.Visit.DefaultStrategy (tree,
								 subject,
								 cases);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Visit.DefaultStrategy) table.
      get (x);
  }
  public IntegerLiteral.
    OctalIntegerLiteral makeIntegerLiteralOctalIntegerLiteral (ITree tree)
  {
    org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral x =
      new org.meta_environment.rascal.ast.IntegerLiteral.
      OctalIntegerLiteral (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.IntegerLiteral.
	    OctalIntegerLiteral) table.get (x);
  }
  public IntegerLiteral.
    HexIntegerLiteral makeIntegerLiteralHexIntegerLiteral (ITree tree)
  {
    org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral x =
      new org.meta_environment.rascal.ast.IntegerLiteral.
      HexIntegerLiteral (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.IntegerLiteral.
	    HexIntegerLiteral) table.get (x);
  }
  public IntegerLiteral.
    DecimalIntegerLiteral makeIntegerLiteralDecimalIntegerLiteral (ITree tree)
  {
    org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral x =
      new org.meta_environment.rascal.ast.IntegerLiteral.
      DecimalIntegerLiteral (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.IntegerLiteral.
	    DecimalIntegerLiteral) table.get (x);
  }
  public LongLiteral.
    OctalLongLiteral makeLongLiteralOctalLongLiteral (ITree tree)
  {
    org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral x =
      new org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LongLiteral.
	    OctalLongLiteral) table.get (x);
  }
  public LongLiteral.HexLongLiteral makeLongLiteralHexLongLiteral (ITree tree)
  {
    org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral x =
      new org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral) table.
      get (x);
  }
  public LongLiteral.
    DecimalLongLiteral makeLongLiteralDecimalLongLiteral (ITree tree)
  {
    org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral x =
      new org.meta_environment.rascal.ast.LongLiteral.
      DecimalLongLiteral (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LongLiteral.
	    DecimalLongLiteral) table.get (x);
  }
}
