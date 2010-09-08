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

		String pmap = getParametersAsMap(request);
		//response.setContentType("text/plain; UTF-8");
		response.setContentType("text/xml");
		//response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		Result<IValue> result = evaluator.eval("validateAnswer(" + pmap + ")", URI.create("stdin:///"));

		System.err.println("Validate gets back: " + ((IString)result.getValue()).getValue());
		out.println(((IString)result.getValue()).getValue());
		out.close();
	}
}
