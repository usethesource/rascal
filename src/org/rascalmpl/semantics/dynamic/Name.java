package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IConstructor;

public abstract class Name extends org.rascalmpl.ast.Name {

	public Name(IConstructor node) {
		super(node);
	}

	public static class Lexical extends org.rascalmpl.ast.Name.Lexical {
		public Lexical(IConstructor node, String string) {
			super(node, string.replace('\\', ' ').replaceAll(" ",""));
		}
	}
}
