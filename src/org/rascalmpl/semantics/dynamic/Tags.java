package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Tag;

public abstract class Tags extends org.rascalmpl.ast.Tags {

	public Tags(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.Tags.Default {

		public Default(INode __param1, List<Tag> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Tags.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Tags> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}