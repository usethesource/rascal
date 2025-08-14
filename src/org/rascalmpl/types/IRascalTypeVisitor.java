package org.rascalmpl.types;

import io.usethesource.vallang.type.ITypeVisitor;

public interface IRascalTypeVisitor<T,E extends Throwable> extends ITypeVisitor<T, E> {
  T visitReified(RascalType THIS) throws E;
  T visitNonTerminal(RascalType THIS) throws E;
  T visitRoleModifier(RascalType THIS) throws E;
}
