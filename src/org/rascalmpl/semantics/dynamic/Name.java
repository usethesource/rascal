package org.rascalmpl.semantics.dynamic;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;

public abstract class Name extends org.rascalmpl.ast.Name {

	public Name(ISourceLocation src, IConstructor node) {
		super(src, node);
	}

	public static class Lexical extends org.rascalmpl.ast.Name.Lexical {
		public Lexical(ISourceLocation src, IConstructor node, String string) {
			super(src, node, string.replace('\\', ' ').replaceAll(" ",""));
		}
	}
}
