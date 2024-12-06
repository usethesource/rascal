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
public abstract class OctalIntegerLiteral extends AbstractAST {
  public OctalIntegerLiteral(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  

  static public class Lexical extends OctalIntegerLiteral {
  private final java.lang.String string;
  public Lexical(ISourceLocation src, IConstructor node, java.lang.String string) {
    super(src, node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }

  @Override
  public int hashCode() {
    return string.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Lexical && ((Lexical) o).string.equals(string);  
  }

  @Override
  public Object clone()  {
    return newInstance(getClass(), src, (IConstructor) null, string);
  }

  @Override
  public AbstractAST findNode(int offset) {
    if (src.getOffset() <= offset && offset < src.getOffset() + src.getLength()) {
      return this;
    }
    return null;
  }

  public java.lang.String toString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitOctalIntegerLiteralLexical(this);
  }
}

  
}