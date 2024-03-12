/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
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


import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;

@SuppressWarnings(value = {"unused"})
public abstract class Commands extends AbstractAST {
  public Commands(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasCommands() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.EvalCommand> getCommands() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isCommandlist() {
    return false;
  }

  static public class Commandlist extends Commands {
    // Production: sig("Commandlist",[arg("java.util.List\<org.rascalmpl.ast.EvalCommand\>","commands")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.EvalCommand> commands;
  
    public Commandlist(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.EvalCommand> commands) {
      super(src, node);
      
      this.commands = commands;
    }
  
    @Override
    public boolean isCommandlist() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCommandsCommandlist(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : commands) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Commandlist)) {
        return false;
      }        
      Commandlist tmp = (Commandlist) o;
      return true && tmp.commands.equals(this.commands) ; 
    }
   
    @Override
    public int hashCode() {
      return 127 + 389 * commands.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.EvalCommand> getCommands() {
      return this.commands;
    }
  
    @Override
    public boolean hasCommands() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(commands));
    }
            
  }
}