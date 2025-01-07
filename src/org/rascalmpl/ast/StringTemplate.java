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
public abstract class StringTemplate extends AbstractAST {
  public StringTemplate(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
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

  

  
  public boolean isDoWhile() {
    return false;
  }

  static public class DoWhile extends StringTemplate {
    // Production: sig("DoWhile",[arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("org.rascalmpl.ast.StringMiddle","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats"),arg("org.rascalmpl.ast.Expression","condition")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final org.rascalmpl.ast.StringMiddle body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
    private final org.rascalmpl.ast.Expression condition;
  
    public DoWhile(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats,  org.rascalmpl.ast.Expression condition) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : preStats) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : postStats) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = condition.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        condition.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DoWhile)) {
        return false;
      }        
      DoWhile tmp = (DoWhile) o;
      return true && tmp.preStats.equals(this.preStats) && tmp.body.equals(this.body) && tmp.postStats.equals(this.postStats) && tmp.condition.equals(this.condition) ; 
    }
   
    @Override
    public int hashCode() {
      return 757 + 347 * preStats.hashCode() + 73 * body.hashCode() + 181 * postStats.hashCode() + 499 * condition.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(preStats), clone(body), clone(postStats), clone(condition));
    }
            
  }
  public boolean isFor() {
    return false;
  }

  static public class For extends StringTemplate {
    // Production: sig("For",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("org.rascalmpl.ast.StringMiddle","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final org.rascalmpl.ast.StringMiddle body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
  
    public For(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Expression> generators,  java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : generators) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      for (AbstractAST $elem : preStats) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : postStats) {
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
      if (!(o instanceof For)) {
        return false;
      }        
      For tmp = (For) o;
      return true && tmp.generators.equals(this.generators) && tmp.preStats.equals(this.preStats) && tmp.body.equals(this.body) && tmp.postStats.equals(this.postStats) ; 
    }
   
    @Override
    public int hashCode() {
      return 227 + 389 * generators.hashCode() + 479 * preStats.hashCode() + 103 * body.hashCode() + 859 * postStats.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(generators), clone(preStats), clone(body), clone(postStats));
    }
            
  }
  public boolean isIfThen() {
    return false;
  }

  static public class IfThen extends StringTemplate {
    // Production: sig("IfThen",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("org.rascalmpl.ast.StringMiddle","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final org.rascalmpl.ast.StringMiddle body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
  
    public IfThen(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Expression> conditions,  java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : conditions) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      for (AbstractAST $elem : preStats) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : postStats) {
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
      if (!(o instanceof IfThen)) {
        return false;
      }        
      IfThen tmp = (IfThen) o;
      return true && tmp.conditions.equals(this.conditions) && tmp.preStats.equals(this.preStats) && tmp.body.equals(this.body) && tmp.postStats.equals(this.postStats) ; 
    }
   
    @Override
    public int hashCode() {
      return 163 + 739 * conditions.hashCode() + 821 * preStats.hashCode() + 977 * body.hashCode() + 677 * postStats.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(conditions), clone(preStats), clone(body), clone(postStats));
    }
            
  }
  public boolean isIfThenElse() {
    return false;
  }

  static public class IfThenElse extends StringTemplate {
    // Production: sig("IfThenElse",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStatsThen"),arg("org.rascalmpl.ast.StringMiddle","thenString"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStatsThen"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStatsElse"),arg("org.rascalmpl.ast.StringMiddle","elseString"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStatsElse")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final java.util.List<org.rascalmpl.ast.Statement> preStatsThen;
    private final org.rascalmpl.ast.StringMiddle thenString;
    private final java.util.List<org.rascalmpl.ast.Statement> postStatsThen;
    private final java.util.List<org.rascalmpl.ast.Statement> preStatsElse;
    private final org.rascalmpl.ast.StringMiddle elseString;
    private final java.util.List<org.rascalmpl.ast.Statement> postStatsElse;
  
    public IfThenElse(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Expression> conditions,  java.util.List<org.rascalmpl.ast.Statement> preStatsThen,  org.rascalmpl.ast.StringMiddle thenString,  java.util.List<org.rascalmpl.ast.Statement> postStatsThen,  java.util.List<org.rascalmpl.ast.Statement> preStatsElse,  org.rascalmpl.ast.StringMiddle elseString,  java.util.List<org.rascalmpl.ast.Statement> postStatsElse) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : conditions) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      for (AbstractAST $elem : preStatsThen) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = thenString.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        thenString.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : postStatsThen) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      for (AbstractAST $elem : preStatsElse) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = elseString.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        elseString.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : postStatsElse) {
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
      if (!(o instanceof IfThenElse)) {
        return false;
      }        
      IfThenElse tmp = (IfThenElse) o;
      return true && tmp.conditions.equals(this.conditions) && tmp.preStatsThen.equals(this.preStatsThen) && tmp.thenString.equals(this.thenString) && tmp.postStatsThen.equals(this.postStatsThen) && tmp.preStatsElse.equals(this.preStatsElse) && tmp.elseString.equals(this.elseString) && tmp.postStatsElse.equals(this.postStatsElse) ; 
    }
   
    @Override
    public int hashCode() {
      return 11 + 691 * conditions.hashCode() + 71 * preStatsThen.hashCode() + 181 * thenString.hashCode() + 199 * postStatsThen.hashCode() + 787 * preStatsElse.hashCode() + 193 * elseString.hashCode() + 199 * postStatsElse.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(conditions), clone(preStatsThen), clone(thenString), clone(postStatsThen), clone(preStatsElse), clone(elseString), clone(postStatsElse));
    }
            
  }
  public boolean isWhile() {
    return false;
  }

  static public class While extends StringTemplate {
    // Production: sig("While",[arg("org.rascalmpl.ast.Expression","condition"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("org.rascalmpl.ast.StringMiddle","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final org.rascalmpl.ast.StringMiddle body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
  
    public While(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression condition,  java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = condition.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        condition.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : preStats) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : postStats) {
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
      if (!(o instanceof While)) {
        return false;
      }        
      While tmp = (While) o;
      return true && tmp.condition.equals(this.condition) && tmp.preStats.equals(this.preStats) && tmp.body.equals(this.body) && tmp.postStats.equals(this.postStats) ; 
    }
   
    @Override
    public int hashCode() {
      return 389 + 281 * condition.hashCode() + 389 * preStats.hashCode() + 683 * body.hashCode() + 577 * postStats.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(condition), clone(preStats), clone(body), clone(postStats));
    }
            
  }
}