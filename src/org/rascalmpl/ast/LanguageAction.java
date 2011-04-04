/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/

package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.eclipse.imp.pdb.facts.IConstructor;

import org.eclipse.imp.pdb.facts.IValue;

import org.rascalmpl.interpreter.BooleanEvaluator;

import org.rascalmpl.interpreter.Evaluator;

import org.rascalmpl.interpreter.PatternEvaluator;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.rascalmpl.interpreter.env.Environment;

import org.rascalmpl.interpreter.matching.IBooleanResult;

import org.rascalmpl.interpreter.matching.IMatchingResult;

import org.rascalmpl.interpreter.result.Result;


public abstract class LanguageAction extends AbstractAST {
  public LanguageAction(INode node) {
    super(node);
  }
  

  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }

  public boolean hasStatements() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
    throw new UnsupportedOperationException();
  }

  public boolean hasConditions() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends LanguageAction {
  private final java.util.List<org.rascalmpl.ast.LanguageAction> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LanguageAction> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  @Override
  public Result<IValue> interpret(Evaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  @Override
  public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  @Override
  public IBooleanResult buildBooleanBacktracker(BooleanEvaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }

  @Override
  public IMatchingResult buildMatcher(PatternEvaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  public java.util.List<org.rascalmpl.ast.LanguageAction> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitLanguageActionAmbiguity(this);
  }
}





  public boolean isAction() {
    return false;
  }
  
static public class Action extends LanguageAction {
  // Production: sig("Action",[arg("java.util.List\<org.rascalmpl.ast.Statement\>","statements")])

  
     private final java.util.List<org.rascalmpl.ast.Statement> statements;
  

  
public Action(INode node , java.util.List<org.rascalmpl.ast.Statement> statements) {
  super(node);
  
    this.statements = statements;
  
}


  @Override
  public boolean isAction() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLanguageActionAction(this);
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


  public boolean isWhen() {
    return false;
  }
  
static public class When extends LanguageAction {
  // Production: sig("When",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions")])

  
     private final java.util.List<org.rascalmpl.ast.Expression> conditions;
  

  
public When(INode node , java.util.List<org.rascalmpl.ast.Expression> conditions) {
  super(node);
  
    this.conditions = conditions;
  
}


  @Override
  public boolean isWhen() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLanguageActionWhen(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
        return this.conditions;
     }
     
     @Override
     public boolean hasConditions() {
        return true;
     }
  	
}


  public boolean isReplace() {
    return false;
  }
  
static public class Replace extends LanguageAction {
  // Production: sig("Replace",[arg("org.rascalmpl.ast.Expression","expression")])

  
     private final org.rascalmpl.ast.Expression expression;
  

  
public Replace(INode node , org.rascalmpl.ast.Expression expression) {
  super(node);
  
    this.expression = expression;
  
}


  @Override
  public boolean isReplace() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLanguageActionReplace(this);
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


  public boolean isReplaceWhen() {
    return false;
  }
  
static public class ReplaceWhen extends LanguageAction {
  // Production: sig("ReplaceWhen",[arg("org.rascalmpl.ast.Expression","expression"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions")])

  
     private final org.rascalmpl.ast.Expression expression;
  
     private final java.util.List<org.rascalmpl.ast.Expression> conditions;
  

  
public ReplaceWhen(INode node , org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Expression> conditions) {
  super(node);
  
    this.expression = expression;
  
    this.conditions = conditions;
  
}


  @Override
  public boolean isReplaceWhen() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLanguageActionReplaceWhen(this);
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
     public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
        return this.conditions;
     }
     
     @Override
     public boolean hasConditions() {
        return true;
     }
  	
}



}
