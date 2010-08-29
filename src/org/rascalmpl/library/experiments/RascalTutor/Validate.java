package org.rascalmpl.library.experiments.RascalTutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.result.Result;

@SuppressWarnings("serial")
public class Validate extends TutorHttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.err.println("Validate, doGet: " + request);
/*		
		String concept = getStringParameter(request, "concept");
		String exercise = getStringParameter(request, "exercise");
		String answer = escapeForRascal(getOptionalStringParameter(request, "answer"));
		String expr = escapeForRascal(getOptionalStringParameter(request,"expr"));	
		String cheat = (escapeForRascal(getOptionalStringParameter(request,"cheat")).equals("")) ? "false" : "true";
		String another = (escapeForRascal(getOptionalStringParameter(request,"another")).equals("")) ? "false" : "true";
		String A = getOptionalStringParameter(request, "A");
		String B = getOptionalStringParameter(request, "B");
		String C = getOptionalStringParameter(request, "C");
		String D = getOptionalStringParameter(request, "D");
		String E = getOptionalStringParameter(request, "E");
		*/
//		Enumeration<String> pnames = request.getParameterNames();
//
//		
//		StringBuffer pmap = new StringBuffer().append("(");
//		boolean first = true;
//		while(pnames.hasMoreElements()){
//			String pname = pnames.nextElement();
//			if(first){
//				first = false;
//			} else {
//				pmap.append(", ");
//			}
//			pmap.append("\"").append(pname).append("\"").append(" : ").
//			     append("\"").append(escapeForRascal(request.getParameter(pname))).append("\"");
//		}
//		pmap.append(")");
//		System.err.println("pmap = " + pmap);
		String pmap = getParametersAsMap(request);
		//response.setContentType("text/plain; UTF-8");
		response.setContentType("text/xml");
		//response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("utf-8");
		System.err.println("Character encoding is: " + response.getCharacterEncoding());
		PrintWriter out = response.getWriter();
		Result<IValue> result = evaluator.eval("validateAnswer(" + pmap + ")", URI.create("stdin:///"));
		
//		Result<IValue> result = evaluator.eval("validateAnswer(\"" + concept + "\",\"" + 
//                exercise + "\",\"" + 
//                answer + "\", \"" +
//                expr + "\", " +
//                cheat + ", " +
//                another + ", \"" +
//                A + "\", \"" +
//                B + "\", \"" +
//                C + "\", \"" +
//                D + "\", \"" +
//                E + "\")",
//                URI.create("stdin:///"));
		System.err.println("Validate gets back: " + ((IString)result.getValue()).getValue());
		out.println(((IString)result.getValue()).getValue());
		out.close();
	}
}
