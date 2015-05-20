/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.Nonterminal;
import org.rascalmpl.ast.Prod;
import org.rascalmpl.ast.Start;
import org.rascalmpl.ast.Sym;
import org.rascalmpl.ast.Visibility;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.values.uptr.Factory;

/**
 * This class dispatches over different kinds of syntax definitions to make sure non-terminals are declared in the right class
 * (sort, lex, keywords, layout).
 */
public abstract class SyntaxDefinition extends
		org.rascalmpl.ast.SyntaxDefinition {
	
	public SyntaxDefinition(ISourceLocation src, IConstructor node) {
		super(src, node);
	}

	public static class Language extends org.rascalmpl.ast.SyntaxDefinition.Language {
		private final IConstructor node;

		public Language(ISourceLocation src, IConstructor node, Start start, Sym defined,
				Prod production) {
			super(src, node, start, defined, production);
			this.node = node;
		}

		@Override
		public IConstructor getTree() {
			return node;
		}
		
		@Override
		public Object clone() {
			return new Language(src, node, clone(getStart()), clone(getDefined()), clone(getProduction()));
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			Sym type = getDefined();
			IValueFactory vf = eval.getValueFactory();
			
			if (type.isNonterminal()) {
				String nt = ((Nonterminal.Lexical) type.getNonterminal()).getString();
				eval.getCurrentEnvt().concreteSyntaxType(nt, vf.constructor(Factory.Symbol_Sort, vf.string(nt)));
			}
			
			eval.getCurrentModuleEnvironment().declareProduction(getTree());
			return null;
		}
	}
	
	public static class Lexical extends org.rascalmpl.ast.SyntaxDefinition.Lexical {
		private final IConstructor node;

		public Lexical(ISourceLocation src, IConstructor node, Sym defined, Prod production) {
			super(src, node, defined, production);
			this.node = node;
		}

		@Override
		public IConstructor getTree() {
			return node;
		}
		
		
		@Override
		public Object clone() {
			return new Lexical(src, node, clone(getDefined()), clone(getProduction()));
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
		  Sym type = getDefined();
      IValueFactory vf = eval.getValueFactory();
      
      if (type.isNonterminal()) {
        String nt = ((Nonterminal.Lexical) type.getNonterminal()).getString();
        eval.getCurrentEnvt().concreteSyntaxType(nt, vf.constructor(Factory.Symbol_Sort, vf.string(nt)));
      }
      
      eval.getCurrentModuleEnvironment().declareProduction(getTree());
      return null;
		}
	}
	
	public static class Layout extends org.rascalmpl.ast.SyntaxDefinition.Layout {
		private final IConstructor node;

		public Layout(ISourceLocation src, IConstructor node, Visibility vis, Sym defined,
				Prod production) {
			super(src, node, vis, defined, production);
			this.node = node;
		}

		@Override
		public IConstructor getTree() {
			return node;
		}
		
		@Override
		public Object clone() {
			return new Layout(src, node, clone(getVis()), clone(getDefined()), clone(getProduction()));
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
		  Sym type = getDefined();
      IValueFactory vf = eval.getValueFactory();
      
      if (type.isNonterminal()) {
        String nt = ((Nonterminal.Lexical) type.getNonterminal()).getString();
        eval.getCurrentEnvt().concreteSyntaxType(nt, vf.constructor(Factory.Symbol_Sort, vf.string(nt)));
      }
      
      eval.getCurrentModuleEnvironment().declareProduction(getTree());
      return null;
		}
	}
	
	public static class Keyword extends org.rascalmpl.ast.SyntaxDefinition.Keyword {
		private final IConstructor node;

		public Keyword(ISourceLocation src, IConstructor node, Sym defined, Prod production) {
			super(src, node, defined, production);
			this.node = node;
		}
		
		@Override
		public IConstructor getTree() {
			return node;
		}
		
		@Override
		public Object clone() {
			return new Keyword(src, node, clone(getDefined()), clone(getProduction()));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			Sym type = getDefined();
			IValueFactory vf = eval.getValueFactory();
			
			if (type.isNonterminal()) {
				String nt = ((Nonterminal.Lexical) type.getNonterminal()).getString();
				eval.getCurrentEnvt().concreteSyntaxType(nt, vf.constructor(Factory.Symbol_Keyword, vf.string(nt)));
			}
			
			eval.getCurrentModuleEnvironment().declareProduction(getTree());
			return null;
		}
	}
}
