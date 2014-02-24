/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
package org.rascalmpl.tutor;

import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.uri.ClassResourceInput;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

public class RascalTutor {
	private final Evaluator eval;
  private ISourceLocation server;
	
	public RascalTutor() {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("___TUTOR___", heap));
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);
		eval = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, root, heap);
	  eval.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
	 
	  URIResolverRegistry reg = eval.getResolverRegistry();
    reg.registerInput(new ClassResourceInput(reg, "courses", eval.getClass(), "/org/rascalmpl/courses"));
    eval.addRascalSearchPath(URIUtil.rootScheme("tutor"));
    eval.addRascalSearchPath(URIUtil.rootScheme("courses"));
    
    for (final String lib : new String[] { "rascal", "rascal-eclipse" }) {
      final String courseSrc = System.getProperty("rascal.courses.lib." + lib);
    
      if (courseSrc != null) {
        FileURIResolver fileURIResolver = new FileURIResolver() {
          @Override
          public String scheme() {
            return "clib-" + lib;
          }

          @Override
          protected String getPath(URI uri) {
            String path = uri.getPath();
            return courseSrc + (path.startsWith("/") ? path : ("/" + path));
          }
        };
        
        reg.registerInputOutput(fileURIResolver);
        eval.addRascalSearchPath(URIUtil.rootScheme("clib-" + lib));
      }
    }
	}
	
	public org.rascalmpl.interpreter.Evaluator getRascalEvaluator() {
		return eval;
	}
	
	public void start(IRascalMonitor monitor) throws Exception {
		monitor.startJob("Loading Course Manager");
		eval.doImport(monitor, "TutorWebserver");
		server = (ISourceLocation) call("startTutor", new IValue[] { });
	}
	
	public void stop() throws Exception {
		if (server != null) {
			call("stopTutor", new IValue[] { server });
		}
	}
	
	private IValue call(String func, IValue[] args) {
	  return eval.call(func, "TutorWebserver", null, args);
	}
	
	public URI getServer() {
	  return server.getURI();
	}
	
	public static void main(String[] args) {
		RascalTutor tutor = new RascalTutor();
		try {
			tutor.start(new NullRascalMonitor());
		}
		catch (Exception e) {
			System.err.println("Cannot set up RascalTutor: " + e.getMessage());
		}
	}
}
