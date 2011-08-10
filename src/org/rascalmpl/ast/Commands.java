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
import org.rascalmpl.interpreter.result.Result;

public abstract class Commands extends AbstractAST {
  public Commands(IConstructor node) {
    super(node);
  }

  
  public boolean hasCommands() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Command> getCommands() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Commands {
    private final java.util.List<org.rascalmpl.ast.Commands> alternatives;
  
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Commands> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Commands> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitCommandsAmbiguity(this);
    }
  }

  

  
  public boolean isList() {
    return false;
  }

  static public class List extends Commands {
    // Production: sig("List",[arg("java.util.List\<org.rascalmpl.ast.Command\>","commands")])
  
    
    private final java.util.List<org.rascalmpl.ast.Command> commands;
  
    public List(IConstructor node , java.util.List<org.rascalmpl.ast.Command> commands) {
      super(node);
      
      this.commands = commands;
    }
  
    @Override
    public boolean isList() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCommandsList(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Command> getCommands() {
      return this.commands;
    }
  
    @Override
    public boolean hasCommands() {
      return true;
    }	
  }
}