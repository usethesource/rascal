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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;

public abstract class Test extends AbstractAST {
  public Test(IConstructor node) {
    super(node);
  }

  
  public boolean hasTags() {
    return false;
  }

  public org.rascalmpl.ast.Tags getTags() {
    throw new UnsupportedOperationException();
  }
  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLabel() {
    return false;
  }

  public org.rascalmpl.ast.Name getLabel() {
    throw new UnsupportedOperationException();
  }
  public boolean hasParameters() {
    return false;
  }

  public org.rascalmpl.ast.Parameters getParameters() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Test {
    private final java.util.List<org.rascalmpl.ast.Test> alternatives;
  
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Test> alternatives) {
      super(node);
      this.alternatives = java.util.Collections.unmodifiableList(alternatives);
    }
    
    @Override
    public Result<IValue> interpret(Evaluator __eval) {
      throw new Ambiguous(this.getTree());
    }
      
    @Override
    public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
      throw new Ambiguous(this.getTree());
    }
    
    public java.util.List<org.rascalmpl.ast.Test> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitTestAmbiguity(this);
    }
  }

  

  
  public boolean isLabeled() {
    return false;
  }

  static public class Labeled extends Test {
    // Production: sig("Labeled",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Name","label"),arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Name label;
    private final org.rascalmpl.ast.Expression expression;
  
    public Labeled(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Name label,  org.rascalmpl.ast.Expression expression) {
      super(node);
      
      this.tags = tags;
      this.label = label;
      this.expression = expression;
    }
  
    @Override
    public boolean isLabeled() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTestLabeled(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Tags getTags() {
      return this.tags;
    }
  
    @Override
    public boolean hasTags() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
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
  public boolean isLabeledParameterized() {
    return false;
  }

  static public class LabeledParameterized extends Test {
    // Production: sig("LabeledParameterized",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Name","label"),arg("org.rascalmpl.ast.Parameters","parameters"),arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Name label;
    private final org.rascalmpl.ast.Parameters parameters;
    private final org.rascalmpl.ast.Expression expression;
  
    public LabeledParameterized(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Name label,  org.rascalmpl.ast.Parameters parameters,  org.rascalmpl.ast.Expression expression) {
      super(node);
      
      this.tags = tags;
      this.label = label;
      this.parameters = parameters;
      this.expression = expression;
    }
  
    @Override
    public boolean isLabeledParameterized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTestLabeledParameterized(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Tags getTags() {
      return this.tags;
    }
  
    @Override
    public boolean hasTags() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
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
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }	
  }
  public boolean isUnlabeled() {
    return false;
  }

  static public class Unlabeled extends Test {
    // Production: sig("Unlabeled",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Expression expression;
  
    public Unlabeled(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Expression expression) {
      super(node);
      
      this.tags = tags;
      this.expression = expression;
    }
  
    @Override
    public boolean isUnlabeled() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTestUnlabeled(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Tags getTags() {
      return this.tags;
    }
  
    @Override
    public boolean hasTags() {
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
  public boolean isParameterized() {
    return false;
  }

  static public class Parameterized extends Test {
    // Production: sig("Parameterized",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Parameters","parameters"),arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Parameters parameters;
    private final org.rascalmpl.ast.Expression expression;
  
    public Parameterized(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Parameters parameters,  org.rascalmpl.ast.Expression expression) {
      super(node);
      
      this.tags = tags;
      this.parameters = parameters;
      this.expression = expression;
    }
  
    @Override
    public boolean isParameterized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTestParameterized(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Tags getTags() {
      return this.tags;
    }
  
    @Override
    public boolean hasTags() {
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
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }	
  }
}