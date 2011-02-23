package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.BasicType;
import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.interpreter.BasicTypeEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.utils.TypeUtils;

public abstract class StructuredType extends org.rascalmpl.ast.StructuredType {

	static public class Ambiguity extends
			org.rascalmpl.ast.StructuredType.Ambiguity {

		public Ambiguity(INode __param1,
				List<org.rascalmpl.ast.StructuredType> __param2) {
			super(__param1, __param2);
		}

	}

	static public class Default extends
			org.rascalmpl.ast.StructuredType.Default {

		public Default(INode __param1, BasicType __param2,
				List<TypeArg> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment __eval) {
			return this.getBasicType().__evaluate(
					new BasicTypeEvaluator(__eval, TypeUtils.typeOf(this
							.getArguments(), __eval), null));
		}

	}

	public StructuredType(INode __param1) {
		super(__param1);
	}
}
