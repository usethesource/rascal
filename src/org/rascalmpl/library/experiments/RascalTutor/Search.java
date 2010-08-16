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
public class Search extends TutorHttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.err.println("Search, doGet: " + request.getRequestURI() + request.getQueryString());
		String term = escapeForRascal(getStringParameter(request,"term"));
		
		PrintWriter out = response.getWriter();
		Result<IValue> result = evaluator.eval("search(\"" + term + "\")", URI.create("stdin:///"));

		out.println(((IString) result.getValue()).getValue());
		out.close();
	}
}
