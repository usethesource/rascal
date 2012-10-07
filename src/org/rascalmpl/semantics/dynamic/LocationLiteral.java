/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.PathPart;
import org.rascalmpl.ast.ProtocolPart;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.uri.URIUtil;

public abstract class LocationLiteral extends org.rascalmpl.ast.LocationLiteral {

	static public class Default extends
			org.rascalmpl.ast.LocationLiteral.Default {

		public Default(IConstructor __param1, ProtocolPart __param2, PathPart __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			Result<IValue> protocolPart = this.getProtocolPart().interpret(
					__eval);
			Result<IValue> pathPart = this.getPathPart().interpret(__eval);

			String uri = ((IString) protocolPart.getValue()).getValue() + "://"
					+ ((IString) pathPart.getValue()).getValue();

			try {
				URI url = URIUtil.createFromEncoded(uri);
				ISourceLocation r = __eval.__getVf().sourceLocation(url);
				return org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(org.rascalmpl.interpreter.Evaluator
								.__getTf().sourceLocationType(), r, __eval);
			} catch (URISyntaxException e) {
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
						.malformedURI(uri, this, __eval.getStackTrace());
			}

		}

	}

	public LocationLiteral(IConstructor __param1) {
		super(__param1);
	}

}
