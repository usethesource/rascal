/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.tutor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.rascalmpl.interpreter.Evaluator;

public class TutorDefaultHttpServlet extends DefaultServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Evaluator evaluator;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
			super.init();
		} catch (UnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		evaluator = (Evaluator) getServletContext().getAttribute("RascalEvaluator");
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		System.err.println("TutorDefaultHttpServlet, doGet: " + request.getRequestURI());
		String rname = request.getRequestURI();
		if(rname.equals("/"))
			rname = "/index.html";
		
		// Send correct MIME Type to browser
		String mime = getServletContext().getMimeType(rname);
		response.setContentType(mime);
		
		String fname = "courses:///" + rname;
		InputStream in = evaluator.getResolverRegistry().getInputStream(URI.create(fname));
		ServletOutputStream out = response.getOutputStream();
		byte buf[] = new byte[10000];
		while(in.available() > 0){
			int n = in.read(buf);
			out.write(buf, 0, n);
		}
		out.close();
	}
}
