package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.StringMiddle;

public abstract class StringTemplate extends org.rascalmpl.ast.StringTemplate {

	public StringTemplate(INode __param1) {
		super(__param1);
	}

	static public class DoWhile extends org.rascalmpl.ast.StringTemplate.DoWhile {

		public DoWhile(INode __param1, List<Statement> __param2, StringMiddle __param3, List<Statement> __param4, Expression __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}


	}

	static public class While extends org.rascalmpl.ast.StringTemplate.While {

		public While(INode __param1, Expression __param2, List<Statement> __param3, StringMiddle __param4, List<Statement> __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}


	}

	static public class IfThenElse extends org.rascalmpl.ast.StringTemplate.IfThenElse {

		public IfThenElse(INode __param1, List<Expression> __param2, List<Statement> __param3, StringMiddle __param4, List<Statement> __param5, List<Statement> __param6, StringMiddle __param7,
				List<Statement> __param8) {
			super(__param1, __param2, __param3, __param4, __param5, __param6, __param7, __param8);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.StringTemplate.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.StringTemplate> __param2) {
			super(__param1, __param2);
		}


	}

	static public class For extends org.rascalmpl.ast.StringTemplate.For {

		public For(INode __param1, List<Expression> __param2, List<Statement> __param3, StringMiddle __param4, List<Statement> __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}


	}

	static public class IfThen extends org.rascalmpl.ast.StringTemplate.IfThen {

		public IfThen(INode __param1, List<Expression> __param2, List<Statement> __param3, StringMiddle __param4, List<Statement> __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}


	}
}
