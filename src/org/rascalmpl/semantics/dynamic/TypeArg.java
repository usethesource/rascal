package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.env.Environment;

public abstract class TypeArg extends org.rascalmpl.ast.TypeArg {

	static public class Default extends org.rascalmpl.ast.TypeArg.Default {

		public Default(INode __param1, org.rascalmpl.ast.Type __param2) {
			super(__param1, __param2);
		}

		@Override
		public Type typeOf(Environment __eval) {
			return this.getType().typeOf(__eval);
		}

	}

	static public class Named extends org.rascalmpl.ast.TypeArg.Named {

		public Named(INode __param1, org.rascalmpl.ast.Type __param2,
				Name __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment __eval) {
			return this.getType().typeOf(__eval);
		}

	}

	public TypeArg(INode __param1) {
		super(__param1);
	}
}
