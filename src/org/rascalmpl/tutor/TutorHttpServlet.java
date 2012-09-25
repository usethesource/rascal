/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.tutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rascalmpl.interpreter.Evaluator;

@SuppressWarnings("serial")
public class TutorHttpServlet extends HttpServlet {
	protected Evaluator evaluator;
	protected String resourceBase;
	protected static boolean debug = false;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		evaluator = (Evaluator) getServletContext().getAttribute("RascalEvaluator");
		resourceBase = (String) getServletContext().getAttribute("ResourceBase");
	}
	
	/** Read a parameter with the specified name, convert it
	   *  to an int, and return it. Return the designated default
	   *  value if the parameter doesn't exist or if it is an
	   *  illegal integer format.
	   */
	  
	  public static int getIntParameter(HttpServletRequest request,
	                                    String paramName,
	                                    int defaultValue) {
	    String paramString = request.getParameter(paramName);
	    int paramValue;
	    try {
	      paramValue = Integer.parseInt(paramString);
	    } catch(Exception nfe) { // null or bad format
	      paramValue = defaultValue;
	    }
	    return(paramValue);
	  }
	  
	  /** Read a parameter with the specified name and return it. Return the designated default
	   *  value if the parameter doesn't exist or if it is an
	   *  illegal integer format.
	   * @throws ServletException 
	   */
	  
	  public static String getStringParameter(HttpServletRequest request,
	                                    String paramName) throws ServletException {
	    String paramString = request.getParameter(paramName);
	    if(paramString == null)
	       throw new ServletException(paramName + " is missing or empty, page was probably damaged");
	    if(debug) System.err.println("StringParameter " + paramName + " = " + paramString);
	    return paramString;
	  }
	  
	  public static String getOptionalStringParameter(HttpServletRequest request,
              String paramName) {
		  String paramString = request.getParameter(paramName);
		  if(debug) System.err.println("StringParameter " + paramName + " = " + paramString);
		  return (paramString == null) ? "" : paramString;
	}
	  
	  @SuppressWarnings("unchecked")
	public static String getParametersAsMap(HttpServletRequest request){
		  Enumeration<String> pnames = request.getParameterNames();

		  StringBuffer pmap = new StringBuffer().append("(");
		  boolean first = true;
		  while(pnames.hasMoreElements()){
			  String pname = pnames.nextElement();
			  String pvalue = escapeForRascal(request.getParameter(pname));
			  pname = escapeForRascal(pname);
			  if(first){
				  first = false;
			  } else {
				  pmap.append(", ");
			  }
			  pmap.append("\"").append(pname).append("\"").append(" : ").
			  append("\"").append(pvalue).append("\"");
		  }
		  pmap.append(")");
		  if(debug) System.err.println("pmap = " + pmap);
		  return pmap.toString();
	  }
	  
	  /** Escape characters that have special meaning in a Rascal string.
	   *  Specifically, given a string, this method replaces all 
	   *  occurrences of  
	   *  {@literal
	   *  '<' with '\<', all occurrences of '>' with
	   *  '\>', and (to handle cases that occur inside attribute
	   *  values), all occurrences of double quotes with
	   *  '\"' and the escape character itself.
	   *  Without such filtering, an arbitrary string
	   *  could not safely be inserted in a Rascal expression.
	   *  }
	   */

	  public static String escapeForRascal(String input) {
	    if (!hasSpecialRascalChars(input)) {
	      return(input);
	    }
	    StringBuilder filtered = new StringBuilder(input.length());
	    char c;
	    for(int i=0; i<input.length(); i++) {
	      c = input.charAt(i);
	      switch(c) {
	        case '<': filtered.append("\\<"); break;
	        case '>': filtered.append("\\>"); break;
	        case '"': filtered.append("\\\""); break;
	        case '\'': filtered.append("\\'"); break;
	        case '\\': filtered.append("\\\\"); break;
	        
	        default: filtered.append(c);
	      }
	    }
	    return(filtered.toString());
	  }

	  private static boolean hasSpecialRascalChars(String input) {
	    boolean flag = false;
	    if ((input != null) && (input.length() > 0)) {
	      char c;
	      for(int i=0; i<input.length(); i++) {
	        c = input.charAt(i);
	        switch(c) {
	          case '<': flag = true; break;
	          case '>': flag = true; break;
	          case '"': flag = true; break;
	          case '\'': flag = true; break;
	          case '\\': flag = true; break;
	        }
	      }
	    }
	    return(flag);
	  }
	  
	  /** Escape characters that have special meaning in a Rascal string.
	   *  Specifically, given a string, this method replaces all 
	   *  occurrences of  
	   *  {@literal
	   *  '<' with '\<', all occurrences of '>' with
	   *  '\>', and (to handle cases that occur inside attribute
	   *  values), all occurrences of double quotes with
	   *  '\"' and the escape character itself.
	   *  Without such filtering, an arbitrary string
	   *  could not safely be inserted in a Rascal expression.
	   *  }
	   */

	  public static String escapeForHtml(String input) {
	    if (!hasSpecialHtmlChars(input)) {
	      return(input);
	    }
	    StringBuilder filtered = new StringBuilder(input.length());
	    char c;
	    for(int i=0; i<input.length(); i++) {
	      c = input.charAt(i);
	      switch(c) {
	        case '<': filtered.append("&lt;"); break;
	        case '>': filtered.append("&gt;"); break;
	        case '"': filtered.append("&quot;"); break;
	        case '&': filtered.append("&amp;"); break;
	        
	        default: filtered.append(c);
	      }
	    }
	    return(filtered.toString());
	  }

	  private static boolean hasSpecialHtmlChars(String input) {
	    boolean flag = false;
	    if ((input != null) && (input.length() > 0)) {
	      char c;
	      for(int i=0; i<input.length(); i++) {
	        c = input.charAt(i);
	        switch(c) {
	          case '<': flag = true; break;
	          case '>': flag = true; break;
	          case '"': flag = true; break;
	          case '&': flag = true; break;
	        }
	      }
	    }
	    return(flag);
	  }

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println(request.getRequestURI() + ": Unexpected doGet");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println(request.getRequestURI() + ": Unexpected doPost");
		out.close();
	}
}
