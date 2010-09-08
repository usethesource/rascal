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
public class Edit extends TutorHttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.err.println("Edit, doGet: " + request.getRequestURI() + "?" + request.getQueryString());
		String concept = getStringParameter(request, "concept");
		boolean newConcept = getStringParameter(request, "new").equals("true");
		boolean check = getStringParameter(request, "check").equals("true");
		
		Result<IValue> result = evaluator.eval("edit(\"" + concept + "\"," + newConcept + "," + check + ")", URI.create("stdin:///"));

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		String resp = ((IString) result.getValue()).getValue();
		out.println(resp);
		out.close();
		//System.err.println("Returns: " + resp);
	}

}
