package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.utils.StringUtils;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;

public class StringCharacter {
	static public class Lexical extends org.rascalmpl.ast.StringCharacter.Lexical {

		public Lexical(ISourceLocation __param1, IConstructor tree, String __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			return ResultFactory.makeResult(TF.stringType(), VF.string(StringUtils.unescapeBase(getString())), eval);
		}
	}
}
