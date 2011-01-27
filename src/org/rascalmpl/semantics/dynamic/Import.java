package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.ImportedModule;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.SyntaxDefinition;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;

public abstract class Import extends org.rascalmpl.ast.Import {

	public Import(INode __param1) {
		super(__param1);
	}

	static public class Extend extends org.rascalmpl.ast.Import.Extend {

		public Extend(INode __param1, ImportedModule __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Import.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Import> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Default extends org.rascalmpl.ast.Import.Default {

		public Default(INode __param1, ImportedModule __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			// TODO support for full complexity of import declarations
			String name = __eval.getUnescapedModuleName(this);

			if (!__eval.__getHeap().existsModule(name)) {
				// deal with a fresh module that needs initialization
				__eval.__getHeap().addModule(new ModuleEnvironment(name));
				__eval.evalRascalModule(this, name);
				__eval.addImportToCurrentModule(this, name);
			} else if (__eval.getCurrentEnvt() == __eval.__getRootScope()) {
				// in the root scope we treat an import as a "reload"
				__eval.__getHeap().resetModule(name);
				__eval.evalRascalModule(this, name);
				__eval.addImportToCurrentModule(this, name);
			} else {
				// otherwise simply add the current imported name to the imports
				// of the current module
				__eval.addImportToCurrentModule(this, name);
			}

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class Syntax extends org.rascalmpl.ast.Import.Syntax {

		public Syntax(INode __param1, SyntaxDefinition __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.__getTypeDeclarator().declareSyntaxType(this.getSyntax().getDefined(), __eval.getCurrentEnvt());
			__eval.getCurrentEnvt().declareProduction(this);
			__eval.loadParseTreeModule(this);
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}
}