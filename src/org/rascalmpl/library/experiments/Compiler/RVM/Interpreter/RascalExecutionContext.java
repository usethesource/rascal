package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.interpreter.DefaultTestResultListener;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.load.IRascalSearchPathContributor;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.load.URIContributor;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.uri.CWDURIResolver;
import org.rascalmpl.uri.ClassResourceInput;
import org.rascalmpl.uri.CompressedStreamResolver;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.uri.HomeURIResolver;
import org.rascalmpl.uri.HttpURIResolver;
import org.rascalmpl.uri.HttpsURIResolver;
import org.rascalmpl.uri.ISourceLocationInputOutput;
import org.rascalmpl.uri.JarURIResolver;
import org.rascalmpl.uri.TempURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

/**
 * Provide all context information that is needed during the execution of a compiled Rascal program
 *
 */
public class RascalExecutionContext implements IRascalMonitor {

	private IRascalMonitor monitor;
	private final PrintWriter stderr;
	private final Configuration config;
	private final List<ClassLoader> classLoaders;
	private final PrintWriter stdout;
	//private final IEvaluatorContext ctx;
	private final IValueFactory vf;
	private final TypeStore typeStore;
	private final boolean debug;
	private final boolean testsuite;
	private final boolean profile;
	private final boolean trackCalls;
	private final ITestResultListener testResultListener;
	private final IMap symbol_definitions;
	private RascalSearchPath rascalSearchPath;
	
	private String currentModuleName;
	//private ILocationCollector locationReporter;
	private RVM rvm;
	private boolean coverage;
	private boolean useJVM;
	private final IMap moduleTags;
	
	public RascalExecutionContext(
			IValueFactory vf, 
			PrintWriter stdout, 
			PrintWriter stderr, 
			IMap moduleTags, 
			IMap symbol_definitions, 
			TypeStore typeStore, 
			boolean debug, 
			boolean testsuite, 
			boolean profile, 
			boolean trackCalls, 
			boolean coverage, 
			boolean useJVM, 
			ITestResultListener testResultListener
	){
		
		this.vf = vf;
		this.moduleTags = moduleTags;
		this.symbol_definitions = symbol_definitions;
		this.typeStore = typeStore;
		this.debug = debug;
		this.testsuite = testsuite;
		this.profile = profile;
		this.coverage = coverage;
		this.useJVM = useJVM;
		this.trackCalls = trackCalls;
		
		currentModuleName = "UNDEFINED";
		
		this.rascalSearchPath = new RascalSearchPath();
		registerCommonSchemes();
	
		monitor = new ConsoleRascalMonitor(); //ctx.getEvaluator().getMonitor();
		this.stdout = stdout;
		this.stderr = stderr;
		config = new Configuration();
		this.classLoaders = new ArrayList<ClassLoader>(Collections.singleton(Evaluator.class.getClassLoader()));
		this.testResultListener = (testResultListener == null) ? (ITestResultListener) new DefaultTestResultListener(stderr)
															  : testResultListener;
	}

	IValueFactory getValueFactory(){ return vf; }
	
	public IMap getSymbolDefinitions() { return symbol_definitions; }
	
	public TypeStore getTypeStore() { 
		return typeStore; 
	}
	
	boolean getDebug() { return debug; }
	
	boolean getTestSuite() { return testsuite; }
	
	boolean getProfile(){ return profile; }
	
	boolean getCoverage(){ return coverage; }
	
	boolean getUseJVM() { return useJVM; }
	
	boolean getTrackCalls() { return trackCalls; }
	
	public RVM getRVM(){ return rvm; }
	
	void setRVM(RVM rvm){ this.rvm = rvm; }
	
	public void addClassLoader(ClassLoader loader) {
		// later loaders have precedence
		classLoaders.add(0, loader);
	}
	
	List<ClassLoader> getClassLoaders() { return classLoaders; }
	
	IRascalMonitor getMonitor() {return monitor;}
	
	void setMonitor(IRascalMonitor monitor) {
		this.monitor = monitor;
	}
	
	public PrintWriter getStdErr() { return stderr; }
	
	public PrintWriter getStdOut() { return stdout; }
	
	Configuration getConfiguration() { return config; }
	
	//IEvaluatorContext getEvaluatorContext() { return ctx; }
	
	ITestResultListener getTestResultListener() { return testResultListener; }
	
	public String getCurrentModuleName(){ return currentModuleName; }
	
	public void setCurrentModuleName(String moduleName) { currentModuleName = moduleName; }
	
	boolean bootstrapParser(String moduleName){
		if(moduleTags != null){
			IMap tags = (IMap) moduleTags.get(vf.string(moduleName));
			if(tags != null)
				return tags.get(vf.string("bootstrapParser")) != null;
		}
		return false;
	}
	
	public int endJob(boolean succeeded) {
		if (monitor != null)
			return monitor.endJob(succeeded);
		return 0;
	}
	
	public void event(int inc) {
		if (monitor != null)
			monitor.event(inc);
	}
	
	public void event(String name, int inc) {
		if (monitor != null)
			monitor.event(name, inc);
	}

	public void event(String name) {
		if (monitor != null)
			monitor.event(name);
	}

	public void startJob(String name, int workShare, int totalWork) {
		if (monitor != null)
			monitor.startJob(name, workShare, totalWork);
		stdout.println(name);
		stdout.flush();
	}
	
	public void startJob(String name, int totalWork) {
		if (monitor != null)
			monitor.startJob(name, totalWork);
	}
	
	public void startJob(String name) {
		if (monitor != null)
			monitor.startJob(name);
		stdout.println(name);
		stdout.flush();
	}
		
	public void todo(int work) {
		if (monitor != null)
			monitor.todo(work);
	}
	
	@Override
	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void warning(String message, ISourceLocation src) {
		stdout.println("Warning: " + message);
		stdout.flush();
	}

	public RascalSearchPath getRascalSearchPath() { return rascalSearchPath; }
	
	public void addRascalSearchPathContributor(IRascalSearchPathContributor contrib) {
		rascalSearchPath.addPathContributor(contrib);
	}
	
	public void addRascalSearchPath(final ISourceLocation uri) {
		rascalSearchPath.addPathContributor(new URIContributor(uri));
	}
	/**
	 * Source location resolvers map user defined schemes to primitive schemes
	 */
	private final HashMap<String, ICallableValue> sourceResolvers = new HashMap<String, ICallableValue>();
	
	/**
	 * Register a source resolver for a specific scheme. Will overwrite the previously
	 * registered source resolver for this scheme.
	 * 
	 * @param scheme   intended be a scheme name without + or :
	 * @param function a Rascal function of type `loc (loc)`
	 */
	public void registerSourceResolver(String scheme, ICallableValue function) {
		sourceResolvers.put(scheme, function);
	}
	
	public ISourceLocation resolveSourceLocation(ISourceLocation loc) {
		String scheme = loc.getScheme();
		int pos;
		
		ICallableValue resolver = sourceResolvers.get(scheme);
		if (resolver == null) {
			for (char sep : new char[] {'+',':'}) {
				pos = scheme.indexOf(sep);
				if (pos != -1) {
					scheme = scheme.substring(0, pos);
				}
			}

			resolver = sourceResolvers.get(scheme);
			if (resolver == null) {
				return loc;
			}
		}
		
		Type[] argTypes = new Type[] { TypeFactory.getInstance().sourceLocationType() };
		IValue[] argValues = new IValue[] { loc };
		
		return (ISourceLocation) resolver.call(argTypes, argValues, null).getValue();
	}
	
	void registerCommonSchemes(){
		URIResolverRegistry resolverRegistry = rascalSearchPath.getRegistry();
		
		
		// register some schemes
		FileURIResolver files = new FileURIResolver();
		resolverRegistry.registerInputOutput(files);

		HttpURIResolver http = new HttpURIResolver();
		resolverRegistry.registerInput(http);

		//added
		HttpsURIResolver https = new HttpsURIResolver();
		resolverRegistry.registerInput(https);

		CWDURIResolver cwd = new CWDURIResolver();
		resolverRegistry.registerLogical(cwd);

		ClassResourceInput library = new ClassResourceInput("std", getClass(), "/org/rascalmpl/library");
		resolverRegistry.registerInput(library);

		ClassResourceInput testdata = new ClassResourceInput("testdata", getClass(), "/org/rascalmpl/test/data");
		resolverRegistry.registerInput(testdata);

		ClassResourceInput benchmarkdata = new ClassResourceInput("benchmarks", getClass(), "/org/rascalmpl/benchmark");
		resolverRegistry.registerInput(benchmarkdata);

		resolverRegistry.registerInput(new JarURIResolver());

		resolverRegistry.registerLogical(new HomeURIResolver());
		resolverRegistry.registerInputOutput(new TempURIResolver());

		resolverRegistry.registerInputOutput(new CompressedStreamResolver(resolverRegistry));

		// here we have code that makes sure that courses can be edited by
		// maintainers of Rascal, using the -Drascal.courses=/path/to/courses property.
		final String courseSrc = System.getProperty("rascal.courses");
		if (courseSrc != null) {
			FileURIResolver fileURIResolver = new CourseResolver(courseSrc);

			resolverRegistry.registerInputOutput(fileURIResolver);
		}
		else {
			resolverRegistry.registerInput(new ClassResourceInput("courses", getClass(), "/org/rascalmpl/courses"));
		}

		ISourceLocationInputOutput testModuleResolver = new TestModuleResolver();

		addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		
		resolverRegistry.registerInputOutput(testModuleResolver);
		addRascalSearchPath(URIUtil.rootLocation("test-modules"));

		ClassResourceInput tutor = new ClassResourceInput("tutor", getClass(), "/org/rascalmpl/tutor");
		resolverRegistry.registerInput(tutor);
	}


	private static final class CourseResolver extends FileURIResolver {
		private final String courseSrc;

		private CourseResolver(String courseSrc) {
			this.courseSrc = courseSrc;
		}

		@Override
		public String scheme() {
			return "courses";
		}

		@Override
		protected String getPath(ISourceLocation uri) {
			String path = uri.getPath();
			return courseSrc + (path.startsWith("/") ? path : ("/" + path));
		}
	}

	/**
	 * The scheme "test-modules" is used, amongst others, to generate modules during tests.
	 * These modules are implemented via an in-memory "file system" that guarantees
	 * that "lastModified" is monotone increasing, i.e. after a write to a file lastModified
	 * is ALWAYS larger than for the previous version of the same file.
	 * When files are written at high speeed (e.g. with 10-30 ms intervals ), this property is, 
	 * unfortunately, not guaranteed on all operating systems.
	 *
	 */
	private static final class TestModuleResolver implements ISourceLocationInputOutput {
		
		@Override
		public String scheme() {
			return "test-modules";
		}
		
		private static final class File {
			byte[] contents;
			long timestamp;
			public File() {
				contents = new byte[0];
				timestamp = System.currentTimeMillis();
			}
			public void newContent(byte[] byteArray) {
				long newTimestamp = System.currentTimeMillis();
				if (newTimestamp <= timestamp) {
					newTimestamp =  timestamp +1;
				}
				timestamp = newTimestamp;
				contents = byteArray;
			}
		}
		
		private final Map<ISourceLocation, File> files = new HashMap<>();

		@Override
		public InputStream getInputStream(ISourceLocation uri)
				throws IOException {
			File file = files.get(uri);
			if (file == null) {
				throw new IOException();
			}
			return new ByteArrayInputStream(file.contents);
		}

		@Override
		public OutputStream getOutputStream(ISourceLocation uri, boolean append)
				throws IOException {
			File file = files.get(uri);
			final File result = file == null ? new File() : file; 
			return new ByteArrayOutputStream() {
				@Override
				public void close() throws IOException {
					super.close();
					result.newContent(this.toByteArray());
					files.put(uri, result);
				}
			};
		}
		
		@Override
		public long lastModified(ISourceLocation uri) throws IOException {
			File file = files.get(uri);
			if (file == null) {
				throw new IOException();
			}
			return file.timestamp;
		}
		
		@Override
		public Charset getCharset(ISourceLocation uri) throws IOException {
			return null;
		}

		@Override
		public boolean exists(ISourceLocation uri) {
			return files.containsKey(uri);
		}

		@Override
		public boolean isDirectory(ISourceLocation uri) {
			return false;
		}

		@Override
		public boolean isFile(ISourceLocation uri) {
			return files.containsKey(uri);
		}

		@Override
		public String[] list(ISourceLocation uri) throws IOException {
			return null;
		}

		@Override
		public boolean supportsHost() {
			return false;
		}

		@Override
		public void mkDirectory(ISourceLocation uri) throws IOException {
		}

		@Override
		public void remove(ISourceLocation uri) throws IOException {
			files.remove(uri);
		}
	}

	
}
