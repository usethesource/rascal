package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Import;
import org.rascalmpl.ast.ModuleParameters;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class Header extends org.rascalmpl.ast.Header {

	public Header(INode __param1) {
		super(__param1);
	}

	static public class Parameters extends org.rascalmpl.ast.Header.Parameters {

		public Parameters(INode __param1, Tags __param2, QualifiedName __param3, ModuleParameters __param4, List<Import> __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.visitImports(this.getImports());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Header.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Header> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Default extends org.rascalmpl.ast.Header.Default {

		public Default(INode __param1, Tags __param2, QualifiedName __param3, List<Import> __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.visitImports(this.getImports());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}