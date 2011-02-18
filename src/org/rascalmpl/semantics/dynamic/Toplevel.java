package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Declaration;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class Toplevel extends org.rascalmpl.ast.Toplevel {

	public Toplevel(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Toplevel.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Toplevel> __param2) {
			super(__param1, __param2);
		}


	}

	static public class GivenVisibility extends org.rascalmpl.ast.Toplevel.GivenVisibility {

		public GivenVisibility(INode __param1, Declaration __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getDeclaration().interpret(__eval);

		}

	}
}
