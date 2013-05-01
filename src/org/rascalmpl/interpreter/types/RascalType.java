package org.rascalmpl.interpreter.types;

import org.eclipse.imp.pdb.facts.type.ExternalType;
import org.eclipse.imp.pdb.facts.type.Type;

public abstract class RascalType extends ExternalType {

  public abstract <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> visitor) throws E;
  
  @Override
  protected Type lubWithExternal(Type type) {
    assert type instanceof RascalType;
    return lub((RascalType) type);
  }

  protected abstract Type lub(RascalType type);

  @Override
  protected boolean isSubtypeOfExternal(Type type) {
    assert type instanceof RascalType;
    return ((RascalType) type).isSupertypeOf(this);
  }

  protected abstract boolean isSupertypeOf(RascalType type);
  
  protected boolean isSubtypeOfNonTerminal(RascalType type) {
    return false;
  }
  
  protected boolean isSubtypeOfFunction(RascalType type) {
    return false;
  }
  
  protected boolean isSubtypeOfOverloadedFunction(RascalType type) {
    return false;
  }
  
  protected boolean isSubtypeOfReified(RascalType type) {
    return false;
  }
  
  protected Type lubWithNonTerminal(RascalType type) {
    return TF.valueType();
  }
  
  protected Type lubWithFunction(RascalType type) {
    return TF.valueType();
  }
  
  protected Type lubWithOverloadedFunction(RascalType type) {
    return TF.valueType();
  }
  
  protected Type lubWithReified(RascalType type) {
    return TF.valueType();
  }
}
