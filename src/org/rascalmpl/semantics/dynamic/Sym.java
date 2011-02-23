package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.env.Environment;

public abstract class Sym extends org.rascalmpl.ast.Sym {

	static public class Nonterminal extends org.rascalmpl.ast.Sym.Nonterminal {

		public Nonterminal(INode __param1,
				org.rascalmpl.ast.Nonterminal __param2) {
			super(__param1, __param2);
		}

		@Override
		public Type typeOf(Environment env) {
			return getNonterminal().typeOf(env);
		}
	}

	public Sym(INode __param1) {
		super(__param1);
	}
}
