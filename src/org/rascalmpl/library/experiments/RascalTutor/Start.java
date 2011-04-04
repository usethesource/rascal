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
package org.rascalmpl.library.experiments.RascalTutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.result.Result;

@SuppressWarnings("serial")
public class Start extends TutorHttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		System.err.println("Start, doGet: " + request.getRequestURI());
		String name = escapeForRascal(getStringParameter(request, "name"));
		PrintWriter out = response.getWriter();
		
		String serverName = request.getServerName() + ":" + request.getLocalPort();
		
		System.err.println("Start, localName = " + request.getServerName() + ", port = " + request.getLocalPort());
		
		Result<IValue> result = evaluator.eval(null, "start(\"" + serverName  + "\", \"" + name + "\")", URI.create("stdin:///"));
		out.println(((IString) result.getValue()).getValue());
		out.close();
		//System.err.println("ShowConcept, " + ((IString) result.getValue()).getValue());
	}
}
