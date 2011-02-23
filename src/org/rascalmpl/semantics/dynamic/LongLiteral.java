package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;

public abstract class LongLiteral extends org.rascalmpl.ast.LongLiteral {

	public LongLiteral(INode __param1) {
		super(__param1);
	}

	static public class DecimalLongLiteral extends org.rascalmpl.ast.LongLiteral.DecimalLongLiteral {

		public DecimalLongLiteral(INode __param1, org.rascalmpl.ast.DecimalLongLiteral __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.LongLiteral.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.LongLiteral> __param2) {
			super(__param1, __param2);
		}


	}

	static public class HexLongLiteral extends org.rascalmpl.ast.LongLiteral.HexLongLiteral {

		public HexLongLiteral(INode __param1, org.rascalmpl.ast.HexLongLiteral __param2) {
			super(__param1, __param2);
		}


	}

	static public class OctalLongLiteral extends org.rascalmpl.ast.LongLiteral.OctalLongLiteral {

		public OctalLongLiteral(INode __param1, org.rascalmpl.ast.OctalLongLiteral __param2) {
			super(__param1, __param2);
		}


	}
}
