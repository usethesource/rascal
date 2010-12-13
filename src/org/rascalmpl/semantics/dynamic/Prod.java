package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Assoc;
import org.rascalmpl.ast.LanguageAction;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.ProdModifier;
import org.rascalmpl.ast.Sym;

public abstract class Prod extends org.rascalmpl.ast.Prod {

	public Prod(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Prod.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Prod> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Reject extends org.rascalmpl.ast.Prod.Reject {

		public Reject(INode __param1, org.rascalmpl.ast.Prod __param2, org.rascalmpl.ast.Prod __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Reference extends org.rascalmpl.ast.Prod.Reference {

		public Reference(INode __param1, Name __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Unlabeled extends org.rascalmpl.ast.Prod.Unlabeled {

		public Unlabeled(INode __param1, List<ProdModifier> __param2, List<Sym> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class AssociativityGroup extends org.rascalmpl.ast.Prod.AssociativityGroup {

		public AssociativityGroup(INode __param1, Assoc __param2, org.rascalmpl.ast.Prod __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Action extends org.rascalmpl.ast.Prod.Action {

		public Action(INode __param1, org.rascalmpl.ast.Prod __param2, LanguageAction __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Follow extends org.rascalmpl.ast.Prod.Follow {

		public Follow(INode __param1, org.rascalmpl.ast.Prod __param2, org.rascalmpl.ast.Prod __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class All extends org.rascalmpl.ast.Prod.All {

		public All(INode __param1, org.rascalmpl.ast.Prod __param2, org.rascalmpl.ast.Prod __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class First extends org.rascalmpl.ast.Prod.First {

		public First(INode __param1, org.rascalmpl.ast.Prod __param2, org.rascalmpl.ast.Prod __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Labeled extends org.rascalmpl.ast.Prod.Labeled {

		public Labeled(INode __param1, List<ProdModifier> __param2, Name __param3, List<Sym> __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Others extends org.rascalmpl.ast.Prod.Others {

		public Others(INode __param1) {
			super(__param1);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}