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

public abstract class StringTemplate extends AbstractAST {
  public StringTemplate(IConstructor node) {
    super();
  }

  
  public boolean hasConditions() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
    throw new UnsupportedOperationException();
  }
  public boolean hasGenerators() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getGenerators() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPostStats() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getPostStats() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPostStatsElse() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getPostStatsElse() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPostStatsThen() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getPostStatsThen() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPreStats() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getPreStats() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPreStatsElse() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getPreStatsElse() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPreStatsThen() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getPreStatsThen() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCondition() {
    return false;
  }

  public org.rascalmpl.ast.Expression getCondition() {
    throw new UnsupportedOperationException();
  }
  public boolean hasBody() {
    return false;
  }

  public org.rascalmpl.ast.StringMiddle getBody() {
    throw new UnsupportedOperationException();
  }
  public boolean hasElseString() {
    return false;
  }

  public org.rascalmpl.ast.StringMiddle getElseString() {
    throw new UnsupportedOperationException();
  }
  public boolean hasThenString() {
    return false;
  }

  public org.rascalmpl.ast.StringMiddle getThenString() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends StringTemplate {
    private final java.util.List<org.rascalmpl.ast.StringTemplate> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.StringTemplate> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.StringTemplate> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitStringTemplateAmbiguity(this);
    }
  }

  

  
  public boolean isDoWhile() {
    return false;
  }

  static public class DoWhile extends StringTemplate {
    // Production: sig("DoWhile",[arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("org.rascalmpl.ast.StringMiddle","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats"),arg("org.rascalmpl.ast.Expression","condition")])
  
    
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final org.rascalmpl.ast.StringMiddle body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
    private final org.rascalmpl.ast.Expression condition;
  
    public DoWhile(IConstructor node , java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats,  org.rascalmpl.ast.Expression condition) {
      super(node);
      
      this.preStats = preStats;
      this.body = body;
      this.postStats = postStats;
      this.condition = condition;
    }
  
    @Override
    public boolean isDoWhile() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringTemplateDoWhile(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPreStats() {
      return this.preStats;
    }
  
    @Override
    public boolean hasPreStats() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.StringMiddle getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPostStats() {
      return this.postStats;
    }
  
    @Override
    public boolean hasPostStats() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getCondition() {
      return this.condition;
    }
  
    @Override
    public boolean hasCondition() {
      return true;
    }	
  }
  public boolean isFor() {
    return false;
  }

  static public class For extends StringTemplate {
    // Production: sig("For",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("org.rascalmpl.ast.StringMiddle","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats")])
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final org.rascalmpl.ast.StringMiddle body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
  
    public For(IConstructor node , java.util.List<org.rascalmpl.ast.Expression> generators,  java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
      super(node);
      
      this.generators = generators;
      this.preStats = preStats;
      this.body = body;
      this.postStats = postStats;
    }
  
    @Override
    public boolean isFor() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringTemplateFor(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getGenerators() {
      return this.generators;
    }
  
    @Override
    public boolean hasGenerators() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPreStats() {
      return this.preStats;
    }
  
    @Override
    public boolean hasPreStats() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.StringMiddle getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPostStats() {
      return this.postStats;
    }
  
    @Override
    public boolean hasPostStats() {
      return true;
    }	
  }
  public boolean isIfThen() {
    return false;
  }

  static public class IfThen extends StringTemplate {
    // Production: sig("IfThen",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("org.rascalmpl.ast.StringMiddle","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats")])
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final org.rascalmpl.ast.StringMiddle body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
  
    public IfThen(IConstructor node , java.util.List<org.rascalmpl.ast.Expression> conditions,  java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
      super(node);
      
      this.conditions = conditions;
      this.preStats = preStats;
      this.body = body;
      this.postStats = postStats;
    }
  
    @Override
    public boolean isIfThen() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringTemplateIfThen(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
      return this.conditions;
    }
  
    @Override
    public boolean hasConditions() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPreStats() {
      return this.preStats;
    }
  
    @Override
    public boolean hasPreStats() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.StringMiddle getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPostStats() {
      return this.postStats;
    }
  
    @Override
    public boolean hasPostStats() {
      return true;
    }	
  }
  public boolean isIfThenElse() {
    return false;
  }

  static public class IfThenElse extends StringTemplate {
    // Production: sig("IfThenElse",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStatsThen"),arg("org.rascalmpl.ast.StringMiddle","thenString"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStatsThen"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStatsElse"),arg("org.rascalmpl.ast.StringMiddle","elseString"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStatsElse")])
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final java.util.List<org.rascalmpl.ast.Statement> preStatsThen;
    private final org.rascalmpl.ast.StringMiddle thenString;
    private final java.util.List<org.rascalmpl.ast.Statement> postStatsThen;
    private final java.util.List<org.rascalmpl.ast.Statement> preStatsElse;
    private final org.rascalmpl.ast.StringMiddle elseString;
    private final java.util.List<org.rascalmpl.ast.Statement> postStatsElse;
  
    public IfThenElse(IConstructor node , java.util.List<org.rascalmpl.ast.Expression> conditions,  java.util.List<org.rascalmpl.ast.Statement> preStatsThen,  org.rascalmpl.ast.StringMiddle thenString,  java.util.List<org.rascalmpl.ast.Statement> postStatsThen,  java.util.List<org.rascalmpl.ast.Statement> preStatsElse,  org.rascalmpl.ast.StringMiddle elseString,  java.util.List<org.rascalmpl.ast.Statement> postStatsElse) {
      super(node);
      
      this.conditions = conditions;
      this.preStatsThen = preStatsThen;
      this.thenString = thenString;
      this.postStatsThen = postStatsThen;
      this.preStatsElse = preStatsElse;
      this.elseString = elseString;
      this.postStatsElse = postStatsElse;
    }
  
    @Override
    public boolean isIfThenElse() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringTemplateIfThenElse(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
      return this.conditions;
    }
  
    @Override
    public boolean hasConditions() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPreStatsThen() {
      return this.preStatsThen;
    }
  
    @Override
    public boolean hasPreStatsThen() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.StringMiddle getThenString() {
      return this.thenString;
    }
  
    @Override
    public boolean hasThenString() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPostStatsThen() {
      return this.postStatsThen;
    }
  
    @Override
    public boolean hasPostStatsThen() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPreStatsElse() {
      return this.preStatsElse;
    }
  
    @Override
    public boolean hasPreStatsElse() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.StringMiddle getElseString() {
      return this.elseString;
    }
  
    @Override
    public boolean hasElseString() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPostStatsElse() {
      return this.postStatsElse;
    }
  
    @Override
    public boolean hasPostStatsElse() {
      return true;
    }	
  }
  public boolean isWhile() {
    return false;
  }

  static public class While extends StringTemplate {
    // Production: sig("While",[arg("org.rascalmpl.ast.Expression","condition"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("org.rascalmpl.ast.StringMiddle","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats")])
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final org.rascalmpl.ast.StringMiddle body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
  
    public While(IConstructor node , org.rascalmpl.ast.Expression condition,  java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
      super(node);
      
      this.condition = condition;
      this.preStats = preStats;
      this.body = body;
      this.postStats = postStats;
    }
  
    @Override
    public boolean isWhile() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringTemplateWhile(this);
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
    public java.util.List<org.rascalmpl.ast.Statement> getPreStats() {
      return this.preStats;
    }
  
    @Override
    public boolean hasPreStats() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.StringMiddle getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getPostStats() {
      return this.postStats;
    }
  
    @Override
    public boolean hasPostStats() {
      return true;
    }	
  }
}