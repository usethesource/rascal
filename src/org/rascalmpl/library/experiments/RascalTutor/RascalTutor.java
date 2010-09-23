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
	
	final static String BASE = System.getProperty("user.dir") +
	                           "/src/org/rascalmpl/library/experiments/RascalTutor/";
	
	public static void main(String[] args) throws Exception
	    {
		 	try {
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
		        
		        System.err.println("context path = " + context.getContextPath());
		        System.err.println("resource base = " + context.getResourceBase());
		        
		        //We will create our server running at http://localhost:8081
		        Server server = new Server(8081);
		        server.setHandler(context);
		        //server.setHandler(wac);
		        server.start();
			} catch (Exception e) {
				System.err.println("Cannot set up RascalTutor: " + e.getMessage());
			}
		 	

	    }
}
