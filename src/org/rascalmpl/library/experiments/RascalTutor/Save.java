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
public class Save extends TutorHttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.err.println("Save, doPost: " + request.getRequestURI());
		
		String concept = getStringParameter(request, "concept");
		String newContent = escapeForRascal(getStringParameter(request, "newcontent"));
		boolean newConcept = getStringParameter(request, "new").equals("true");

		Result<IValue> result = evaluator.eval("save(\"" + concept + "\",\"" + newContent + "\"," + newConcept + ")", URI.create("stdin:///"));

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		String resp = ((IString) result.getValue()).getValue();
		out.println(resp);
		out.close();
	}
}
