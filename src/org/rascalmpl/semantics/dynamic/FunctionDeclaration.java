package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.FunctionBody;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Signature;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.ast.Visibility;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.TypeEvaluator.Visitor;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.JavaMethod;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.JavaMethodLinkError;
import org.rascalmpl.interpreter.staticErrors.MissingModifierError;

public abstract class FunctionDeclaration extends org.rascalmpl.ast.FunctionDeclaration {

	public FunctionDeclaration(INode __param1) {
		super(__param1);
	}

	static public class Abstract extends org.rascalmpl.ast.FunctionDeclaration.Abstract {

		public Abstract(INode __param1, Tags __param2, Visibility __param3, Signature __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			boolean varArgs = this.getSignature().getParameters().isVarArgs();

			if (!__eval.hasJavaModifier(this)) {
				throw new MissingModifierError("java", this);
			}

			AbstractFunction lambda = new JavaMethod(__eval, this, varArgs, __eval.getCurrentEnvt(), __eval.__getJavaBridge());
			String name = org.rascalmpl.interpreter.utils.Names.name(this.getSignature().getName());
			__eval.getCurrentEnvt().storeFunction(name, lambda);
			__eval.getCurrentEnvt().markNameFinal(lambda.getName());
			__eval.getCurrentEnvt().markNameOverloadable(lambda.getName());

			lambda.setPublic(this.getVisibility().isPublic());
			return lambda;

		}

	}

	static public class Default extends org.rascalmpl.ast.FunctionDeclaration.Default {

		public Default(INode __param1, Tags __param2, Visibility __param3, Signature __param4, FunctionBody __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			AbstractFunction lambda;
			boolean varArgs = this.getSignature().getParameters().isVarArgs();

			if (__eval.hasJavaModifier(this)) {
				throw new JavaMethodLinkError("may not use java modifier with a function that has a body", null, this);
			}

			if (!this.getBody().isDefault()) {
				throw new MissingModifierError("java", this);
			}

			lambda = new RascalFunction(__eval, this, varArgs, __eval.getCurrentEnvt(), __eval.__getAccumulators());

			__eval.getCurrentEnvt().storeFunction(lambda.getName(), lambda);
			__eval.getCurrentEnvt().markNameFinal(lambda.getName());
			__eval.getCurrentEnvt().markNameOverloadable(lambda.getName());

			lambda.setPublic(this.getVisibility().isPublic());
			return lambda;

		}

		@Override
		public Type __evaluate(Visitor __eval) {

			return this.getSignature().__evaluate(__eval);

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.FunctionDeclaration.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.FunctionDeclaration> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}