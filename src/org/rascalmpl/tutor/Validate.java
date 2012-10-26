/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.tutor;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.uri.URIUtil;

@SuppressWarnings("serial")
public class Validate extends TutorHttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(debug)System.err.println("Validate, doGet: " + request);

		String pmap = getParametersAsMap(request);
		//response.setContentType("text/plain; UTF-8");
		response.setContentType("text/xml");
		//response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		Result<IValue> result = evaluator.eval(null, "validateAnswer(" + pmap + ")", URIUtil.rootScheme("stdin"));
		if(debug) {
			System.err.println("Validate gets back: " + ((IString)result.getValue()).getValue());
		}
		out.println(((IString)result.getValue()).getValue());
		out.close();
	}
}

