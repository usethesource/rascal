package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.BasicType;
import org.rascalmpl.ast.DataTypeSelector;
import org.rascalmpl.ast.FunctionType;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.StructuredType;
import org.rascalmpl.ast.Sym;
import org.rascalmpl.ast.TypeVar;
import org.rascalmpl.ast.UserType;
import org.rascalmpl.interpreter.TypeEvaluator.Visitor;
import org.rascalmpl.interpreter.asserts.Ambiguous;

public abstract class Type extends org.rascalmpl.ast.Type {

	public Type(INode __param1) {
		super(__param1);
	}

	static public class Structured extends org.rascalmpl.ast.Type.Structured {

		public Structured(INode __param1, StructuredType __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(Visitor __eval) {

			return this.getStructured().__evaluate(__eval);

		}

	}

	static public class Selector extends org.rascalmpl.ast.Type.Selector {

		public Selector(INode __param1, DataTypeSelector __param2) {
			super(__param1, __param2);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(Visitor __eval) {

			return this.getSelector().__evaluate(__eval);

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Basic extends org.rascalmpl.ast.Type.Basic {

		public Basic(INode __param1, BasicType __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(Visitor __eval) {

			return this.getBasic().__evaluate(__eval);

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Type.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Type> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(Visitor __eval) {

			throw new Ambiguous((IConstructor) this.getTree());

		}

	}

	static public class Variable extends org.rascalmpl.ast.Type.Variable {

		public Variable(INode __param1, TypeVar __param2) {
			super(__param1, __param2);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(Visitor __eval) {

			TypeVar var = this.getTypeVar();
			org.eclipse.imp.pdb.facts.type.Type param;

			if (var.isBounded()) {
				param = org.rascalmpl.interpreter.TypeEvaluator.__getTf().parameterType(org.rascalmpl.interpreter.utils.Names.name(var.getName()), var.getBound().__evaluate(__eval));
			} else {
				param = org.rascalmpl.interpreter.TypeEvaluator.__getTf().parameterType(org.rascalmpl.interpreter.utils.Names.name(var.getName()));
			}
			if (__eval.__getEnv() != null) {
				return param.instantiate(__eval.__getEnv().getTypeBindings());
			}
			return param;

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class User extends org.rascalmpl.ast.Type.User {

		public User(INode __param1, UserType __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(Visitor __eval) {

			return this.getUser().__evaluate(__eval);

		}

	}

	static public class Bracket extends org.rascalmpl.ast.Type.Bracket {

		public Bracket(INode __param1, org.rascalmpl.ast.Type __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(Visitor __eval) {

			return this.getType().__evaluate(__eval);

		}

	}

	static public class Function extends org.rascalmpl.ast.Type.Function {

		public Function(INode __param1, FunctionType __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(Visitor __eval) {

			return this.getFunction().__evaluate(__eval);

		}

	}

	static public class Symbol extends org.rascalmpl.ast.Type.Symbol {

		public Symbol(INode __param1, Sym __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(Visitor __eval) {

			return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().nonTerminalType(this);

		}

	}
}