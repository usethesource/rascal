package org.rascalmpl.library.experiments.RascalTutor;

import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.parser.LegacyRascalParser;
import org.rascalmpl.values.ValueFactoryFactory;

public class RascalTutor {
	
	private static org.rascalmpl.interpreter.Evaluator getRascalEvaluator(String tutorModule){
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("*** TUTOR ***"));
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);
		Evaluator eval = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, new LegacyRascalParser(), root, heap);
		eval.eval("import " + tutorModule + ";", URI.create("stdin:///"));
		return eval;
	}
	
	final static String BASE = // System.getProperty("user.dir") +
	                           "/Users/jurgenv/Sources/Rascal/rascal/src/org/rascalmpl/library/experiments/RascalTutor/";
	private Server server;
	
	public void start(final int port) throws Exception {
		server = new Server(port);
		server.setHandler(getTutorHandler());
		server.start();
	}
	
	public void stop() throws Exception {
		if (server != null) {
			server.stop();
		}
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

	private static ServletContextHandler getTutorHandler() {
		Evaluator evaluator = getRascalEvaluator("experiments::RascalTutor::CourseManager");
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setAttribute("RascalEvaluator", evaluator);
		context.addServlet(DefaultServlet.class, "/");
		context.addServlet(Show.class, "/show");
		context.addServlet(Validate.class, "/validate");
		context.addServlet(Eval.class, "/eval");
		context.addServlet(Search.class, "/search");
		context.addServlet(Category.class, "/category");
		context.addServlet(Edit.class, "/edit");
		context.addServlet(Save.class, "/save");
		context.addServlet(Start.class, "/start");

		System.err.println("BASE = " + BASE);
		context.setResourceBase(BASE); 
     
		String welcome[] = { BASE + "index.html"};
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
