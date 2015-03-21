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


import org.eclipse.imp.pdb.facts.IConstructor;

public abstract class Declaration extends AbstractAST {
  public Declaration(IConstructor node) {
    super();
  }

  
  public boolean hasTypes() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Type> getTypes() {
    throw new UnsupportedOperationException();
  }
  public boolean hasVariables() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Variable> getVariables() {
    throw new UnsupportedOperationException();
  }
  public boolean hasVariants() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Variant> getVariants() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCommonKeywordParameters() {
    return false;
  }

  public org.rascalmpl.ast.CommonKeywordParameters getCommonKeywordParameters() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFunctionDeclaration() {
    return false;
  }

  public org.rascalmpl.ast.FunctionDeclaration getFunctionDeclaration() {
    throw new UnsupportedOperationException();
  }
  public boolean hasKind() {
    return false;
  }

  public org.rascalmpl.ast.Kind getKind() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTags() {
    return false;
  }

  public org.rascalmpl.ast.Tags getTags() {
    throw new UnsupportedOperationException();
  }
  public boolean hasAnnoType() {
    return false;
  }

  public org.rascalmpl.ast.Type getAnnoType() {
    throw new UnsupportedOperationException();
  }
  public boolean hasBase() {
    return false;
  }

  public org.rascalmpl.ast.Type getBase() {
    throw new UnsupportedOperationException();
  }
  public boolean hasOnType() {
    return false;
  }

  public org.rascalmpl.ast.Type getOnType() {
    throw new UnsupportedOperationException();
  }
  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }
  public boolean hasUser() {
    return false;
  }

  public org.rascalmpl.ast.UserType getUser() {
    throw new UnsupportedOperationException();
  }
  public boolean hasVisibility() {
    return false;
  }

  public org.rascalmpl.ast.Visibility getVisibility() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isAlias() {
    return false;
  }

  static public class Alias extends Declaration {
    // Production: sig("Alias",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.UserType","user"),arg("org.rascalmpl.ast.Type","base")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Visibility visibility;
    private final org.rascalmpl.ast.UserType user;
    private final org.rascalmpl.ast.Type base;
  
    public Alias(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.UserType user,  org.rascalmpl.ast.Type base) {
      super(node);
      
      this.tags = tags;
      this.visibility = visibility;
      this.user = user;
      this.base = base;
    }
  
    @Override
    public boolean isAlias() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDeclarationAlias(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Alias)) {
        return false;
      }        
      Alias tmp = (Alias) o;
      return true && tmp.tags.equals(this.tags) && tmp.visibility.equals(this.visibility) && tmp.user.equals(this.user) && tmp.base.equals(this.base) ; 
    }
   
    @Override
    public int hashCode() {
      return 37 + 61 * tags.hashCode() + 941 * visibility.hashCode() + 941 * user.hashCode() + 461 * base.hashCode() ; 
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
    public org.rascalmpl.ast.Visibility getVisibility() {
      return this.visibility;
    }
  
    @Override
    public boolean hasVisibility() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.UserType getUser() {
      return this.user;
    }
  
    @Override
    public boolean hasUser() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Type getBase() {
      return this.base;
    }
  
    @Override
    public boolean hasBase() {
      return true;
    }	
  }
  public boolean isAnnotation() {
    return false;
  }

  static public class Annotation extends Declaration {
    // Production: sig("Annotation",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.Type","annoType"),arg("org.rascalmpl.ast.Type","onType"),arg("org.rascalmpl.ast.Name","name")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Visibility visibility;
    private final org.rascalmpl.ast.Type annoType;
    private final org.rascalmpl.ast.Type onType;
    private final org.rascalmpl.ast.Name name;
  
    public Annotation(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Type annoType,  org.rascalmpl.ast.Type onType,  org.rascalmpl.ast.Name name) {
      super(node);
      
      this.tags = tags;
      this.visibility = visibility;
      this.annoType = annoType;
      this.onType = onType;
      this.name = name;
    }
  
    @Override
    public boolean isAnnotation() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDeclarationAnnotation(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Annotation)) {
        return false;
      }        
      Annotation tmp = (Annotation) o;
      return true && tmp.tags.equals(this.tags) && tmp.visibility.equals(this.visibility) && tmp.annoType.equals(this.annoType) && tmp.onType.equals(this.onType) && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 643 + 73 * tags.hashCode() + 977 * visibility.hashCode() + 223 * annoType.hashCode() + 467 * onType.hashCode() + 353 * name.hashCode() ; 
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
    public org.rascalmpl.ast.Visibility getVisibility() {
      return this.visibility;
    }
  
    @Override
    public boolean hasVisibility() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Type getAnnoType() {
      return this.annoType;
    }
  
    @Override
    public boolean hasAnnoType() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Type getOnType() {
      return this.onType;
    }
  
    @Override
    public boolean hasOnType() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isData() {
    return false;
  }

  static public class Data extends Declaration {
    // Production: sig("Data",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.UserType","user"),arg("org.rascalmpl.ast.CommonKeywordParameters","commonKeywordParameters"),arg("java.util.List\<org.rascalmpl.ast.Variant\>","variants")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Visibility visibility;
    private final org.rascalmpl.ast.UserType user;
    private final org.rascalmpl.ast.CommonKeywordParameters commonKeywordParameters;
    private final java.util.List<org.rascalmpl.ast.Variant> variants;
  
    public Data(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.UserType user,  org.rascalmpl.ast.CommonKeywordParameters commonKeywordParameters,  java.util.List<org.rascalmpl.ast.Variant> variants) {
      super(node);
      
      this.tags = tags;
      this.visibility = visibility;
      this.user = user;
      this.commonKeywordParameters = commonKeywordParameters;
      this.variants = variants;
    }
  
    @Override
    public boolean isData() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDeclarationData(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Data)) {
        return false;
      }        
      Data tmp = (Data) o;
      return true && tmp.tags.equals(this.tags) && tmp.visibility.equals(this.visibility) && tmp.user.equals(this.user) && tmp.commonKeywordParameters.equals(this.commonKeywordParameters) && tmp.variants.equals(this.variants) ; 
    }
   
    @Override
    public int hashCode() {
      return 547 + 751 * tags.hashCode() + 421 * visibility.hashCode() + 613 * user.hashCode() + 211 * commonKeywordParameters.hashCode() + 631 * variants.hashCode() ; 
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
    public org.rascalmpl.ast.Visibility getVisibility() {
      return this.visibility;
    }
  
    @Override
    public boolean hasVisibility() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.UserType getUser() {
      return this.user;
    }
  
    @Override
    public boolean hasUser() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.CommonKeywordParameters getCommonKeywordParameters() {
      return this.commonKeywordParameters;
    }
  
    @Override
    public boolean hasCommonKeywordParameters() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Variant> getVariants() {
      return this.variants;
    }
  
    @Override
    public boolean hasVariants() {
      return true;
    }	
  }
  public boolean isDataAbstract() {
    return false;
  }

  static public class DataAbstract extends Declaration {
    // Production: sig("DataAbstract",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.UserType","user"),arg("org.rascalmpl.ast.CommonKeywordParameters","commonKeywordParameters")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Visibility visibility;
    private final org.rascalmpl.ast.UserType user;
    private final org.rascalmpl.ast.CommonKeywordParameters commonKeywordParameters;
  
    public DataAbstract(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.UserType user,  org.rascalmpl.ast.CommonKeywordParameters commonKeywordParameters) {
      super(node);
      
      this.tags = tags;
      this.visibility = visibility;
      this.user = user;
      this.commonKeywordParameters = commonKeywordParameters;
    }
  
    @Override
    public boolean isDataAbstract() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDeclarationDataAbstract(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DataAbstract)) {
        return false;
      }        
      DataAbstract tmp = (DataAbstract) o;
      return true && tmp.tags.equals(this.tags) && tmp.visibility.equals(this.visibility) && tmp.user.equals(this.user) && tmp.commonKeywordParameters.equals(this.commonKeywordParameters) ; 
    }
   
    @Override
    public int hashCode() {
      return 41 + 787 * tags.hashCode() + 193 * visibility.hashCode() + 523 * user.hashCode() + 673 * commonKeywordParameters.hashCode() ; 
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
    public org.rascalmpl.ast.Visibility getVisibility() {
      return this.visibility;
    }
  
    @Override
    public boolean hasVisibility() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.UserType getUser() {
      return this.user;
    }
  
    @Override
    public boolean hasUser() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.CommonKeywordParameters getCommonKeywordParameters() {
      return this.commonKeywordParameters;
    }
  
    @Override
    public boolean hasCommonKeywordParameters() {
      return true;
    }	
  }
  public boolean isFunction() {
    return false;
  }

  static public class Function extends Declaration {
    // Production: sig("Function",[arg("org.rascalmpl.ast.FunctionDeclaration","functionDeclaration")])
  
    
    private final org.rascalmpl.ast.FunctionDeclaration functionDeclaration;
  
    public Function(IConstructor node , org.rascalmpl.ast.FunctionDeclaration functionDeclaration) {
      super(node);
      
      this.functionDeclaration = functionDeclaration;
    }
  
    @Override
    public boolean isFunction() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDeclarationFunction(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Function)) {
        return false;
      }        
      Function tmp = (Function) o;
      return true && tmp.functionDeclaration.equals(this.functionDeclaration) ; 
    }
   
    @Override
    public int hashCode() {
      return 953 + 877 * functionDeclaration.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.FunctionDeclaration getFunctionDeclaration() {
      return this.functionDeclaration;
    }
  
    @Override
    public boolean hasFunctionDeclaration() {
      return true;
    }	
  }
  public boolean isTag() {
    return false;
  }

  static public class Tag extends Declaration {
    // Production: sig("Tag",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.Kind","kind"),arg("org.rascalmpl.ast.Name","name"),arg("java.util.List\<org.rascalmpl.ast.Type\>","types")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Visibility visibility;
    private final org.rascalmpl.ast.Kind kind;
    private final org.rascalmpl.ast.Name name;
    private final java.util.List<org.rascalmpl.ast.Type> types;
  
    public Tag(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Kind kind,  org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.Type> types) {
      super(node);
      
      this.tags = tags;
      this.visibility = visibility;
      this.kind = kind;
      this.name = name;
      this.types = types;
    }
  
    @Override
    public boolean isTag() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDeclarationTag(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Tag)) {
        return false;
      }        
      Tag tmp = (Tag) o;
      return true && tmp.tags.equals(this.tags) && tmp.visibility.equals(this.visibility) && tmp.kind.equals(this.kind) && tmp.name.equals(this.name) && tmp.types.equals(this.types) ; 
    }
   
    @Override
    public int hashCode() {
      return 439 + 877 * tags.hashCode() + 359 * visibility.hashCode() + 11 * kind.hashCode() + 461 * name.hashCode() + 109 * types.hashCode() ; 
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
    public org.rascalmpl.ast.Visibility getVisibility() {
      return this.visibility;
    }
  
    @Override
    public boolean hasVisibility() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Kind getKind() {
      return this.kind;
    }
  
    @Override
    public boolean hasKind() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Type> getTypes() {
      return this.types;
    }
  
    @Override
    public boolean hasTypes() {
      return true;
    }	
  }
  public boolean isVariable() {
    return false;
  }

  static public class Variable extends Declaration {
    // Production: sig("Variable",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.Type","type"),arg("java.util.List\<org.rascalmpl.ast.Variable\>","variables")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Visibility visibility;
    private final org.rascalmpl.ast.Type type;
    private final java.util.List<org.rascalmpl.ast.Variable> variables;
  
    public Variable(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.Variable> variables) {
      super(node);
      
      this.tags = tags;
      this.visibility = visibility;
      this.type = type;
      this.variables = variables;
    }
  
    @Override
    public boolean isVariable() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDeclarationVariable(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Variable)) {
        return false;
      }        
      Variable tmp = (Variable) o;
      return true && tmp.tags.equals(this.tags) && tmp.visibility.equals(this.visibility) && tmp.type.equals(this.type) && tmp.variables.equals(this.variables) ; 
    }
   
    @Override
    public int hashCode() {
      return 929 + 587 * tags.hashCode() + 487 * visibility.hashCode() + 487 * type.hashCode() + 797 * variables.hashCode() ; 
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
    public org.rascalmpl.ast.Visibility getVisibility() {
      return this.visibility;
    }
  
    @Override
    public boolean hasVisibility() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Variable> getVariables() {
      return this.variables;
    }
  
    @Override
    public boolean hasVariables() {
      return true;
    }	
  }
}