package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Declarator;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class LocalVariableDeclaration extends org.rascalmpl.ast.LocalVariableDeclaration {

	public LocalVariableDeclaration(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.LocalVariableDeclaration.Default {

		public Default(INode __param1, Declarator __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			// TODO deal with dynamic variables
			return this.getDeclarator().interpret(__eval);

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.LocalVariableDeclaration.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.LocalVariableDeclaration> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Dynamic extends org.rascalmpl.ast.LocalVariableDeclaration.Dynamic {

		public Dynamic(INode __param1, Declarator __param2) {
			super(__param1, __param2);
		}


	}
}
