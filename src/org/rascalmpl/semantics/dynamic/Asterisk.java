package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;

public abstract class Asterisk extends org.rascalmpl.ast.Asterisk {

	public Asterisk(INode __param1) {
		super(__param1);
	}

	static public class Lexical extends org.rascalmpl.ast.Asterisk.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Asterisk.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Asterisk> __param2) {
			super(__param1, __param2);
		}


	}
}
