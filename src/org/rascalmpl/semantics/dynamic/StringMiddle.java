package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.MidStringChars;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.StringTemplate;

public abstract class StringMiddle extends org.rascalmpl.ast.StringMiddle {

	public StringMiddle(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.StringMiddle.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.StringMiddle> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Template extends org.rascalmpl.ast.StringMiddle.Template {

		public Template(INode __param1, MidStringChars __param2, StringTemplate __param3, org.rascalmpl.ast.StringMiddle __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Mid extends org.rascalmpl.ast.StringMiddle.Mid {

		public Mid(INode __param1, MidStringChars __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Interpolated extends org.rascalmpl.ast.StringMiddle.Interpolated {

		public Interpolated(INode __param1, MidStringChars __param2, Expression __param3, org.rascalmpl.ast.StringMiddle __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}