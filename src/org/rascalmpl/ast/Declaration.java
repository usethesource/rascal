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

  static public class Ambiguity extends Declaration {
    private final java.util.List<org.rascalmpl.ast.Declaration> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Declaration> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Declaration> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitDeclarationAmbiguity(this);
    }
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
    // Production: sig("Data",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.UserType","user"),arg("java.util.List\<org.rascalmpl.ast.Variant\>","variants")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Visibility visibility;
    private final org.rascalmpl.ast.UserType user;
    private final java.util.List<org.rascalmpl.ast.Variant> variants;
  
    public Data(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.UserType user,  java.util.List<org.rascalmpl.ast.Variant> variants) {
      super(node);
      
      this.tags = tags;
      this.visibility = visibility;
      this.user = user;
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
    // Production: sig("DataAbstract",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.UserType","user")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.Visibility visibility;
    private final org.rascalmpl.ast.UserType user;
  
    public DataAbstract(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.UserType user) {
      super(node);
      
      this.tags = tags;
      this.visibility = visibility;
      this.user = user;
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