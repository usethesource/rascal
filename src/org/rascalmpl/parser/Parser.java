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

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.iguana.grammar.Grammar;
import org.iguana.grammar.symbol.Nonterminal;
import org.iguana.parser.GLLParser;
import org.iguana.parser.ParseError;
import org.iguana.parser.ParseResult;
import org.iguana.parser.ParserFactory;
import org.iguana.util.Configuration;
import org.iguana.util.Input;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;


public class Parser {
	public static final String START_COMMAND = "start__Command";
	public static final String START_COMMANDS = "start__Commands";
	public static final String START_MODULE = "start__Module";
	private final IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private Grammar rascalGrammar;
	private final List<ClassLoader> loaders;
	
	public Parser(List<ClassLoader> loaders) {
		assert loaders != null;
		this.loaders = loaders;
	}
	
	public Grammar getRascalGrammar() {
		return rascalGrammar;
	}
	
	public IConstructor parseModule(char[] data, URI location) {
		// TODO: mind the filtering actions :-)
		rascalGrammar = initRascalGrammar();
  		return parseObject(rascalGrammar, "start[Module]", data, location);
	}
	
	public IConstructor parseObject(Grammar grammar, String nt, char[] data, URI location) {
		Input input = Input.fromCharArray(data, location);
		
  		GLLParser parser = ParserFactory.getParser(Configuration.DEFAULT, input, grammar);
		ParseResult result = parser.parse(input, grammar, Nonterminal.withName(nt));

  		if (result.isParseError()) {
  			ParseError e = result.asParseError();
			throw RuntimeExceptionFactory.parseError(vf.sourceLocation(vf.sourceLocation(location), 
																	   e.getInputIndex(), 
																	   1,
																	   input.getLineNumber(e.getInputIndex()),
																	   input.getLineNumber(e.getInputIndex()),
																	   input.getColumnNumber(e.getInputIndex()) - 1,
																	   input.getColumnNumber(e.getInputIndex()) - 1), null, null);
		}

  		// TODO: parse tree builder has to call rascal normalization/filtering functions
		return null; // result.asParseSuccess().build(new ModelBuilderVisitor<>(input, new ParsetreeBuilder(), grammar));
	}

	private Grammar initRascalGrammar() {
		if (rascalGrammar != null) {
			return rascalGrammar;
		}
		
		try {
			IConstructor g = (IConstructor) new StandardTextReader().read(vf, URIResolverRegistry.getInstance().getCharacterReader(URIUtil.correctLocation("std", "", "/lang/rascal/syntax/Rascal.grammar")));
			return new RascalToIguanaGrammarConverter().convert("rascal-bootstrap", g);
		} catch (FactTypeUseException | IOException e1) {
			assert false;
			throw new ImplementationError("can not get bootstrap parser", e1);
		}
		
	}

}
