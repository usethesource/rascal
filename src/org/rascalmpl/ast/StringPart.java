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


import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;

public abstract class StringPart extends AbstractAST {
  public StringPart(ISourceLocation src, IConstructor node) {
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
  public boolean hasBody() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.StringPart> getBody() {
    throw new UnsupportedOperationException();
  }
  public boolean hasElseBody() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.StringPart> getElseBody() {
    throw new UnsupportedOperationException();
  }
  public boolean hasArg() {
    return false;
  }

  public org.rascalmpl.ast.Expression getArg() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCondition() {
    return false;
  }

  public org.rascalmpl.ast.Expression getCondition() {
    throw new UnsupportedOperationException();
  }
  public boolean hasIndent() {
    return false;
  }

  public org.rascalmpl.ast.Indentation getIndent() {
    throw new UnsupportedOperationException();
  }
  public boolean hasMargin() {
    return false;
  }

  public org.rascalmpl.ast.Indentation getMargin() {
    throw new UnsupportedOperationException();
  }
  public boolean hasKeywordArguments() {
    return false;
  }

  public org.rascalmpl.ast.KeywordArguments_Expression getKeywordArguments() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCharacters() {
    return false;
  }

  public org.rascalmpl.ast.StringCharacters getCharacters() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isCharacters() {
    return false;
  }

  static public class Characters extends StringPart {
    // Production: sig("Characters",[arg("org.rascalmpl.ast.StringCharacters","characters")],breakable=false)
  
    
    private final org.rascalmpl.ast.StringCharacters characters;
  
    public Characters(ISourceLocation src, IConstructor node , org.rascalmpl.ast.StringCharacters characters) {
      super(src, node);
      
      this.characters = characters;
    }
  
    @Override
    public boolean isCharacters() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringPartCharacters(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = characters.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        characters.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Characters)) {
        return false;
      }        
      Characters tmp = (Characters) o;
      return true && tmp.characters.equals(this.characters) ; 
    }
   
    @Override
    public int hashCode() {
      return 103 + 619 * characters.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.StringCharacters getCharacters() {
      return this.characters;
    }
  
    @Override
    public boolean hasCharacters() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(characters));
    }
            
  }
  public boolean isDoWhile() {
    return false;
  }

  static public class DoWhile extends StringPart {
    // Production: sig("DoWhile",[arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("java.util.List\<org.rascalmpl.ast.StringPart\>","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats"),arg("org.rascalmpl.ast.Expression","condition")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final java.util.List<org.rascalmpl.ast.StringPart> body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
    private final org.rascalmpl.ast.Expression condition;
  
    public DoWhile(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Statement> preStats,  java.util.List<org.rascalmpl.ast.StringPart> body,  java.util.List<org.rascalmpl.ast.Statement> postStats,  org.rascalmpl.ast.Expression condition) {
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
      return visitor.visitStringPartDoWhile(this);
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
      for (AbstractAST $elem : body) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
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
      return 421 + 541 * preStats.hashCode() + 509 * body.hashCode() + 941 * postStats.hashCode() + 653 * condition.hashCode() ; 
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
    public java.util.List<org.rascalmpl.ast.StringPart> getBody() {
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

  static public class For extends StringPart {
    // Production: sig("For",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("java.util.List\<org.rascalmpl.ast.StringPart\>","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final java.util.List<org.rascalmpl.ast.StringPart> body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
  
    public For(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Expression> generators,  java.util.List<org.rascalmpl.ast.Statement> preStats,  java.util.List<org.rascalmpl.ast.StringPart> body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
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
      return visitor.visitStringPartFor(this);
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
      for (AbstractAST $elem : body) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
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
      return 13 + 787 * generators.hashCode() + 37 * preStats.hashCode() + 131 * body.hashCode() + 331 * postStats.hashCode() ; 
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
    public java.util.List<org.rascalmpl.ast.StringPart> getBody() {
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
  public boolean isHole() {
    return false;
  }

  static public class Hole extends StringPart {
    // Production: sig("Hole",[arg("org.rascalmpl.ast.Expression","arg"),arg("org.rascalmpl.ast.KeywordArguments_Expression","keywordArguments")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression arg;
    private final org.rascalmpl.ast.KeywordArguments_Expression keywordArguments;
  
    public Hole(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression arg,  org.rascalmpl.ast.KeywordArguments_Expression keywordArguments) {
      super(src, node);
      
      this.arg = arg;
      this.keywordArguments = keywordArguments;
    }
  
    @Override
    public boolean isHole() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringPartHole(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = arg.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        arg.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = keywordArguments.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        keywordArguments.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Hole)) {
        return false;
      }        
      Hole tmp = (Hole) o;
      return true && tmp.arg.equals(this.arg) && tmp.keywordArguments.equals(this.keywordArguments) ; 
    }
   
    @Override
    public int hashCode() {
      return 521 + 839 * arg.hashCode() + 191 * keywordArguments.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getArg() {
      return this.arg;
    }
  
    @Override
    public boolean hasArg() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.KeywordArguments_Expression getKeywordArguments() {
      return this.keywordArguments;
    }
  
    @Override
    public boolean hasKeywordArguments() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(arg), clone(keywordArguments));
    }
            
  }
  public boolean isIfThen() {
    return false;
  }

  static public class IfThen extends StringPart {
    // Production: sig("IfThen",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("java.util.List\<org.rascalmpl.ast.StringPart\>","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final java.util.List<org.rascalmpl.ast.StringPart> body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
  
    public IfThen(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Expression> conditions,  java.util.List<org.rascalmpl.ast.Statement> preStats,  java.util.List<org.rascalmpl.ast.StringPart> body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
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
      return visitor.visitStringPartIfThen(this);
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
      for (AbstractAST $elem : body) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
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
      return 599 + 109 * conditions.hashCode() + 977 * preStats.hashCode() + 2 * body.hashCode() + 947 * postStats.hashCode() ; 
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
    public java.util.List<org.rascalmpl.ast.StringPart> getBody() {
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

  static public class IfThenElse extends StringPart {
    // Production: sig("IfThenElse",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStatsThen"),arg("java.util.List\<org.rascalmpl.ast.StringPart\>","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStatsThen"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStatsElse"),arg("java.util.List\<org.rascalmpl.ast.StringPart\>","elseBody"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStatsElse")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final java.util.List<org.rascalmpl.ast.Statement> preStatsThen;
    private final java.util.List<org.rascalmpl.ast.StringPart> body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStatsThen;
    private final java.util.List<org.rascalmpl.ast.Statement> preStatsElse;
    private final java.util.List<org.rascalmpl.ast.StringPart> elseBody;
    private final java.util.List<org.rascalmpl.ast.Statement> postStatsElse;
  
    public IfThenElse(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Expression> conditions,  java.util.List<org.rascalmpl.ast.Statement> preStatsThen,  java.util.List<org.rascalmpl.ast.StringPart> body,  java.util.List<org.rascalmpl.ast.Statement> postStatsThen,  java.util.List<org.rascalmpl.ast.Statement> preStatsElse,  java.util.List<org.rascalmpl.ast.StringPart> elseBody,  java.util.List<org.rascalmpl.ast.Statement> postStatsElse) {
      super(src, node);
      
      this.conditions = conditions;
      this.preStatsThen = preStatsThen;
      this.body = body;
      this.postStatsThen = postStatsThen;
      this.preStatsElse = preStatsElse;
      this.elseBody = elseBody;
      this.postStatsElse = postStatsElse;
    }
  
    @Override
    public boolean isIfThenElse() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringPartIfThenElse(this);
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
      for (AbstractAST $elem : body) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
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
      for (AbstractAST $elem : elseBody) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
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
      return true && tmp.conditions.equals(this.conditions) && tmp.preStatsThen.equals(this.preStatsThen) && tmp.body.equals(this.body) && tmp.postStatsThen.equals(this.postStatsThen) && tmp.preStatsElse.equals(this.preStatsElse) && tmp.elseBody.equals(this.elseBody) && tmp.postStatsElse.equals(this.postStatsElse) ; 
    }
   
    @Override
    public int hashCode() {
      return 281 + 409 * conditions.hashCode() + 97 * preStatsThen.hashCode() + 607 * body.hashCode() + 547 * postStatsThen.hashCode() + 283 * preStatsElse.hashCode() + 757 * elseBody.hashCode() + 347 * postStatsElse.hashCode() ; 
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
    public java.util.List<org.rascalmpl.ast.StringPart> getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
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
    public java.util.List<org.rascalmpl.ast.StringPart> getElseBody() {
      return this.elseBody;
    }
  
    @Override
    public boolean hasElseBody() {
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
      return newInstance(getClass(), src, (IConstructor) null , clone(conditions), clone(preStatsThen), clone(body), clone(postStatsThen), clone(preStatsElse), clone(elseBody), clone(postStatsElse));
    }
            
  }
  public boolean isMargin() {
    return false;
  }

  static public class Margin extends StringPart {
    // Production: sig("Margin",[arg("org.rascalmpl.ast.Indentation","margin"),arg("org.rascalmpl.ast.Indentation","indent")],breakable=false)
  
    
    private final org.rascalmpl.ast.Indentation margin;
    private final org.rascalmpl.ast.Indentation indent;
  
    public Margin(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Indentation margin,  org.rascalmpl.ast.Indentation indent) {
      super(src, node);
      
      this.margin = margin;
      this.indent = indent;
    }
  
    @Override
    public boolean isMargin() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringPartMargin(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = margin.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        margin.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = indent.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        indent.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Margin)) {
        return false;
      }        
      Margin tmp = (Margin) o;
      return true && tmp.margin.equals(this.margin) && tmp.indent.equals(this.indent) ; 
    }
   
    @Override
    public int hashCode() {
      return 73 + 181 * margin.hashCode() + 499 * indent.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Indentation getMargin() {
      return this.margin;
    }
  
    @Override
    public boolean hasMargin() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Indentation getIndent() {
      return this.indent;
    }
  
    @Override
    public boolean hasIndent() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(margin), clone(indent));
    }
            
  }
  public boolean isWhile() {
    return false;
  }

  static public class While extends StringPart {
    // Production: sig("While",[arg("org.rascalmpl.ast.Expression","condition"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","preStats"),arg("java.util.List\<org.rascalmpl.ast.StringPart\>","body"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","postStats")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final java.util.List<org.rascalmpl.ast.Statement> preStats;
    private final java.util.List<org.rascalmpl.ast.StringPart> body;
    private final java.util.List<org.rascalmpl.ast.Statement> postStats;
  
    public While(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression condition,  java.util.List<org.rascalmpl.ast.Statement> preStats,  java.util.List<org.rascalmpl.ast.StringPart> body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
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
      return visitor.visitStringPartWhile(this);
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
      for (AbstractAST $elem : body) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
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
      return 227 + 389 * condition.hashCode() + 479 * preStats.hashCode() + 103 * body.hashCode() + 859 * postStats.hashCode() ; 
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
    public java.util.List<org.rascalmpl.ast.StringPart> getBody() {
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