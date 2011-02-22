package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.MidProtocolChars;
import org.rascalmpl.ast.PostProtocolChars;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class ProtocolTail extends org.rascalmpl.ast.ProtocolTail {

	public ProtocolTail(INode __param1) {
		super(__param1);
	}

	static public class Post extends org.rascalmpl.ast.ProtocolTail.Post {

		public Post(INode __param1, PostProtocolChars __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getPost().interpret(__eval);

		}

	}

	static public class Mid extends org.rascalmpl.ast.ProtocolTail.Mid {

		public Mid(INode __param1, MidProtocolChars __param2, Expression __param3, org.rascalmpl.ast.ProtocolTail __param4) {
			super(__param1, __param2, __param3, __param4);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			Result<IValue> pre = this.getMid().interpret(__eval);
			Result<IValue> expr = this.getExpression().interpret(__eval);
			Result<IValue> tail = this.getTail().interpret(__eval);

			return pre.add(expr).add(tail);
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.ProtocolTail.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.ProtocolTail> __param2) {
			super(__param1, __param2);
		}


	}
}
