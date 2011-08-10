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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.experiments.RascalTutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;

public class RascalTutor {
	private final Evaluator eval;

	public RascalTutor() {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("___TUTOR___", heap));
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);
		eval = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, root, heap);
	}
	
	public org.rascalmpl.interpreter.Evaluator getRascalEvaluator() {
		return eval;
	}
	
	final static String BASE = "std:///experiments/RascalTutor/";
	private Server server;
	
	public void start(final int port) throws Exception {
		eval.eval(null, "import " + "experiments::RascalTutor::CourseManager" + ";", URI.create("stdin:///"));
		server = new Server();
		
		SelectChannelConnector connector=new SelectChannelConnector();
		connector.setPort(port);
		connector.setMaxIdleTime(30000);
		connector.setHeaderBufferSize(1000*1000);
		connector.setRequestBufferSize(1000*1000);
		connector.setConfidentialPort(8443);
		server.setConnectors(new Connector[]{connector});
		
		server.setHandler(getTutorHandler());
		server.start();
	}
	
	public void stop() throws Exception {
		if (server != null) {
			server.stop();
		}
	}
	
	public URIResolverRegistry getResolverRegistry() {
		return eval.getResolverRegistry();
	}
	
	public static void main(String[] args) {
		RascalTutor tutor = new RascalTutor();
		try {
			tutor.start(8081);
		}
		catch (Exception e) {
			System.err.println("Cannot set up RascalTutor: " + e.getMessage());
		}
	}

	private ServletContextHandler getTutorHandler() throws IOException {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setAttribute("RascalEvaluator", eval);
		context.addServlet(TutorDefaultHttpServlet.class, "/");
		context.addServlet(Show.class, "/show");
		context.addServlet(Validate.class, "/validate");
		context.addServlet(Eval.class, "/eval");
		context.addServlet(Search.class, "/search");
		context.addServlet(Category.class, "/category");
		context.addServlet(Edit.class, "/edit");
		context.addServlet(Save.class, "/save");
		context.addServlet(Start.class, "/start");

		System.err.println("BASE = " + BASE);
		
		URI baseURI = getResolverRegistry().getResourceURI(URI.create(BASE));
		
		System.err.println("resourceBase = " + baseURI);
		context.setResourceBase(baseURI.toASCIIString()); 
     
		String welcome[] = { BASE + "Courses/index.html"};
		context.setWelcomeFiles(welcome);
		
/*		        
		WebAppContext wac = new WebAppContext();
		
		wac.setAttribute("RascalEvaluator", evaluator);
		wac.addServlet(DefaultServlet.class, "/");
		wac.addServlet(ShowConcept.class, "/concept");
		wac.addServlet(ValidateAnswer.class, "/ValidateAnswer");
		wac.setResourceBase(BASE); 
		
		MimeTypes mimeTypes = new MimeTypes();
		mimeTypes.addMimeMapping(".css", "text/css");
		wac.setMimeTypes(mimeTypes);
*/
		return context;
	}
}
