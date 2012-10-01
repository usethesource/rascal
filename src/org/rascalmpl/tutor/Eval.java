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
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.uri.URIUtil;

@SuppressWarnings("serial")
public class Eval extends TutorHttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if(debug)System.err.println("EvalExpr, doGet: " + request.getRequestURI() + "?" + request.getQueryString());
		
		String expr = getStringParameter(request,"expr");
		PrintWriter out = response.getWriter();

		try {
			Result<IValue> result = evaluator.eval(null, expr, URIUtil.rootScheme("stdin"));
			String resp = "<tt>" + result.getValue().toString() + "</tt>";
			out.println(resp);
		}
		catch (Throwable e) {
			out.println(escapeForHtml(e.getMessage()));
			e.printStackTrace(out);
		}
		finally {
			out.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();		                
		out.println("EvalExpr: Unexpected post request" + request.getRemoteUser());
		out.close();
	}

}
