package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredNonTerminal;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.exception.UndeclaredNonTerminalException;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.RascalFunctionActionExecutor;
import org.rascalmpl.parser.uptr.recovery.Recoverer;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;
import org.rascalmpl.values.uptr.visitors.IdentityTreeVisitor;

public class ParsingTools {

	private IValueFactory vf;
	private URIResolverRegistry resolverRegistry;
	private IRascalMonitor monitor;
	private List<ClassLoader> classLoaders;
	private PrintWriter stderr;
	private PrintWriter stdout;
	private Configuration config;
	private HashMap<String,  Class<IGTD<IConstructor, IConstructor, ISourceLocation>>> parsers;
	private IEvaluatorContext ctx;
	
	ParsingTools(IValueFactory fact, IEvaluatorContext ctx){
		vf = fact;
		this.ctx = ctx;
		resolverRegistry = ctx.getResolverRegistry();
		monitor = ctx.getEvaluator().getMonitor();
		stderr = ctx.getEvaluator().getStdErr();
		stdout = ctx.getEvaluator().getStdOut();
		config = ctx.getEvaluator().getConfiguration();
		parsers = new HashMap<String,  Class<IGTD<IConstructor, IConstructor, ISourceLocation>>>();
		
		List<ClassLoader> oldClassLoaders = ctx.getEvaluator().getClassLoaders();
		
		classLoaders = oldClassLoaders;
		//classLoaders = new ArrayList<ClassLoader>(); 
		
		 stderr.println("Class loaders:");
		  for(ClassLoader cl : oldClassLoaders){
			  stderr.println(cl);
			  //classLoaders.add(cl);
		  }
//		  classLoaders.add(org.rascalmpl.parser.gtd.IGTD.class.getClassLoader());
//		  classLoaders.add(org.rascalmpl.parser.gtd.result.out.INodeConstructorFactory.class.getClassLoader());
//		  classLoaders.add(org.rascalmpl.parser.gtd.result.out.INodeFlattener.class.getClassLoader());
//		  classLoaders.add(org.rascalmpl.parser.gtd.recovery.IRecoverer.class.getClassLoader());
//		  classLoaders.add(0, ParsingTools.class.getClassLoader());
		  
//		  stderr.println("Class loaders after:");
//		  for(ClassLoader cl : classLoaders){
//			  stderr.println(cl);
//		  }  
		
	}
	
	private void storeObjectParser(String name, Class<IGTD<IConstructor, IConstructor, ISourceLocation>> parser) {
		parsers.put(name, parser);
	}

	private Class<IGTD<IConstructor, IConstructor, ISourceLocation>> getObjectParser(String name) {
		return parsers.get(name);
	}

	private boolean isBootstrapper() { return false; }

	public IValue parse(IValue start, ISourceLocation input, IMap syntax) {
		return parse(start, vf.mapWriter().done(), input, syntax);
	}
	
	public IValue parse(IValue start, IMap robust, ISourceLocation input, IMap syntax) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		
		
		try {
			IConstructor pt = parseObject(monitor, startSort, robust, input.getURI(), syntax);

			if (TreeAdapter.isAppl(pt)) {
				if (SymbolAdapter.isStart(TreeAdapter.getType(pt))) {
					pt = (IConstructor) TreeAdapter.getArgs(pt).get(1);
				}
			}
			return pt;
		}
		catch (ParseError pe) {
			ISourceLocation errorLoc = vf.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine() + 1, pe.getEndLine() + 1, pe.getBeginColumn(), pe.getEndColumn());
			throw RuntimeExceptionFactory.parseError(errorLoc, null, null);
		}
		catch (UndeclaredNonTerminalException e){
			throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), null);
		}
	}

	public IValue parse(IValue start, IString input) {
		return parse(start, vf.mapWriter().done(), input);
	}
	
	public IValue parse(IValue start, IMap robust, IString input) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		
		IMap syntax = (IMap) ((IConstructor) start).get(1);
		try {
			IConstructor pt = parseObject(monitor, startSort, robust, input.getValue(), syntax);

			if (TreeAdapter.isAppl(pt)) {
				if (SymbolAdapter.isStart(TreeAdapter.getType(pt))) {
					pt = (IConstructor) TreeAdapter.getArgs(pt).get(1);
				}
			}

			return pt;
		}
		catch (ParseError pe) {
			ISourceLocation errorLoc = vf.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine() + 1, pe.getEndLine() + 1, pe.getBeginColumn(), pe.getEndColumn());
			throw RuntimeExceptionFactory.parseError(errorLoc, null, null);
		}
		catch (UndeclaredNonTerminalException e){
			throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), null);
		}
	}
	
	public IValue parse(IValue start, IString input, ISourceLocation loc, IMap syntax) {
		return parse(start, vf.mapWriter().done(), input, loc, syntax);
	}
	
	public IValue parse(IValue start, IMap robust, IString input, ISourceLocation loc, IMap syntax) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		try {
			IConstructor pt = parseObject(monitor, startSort, robust, input.getValue(), loc, syntax);

			if (TreeAdapter.isAppl(pt)) {
				if (SymbolAdapter.isStart(TreeAdapter.getType(pt))) {
					pt = (IConstructor) TreeAdapter.getArgs(pt).get(1);
				}
			}

			return pt;
		}
		catch (ParseError pe) {
			ISourceLocation errorLoc = vf.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine(), pe.getEndLine(), pe.getBeginColumn(), pe.getEndColumn());
			throw RuntimeExceptionFactory.parseError(errorLoc, null, null);
		}
		catch (UndeclaredNonTerminalException e){
			throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), null);
		}
	}
	
//	public IString saveParser(ISourceLocation outFile) {
//		
//		IGTD<IConstructor, IConstructor, ISourceLocation> parser = org.rascalmpl.semantics.dynamic.Import.getParser(ctx.getEvaluator(), (ModuleEnvironment) ctx.getCurrentEnvt().getRoot(), URIUtil.invalidURI(), false);
//		Class<IGTD<IConstructor, IConstructor, ISourceLocation>> parserClass = (Class<IGTD<IConstructor, IConstructor, ISourceLocation>>) parser.getClass();
//		
//		
//		try(OutputStream outStream = resolverRegistry.getOutputStream(outFile.getURI(), false)) {
//			ctx.getEvaluator().getParserGenerator().saveToJar(parserClass, outStream);
//		} catch (IOException e) {
//			throw RuntimeExceptionFactory.io(vf.string("Unable to save to output file '" + outFile.getURI() + "'"), null, null);
//		}
//		return vf.string(parserClass.getName());
//
//	}
	public IString unparse(IConstructor tree) {
		return vf.string(TreeAdapter.yield(tree));
	}
	
	private static IConstructor checkPreconditions(IValue start, Type reified) {
		if (!(reified instanceof ReifiedType)) {
		   throw RuntimeExceptionFactory.illegalArgument(start, null, null, "A reified type is required instead of " + reified);
		}
		
		Type nt = reified.getTypeParameters().getFieldType(0);
		
		if (!(nt instanceof NonTerminalType)) {
			throw RuntimeExceptionFactory.illegalArgument(start, null, null, "A non-terminal type is required instead of  " + nt);
		}
		
		IConstructor symbol = ((NonTerminalType) nt).getSymbol();
		
		return symbol;
	}
	
	public IConstructor parseObject(IConstructor startSort, IMap robust, URI location, char[] input, IMap syntax){
		IGTD<IConstructor, IConstructor, ISourceLocation> parser = getObjectParser(location, syntax);
		String name = "";
		if (SymbolAdapter.isStartSort(startSort)) {
			name = "start__";
			startSort = SymbolAdapter.getStart(startSort);
		}
		
		if (SymbolAdapter.isSort(startSort) || SymbolAdapter.isLex(startSort) || SymbolAdapter.isLayouts(startSort)) {
			name += SymbolAdapter.getName(startSort);
		}

		int[][] lookaheads = new int[robust.size()][];
		IConstructor[] robustProds = new IConstructor[robust.size()];
		initializeRecovery(robust, lookaheads, robustProds);
		
		//__setInterrupt(false);
		IActionExecutor<IConstructor> exec = new RascalFunctionActionExecutor(ctx);
		
	      String className = name;
	      Class<?> clazz;
	      for (ClassLoader cl: classLoaders) {
	        try {
	          stderr.println("Trying classloader: " + cl);
	          clazz = cl.loadClass(className);
	          parser =  (IGTD<IConstructor, IConstructor, ISourceLocation>) clazz.newInstance();
	          stderr.println("succeeded!");
	          break;
	        } catch (ClassNotFoundException e) {
	          continue;
	        } catch (InstantiationException e) {
	          throw new ImplementationError("could not instantiate " + className + " to valid IGTD parser", e);
	        } catch (IllegalAccessException e) {
	          throw new ImplementationError("not allowed to instantiate " + className + " to valid IGTD parser", e);
	        } catch (LinkageError e){
	        	continue;
	        }
	        //throw new ImplementationError("class for cached parser " + className + " could not be found");
	      }
	      
	     
		return (IConstructor) parser.parse(name, location, input, exec, new DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation>(), new UPTRNodeFactory(), robustProds.length == 0 ? null : new Recoverer(robustProds, lookaheads));
	}
	
	/**
	 * This converts a map from productions to character classes to
	 * two pair-wise arrays, with char-classes unfolded as lists of ints.
	 */
	private void initializeRecovery(IMap robust, int[][] lookaheads, IConstructor[] robustProds) {
		int i = 0;
		
		for (IValue prod : robust) {
			robustProds[i] = (IConstructor) prod;
			List<Integer> chars = new LinkedList<Integer>();
			IList ranges = (IList) robust.get(prod);
			
			for (IValue range : ranges) {
				int from = ((IInteger) ((IConstructor) range).get("begin")).intValue();
				int to = ((IInteger) ((IConstructor) range).get("end")).intValue();
				
				for (int j = from; j <= to; j++) {
					chars.add(j);
				}
			}
			
			lookaheads[i] = new int[chars.size()];
			for (int k = 0; k < chars.size(); k++) {
				lookaheads[i][k] = chars.get(k);
			}
			
			i++;
		}
	}

	public IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort, IMap robust, URI location, IMap syntax){
		//IRascalMonitor old = setMonitor(monitor);
		
		try{
			char[] input = getResourceContent(location);
			return parseObject(startSort, robust, location, input, syntax);
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(vf.string(ioex.getMessage()), null, null);
		} finally{
//			setMonitor(old);
		}
	}
	
	public IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort, IMap robust, String input, IMap syntax){
//		IRascalMonitor old = setMonitor(monitor);
		try{
			return parseObject(startSort, robust, URIUtil.invalidURI(), input.toCharArray(), syntax);
		}finally{
//			setMonitor(old);
		}
	}
	
	public IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort, IMap robust, String input, ISourceLocation loc, IMap syntax){
//		IRascalMonitor old = setMonitor(monitor);
		try{
			return parseObject(startSort, robust, loc.getURI(), input.toCharArray(), syntax);
		}finally{
//			setMonitor(old);
		}
	}
	
	private IGTD<IConstructor, IConstructor, ISourceLocation> getObjectParser(URI loc, IMap syntax){
		return getParser("XXX", loc, false, syntax);
	}

	
//	public IConstructor getGrammar(Environment env) {
//		ModuleEnvironment root = (ModuleEnvironment) env.getRoot();
//		return getParserGenerator().getGrammar(monitor, root.getName(), root.getSyntaxDefinition());
//	}
	
	
//	public IConstructor getGrammar(IRascalMonitor monitor, URI uri) {
////		IRascalMonitor old = setMonitor(monitor);
//		try {
//			ParserGenerator pgen = getParserGenerator();
//			String main = uri.getAuthority();
//			ModuleEnvironment env = getHeap().getModule(main);
//			return pgen.getGrammar(monitor, main, env.getSyntaxDefinition());
//		}
//		finally {
////			setMonitor(old);
//		}
//	}
	
//	public IValue diagnoseAmbiguity(IRascalMonitor monitor, IConstructor parseTree) {
////		IRascalMonitor old = setMonitor(monitor);
//		try {
//			ParserGenerator pgen = getParserGenerator();
//			return pgen.diagnoseAmbiguity(parseTree);
//		}
//		finally {
////			setMonitor(old);
//		}
//	}
	
//	public IConstructor getExpandedGrammar(IRascalMonitor monitor, URI uri) {
////		IRascalMonitor old = setMonitor(monitor);
//		try {
//			ParserGenerator pgen = getParserGenerator();
//			String main = uri.getAuthority();
//			ModuleEnvironment env = getHeap().getModule(main);
//			monitor.startJob("Expanding Grammar");
//			return pgen.getExpandedGrammar(monitor, main, env.getSyntaxDefinition());
//		}
//		finally {
////			monitor.endJob(true);
////			setMonitor(old);
//		}
//	}
	
	public ISet getNestingRestrictions(IRascalMonitor monitor, IConstructor g) {
//		IRascalMonitor old = setMonitor(monitor);
		try {
			ParserGenerator pgen = getParserGenerator();
			return pgen.getNestingRestrictions(monitor, g);
		}
		finally {
//			setMonitor(old);
		}
	}
	
	private ParserGenerator parserGenerator;
  
	
	public ParserGenerator getParserGenerator() {
//		startJob("Loading parser generator", 40);
		if(parserGenerator == null ){
		  if (isBootstrapper()) {
		    throw new ImplementationError("Cyclic bootstrapping is occurring, probably because a module in the bootstrap dependencies is using the concrete syntax feature.");
		  }
		 
		  parserGenerator = new ParserGenerator(monitor, stderr, classLoaders, vf, config);
		}
//		endJob(true);
		return parserGenerator;
	}
	
	private char[] getResourceContent(URI location) throws IOException{
		char[] data;
		Reader textStream = null;
		
		try {
			textStream = resolverRegistry.getCharacterReader(location);
			data = InputConverter.toChar(textStream);
		}
		finally{
			if(textStream != null){
				textStream.close();
			}
		}
		
		return data;
	}
	
	// From import.java
	
	 /**
	   * This function will reconstruct a parse tree of a module, where all nested concrete syntax fragments
	   * have been parsed and their original flat literal strings replaced by fully structured parse trees.
	   * 
	   * @param module is a parse tree of a Rascal module containing flat concrete literals
	   * @param parser is the parser to use for the concrete literals
	   * @return parse tree of a module with structured concrete literals, or parse errors
	   */
	  public IConstructor parseFragments(final IEvaluator<Result<IValue>> eval, IConstructor module, final URI location, final ModuleEnvironment env) {
	    // TODO: update source code locations!!
	    
	     return (IConstructor) module.accept(new IdentityTreeVisitor<ImplementationError>() {
	       final IValueFactory vf = eval.getValueFactory();
	       
	       @Override
	       public IConstructor visitTreeAppl(IConstructor tree)  {
	         IConstructor pattern = getConcretePattern(tree);
	         
	         if (pattern != null) {
	           IConstructor parsedFragment = parseFragment(eval, env, (IConstructor) TreeAdapter.getArgs(tree).get(0), location);
	           return TreeAdapter.setArgs(tree, vf.list(parsedFragment));
	         }
	         else {
	           IListWriter w = vf.listWriter();
	           IList args = TreeAdapter.getArgs(tree);
	           for (IValue arg : args) {
	             w.append(arg.accept(this));
	           }
	           args = w.done();
	           
	           return TreeAdapter.setArgs(tree, args);
	         }
	       }

	       private IConstructor getConcretePattern(IConstructor tree) {
	         String sort = TreeAdapter.getSortName(tree);
	         if (sort.equals("Expression") || sort.equals("Pattern")) {
	           String cons = TreeAdapter.getConstructorName(tree);
	           if (cons.equals("concrete")) {
	             return (IConstructor) TreeAdapter.getArgs(tree).get(0);
	           }
	         }
	         return null;
	      }

	      @Override
	       public IConstructor visitTreeAmb(IConstructor arg) {
	         throw new ImplementationError("unexpected ambiguity: " + arg);
	       }
	     });
	  }
	  
	  @SuppressWarnings("unchecked")
	  public IGTD<IConstructor, IConstructor, ISourceLocation> getParser(String name, URI loc, boolean force, IMap syntax) {
//	    if (currentModule.getBootstrap()) {
//	      return new RascalParser();
//	    }
//	    
//	    if (currentModule.hasCachedParser()) {
//	      String className = currentModule.getCachedParser();
//	      Class<?> clazz;
//	      for (ClassLoader cl: eval.getClassLoaders()) {
//	        try {
//	          clazz = cl.loadClass(className);
//	          return (IGTD<IConstructor, IConstructor, ISourceLocation>) clazz.newInstance();
//	        } catch (ClassNotFoundException e) {
//	          continue;
//	        } catch (InstantiationException e) {
//	          throw new ImplementationError("could not instantiate " + className + " to valid IGTD parser", e);
//	        } catch (IllegalAccessException e) {
//	          throw new ImplementationError("not allowed to instantiate " + className + " to valid IGTD parser", e);
//	        }
//	      }
//	      throw new ImplementationError("class for cached parser " + className + " could not be found");
//	    }

	    ParserGenerator pg = getParserGenerator();
	    IMap definitions = syntax;
	    
	    Class<IGTD<IConstructor, IConstructor, ISourceLocation>> parser = getObjectParser(name);

	    if (parser == null || force) {
	      String parserName = name; // .replaceAll("::", ".");

	      parser = pg.getNewParser(monitor, loc, parserName, definitions);
	      storeObjectParser(name, parser);
	    }

	    try {
	      return parser.newInstance();
	    } catch (InstantiationException e) {
	      throw new ImplementationError(e.getMessage(), e);
	    } catch (IllegalAccessException e) {
	      throw new ImplementationError(e.getMessage(), e);
	    } catch (ExceptionInInitializerError e) {
	      throw new ImplementationError(e.getMessage(), e);
	    }
	  }
	  
	

	private IConstructor parseFragment(IEvaluator<Result<IValue>> eval, ModuleEnvironment env, IConstructor tree, URI uri) {
	    IConstructor symTree = TreeAdapter.getArg(tree, "symbol");
	    IConstructor lit = TreeAdapter.getArg(tree, "parts");
	    Map<String, IConstructor> antiquotes = new HashMap<String,IConstructor>();
	    
	    IGTD<IConstructor, IConstructor, ISourceLocation> parser = env.getBootstrap() ? new RascalParser() : getParser("XXX", TreeAdapter.getLocation(tree).getURI(), false, null);
	    
	    try {
	      String parserMethodName = eval.getParserGenerator().getParserMethodName(symTree);
	      DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation> converter = new DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation>();
	      UPTRNodeFactory nodeFactory = new UPTRNodeFactory();
	    
	      char[] input = replaceAntiQuotesByHoles(eval, lit, antiquotes);
	      
	      IConstructor fragment = (IConstructor) parser.parse(parserMethodName, uri, input, converter, nodeFactory);
	      fragment = replaceHolesByAntiQuotes(eval, fragment, antiquotes);

	      IConstructor prod = TreeAdapter.getProduction(tree);
	      IConstructor sym = ProductionAdapter.getDefined(prod);
	      sym = SymbolAdapter.delabel(sym); 
	      IValueFactory vf = eval.getValueFactory();
	      prod = ProductionAdapter.setDefined(prod, vf.constructor(Factory.Symbol_Label, vf.string("$parsed"), sym));
	      return TreeAdapter.setProduction(TreeAdapter.setArg(tree, "parts", fragment), prod);
	    }
	    catch (ParseError e) {
	      ISourceLocation loc = TreeAdapter.getLocation(tree);
	      ISourceLocation src = eval.getValueFactory().sourceLocation(loc.getURI(), loc.getOffset() + e.getOffset(), loc.getLength(), loc.getBeginLine() + e.getBeginLine() - 1, loc.getEndLine() + e.getEndLine() - 1, loc.getBeginColumn() + e.getBeginColumn(), loc.getBeginColumn() + e.getEndColumn());
	      eval.getMonitor().warning("parse error in concrete syntax", src);
	      return tree.asAnnotatable().setAnnotation("parseError", src);
	    }
	    catch (StaticError e) {
	      ISourceLocation loc = TreeAdapter.getLocation(tree);
	      ISourceLocation src = eval.getValueFactory().sourceLocation(loc.getURI(), loc.getOffset(), loc.getLength(), loc.getBeginLine(), loc.getEndLine(), loc.getBeginColumn(), loc.getBeginColumn());
	      eval.getMonitor().warning(e.getMessage(), e.getLocation());
	      return tree.asAnnotatable().setAnnotation("can not parse fragment due to " + e.getMessage(), src);
	    }
	    catch (UndeclaredNonTerminalException e) {
	      ISourceLocation loc = TreeAdapter.getLocation(tree);
	      ISourceLocation src = eval.getValueFactory().sourceLocation(loc.getURI(), loc.getOffset(), loc.getLength(), loc.getBeginLine(), loc.getEndLine(), loc.getBeginColumn(), loc.getBeginColumn());
	      eval.getMonitor().warning(e.getMessage(), src);
	      return tree.asAnnotatable().setAnnotation("can not parse fragment due to " + e.getMessage(), src);
	    }
	  }
	  
	  private static char[] replaceAntiQuotesByHoles(IEvaluator<Result<IValue>> eval, IConstructor lit, Map<String, IConstructor> antiquotes) {
	    IList parts = TreeAdapter.getArgs(lit);
	    StringBuilder b = new StringBuilder();
	    
	    for (IValue elem : parts) {
	      IConstructor part = (IConstructor) elem;
	      String cons = TreeAdapter.getConstructorName(part);
	      
	      if (cons.equals("text")) {
	        b.append(TreeAdapter.yield(part));
	      }
	      else if (cons.equals("newline")) {
	        b.append('\n');
	      }
	      else if (cons.equals("lt")) {
	        b.append('<');
	      }
	      else if (cons.equals("gt")) {
	        b.append('>');
	      }
	      else if (cons.equals("bq")) {
	        b.append('`');
	      }
	      else if (cons.equals("bs")) {
	        b.append('\\');
	      }
	      else if (cons.equals("hole")) {
	        b.append(createHole(eval, part, antiquotes));
	      }
	    }
	    
	    return b.toString().toCharArray();
	  }

	  private static String createHole(IEvaluator<Result<IValue>> ctx, IConstructor part, Map<String, IConstructor> antiquotes) {
	    String ph = ctx.getParserGenerator().createHole(part, antiquotes.size());
	    antiquotes.put(ph, part);
	    return ph;
	  }

	  private static IConstructor replaceHolesByAntiQuotes(final IEvaluator<Result<IValue>> eval, IConstructor fragment, final Map<String, IConstructor> antiquotes) {
	      return (IConstructor) fragment.accept(new IdentityTreeVisitor<ImplementationError>() {
	        private final IValueFactory vf = eval.getValueFactory();
	        
	        @Override
	        public IConstructor visitTreeAppl(IConstructor tree)  {
	          String cons = TreeAdapter.getConstructorName(tree);
	          if (cons == null || !cons.equals("$MetaHole") ) {
	            IListWriter w = eval.getValueFactory().listWriter();
	            IList args = TreeAdapter.getArgs(tree);
	            for (IValue elem : args) {
	              w.append(elem.accept(this));
	            }
	            args = w.done();
	            
	            return TreeAdapter.setArgs(tree, args);
	          }
	          
	          IConstructor type = retrieveHoleType(tree);
	          return antiquotes.get(TreeAdapter.yield(tree)).asAnnotatable().setAnnotation("holeType", type);
	        }
	        
	        private IConstructor retrieveHoleType(IConstructor tree) {
	          IConstructor prod = TreeAdapter.getProduction(tree);
	          ISet attrs = ProductionAdapter.getAttributes(prod);

	          for (IValue attr : attrs) {
	            if (((IConstructor) attr).getConstructorType() == Factory.Attr_Tag) {
	              IValue arg = ((IConstructor) attr).get(0);
	              
	              if (arg.getType().isNode() && ((INode) arg).getName().equals("holeType")) {
	                return (IConstructor) ((INode) arg).get(0);
	              }
	            }
	          }
	          
	          throw new ImplementationError("expected to find a holeType, but did not: " + tree);
	        }

	        @Override
	        public IConstructor visitTreeAmb(IConstructor arg)  {
	          ISetWriter w = vf.setWriter();
	          for (IValue elem : TreeAdapter.getAlternatives(arg)) {
	            w.insert(elem.accept(this));
	          }
	          return arg.set("alternatives", w.done());
	        }
	      });
	  }
	 
//	  private static boolean containsBackTick(char[] data, int offset) {
//	    for (int i = data.length - 1; i >= offset; --i) {
//	      if (data[i] == '`')
//	        return true;
//	    }
//	    return false;
//	  }
//	  
//	  private static boolean needBootstrapParser(char[] input) {
//	    return new String(input).contains("@bootstrapParser");
//	  }
}
