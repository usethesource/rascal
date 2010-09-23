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
		
		Result<IValue> result = evaluator.eval("start(\"" + name + "\")", URI.create("stdin:///"));
		out.println(((IString) result.getValue()).getValue());
		out.close();
		//System.err.println("ShowConcept, " + ((IString) result.getValue()).getValue());
	}
}
