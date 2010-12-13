package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Char;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class Range extends org.rascalmpl.ast.Range {

	public Range(INode __param1) {
		super(__param1);
	}

	static public class Character extends org.rascalmpl.ast.Range.Character {

		public Character(INode __param1, Char __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Range.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Range> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class FromTo extends org.rascalmpl.ast.Range.FromTo {

		public FromTo(INode __param1, Char __param2, Char __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}