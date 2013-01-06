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

public abstract class Kind extends AbstractAST {
  public Kind(IConstructor node) {
    super();
  }

  

  static public class Ambiguity extends Kind {
    private final java.util.List<org.rascalmpl.ast.Kind> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Kind> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Kind> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitKindAmbiguity(this);
    }
  }

  

  
  public boolean isAlias() {
    return false;
  }

  static public class Alias extends Kind {
    // Production: sig("Alias",[])
  
    
  
    public Alias(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAlias() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindAlias(this);
    }
  
    	
  }
  public boolean isAll() {
    return false;
  }

  static public class All extends Kind {
    // Production: sig("All",[])
  
    
  
    public All(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAll() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindAll(this);
    }
  
    	
  }
  public boolean isAnno() {
    return false;
  }

  static public class Anno extends Kind {
    // Production: sig("Anno",[])
  
    
  
    public Anno(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAnno() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindAnno(this);
    }
  
    	
  }
  public boolean isData() {
    return false;
  }

  static public class Data extends Kind {
    // Production: sig("Data",[])
  
    
  
    public Data(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isData() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindData(this);
    }
  
    	
  }
  public boolean isFunction() {
    return false;
  }

  static public class Function extends Kind {
    // Production: sig("Function",[])
  
    
  
    public Function(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isFunction() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindFunction(this);
    }
  
    	
  }
  public boolean isModule() {
    return false;
  }

  static public class Module extends Kind {
    // Production: sig("Module",[])
  
    
  
    public Module(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isModule() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindModule(this);
    }
  
    	
  }
  public boolean isTag() {
    return false;
  }

  static public class Tag extends Kind {
    // Production: sig("Tag",[])
  
    
  
    public Tag(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isTag() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindTag(this);
    }
  
    	
  }
  public boolean isVariable() {
    return false;
  }

  static public class Variable extends Kind {
    // Production: sig("Variable",[])
  
    
  
    public Variable(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isVariable() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindVariable(this);
    }
  
    	
  }
  public boolean isView() {
    return false;
  }

  static public class View extends Kind {
    // Production: sig("View",[])
  
    
  
    public View(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isView() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindView(this);
    }
  
    	
  }
}