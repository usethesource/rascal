package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;

public abstract class QualifiedName extends org.rascalmpl.ast.QualifiedName {

	public QualifiedName(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.QualifiedName.Ambiguity {
		public Ambiguity(INode __param1, List<org.rascalmpl.ast.QualifiedName> __param2) {
			super(__param1, __param2);
		}
	}

	static public class Default extends org.rascalmpl.ast.QualifiedName.Default {
		private static final TypeFactory TF = TypeFactory.getInstance();

		public Default(INode __param1, List<Name> __param2) {
			super(__param1, __param2);
		}

		@Override
		public Type typeOf(Environment env) {
			if (getNames().size() == 1 && Names.name(getNames().get(0)).equals("_")) {
				return TF.valueType();
			}
			else {
				Result<IValue> varRes = env.getVariable(this);
				if (varRes == null || varRes.getType() == null) {
					return TF.valueType();
				} else {
					return varRes.getType();
				}
			}
		}

	}
}
