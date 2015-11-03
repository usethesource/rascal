package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.rascalmpl.ast.StringCharacter;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;

public class StringPart  {
	public static class Characters extends org.rascalmpl.ast.StringPart.Characters {
		public Characters(ISourceLocation src, IConstructor node, List<StringCharacter> characters) {
			super(src, node, characters);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			StringBuilder b = new StringBuilder();
			
			for (StringCharacter ch : getCharacters()) {
				b.append(((IString) ch.interpret(eval).getValue()).getValue());
			}
			
			return ResultFactory.makeResult(TF.stringType(), VF.string(b.toString()), eval);
		}
	}
}
