/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.runtime;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.interpreter.load.SourceLocationListContributor;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.runtime.traverse.Traverse;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.uri.project.TargetURIResolver;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class RascalExecutionContext implements IRascalMonitor {
	private String currentModuleName;
	private $RascalModule module;
	private final IRascalValueFactory $RVF;
	private final Reader inReader;
	private final PrintWriter outwriter;
	private final PrintWriter errwriter;
	private final PathConfig pcfg;
	private final IDEServices ideServices;
	private final Traverse $TRAVERSE;
	private final ModuleStore mstore;
	private final TypeStore $TS;
	private final TypeFactory $TF;
	private final RascalTypeFactory $RTF;
	private IValueFactory $VF;
	private RascalSearchPath rascalSearchPath;

	public RascalExecutionContext(
			Reader inReader,
			PrintWriter outwriter,
			PrintWriter errwriter, 
			PathConfig pcfg, 
			IDEServices ideServices,
			Class<?> clazz
			){
		
		currentModuleName = "UNDEFINED";
		
		this.inReader = inReader;
		this.outwriter = outwriter;
		this.errwriter = errwriter;
		
		this.pcfg = pcfg == null ? new PathConfig() : pcfg;
		this.ideServices = ideServices == null ? new BasicIDEServices(errwriter, this, null) : ideServices;
		$RVF = new RascalRuntimeValueFactory(this);
		$VF = ValueFactoryFactory.getValueFactory();
		$TF = TypeFactory.getInstance();
		$RTF = RascalTypeFactory.getInstance();
		$TRAVERSE = new Traverse($RVF);
		mstore = new ModuleStore();
		$TS = new TypeStore();
		rascalSearchPath = new RascalSearchPath();
		
		ISourceLocation projectRoot = inferProjectRoot(clazz);
	    URIResolverRegistry reg = URIResolverRegistry.getInstance();
	    String projectName = new RascalManifest().getProjectName(projectRoot);
	    if(!projectName.isEmpty()) {
	    	reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
	    	reg.registerLogical(new TargetURIResolver(projectRoot, projectName));
	    }
	    
	    String projectPath =  projectRoot.getPath();
	    String projectsDirPath = projectPath.substring(0, projectPath.length() - projectName.length()-1);
	    
		try {
			ISourceLocation projectsDir = $RVF.sourceLocation(projectRoot.getScheme(), projectRoot.getAuthority(),projectsDirPath);
			String[]entries = URIResolverRegistry.getInstance().listEntries(projectsDir);
			if (entries != null) {
				//System.err.print("INFO adding projects: ");
				for(String entryName : entries) {
					if(entryName.charAt(0) != '.' && !(entryName.equals("pom-parent") || entryName.equals("bin") || entryName.equals("src") || entryName.equals("META-INF"))) {
						ISourceLocation entryRoot = $RVF.sourceLocation(projectsDir.getScheme(), projectsDir.getAuthority(), projectsDir.getPath() + "/" + entryName);
						if(URIResolverRegistry.getInstance().isDirectory(entryRoot)) {
							reg.registerLogical(new ProjectURIResolver(entryRoot, entryName));
							reg.registerLogical(new TargetURIResolver(entryRoot, entryName));
							rascalSearchPath.addPathContributor(new SourceLocationListContributor(entryName, $VF.list(entryRoot)));
							//System.err.print(entryName + " ");
						}
					}
				}
				//System.err.println("");
			}
		} catch (IOException e) {
			return;
		} catch (URISyntaxException e) {
			return;
		}
	}
	
	IRascalValueFactory getRascalRuntimeValueFactory() { return $RVF; }
	
	public Traverse getTraverse() { return $TRAVERSE; }
	
	public Reader getInReader() { return inReader; }
	
	public PrintWriter getOutWriter() { return outwriter; }
	
	public PrintWriter getErrWriter() { return errwriter; }
	
	public PathConfig getPathConfig() { return pcfg; }
	
	public void setModule($RascalModule module) { this.module = module; }
	
	public $RascalModule getModule() { return module; }

	public String getFullModuleName(){ return currentModuleName; }

	public String getFullModuleNameAsPath() { return currentModuleName.replaceAll("::",  "/") + ".rsc"; }

	public void setFullModuleName(String moduleName) { currentModuleName = moduleName; }
	
	public ModuleStore getModuleStore() { return mstore; }
	
	public TypeStore getTypeStore() { return $TS; }
	
	public TypeFactory getTypeFactory() { return $TF; }
	
	public RascalTypeFactory getRascalTypeFactory() { return $RTF; }
	
	public IValueFactory getIValueFactory() { return $VF; }
	
	public RascalSearchPath getRascalSearchPath() { return rascalSearchPath; }



	
	
	@Override
	public int jobEnd(String name, boolean succeeded) {
		errwriter.println(name + " ends");
		return 0;
		//return ideServices.jobEnd(name, succeeded);
	}

	@Override
	public void jobStep(String name, String message, int worked) {
		//errstream.println(name + ": " + message);
		//ideServices.jobStep(name, message, worked);
	}

	@Override
	public void jobStart(String name, int workShare, int totalWork) {
		errwriter.println(name + " starts");
		//ideServices.jobStart(name, workShare, totalWork);
	}


	@Override
	public void jobTodo(String name, int work) {
		//ideServices.jobTodo(name, work);
	}

	@Override
	public boolean jobIsCanceled(String name) {
		errwriter.println(name + " canceled");
		return true;
		//return ideServices.jobIsCanceled(name);
	}

	@Override
	public void warning(String message, ISourceLocation src) {
		errwriter.println(message);
		//ideServices.warning(message,  src);;
	}
	
	 public static ISourceLocation inferProjectRoot(Class<?> clazz) {
	        try {
	            String file = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
	            if (file.endsWith(".jar")) {
	                throw new IllegalArgumentException("can not run Rascal JUnit tests from within a jar file");
	            }

	            File current = new File(file);
	            
	            while (current != null && current.exists() && current.isDirectory()) {
	                if (new File(current, "META-INF/RASCAL.MF").exists()) {
	                    // this is perhaps the copy of RASCAL.MF in a bin/target folder;
	                    // it would be better to find the source RASCAL.MF such that tests
	                    // are run against the sources of test files rather than the ones accidentally
	                    // copied to the bin folder.
	                    
	                    // TODO: if someone knows how to parametrize this nicely instead of hard-coding the
	                    // mvn project setup, I'm all ears. It has to work from both the Eclipse JUnit runner 
	                    // and MVN surefire calling contexts. 
	                    if (current.getName().equals("classes") && current.getParentFile().getName().equals("target")) {
	                        current = current.getParentFile().getParentFile();
	                        continue; // try again in the source folder
	                    }
	                    
	                    return URIUtil.createFileLocation(current.getAbsolutePath());
	                }
	                current = current.getParentFile();
	            }
	        }
	        catch (URISyntaxException e) {
	            System.err.println("[ERROR] can not infer project root:" + e);
	            return null;
	        }
	        
	        return null;
	    }

	@Override
	public void endAllJobs() {
		ideServices.endAllJobs();
	}
}
