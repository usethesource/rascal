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
import java.io.InputStream;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rascalmpl.uri.URIUtil;

@SuppressWarnings("serial")
public class Show extends TutorHttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		if(debug) System.err.println("ShowConcept, doGet: " + request.getRequestURI());
		if(debug) System.err.println("ShowConcept, resourceBase: " + resourceBase);
		String concept = getStringParameter(request, "concept");
		int i = concept.lastIndexOf("/");
		String baseName = i > 0 ? concept.substring(i) : concept;
		if(!baseName.endsWith(".html"))
			baseName += ".html";
		String fileName = resourceBase + concept + "/" + baseName;
		if(debug) System.err.println("ShowConcept, fileName: " + fileName);
		// this code should not be doing string concat, but using the correct URIUtil.create overload
		InputStream in = evaluator.getResolverRegistry().getInputStream(URIUtil.assumeCorrect(fileName));
		ServletOutputStream out = response.getOutputStream();
		
		try {
			byte buf[] = new byte[10000];
			while(in.available() > 0){
				int n = in.read(buf);
				out.write(buf, 0, n);
			}
		}

		finally {
			out.close();
		}
	}
}
