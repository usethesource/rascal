package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;

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
