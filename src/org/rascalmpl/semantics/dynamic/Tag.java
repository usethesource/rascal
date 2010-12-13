package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.TagString;

public abstract class Tag extends org.rascalmpl.ast.Tag {

	public Tag(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.Tag.Default {

		public Default(INode __param1, Name __param2, TagString __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Empty extends org.rascalmpl.ast.Tag.Empty {

		public Empty(INode __param1, Name __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Tag.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Tag> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Expression extends org.rascalmpl.ast.Tag.Expression {

		public Expression(INode __param1, Name __param2, org.rascalmpl.ast.Expression __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}