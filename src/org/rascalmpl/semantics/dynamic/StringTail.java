package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.MidStringChars;
import org.rascalmpl.ast.PostStringChars;
import org.rascalmpl.ast.StringTemplate;

public abstract class StringTail extends org.rascalmpl.ast.StringTail {

	public StringTail(INode __param1) {
		super(__param1);
	}

	static public class Post extends org.rascalmpl.ast.StringTail.Post {

		public Post(INode __param1, PostStringChars __param2) {
			super(__param1, __param2);
		}


	}

	static public class MidTemplate extends org.rascalmpl.ast.StringTail.MidTemplate {

		public MidTemplate(INode __param1, MidStringChars __param2, StringTemplate __param3, org.rascalmpl.ast.StringTail __param4) {
			super(__param1, __param2, __param3, __param4);
		}


	}

	static public class MidInterpolated extends org.rascalmpl.ast.StringTail.MidInterpolated {

		public MidInterpolated(INode __param1, MidStringChars __param2, Expression __param3, org.rascalmpl.ast.StringTail __param4) {
			super(__param1, __param2, __param3, __param4);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.StringTail.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.StringTail> __param2) {
			super(__param1, __param2);
		}


	}
}
