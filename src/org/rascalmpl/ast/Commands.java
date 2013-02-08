/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

public abstract class Commands extends AbstractAST {
  public Commands(IConstructor node) {
    super();
  }

  
  public boolean hasCommands() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.EvalCommand> getCommands() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isList() {
    return false;
  }

  static public class List extends Commands {
    // Production: sig("List",[arg("java.util.List\<org.rascalmpl.ast.EvalCommand\>","commands")])
  
    
    private final java.util.List<org.rascalmpl.ast.EvalCommand> commands;
  
    public List(IConstructor node , java.util.List<org.rascalmpl.ast.EvalCommand> commands) {
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
    public java.util.List<org.rascalmpl.ast.EvalCommand> getCommands() {
      return this.commands;
    }
  
    @Override
    public boolean hasCommands() {
      return true;
    }	
  }
}