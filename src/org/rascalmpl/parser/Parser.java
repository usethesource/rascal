/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.URI;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.jgll.grammar.GrammarGraph;
import org.jgll.parser.GLLParser;
import org.jgll.parser.ParseError;
import org.jgll.parser.ParserFactory;
import org.jgll.sppf.NonterminalSymbolNode;
import org.jgll.traversal.ModelBuilderVisitor;
import org.jgll.util.Input;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;


public class Parser {
	public static final String START_COMMAND = "start__Command";
	public static final String START_COMMANDS = "start__Commands";
	public static final String START_MODULE = "start__Module";
	private final IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private GrammarGraph rascalGrammar;
	private final List<ClassLoader> loaders;
	
	public Parser(List<ClassLoader> loaders) {
		assert loaders != null;
		this.loaders = loaders;
	}
	
	public GrammarGraph getRascalGrammar() {
		return rascalGrammar;
	}
	
	public IConstructor parseModule(char[] data, URI location) {
		// TODO: mind the filtering actions :-)
		rascalGrammar = initRascalGrammar();
  		return parseObject(rascalGrammar, "start[Module]", data, location);
	}
	
	public IConstructor parseObject(GrammarGraph grammar, String nt, char[] data, URI location) {
		Input input = Input.fromCharArray(data, location);
  		GLLParser parser = ParserFactory.newParser(grammar, input);
  		
  		NonterminalSymbolNode sppf;
  		
  		try {
  			sppf = parser.parse(input, grammar, nt);
		} catch (ParseError e) {
			throw RuntimeExceptionFactory.parseError(vf.sourceLocation(vf.sourceLocation(location), 
																	   e.getInputIndex(), 
																	   1,
																	   input.getLineNumber(e.getInputIndex()),
																	   input.getLineNumber(e.getInputIndex()),
																	   input.getColumnNumber(e.getInputIndex()) - 1,
																	   input.getColumnNumber(e.getInputIndex()) - 1), null, null);
		}

  		// TODO: parse tree builder has to call rascal normalization/filtering functions
		sppf.accept(new ModelBuilderVisitor<>(input, new ParsetreeBuilder(), grammar));

		return ((org.jgll.traversal.Result<IConstructor>) sppf.getObject()).getObject();
	}

	private GrammarGraph initRascalGrammar() {
		if (rascalGrammar != null) {
			return rascalGrammar;
		}
		
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(getClass().getResourceAsStream("/org/rascalmpl/library/lang/rascal/syntax/RascalGrammar.igr"))) {

				@Override
				protected Class< ? > resolveClass(ObjectStreamClass desc) throws ClassNotFoundException, IOException {

					for (ClassLoader cl : loaders) {
						try {
							return  cl.loadClass(desc.getName());
						}
						catch (ClassNotFoundException e) {
							// continue search
						} catch (Exception e) {
							throw new ImplementationError("failed to load Rascal grammar", e);
						}
					}

					// Fallback (for void and primitives)
					return super.resolveClass(desc);
				}
			};
			
			return (GrammarGraph) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			throw new ImplementationError("failed to load Rascal grammar", e);
		}
	}
	
}
