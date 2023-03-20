package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse.Traverse;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.util.PathConfig;
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
	private final InputStream instream;
	private final PrintStream outstream;
	private final PrintWriter outwriter;
	private final PrintStream errstream;
	private final PrintWriter errwriter;
	private final PathConfig pcfg;
	private final IDEServices ideServices;
	private final Traverse $TRAVERSE;
	private final ModuleStore mstore;
	private final TypeStore tstore;
	private final TypeFactory $TF;
	private final RascalTypeFactory $RTF;
	private IValueFactory $VF;

	public RascalExecutionContext(
			InputStream instream,
			PrintStream outstream,
			PrintStream errstream, 
			PathConfig pcfg, 
			IDEServices ideServices,
			Class<?> clazz
			){
		
		currentModuleName = "UNDEFINED";
		
		this.instream = instream;
		this.outstream = outstream;
		this.outwriter = new PrintWriter(outstream);
		this.errstream = errstream;
		this.errwriter = new PrintWriter(errstream);
		
		this.pcfg = pcfg == null ? new PathConfig() : pcfg;
		this.ideServices = ideServices == null ? new BasicIDEServices(errwriter) : ideServices;
		$RVF = new RascalRuntimeValueFactory(this);
		$VF = ValueFactoryFactory.getValueFactory();
		$TF = TypeFactory.getInstance();
		$RTF = RascalTypeFactory.getInstance();
		$TRAVERSE = new Traverse($RVF);
		mstore = new ModuleStore();
		tstore = new TypeStore();
		
		ISourceLocation projectRoot = inferProjectRoot(clazz);
	    URIResolverRegistry reg = URIResolverRegistry.getInstance();
	    String projectName = new RascalManifest().getProjectName(projectRoot);
	    reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
	    reg.registerLogical(new TargetURIResolver(projectRoot, projectName));
	    
	    String projectPath =  projectRoot.getPath();
	    String projectsDirPath = projectPath.substring(0, projectPath.length() - projectName.length()-2);
	    
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
	
	public InputStream getInStream() { return instream; }
	
	public PrintWriter getOutWriter() { return outwriter; }
	
	public PrintStream getOutStream() { return outstream; }

	public PrintWriter getErrWriter() { return errwriter; }
	
	public PrintStream getErrStream() { return errstream; }

	public PathConfig getPathConfig() { return pcfg; }
	
	public void setModule($RascalModule module) { this.module = module; }
	
	public $RascalModule getModule() { return module; }

	public String getFullModuleName(){ return currentModuleName; }

	public String getFullModuleNameAsPath() { return currentModuleName.replaceAll("::",  "/") + ".rsc"; }

	public void setFullModuleName(String moduleName) { currentModuleName = moduleName; }
	
	public ModuleStore getModuleStore() { return mstore; }
	
	public TypeStore getTypeStore() { return tstore; }
	
	public TypeFactory getTypeFactory() { return $TF; }
	
	public RascalTypeFactory getRascalTypeFactory() { return $RTF; }
	
	public IValueFactory getIValueFactory() { return $VF; }
	
	@Override
	public int jobEnd(String name, boolean succeeded) {
		return ideServices.jobEnd(name, succeeded);
	}

	@Override
	public void jobStep(String name, String message, int worked) {
		ideServices.jobStep(name, message, worked);
	}

	@Override
	public void jobStart(String name, int workShare, int totalWork) {
		ideServices.jobStart(name, workShare, totalWork);
	}


	@Override
	public void jobTodo(String name, int work) {
		ideServices.jobTodo(name, work);
	}

	@Override
	public boolean jobIsCanceled(String name) {
		return ideServices.jobIsCanceled(name);
	}

	@Override
	public void warning(String message, ISourceLocation src) {
		ideServices.warning(message,  src);;
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
}
