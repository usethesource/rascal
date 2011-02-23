package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.PreStringChars;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.ast.StringTail;
import org.rascalmpl.ast.StringTemplate;

public abstract class StringLiteral extends org.rascalmpl.ast.StringLiteral {

	public StringLiteral(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.StringLiteral.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.StringLiteral> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Template extends org.rascalmpl.ast.StringLiteral.Template {

		public Template(INode __param1, PreStringChars __param2, StringTemplate __param3, StringTail __param4) {
			super(__param1, __param2, __param3, __param4);
		}


	}

	static public class Interpolated extends org.rascalmpl.ast.StringLiteral.Interpolated {

		public Interpolated(INode __param1, PreStringChars __param2, Expression __param3, StringTail __param4) {
			super(__param1, __param2, __param3, __param4);
		}


	}

	static public class NonInterpolated extends org.rascalmpl.ast.StringLiteral.NonInterpolated {

		public NonInterpolated(INode __param1, StringConstant __param2) {
			super(__param1, __param2);
		}


	}
}
