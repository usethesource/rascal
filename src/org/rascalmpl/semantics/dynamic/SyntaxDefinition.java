package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.Nonterminal;
import org.rascalmpl.ast.Prod;
import org.rascalmpl.ast.Start;
import org.rascalmpl.ast.Sym;
import org.rascalmpl.ast.Visibility;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.values.uptr.Factory;

/**
 * This class dispatches over different kinds of syntax definitions to make sure non-terminals are declared in the right class
 * (sort, lex, keywords, layout).
 */
public abstract class SyntaxDefinition extends
		org.rascalmpl.ast.SyntaxDefinition {
	
	public SyntaxDefinition(IConstructor node) {
		super(node);
	}

	public static class Language extends org.rascalmpl.ast.SyntaxDefinition.Language {
		private final IConstructor node;

		public Language(IConstructor node, Start start, Sym defined,
				Prod production) {
			super(node, start, defined, production);
			this.node = node;
		}

		@Override
		public IConstructor getTree() {
			return node;
		}
		
		@Override
		public String declareSyntax(Evaluator eval, boolean withImports) {
			Sym type = getDefined();
			IValueFactory vf = eval.getValueFactory();
			
			if (type.isNonterminal()) {
				String nt = ((Nonterminal.Lexical) type.getNonterminal()).getString();
				eval.getCurrentEnvt().concreteSyntaxType(nt, (IConstructor) Factory.Symbol_Sort.make(vf, vf.string(nt)));
			}
			
			eval.getCurrentEnvt().declareProduction(this);
			return null;
		}
	}
	
	public static class Lexical extends org.rascalmpl.ast.SyntaxDefinition.Lexical {
		private final IConstructor node;

		public Lexical(IConstructor node, Sym defined, Prod production) {
			super(node, defined, production);
			this.node = node;
		}

		@Override
		public IConstructor getTree() {
			return node;
		}
		
		@Override
		public String declareSyntax(Evaluator eval, boolean withImports) {
			Sym type = getDefined();
			IValueFactory vf = eval.getValueFactory();
			
			if (type.isNonterminal()) {
				String nt = ((Nonterminal.Lexical) type.getNonterminal()).getString();
				eval.getCurrentEnvt().concreteSyntaxType(nt, (IConstructor) Factory.Symbol_Lex.make(vf, vf.string(nt)));
			}
			
			eval.getCurrentEnvt().declareProduction(this);
			return null;
		}
	}
	
	public static class Layout extends org.rascalmpl.ast.SyntaxDefinition.Layout {
		private final IConstructor node;

		public Layout(IConstructor node, Visibility vis, Sym defined,
				Prod production) {
			super(node, vis, defined, production);
			this.node = node;
		}

		@Override
		public IConstructor getTree() {
			return node;
		}
		
		@Override
		public String declareSyntax(Evaluator eval, boolean withImports) {
			Sym type = getDefined();
			IValueFactory vf = eval.getValueFactory();
			
			if (type.isNonterminal()) {
				String nt = ((Nonterminal.Lexical) type.getNonterminal()).getString();
				eval.getCurrentEnvt().concreteSyntaxType(nt, (IConstructor) Factory.Symbol_LayoutX.make(vf, vf.string(nt)));
			}
			
			eval.getCurrentEnvt().declareProduction(this);
			return null;
		}
	}
	
	public static class Keyword extends org.rascalmpl.ast.SyntaxDefinition.Keyword {
		private final IConstructor node;

		public Keyword(IConstructor node, Sym defined, Prod production) {
			super(node, defined, production);
			this.node = node;
		}
		
		@Override
		public IConstructor getTree() {
			return node;
		}

		@Override
		public String declareSyntax(Evaluator eval, boolean withImports) {
			Sym type = getDefined();
			IValueFactory vf = eval.getValueFactory();
			
			if (type.isNonterminal()) {
				String nt = ((Nonterminal.Lexical) type.getNonterminal()).getString();
				eval.getCurrentEnvt().concreteSyntaxType(nt, (IConstructor) Factory.Symbol_Keyword.make(vf, vf.string(nt)));
			}
			
			eval.getCurrentEnvt().declareProduction(this);
			return null;
		}
	}
}
