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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

@SuppressWarnings("serial")
public class Compile extends TutorHttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		if(debug) System.err.println("Compile, doGet: " + request.getRequestURI());
		String name = getStringParameter(request, "name");
		PrintWriter out = response.getWriter();
		
		try {
			IValueFactory vf = evaluator.getValueFactory();
			IValue result = evaluator.call("compile", vf.string(name));
			out.println(((IString) result).getValue());
		}
		catch (Throwable e) {
			out.println(escapeForHtml(e.getMessage()));
			e.printStackTrace(out);
		}
		finally {
			out.close();
		}
	}
}
