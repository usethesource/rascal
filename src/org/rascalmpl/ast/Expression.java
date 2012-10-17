/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public abstract class Expression extends AbstractAST {
  public Expression(IConstructor node) {
    super();
  }

  
  public boolean hasArguments() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getArguments() {
    throw new UnsupportedOperationException();
  }
  public boolean hasElements() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getElements() {
    throw new UnsupportedOperationException();
  }
  public boolean hasGenerators() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getGenerators() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSubscripts() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getSubscripts() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFields() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Field> getFields() {
    throw new UnsupportedOperationException();
  }
  public boolean hasMappings() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Mapping_Expression> getMappings() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStatements() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
    throw new UnsupportedOperationException();
  }
  public boolean hasComprehension() {
    return false;
  }

  public org.rascalmpl.ast.Comprehension getComprehension() {
    throw new UnsupportedOperationException();
  }
  public boolean hasArgument() {
    return false;
  }

  public org.rascalmpl.ast.Expression getArgument() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCondition() {
    return false;
  }

  public org.rascalmpl.ast.Expression getCondition() {
    throw new UnsupportedOperationException();
  }
  public boolean hasDefinitions() {
    return false;
  }

  public org.rascalmpl.ast.Expression getDefinitions() {
    throw new UnsupportedOperationException();
  }
  public boolean hasElseExp() {
    return false;
  }

  public org.rascalmpl.ast.Expression getElseExp() {
    throw new UnsupportedOperationException();
  }
  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFirst() {
    return false;
  }

  public org.rascalmpl.ast.Expression getFirst() {
    throw new UnsupportedOperationException();
  }
  public boolean hasInit() {
    return false;
  }

  public org.rascalmpl.ast.Expression getInit() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLast() {
    return false;
  }

  public org.rascalmpl.ast.Expression getLast() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLhs() {
    return false;
  }

  public org.rascalmpl.ast.Expression getLhs() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPattern() {
    return false;
  }

  public org.rascalmpl.ast.Expression getPattern() {
    throw new UnsupportedOperationException();
  }
  public boolean hasReplacement() {
    return false;
  }

  public org.rascalmpl.ast.Expression getReplacement() {
    throw new UnsupportedOperationException();
  }
  public boolean hasResult() {
    return false;
  }

  public org.rascalmpl.ast.Expression getResult() {
    throw new UnsupportedOperationException();
  }
  public boolean hasRhs() {
    return false;
  }

  public org.rascalmpl.ast.Expression getRhs() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSecond() {
    return false;
  }

  public org.rascalmpl.ast.Expression getSecond() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSymbol() {
    return false;
  }

  public org.rascalmpl.ast.Expression getSymbol() {
    throw new UnsupportedOperationException();
  }
  public boolean hasThenExp() {
    return false;
  }

  public org.rascalmpl.ast.Expression getThenExp() {
    throw new UnsupportedOperationException();
  }
  public boolean hasValue() {
    return false;
  }

  public org.rascalmpl.ast.Expression getValue() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLabel() {
    return false;
  }

  public org.rascalmpl.ast.Label getLabel() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLiteral() {
    return false;
  }

  public org.rascalmpl.ast.Literal getLiteral() {
    throw new UnsupportedOperationException();
  }
  public boolean hasField() {
    return false;
  }

  public org.rascalmpl.ast.Name getField() {
    throw new UnsupportedOperationException();
  }
  public boolean hasKey() {
    return false;
  }

  public org.rascalmpl.ast.Name getKey() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasParameters() {
    return false;
  }

  public org.rascalmpl.ast.Parameters getParameters() {
    throw new UnsupportedOperationException();
  }
  public boolean hasQualifiedName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getQualifiedName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }
  public boolean hasVisit() {
    return false;
  }

  public org.rascalmpl.ast.Visit getVisit() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Expression {
    private final java.util.List<org.rascalmpl.ast.Expression> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Expression> alternatives) {
      super(node);
      this.node = node;
      this.alternatives = java.util.Collections.unmodifiableList(alternatives);
    }
    
    @Override
    public IConstructor getTree() {
      return node;
    }
  
    @Override
    public AbstractAST findNode(int offset) {
      return null;
    }
  
    @Override
    public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
      throw new Ambiguous(src);
    }
      
    @Override
    public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
      throw new Ambiguous(src);
    }
    
    public java.util.List<org.rascalmpl.ast.Expression> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitExpressionAmbiguity(this);
    }
  }

  

  
  public boolean isAddition() {
    return false;
  }

  static public class Addition extends Expression {
    // Production: sig("Addition",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Addition(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isAddition() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionAddition(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isAll() {
    return false;
  }

  static public class All extends Expression {
    // Production: sig("All",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators")])
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
  
    public All(IConstructor node , java.util.List<org.rascalmpl.ast.Expression> generators) {
      super(node);
      
      this.generators = generators;
    }
  
    @Override
    public boolean isAll() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionAll(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getGenerators() {
      return this.generators;
    }
  
    @Override
    public boolean hasGenerators() {
      return true;
    }	
  }
  public boolean isAnd() {
    return false;
  }

  static public class And extends Expression {
    // Production: sig("And",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public And(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isAnd() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionAnd(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isAnti() {
    return false;
  }

  static public class Anti extends Expression {
    // Production: sig("Anti",[arg("org.rascalmpl.ast.Expression","pattern")])
  
    
    private final org.rascalmpl.ast.Expression pattern;
  
    public Anti(IConstructor node , org.rascalmpl.ast.Expression pattern) {
      super(node);
      
      this.pattern = pattern;
    }
  
    @Override
    public boolean isAnti() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionAnti(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }	
  }
  public boolean isAny() {
    return false;
  }

  static public class Any extends Expression {
    // Production: sig("Any",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators")])
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
  
    public Any(IConstructor node , java.util.List<org.rascalmpl.ast.Expression> generators) {
      super(node);
      
      this.generators = generators;
    }
  
    @Override
    public boolean isAny() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionAny(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getGenerators() {
      return this.generators;
    }
  
    @Override
    public boolean hasGenerators() {
      return true;
    }	
  }
  public boolean isAppendAfter() {
    return false;
  }

  static public class AppendAfter extends Expression {
    // Production: sig("AppendAfter",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public AppendAfter(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isAppendAfter() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionAppendAfter(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isAsType() {
    return false;
  }

  static public class AsType extends Expression {
    // Production: sig("AsType",[arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Expression","argument")])
  
    
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Expression argument;
  
    public AsType(IConstructor node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Expression argument) {
      super(node);
      
      this.type = type;
      this.argument = argument;
    }
  
    @Override
    public boolean isAsType() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionAsType(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getArgument() {
      return this.argument;
    }
  
    @Override
    public boolean hasArgument() {
      return true;
    }	
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends Expression {
    // Production: sig("Bracket",[arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Expression expression;
  
    public Bracket(IConstructor node , org.rascalmpl.ast.Expression expression) {
      super(node);
      
      this.expression = expression;
    }
  
    @Override
    public boolean isBracket() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionBracket(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }	
  }
  public boolean isCallOrTree() {
    return false;
  }

  static public class CallOrTree extends Expression {
    // Production: sig("CallOrTree",[arg("org.rascalmpl.ast.Expression","expression"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","arguments")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final java.util.List<org.rascalmpl.ast.Expression> arguments;
  
    public CallOrTree(IConstructor node , org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Expression> arguments) {
      super(node);
      
      this.expression = expression;
      this.arguments = arguments;
    }
  
    @Override
    public boolean isCallOrTree() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionCallOrTree(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getArguments() {
      return this.arguments;
    }
  
    @Override
    public boolean hasArguments() {
      return true;
    }	
  }
  public boolean isClosure() {
    return false;
  }

  static public class Closure extends Expression {
    // Production: sig("Closure",[arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Parameters","parameters"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","statements")])
  
    
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Parameters parameters;
    private final java.util.List<org.rascalmpl.ast.Statement> statements;
  
    public Closure(IConstructor node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Parameters parameters,  java.util.List<org.rascalmpl.ast.Statement> statements) {
      super(node);
      
      this.type = type;
      this.parameters = parameters;
      this.statements = statements;
    }
  
    @Override
    public boolean isClosure() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionClosure(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Parameters getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
      return this.statements;
    }
  
    @Override
    public boolean hasStatements() {
      return true;
    }	
  }
  public boolean isComposition() {
    return false;
  }

  static public class Composition extends Expression {
    // Production: sig("Composition",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Composition(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isComposition() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionComposition(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isComprehension() {
    return false;
  }

  static public class Comprehension extends Expression {
    // Production: sig("Comprehension",[arg("org.rascalmpl.ast.Comprehension","comprehension")])
  
    
    private final org.rascalmpl.ast.Comprehension comprehension;
  
    public Comprehension(IConstructor node , org.rascalmpl.ast.Comprehension comprehension) {
      super(node);
      
      this.comprehension = comprehension;
    }
  
    @Override
    public boolean isComprehension() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionComprehension(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Comprehension getComprehension() {
      return this.comprehension;
    }
  
    @Override
    public boolean hasComprehension() {
      return true;
    }	
  }
  public boolean isDescendant() {
    return false;
  }

  static public class Descendant extends Expression {
    // Production: sig("Descendant",[arg("org.rascalmpl.ast.Expression","pattern")])
  
    
    private final org.rascalmpl.ast.Expression pattern;
  
    public Descendant(IConstructor node , org.rascalmpl.ast.Expression pattern) {
      super(node);
      
      this.pattern = pattern;
    }
  
    @Override
    public boolean isDescendant() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionDescendant(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }	
  }
  public boolean isDivision() {
    return false;
  }

  static public class Division extends Expression {
    // Production: sig("Division",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Division(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isDivision() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionDivision(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isEnumerator() {
    return false;
  }

  static public class Enumerator extends Expression {
    // Production: sig("Enumerator",[arg("org.rascalmpl.ast.Expression","pattern"),arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Expression pattern;
    private final org.rascalmpl.ast.Expression expression;
  
    public Enumerator(IConstructor node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Expression expression) {
      super(node);
      
      this.pattern = pattern;
      this.expression = expression;
    }
  
    @Override
    public boolean isEnumerator() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionEnumerator(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }	
  }
  public boolean isEquals() {
    return false;
  }

  static public class Equals extends Expression {
    // Production: sig("Equals",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Equals(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isEquals() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionEquals(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isEquivalence() {
    return false;
  }

  static public class Equivalence extends Expression {
    // Production: sig("Equivalence",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Equivalence(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isEquivalence() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionEquivalence(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isFieldAccess() {
    return false;
  }

  static public class FieldAccess extends Expression {
    // Production: sig("FieldAccess",[arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.Name","field")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.Name field;
  
    public FieldAccess(IConstructor node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name field) {
      super(node);
      
      this.expression = expression;
      this.field = field;
    }
  
    @Override
    public boolean isFieldAccess() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionFieldAccess(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getField() {
      return this.field;
    }
  
    @Override
    public boolean hasField() {
      return true;
    }	
  }
  public boolean isFieldProject() {
    return false;
  }

  static public class FieldProject extends Expression {
    // Production: sig("FieldProject",[arg("org.rascalmpl.ast.Expression","expression"),arg("java.util.List\<org.rascalmpl.ast.Field\>","fields")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final java.util.List<org.rascalmpl.ast.Field> fields;
  
    public FieldProject(IConstructor node , org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Field> fields) {
      super(node);
      
      this.expression = expression;
      this.fields = fields;
    }
  
    @Override
    public boolean isFieldProject() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionFieldProject(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Field> getFields() {
      return this.fields;
    }
  
    @Override
    public boolean hasFields() {
      return true;
    }	
  }
  public boolean isFieldUpdate() {
    return false;
  }

  static public class FieldUpdate extends Expression {
    // Production: sig("FieldUpdate",[arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.Name","key"),arg("org.rascalmpl.ast.Expression","replacement")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.Name key;
    private final org.rascalmpl.ast.Expression replacement;
  
    public FieldUpdate(IConstructor node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name key,  org.rascalmpl.ast.Expression replacement) {
      super(node);
      
      this.expression = expression;
      this.key = key;
      this.replacement = replacement;
    }
  
    @Override
    public boolean isFieldUpdate() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionFieldUpdate(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getKey() {
      return this.key;
    }
  
    @Override
    public boolean hasKey() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getReplacement() {
      return this.replacement;
    }
  
    @Override
    public boolean hasReplacement() {
      return true;
    }	
  }
  public boolean isGetAnnotation() {
    return false;
  }

  static public class GetAnnotation extends Expression {
    // Production: sig("GetAnnotation",[arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.Name","name")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.Name name;
  
    public GetAnnotation(IConstructor node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name name) {
      super(node);
      
      this.expression = expression;
      this.name = name;
    }
  
    @Override
    public boolean isGetAnnotation() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionGetAnnotation(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isGreaterThan() {
    return false;
  }

  static public class GreaterThan extends Expression {
    // Production: sig("GreaterThan",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public GreaterThan(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isGreaterThan() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionGreaterThan(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isGreaterThanOrEq() {
    return false;
  }

  static public class GreaterThanOrEq extends Expression {
    // Production: sig("GreaterThanOrEq",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public GreaterThanOrEq(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isGreaterThanOrEq() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionGreaterThanOrEq(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isHas() {
    return false;
  }

  static public class Has extends Expression {
    // Production: sig("Has",[arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.Name","name")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.Name name;
  
    public Has(IConstructor node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name name) {
      super(node);
      
      this.expression = expression;
      this.name = name;
    }
  
    @Override
    public boolean isHas() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionHas(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isIfDefinedOtherwise() {
    return false;
  }

  static public class IfDefinedOtherwise extends Expression {
    // Production: sig("IfDefinedOtherwise",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public IfDefinedOtherwise(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isIfDefinedOtherwise() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionIfDefinedOtherwise(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isIfThenElse() {
    return false;
  }

  static public class IfThenElse extends Expression {
    // Production: sig("IfThenElse",[arg("org.rascalmpl.ast.Expression","condition"),arg("org.rascalmpl.ast.Expression","thenExp"),arg("org.rascalmpl.ast.Expression","elseExp")])
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final org.rascalmpl.ast.Expression thenExp;
    private final org.rascalmpl.ast.Expression elseExp;
  
    public IfThenElse(IConstructor node , org.rascalmpl.ast.Expression condition,  org.rascalmpl.ast.Expression thenExp,  org.rascalmpl.ast.Expression elseExp) {
      super(node);
      
      this.condition = condition;
      this.thenExp = thenExp;
      this.elseExp = elseExp;
    }
  
    @Override
    public boolean isIfThenElse() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionIfThenElse(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getCondition() {
      return this.condition;
    }
  
    @Override
    public boolean hasCondition() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getThenExp() {
      return this.thenExp;
    }
  
    @Override
    public boolean hasThenExp() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getElseExp() {
      return this.elseExp;
    }
  
    @Override
    public boolean hasElseExp() {
      return true;
    }	
  }
  public boolean isImplication() {
    return false;
  }

  static public class Implication extends Expression {
    // Production: sig("Implication",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Implication(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isImplication() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionImplication(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isIn() {
    return false;
  }

  static public class In extends Expression {
    // Production: sig("In",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public In(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isIn() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionIn(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isInsertBefore() {
    return false;
  }

  static public class InsertBefore extends Expression {
    // Production: sig("InsertBefore",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public InsertBefore(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isInsertBefore() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionInsertBefore(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isIntersection() {
    return false;
  }

  static public class Intersection extends Expression {
    // Production: sig("Intersection",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Intersection(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isIntersection() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionIntersection(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isIs() {
    return false;
  }

  static public class Is extends Expression {
    // Production: sig("Is",[arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.Name","name")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.Name name;
  
    public Is(IConstructor node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name name) {
      super(node);
      
      this.expression = expression;
      this.name = name;
    }
  
    @Override
    public boolean isIs() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionIs(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isIsDefined() {
    return false;
  }

  static public class IsDefined extends Expression {
    // Production: sig("IsDefined",[arg("org.rascalmpl.ast.Expression","argument")])
  
    
    private final org.rascalmpl.ast.Expression argument;
  
    public IsDefined(IConstructor node , org.rascalmpl.ast.Expression argument) {
      super(node);
      
      this.argument = argument;
    }
  
    @Override
    public boolean isIsDefined() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionIsDefined(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getArgument() {
      return this.argument;
    }
  
    @Override
    public boolean hasArgument() {
      return true;
    }	
  }
  public boolean isIt() {
    return false;
  }

  static public class It extends Expression {
    // Production: sig("It",[])
  
    
  
    public It(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isIt() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionIt(this);
    }
  
    	
  }
  public boolean isJoin() {
    return false;
  }

  static public class Join extends Expression {
    // Production: sig("Join",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Join(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isJoin() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionJoin(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isLessThan() {
    return false;
  }

  static public class LessThan extends Expression {
    // Production: sig("LessThan",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public LessThan(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isLessThan() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionLessThan(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isLessThanOrEq() {
    return false;
  }

  static public class LessThanOrEq extends Expression {
    // Production: sig("LessThanOrEq",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public LessThanOrEq(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isLessThanOrEq() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionLessThanOrEq(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isList() {
    return false;
  }

  static public class List extends Expression {
    // Production: sig("List",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","elements")])
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> elements;
  
    public List(IConstructor node , java.util.List<org.rascalmpl.ast.Expression> elements) {
      super(node);
      
      this.elements = elements;
    }
  
    @Override
    public boolean isList() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionList(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getElements() {
      return this.elements;
    }
  
    @Override
    public boolean hasElements() {
      return true;
    }	
  }
  public boolean isLiteral() {
    return false;
  }

  static public class Literal extends Expression {
    // Production: sig("Literal",[arg("org.rascalmpl.ast.Literal","literal")])
  
    
    private final org.rascalmpl.ast.Literal literal;
  
    public Literal(IConstructor node , org.rascalmpl.ast.Literal literal) {
      super(node);
      
      this.literal = literal;
    }
  
    @Override
    public boolean isLiteral() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionLiteral(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Literal getLiteral() {
      return this.literal;
    }
  
    @Override
    public boolean hasLiteral() {
      return true;
    }	
  }
  public boolean isMap() {
    return false;
  }

  static public class Map extends Expression {
    // Production: sig("Map",[arg("java.util.List\<org.rascalmpl.ast.Mapping_Expression\>","mappings")])
  
    
    private final java.util.List<org.rascalmpl.ast.Mapping_Expression> mappings;
  
    public Map(IConstructor node , java.util.List<org.rascalmpl.ast.Mapping_Expression> mappings) {
      super(node);
      
      this.mappings = mappings;
    }
  
    @Override
    public boolean isMap() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionMap(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Mapping_Expression> getMappings() {
      return this.mappings;
    }
  
    @Override
    public boolean hasMappings() {
      return true;
    }	
  }
  public boolean isMatch() {
    return false;
  }

  static public class Match extends Expression {
    // Production: sig("Match",[arg("org.rascalmpl.ast.Expression","pattern"),arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Expression pattern;
    private final org.rascalmpl.ast.Expression expression;
  
    public Match(IConstructor node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Expression expression) {
      super(node);
      
      this.pattern = pattern;
      this.expression = expression;
    }
  
    @Override
    public boolean isMatch() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionMatch(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }	
  }
  public boolean isModulo() {
    return false;
  }

  static public class Modulo extends Expression {
    // Production: sig("Modulo",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Modulo(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isModulo() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionModulo(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isMultiVariable() {
    return false;
  }

  static public class MultiVariable extends Expression {
    // Production: sig("MultiVariable",[arg("org.rascalmpl.ast.QualifiedName","qualifiedName")])
  
    
    private final org.rascalmpl.ast.QualifiedName qualifiedName;
  
    public MultiVariable(IConstructor node , org.rascalmpl.ast.QualifiedName qualifiedName) {
      super(node);
      
      this.qualifiedName = qualifiedName;
    }
  
    @Override
    public boolean isMultiVariable() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionMultiVariable(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getQualifiedName() {
      return this.qualifiedName;
    }
  
    @Override
    public boolean hasQualifiedName() {
      return true;
    }	
  }
  public boolean isNegation() {
    return false;
  }

  static public class Negation extends Expression {
    // Production: sig("Negation",[arg("org.rascalmpl.ast.Expression","argument")])
  
    
    private final org.rascalmpl.ast.Expression argument;
  
    public Negation(IConstructor node , org.rascalmpl.ast.Expression argument) {
      super(node);
      
      this.argument = argument;
    }
  
    @Override
    public boolean isNegation() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionNegation(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getArgument() {
      return this.argument;
    }
  
    @Override
    public boolean hasArgument() {
      return true;
    }	
  }
  public boolean isNegative() {
    return false;
  }

  static public class Negative extends Expression {
    // Production: sig("Negative",[arg("org.rascalmpl.ast.Expression","argument")])
  
    
    private final org.rascalmpl.ast.Expression argument;
  
    public Negative(IConstructor node , org.rascalmpl.ast.Expression argument) {
      super(node);
      
      this.argument = argument;
    }
  
    @Override
    public boolean isNegative() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionNegative(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getArgument() {
      return this.argument;
    }
  
    @Override
    public boolean hasArgument() {
      return true;
    }	
  }
  public boolean isNoMatch() {
    return false;
  }

  static public class NoMatch extends Expression {
    // Production: sig("NoMatch",[arg("org.rascalmpl.ast.Expression","pattern"),arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Expression pattern;
    private final org.rascalmpl.ast.Expression expression;
  
    public NoMatch(IConstructor node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Expression expression) {
      super(node);
      
      this.pattern = pattern;
      this.expression = expression;
    }
  
    @Override
    public boolean isNoMatch() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionNoMatch(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }	
  }
  public boolean isNonEmptyBlock() {
    return false;
  }

  static public class NonEmptyBlock extends Expression {
    // Production: sig("NonEmptyBlock",[arg("java.util.List\<org.rascalmpl.ast.Statement\>","statements")])
  
    
    private final java.util.List<org.rascalmpl.ast.Statement> statements;
  
    public NonEmptyBlock(IConstructor node , java.util.List<org.rascalmpl.ast.Statement> statements) {
      super(node);
      
      this.statements = statements;
    }
  
    @Override
    public boolean isNonEmptyBlock() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionNonEmptyBlock(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
      return this.statements;
    }
  
    @Override
    public boolean hasStatements() {
      return true;
    }	
  }
  public boolean isNonEquals() {
    return false;
  }

  static public class NonEquals extends Expression {
    // Production: sig("NonEquals",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public NonEquals(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isNonEquals() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionNonEquals(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isNotIn() {
    return false;
  }

  static public class NotIn extends Expression {
    // Production: sig("NotIn",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public NotIn(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isNotIn() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionNotIn(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isOr() {
    return false;
  }

  static public class Or extends Expression {
    // Production: sig("Or",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Or(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isOr() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionOr(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isProduct() {
    return false;
  }

  static public class Product extends Expression {
    // Production: sig("Product",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Product(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isProduct() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionProduct(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isQualifiedName() {
    return false;
  }

  static public class QualifiedName extends Expression {
    // Production: sig("QualifiedName",[arg("org.rascalmpl.ast.QualifiedName","qualifiedName")])
  
    
    private final org.rascalmpl.ast.QualifiedName qualifiedName;
  
    public QualifiedName(IConstructor node , org.rascalmpl.ast.QualifiedName qualifiedName) {
      super(node);
      
      this.qualifiedName = qualifiedName;
    }
  
    @Override
    public boolean isQualifiedName() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionQualifiedName(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getQualifiedName() {
      return this.qualifiedName;
    }
  
    @Override
    public boolean hasQualifiedName() {
      return true;
    }	
  }
  public boolean isRange() {
    return false;
  }

  static public class Range extends Expression {
    // Production: sig("Range",[arg("org.rascalmpl.ast.Expression","first"),arg("org.rascalmpl.ast.Expression","last")])
  
    
    private final org.rascalmpl.ast.Expression first;
    private final org.rascalmpl.ast.Expression last;
  
    public Range(IConstructor node , org.rascalmpl.ast.Expression first,  org.rascalmpl.ast.Expression last) {
      super(node);
      
      this.first = first;
      this.last = last;
    }
  
    @Override
    public boolean isRange() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionRange(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getFirst() {
      return this.first;
    }
  
    @Override
    public boolean hasFirst() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getLast() {
      return this.last;
    }
  
    @Override
    public boolean hasLast() {
      return true;
    }	
  }
  public boolean isReducer() {
    return false;
  }

  static public class Reducer extends Expression {
    // Production: sig("Reducer",[arg("org.rascalmpl.ast.Expression","init"),arg("org.rascalmpl.ast.Expression","result"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators")])
  
    
    private final org.rascalmpl.ast.Expression init;
    private final org.rascalmpl.ast.Expression result;
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
  
    public Reducer(IConstructor node , org.rascalmpl.ast.Expression init,  org.rascalmpl.ast.Expression result,  java.util.List<org.rascalmpl.ast.Expression> generators) {
      super(node);
      
      this.init = init;
      this.result = result;
      this.generators = generators;
    }
  
    @Override
    public boolean isReducer() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionReducer(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getInit() {
      return this.init;
    }
  
    @Override
    public boolean hasInit() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getResult() {
      return this.result;
    }
  
    @Override
    public boolean hasResult() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getGenerators() {
      return this.generators;
    }
  
    @Override
    public boolean hasGenerators() {
      return true;
    }	
  }
  public boolean isReifiedType() {
    return false;
  }

  static public class ReifiedType extends Expression {
    // Production: sig("ReifiedType",[arg("org.rascalmpl.ast.Expression","symbol"),arg("org.rascalmpl.ast.Expression","definitions")])
  
    
    private final org.rascalmpl.ast.Expression symbol;
    private final org.rascalmpl.ast.Expression definitions;
  
    public ReifiedType(IConstructor node , org.rascalmpl.ast.Expression symbol,  org.rascalmpl.ast.Expression definitions) {
      super(node);
      
      this.symbol = symbol;
      this.definitions = definitions;
    }
  
    @Override
    public boolean isReifiedType() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionReifiedType(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getDefinitions() {
      return this.definitions;
    }
  
    @Override
    public boolean hasDefinitions() {
      return true;
    }	
  }
  public boolean isReifyType() {
    return false;
  }

  static public class ReifyType extends Expression {
    // Production: sig("ReifyType",[arg("org.rascalmpl.ast.Type","type")])
  
    
    private final org.rascalmpl.ast.Type type;
  
    public ReifyType(IConstructor node , org.rascalmpl.ast.Type type) {
      super(node);
      
      this.type = type;
    }
  
    @Override
    public boolean isReifyType() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionReifyType(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }	
  }
  public boolean isRemainder() {
    return false;
  }

  static public class Remainder extends Expression {
    // Production: sig("Remainder",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Remainder(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isRemainder() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionRemainder(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isSet() {
    return false;
  }

  static public class Set extends Expression {
    // Production: sig("Set",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","elements")])
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> elements;
  
    public Set(IConstructor node , java.util.List<org.rascalmpl.ast.Expression> elements) {
      super(node);
      
      this.elements = elements;
    }
  
    @Override
    public boolean isSet() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionSet(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getElements() {
      return this.elements;
    }
  
    @Override
    public boolean hasElements() {
      return true;
    }	
  }
  public boolean isSetAnnotation() {
    return false;
  }

  static public class SetAnnotation extends Expression {
    // Production: sig("SetAnnotation",[arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Expression","value")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.Name name;
    private final org.rascalmpl.ast.Expression value;
  
    public SetAnnotation(IConstructor node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression value) {
      super(node);
      
      this.expression = expression;
      this.name = name;
      this.value = value;
    }
  
    @Override
    public boolean isSetAnnotation() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionSetAnnotation(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getValue() {
      return this.value;
    }
  
    @Override
    public boolean hasValue() {
      return true;
    }	
  }
  public boolean isSplice() {
    return false;
  }

  static public class Splice extends Expression {
    // Production: sig("Splice",[arg("org.rascalmpl.ast.Expression","argument")])
  
    
    private final org.rascalmpl.ast.Expression argument;
  
    public Splice(IConstructor node , org.rascalmpl.ast.Expression argument) {
      super(node);
      
      this.argument = argument;
    }
  
    @Override
    public boolean isSplice() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionSplice(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getArgument() {
      return this.argument;
    }
  
    @Override
    public boolean hasArgument() {
      return true;
    }	
  }
  public boolean isSplicePlus() {
    return false;
  }

  static public class SplicePlus extends Expression {
    // Production: sig("SplicePlus",[arg("org.rascalmpl.ast.Expression","argument")])
  
    
    private final org.rascalmpl.ast.Expression argument;
  
    public SplicePlus(IConstructor node , org.rascalmpl.ast.Expression argument) {
      super(node);
      
      this.argument = argument;
    }
  
    @Override
    public boolean isSplicePlus() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionSplicePlus(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getArgument() {
      return this.argument;
    }
  
    @Override
    public boolean hasArgument() {
      return true;
    }	
  }
  public boolean isStepRange() {
    return false;
  }

  static public class StepRange extends Expression {
    // Production: sig("StepRange",[arg("org.rascalmpl.ast.Expression","first"),arg("org.rascalmpl.ast.Expression","second"),arg("org.rascalmpl.ast.Expression","last")])
  
    
    private final org.rascalmpl.ast.Expression first;
    private final org.rascalmpl.ast.Expression second;
    private final org.rascalmpl.ast.Expression last;
  
    public StepRange(IConstructor node , org.rascalmpl.ast.Expression first,  org.rascalmpl.ast.Expression second,  org.rascalmpl.ast.Expression last) {
      super(node);
      
      this.first = first;
      this.second = second;
      this.last = last;
    }
  
    @Override
    public boolean isStepRange() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionStepRange(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getFirst() {
      return this.first;
    }
  
    @Override
    public boolean hasFirst() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getSecond() {
      return this.second;
    }
  
    @Override
    public boolean hasSecond() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getLast() {
      return this.last;
    }
  
    @Override
    public boolean hasLast() {
      return true;
    }	
  }
  public boolean isSubscript() {
    return false;
  }

  static public class Subscript extends Expression {
    // Production: sig("Subscript",[arg("org.rascalmpl.ast.Expression","expression"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","subscripts")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final java.util.List<org.rascalmpl.ast.Expression> subscripts;
  
    public Subscript(IConstructor node , org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Expression> subscripts) {
      super(node);
      
      this.expression = expression;
      this.subscripts = subscripts;
    }
  
    @Override
    public boolean isSubscript() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionSubscript(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getSubscripts() {
      return this.subscripts;
    }
  
    @Override
    public boolean hasSubscripts() {
      return true;
    }	
  }
  public boolean isSubtraction() {
    return false;
  }

  static public class Subtraction extends Expression {
    // Production: sig("Subtraction",[arg("org.rascalmpl.ast.Expression","lhs"),arg("org.rascalmpl.ast.Expression","rhs")])
  
    
    private final org.rascalmpl.ast.Expression lhs;
    private final org.rascalmpl.ast.Expression rhs;
  
    public Subtraction(IConstructor node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isSubtraction() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionSubtraction(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isTransitiveClosure() {
    return false;
  }

  static public class TransitiveClosure extends Expression {
    // Production: sig("TransitiveClosure",[arg("org.rascalmpl.ast.Expression","argument")])
  
    
    private final org.rascalmpl.ast.Expression argument;
  
    public TransitiveClosure(IConstructor node , org.rascalmpl.ast.Expression argument) {
      super(node);
      
      this.argument = argument;
    }
  
    @Override
    public boolean isTransitiveClosure() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionTransitiveClosure(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getArgument() {
      return this.argument;
    }
  
    @Override
    public boolean hasArgument() {
      return true;
    }	
  }
  public boolean isTransitiveReflexiveClosure() {
    return false;
  }

  static public class TransitiveReflexiveClosure extends Expression {
    // Production: sig("TransitiveReflexiveClosure",[arg("org.rascalmpl.ast.Expression","argument")])
  
    
    private final org.rascalmpl.ast.Expression argument;
  
    public TransitiveReflexiveClosure(IConstructor node , org.rascalmpl.ast.Expression argument) {
      super(node);
      
      this.argument = argument;
    }
  
    @Override
    public boolean isTransitiveReflexiveClosure() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionTransitiveReflexiveClosure(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getArgument() {
      return this.argument;
    }
  
    @Override
    public boolean hasArgument() {
      return true;
    }	
  }
  public boolean isTuple() {
    return false;
  }

  static public class Tuple extends Expression {
    // Production: sig("Tuple",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","elements")])
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> elements;
  
    public Tuple(IConstructor node , java.util.List<org.rascalmpl.ast.Expression> elements) {
      super(node);
      
      this.elements = elements;
    }
  
    @Override
    public boolean isTuple() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionTuple(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getElements() {
      return this.elements;
    }
  
    @Override
    public boolean hasElements() {
      return true;
    }	
  }
  public boolean isTypedVariable() {
    return false;
  }

  static public class TypedVariable extends Expression {
    // Production: sig("TypedVariable",[arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name")])
  
    
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Name name;
  
    public TypedVariable(IConstructor node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name) {
      super(node);
      
      this.type = type;
      this.name = name;
    }
  
    @Override
    public boolean isTypedVariable() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionTypedVariable(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isTypedVariableBecomes() {
    return false;
  }

  static public class TypedVariableBecomes extends Expression {
    // Production: sig("TypedVariableBecomes",[arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Expression","pattern")])
  
    
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Name name;
    private final org.rascalmpl.ast.Expression pattern;
  
    public TypedVariableBecomes(IConstructor node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression pattern) {
      super(node);
      
      this.type = type;
      this.name = name;
      this.pattern = pattern;
    }
  
    @Override
    public boolean isTypedVariableBecomes() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionTypedVariableBecomes(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }	
  }
  public boolean isVariableBecomes() {
    return false;
  }

  static public class VariableBecomes extends Expression {
    // Production: sig("VariableBecomes",[arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Expression","pattern")])
  
    
    private final org.rascalmpl.ast.Name name;
    private final org.rascalmpl.ast.Expression pattern;
  
    public VariableBecomes(IConstructor node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression pattern) {
      super(node);
      
      this.name = name;
      this.pattern = pattern;
    }
  
    @Override
    public boolean isVariableBecomes() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionVariableBecomes(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }	
  }
  public boolean isVisit() {
    return false;
  }

  static public class Visit extends Expression {
    // Production: sig("Visit",[arg("org.rascalmpl.ast.Label","label"),arg("org.rascalmpl.ast.Visit","visit")])
  
    
    private final org.rascalmpl.ast.Label label;
    private final org.rascalmpl.ast.Visit visit;
  
    public Visit(IConstructor node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Visit visit) {
      super(node);
      
      this.label = label;
      this.visit = visit;
    }
  
    @Override
    public boolean isVisit() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionVisit(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Label getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Visit getVisit() {
      return this.visit;
    }
  
    @Override
    public boolean hasVisit() {
      return true;
    }	
  }
  public boolean isVoidClosure() {
    return false;
  }

  static public class VoidClosure extends Expression {
    // Production: sig("VoidClosure",[arg("org.rascalmpl.ast.Parameters","parameters"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","statements")])
  
    
    private final org.rascalmpl.ast.Parameters parameters;
    private final java.util.List<org.rascalmpl.ast.Statement> statements;
  
    public VoidClosure(IConstructor node , org.rascalmpl.ast.Parameters parameters,  java.util.List<org.rascalmpl.ast.Statement> statements) {
      super(node);
      
      this.parameters = parameters;
      this.statements = statements;
    }
  
    @Override
    public boolean isVoidClosure() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitExpressionVoidClosure(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Parameters getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
      return this.statements;
    }
  
    @Override
    public boolean hasStatements() {
      return true;
    }	
  }
}