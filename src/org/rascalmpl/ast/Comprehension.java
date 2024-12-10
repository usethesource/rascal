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
public abstract class Comprehension extends AbstractAST {
  public Comprehension(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasGenerators() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getGenerators() {
    throw new UnsupportedOperationException();
  }
  public boolean hasResults() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getResults() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFrom() {
    return false;
  }

  public org.rascalmpl.ast.Expression getFrom() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTo() {
    return false;
  }

  public org.rascalmpl.ast.Expression getTo() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isList() {
    return false;
  }

  static public class List extends Comprehension {
    // Production: sig("List",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","results"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> results;
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
  
    public List(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Expression> results,  java.util.List<org.rascalmpl.ast.Expression> generators) {
      super(src, node);
      
      this.results = results;
      this.generators = generators;
    }
  
    @Override
    public boolean isList() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitComprehensionList(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : results) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      for (AbstractAST $elem : generators) {
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
      if (!(o instanceof List)) {
        return false;
      }        
      List tmp = (List) o;
      return true && tmp.results.equals(this.results) && tmp.generators.equals(this.generators) ; 
    }
   
    @Override
    public int hashCode() {
      return 881 + 349 * results.hashCode() + 449 * generators.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getResults() {
      return this.results;
    }
  
    @Override
    public boolean hasResults() {
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(results), clone(generators));
    }
            
  }
  public boolean isMap() {
    return false;
  }

  static public class Map extends Comprehension {
    // Production: sig("Map",[arg("org.rascalmpl.ast.Expression","from"),arg("org.rascalmpl.ast.Expression","to"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression from;
    private final org.rascalmpl.ast.Expression to;
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
  
    public Map(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression from,  org.rascalmpl.ast.Expression to,  java.util.List<org.rascalmpl.ast.Expression> generators) {
      super(src, node);
      
      this.from = from;
      this.to = to;
      this.generators = generators;
    }
  
    @Override
    public boolean isMap() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitComprehensionMap(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = from.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        from.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = to.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        to.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : generators) {
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
      if (!(o instanceof Map)) {
        return false;
      }        
      Map tmp = (Map) o;
      return true && tmp.from.equals(this.from) && tmp.to.equals(this.to) && tmp.generators.equals(this.generators) ; 
    }
   
    @Override
    public int hashCode() {
      return 71 + 523 * from.hashCode() + 937 * to.hashCode() + 617 * generators.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getFrom() {
      return this.from;
    }
  
    @Override
    public boolean hasFrom() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getTo() {
      return this.to;
    }
  
    @Override
    public boolean hasTo() {
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(from), clone(to), clone(generators));
    }
            
  }
  public boolean isSet() {
    return false;
  }

  static public class Set extends Comprehension {
    // Production: sig("Set",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","results"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> results;
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
  
    public Set(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Expression> results,  java.util.List<org.rascalmpl.ast.Expression> generators) {
      super(src, node);
      
      this.results = results;
      this.generators = generators;
    }
  
    @Override
    public boolean isSet() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitComprehensionSet(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : results) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      for (AbstractAST $elem : generators) {
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
      if (!(o instanceof Set)) {
        return false;
      }        
      Set tmp = (Set) o;
      return true && tmp.results.equals(this.results) && tmp.generators.equals(this.generators) ; 
    }
   
    @Override
    public int hashCode() {
      return 821 + 971 * results.hashCode() + 173 * generators.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getResults() {
      return this.results;
    }
  
    @Override
    public boolean hasResults() {
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(results), clone(generators));
    }
            
  }
}