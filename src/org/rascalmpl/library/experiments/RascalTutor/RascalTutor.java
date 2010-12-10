package org.rascalmpl.library.experiments.RascalTutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.jetty.server.Server;
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
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("*** TUTOR ***"));
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
		eval.eval("import " + "experiments::RascalTutor::CourseManager" + ";", URI.create("stdin:///"));
		server = new Server(port);
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
