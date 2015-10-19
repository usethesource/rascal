/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import org.rascalmpl.ast.PathPart;
import org.rascalmpl.ast.ProtocolPart;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;

public abstract class LocationLiteral extends org.rascalmpl.ast.LocationLiteral {

	static public class Default extends
			org.rascalmpl.ast.LocationLiteral.Default {

		public Default(ISourceLocation __param1, IConstructor tree, ProtocolPart __param2, PathPart __param3) {
			super(__param1, tree, __param2, __param3);
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

	public LocationLiteral(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}

}
