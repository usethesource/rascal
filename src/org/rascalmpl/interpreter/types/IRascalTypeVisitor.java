package org.rascalmpl.interpreter.types;

public interface IRascalTypeVisitor<T,E extends Throwable> {
  T visitFunction(RascalType type) throws E;
  T visitOverloadedFunction(RascalType type) throws E;
  T visitReified(RascalType type) throws E;
  T visitNonTerminal(RascalType type) throws E;
}
