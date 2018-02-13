package org.rascalmpl.core.types;

import io.usethesource.vallang.type.ITypeVisitor;

public interface IRascalTypeVisitor<T,E extends Throwable> extends ITypeVisitor<T, E> {
  T visitFunction(RascalType type) throws E;
  T visitOverloadedFunction(RascalType type) throws E;
  T visitReified(RascalType type) throws E;
  T visitNonTerminal(RascalType type) throws E;
  T visitFailureType(RascalType type) throws E;
}
