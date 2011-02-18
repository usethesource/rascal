package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Type;

public abstract class TypeVar extends org.rascalmpl.ast.TypeVar {

	public TypeVar(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.TypeVar.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.TypeVar> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Bounded extends org.rascalmpl.ast.TypeVar.Bounded {

		public Bounded(INode __param1, Name __param2, Type __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class Free extends org.rascalmpl.ast.TypeVar.Free {

		public Free(INode __param1, Name __param2) {
			super(__param1, __param2);
		}


	}
}
