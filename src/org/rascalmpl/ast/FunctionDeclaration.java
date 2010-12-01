
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class FunctionDeclaration extends AbstractAST {
  public FunctionDeclaration(INode node) {
    super(node);
  }
  

  public boolean hasVisibility() {
    return false;
  }

  public org.rascalmpl.ast.Visibility getVisibility() {
    throw new UnsupportedOperationException();
  }

  public boolean hasTags() {
    return false;
  }

  public org.rascalmpl.ast.Tags getTags() {
    throw new UnsupportedOperationException();
  }

  public boolean hasSignature() {
    return false;
  }

  public org.rascalmpl.ast.Signature getSignature() {
    throw new UnsupportedOperationException();
  }

  public boolean hasBody() {
    return false;
  }

  public org.rascalmpl.ast.FunctionBody getBody() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends FunctionDeclaration {
  private final java.util.List<org.rascalmpl.ast.FunctionDeclaration> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionDeclaration> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.FunctionDeclaration> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFunctionDeclarationAmbiguity(this);
  }
}





  public boolean isAbstract() {
    return false;
  }
  
static public class Abstract extends FunctionDeclaration {
  // Production: sig("Abstract",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.Signature","signature")])

  
     private final org.rascalmpl.ast.Tags tags;
  
     private final org.rascalmpl.ast.Visibility visibility;
  
     private final org.rascalmpl.ast.Signature signature;
  

  
public Abstract(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Signature signature) {
  super(node);
  
    this.tags = tags;
  
    this.visibility = visibility;
  
    this.signature = signature;
  
}


  @Override
  public boolean isAbstract() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionDeclarationAbstract(this);
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
     public org.rascalmpl.ast.Signature getSignature() {
        return this.signature;
     }
     
     @Override
     public boolean hasSignature() {
        return true;
     }
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends FunctionDeclaration {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Visibility","visibility"),arg("org.rascalmpl.ast.Signature","signature"),arg("org.rascalmpl.ast.FunctionBody","body")])

  
     private final org.rascalmpl.ast.Tags tags;
  
     private final org.rascalmpl.ast.Visibility visibility;
  
     private final org.rascalmpl.ast.Signature signature;
  
     private final org.rascalmpl.ast.FunctionBody body;
  

  
public Default(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Signature signature,  org.rascalmpl.ast.FunctionBody body) {
  super(node);
  
    this.tags = tags;
  
    this.visibility = visibility;
  
    this.signature = signature;
  
    this.body = body;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionDeclarationDefault(this);
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
     public org.rascalmpl.ast.Signature getSignature() {
        return this.signature;
     }
     
     @Override
     public boolean hasSignature() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.FunctionBody getBody() {
        return this.body;
     }
     
     @Override
     public boolean hasBody() {
        return true;
     }
  	
}



}
