package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.CaseInsensitiveStringConstant;
import org.rascalmpl.ast.Class;
import org.rascalmpl.ast.IntegerLiteral;
import org.rascalmpl.ast.NonterminalLabel;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.ParameterizedNonterminal;
import org.rascalmpl.ast.StringConstant;

public abstract class Sym extends org.rascalmpl.ast.Sym {

	public Sym(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Sym.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Sym> __param2) {
			super(__param1, __param2);
		}


	}

	static public class StartOfLine extends org.rascalmpl.ast.Sym.StartOfLine {

		public StartOfLine(INode __param1) {
			super(__param1);
		}


	}

	static public class CharacterClass extends org.rascalmpl.ast.Sym.CharacterClass {

		public CharacterClass(INode __param1, Class __param2) {
			super(__param1, __param2);
		}


	}

	static public class Literal extends org.rascalmpl.ast.Sym.Literal {

		public Literal(INode __param1, StringConstant __param2) {
			super(__param1, __param2);
		}


	}

	static public class CaseInsensitiveLiteral extends org.rascalmpl.ast.Sym.CaseInsensitiveLiteral {

		public CaseInsensitiveLiteral(INode __param1, CaseInsensitiveStringConstant __param2) {
			super(__param1, __param2);
		}


	}

	static public class Parameter extends org.rascalmpl.ast.Sym.Parameter {

		public Parameter(INode __param1, org.rascalmpl.ast.Nonterminal __param2) {
			super(__param1, __param2);
		}


	}

	static public class Nonterminal extends org.rascalmpl.ast.Sym.Nonterminal {

		public Nonterminal(INode __param1, org.rascalmpl.ast.Nonterminal __param2) {
			super(__param1, __param2);
		}


	}

	static public class Iter extends org.rascalmpl.ast.Sym.Iter {

		public Iter(INode __param1, org.rascalmpl.ast.Sym __param2) {
			super(__param1, __param2);
		}


	}

	static public class IterStar extends org.rascalmpl.ast.Sym.IterStar {

		public IterStar(INode __param1, org.rascalmpl.ast.Sym __param2) {
			super(__param1, __param2);
		}


	}

	static public class IterStarSep extends org.rascalmpl.ast.Sym.IterStarSep {

		public IterStarSep(INode __param1, org.rascalmpl.ast.Sym __param2, StringConstant __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class EndOfLine extends org.rascalmpl.ast.Sym.EndOfLine {

		public EndOfLine(INode __param1) {
			super(__param1);
		}


	}

	static public class Column extends org.rascalmpl.ast.Sym.Column {

		public Column(INode __param1, IntegerLiteral __param2) {
			super(__param1, __param2);
		}


	}

	static public class Parametrized extends org.rascalmpl.ast.Sym.Parametrized {

		public Parametrized(INode __param1, ParameterizedNonterminal __param2, List<org.rascalmpl.ast.Sym> __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class IterSep extends org.rascalmpl.ast.Sym.IterSep {

		public IterSep(INode __param1, org.rascalmpl.ast.Sym __param2, StringConstant __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class Optional extends org.rascalmpl.ast.Sym.Optional {

		public Optional(INode __param1, org.rascalmpl.ast.Sym __param2) {
			super(__param1, __param2);
		}


	}

	static public class Labeled extends org.rascalmpl.ast.Sym.Labeled {

		public Labeled(INode __param1, org.rascalmpl.ast.Sym __param2, NonterminalLabel __param3) {
			super(__param1, __param2, __param3);
		}


	}
}
