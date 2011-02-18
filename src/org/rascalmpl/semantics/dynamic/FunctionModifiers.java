package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class FunctionModifiers extends org.rascalmpl.ast.FunctionModifiers {

	public FunctionModifiers(INode __param1) {
		super(__param1);
	}

	static public class List extends org.rascalmpl.ast.FunctionModifiers.List {

		public List(INode __param1, java.util.List<FunctionModifier> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.FunctionModifiers.Ambiguity {

		public Ambiguity(INode __param1, java.util.List<org.rascalmpl.ast.FunctionModifiers> __param2) {
			super(__param1, __param2);
		}


	}
}
