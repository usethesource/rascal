package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;

public abstract class Strategy extends org.rascalmpl.ast.Strategy {

	public Strategy(INode __param1) {
		super(__param1);
	}

	static public class TopDown extends org.rascalmpl.ast.Strategy.TopDown {

		public TopDown(INode __param1) {
			super(__param1);
		}


	}

	static public class BottomUp extends org.rascalmpl.ast.Strategy.BottomUp {

		public BottomUp(INode __param1) {
			super(__param1);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Strategy.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Strategy> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Outermost extends org.rascalmpl.ast.Strategy.Outermost {

		public Outermost(INode __param1) {
			super(__param1);
		}


	}

	static public class BottomUpBreak extends org.rascalmpl.ast.Strategy.BottomUpBreak {

		public BottomUpBreak(INode __param1) {
			super(__param1);
		}


	}

	static public class Innermost extends org.rascalmpl.ast.Strategy.Innermost {

		public Innermost(INode __param1) {
			super(__param1);
		}


	}

	static public class TopDownBreak extends org.rascalmpl.ast.Strategy.TopDownBreak {

		public TopDownBreak(INode __param1) {
			super(__param1);
		}


	}
}
