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
import org.eclipse.imp.pdb.facts.IValueFactory;

@SuppressWarnings("serial")
public class Save extends TutorHttpServlet {

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if(debug) System.err.println("Save, doGet: " + request.getRequestURI());
		
		String concept = getStringParameter(request, "concept");
		String newContent = getStringParameter(request, "newcontent");
		boolean newConcept = getStringParameter(request, "new").equals("true");
		String resp;

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();

		try {
			IValueFactory vf = evaluator.getValueFactory();
			IValue result = evaluator.call("save", vf.string(concept), vf.string(newContent), vf.bool(newConcept));
		    resp = ((IString) result).getValue();
		    
		    if (resp.startsWith("<!DOCTYPE")) {
				out.println("<responses><response id=\"replacement\">" + escapeForHtml(resp) + "</response></responses>");
		    }
		    else {
				out.println("<responses><response id=\"error\">" + escapeForHtml(resp) + "</response></responses>");
		    }
			
		}
		catch (Throwable e) {
			out.println("<responses><response id=\"error\">" + escapeForHtml(e.getMessage()));
			e.printStackTrace(out);
			out.println("</response></responses>");
		}
		finally {
			out.close();
		}
	}
}
