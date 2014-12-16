package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
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
import org.rascalmpl.interpreter.IEvaluatorContext;				// TODO: remove import: YES
import org.rascalmpl.interpreter.IRascalMonitor;				// remove import: NO
import org.rascalmpl.interpreter.types.NonTerminalType;			// remove import: NO
import org.rascalmpl.interpreter.types.ReifiedType;				// remove import: NO
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
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;
import org.rascalmpl.values.uptr.visitors.IdentityTreeVisitor;

public class ParsingTools {

	private IValueFactory vf;
	private IRascalMonitor monitor;
	private List<ClassLoader> classLoaders;
	private PrintWriter stderr;
	private HashMap<IValue,  Class<IGTD<IConstructor, IConstructor, ISourceLocation>>> parsers;
	private RascalExecutionContext rex;
	
	public ParsingTools(IValueFactory vf){
		super();
		this.vf = vf; 
		parsers = new HashMap<IValue,  Class<IGTD<IConstructor, IConstructor, ISourceLocation>>>();
		stderr = new PrintWriter(System.err);
	}
	
	public void reset(){
		parsers = new HashMap<IValue,  Class<IGTD<IConstructor, IConstructor, ISourceLocation>>>();
	}
	
	public void setContext(RascalExecutionContext rex){
		this.rex = rex;
		monitor = rex.getMonitor();
		stderr = rex.getStdErr();
		parsers = new HashMap<IValue,  Class<IGTD<IConstructor, IConstructor, ISourceLocation>>>();
		classLoaders = rex.getClassLoaders();
	}
	
	private IRascalMonitor getMonitor() {
		return monitor;
	}
	
	private IRascalMonitor setMonitor(IRascalMonitor monitor2) {
		monitor = monitor2;
		return monitor2;
	}
	
	private void startJob(String name, int totalWork) {
		if (monitor != null)
			monitor.startJob(name, totalWork);
	}
	
	private int endJob(boolean succeeded) {
		if (monitor != null)
			return monitor.endJob(succeeded);
		return 0;
	}
	
	/** 
	 * Store a generated and compiled parser.
	 * @param moduleName	Name of module in which grammar is defined
	 * @param start TODO
	 * @param parser		The generated parser class
	 */
	private void storeObjectParser(String moduleName, IValue start, Class<IGTD<IConstructor, IConstructor, ISourceLocation>> parser) {
		stderr.println("Storing parser for " + moduleName + "/" + start);
		parsers.put(start, parser);
	}

	/**
	 * Retrieve a generated and compiled parser
	 * @param moduleName	Name of module in which grammar is defined
	 * @param start TODO
	 * @return				The generated parser class or NULL
	 */
	private Class<IGTD<IConstructor, IConstructor, ISourceLocation>> getObjectParser(String moduleName, IValue start) {
		Class<IGTD<IConstructor, IConstructor, ISourceLocation>> parser = parsers.get(start);
		stderr.println("Retrieving parser for " + moduleName + "/" + start + ((parser == null) ? " fails" : " succeeds"));
		return parser;
	}
	
	private IGTD<IConstructor, IConstructor, ISourceLocation> getObjectParser(IString moduleName, IValue start, URI loc, IMap syntax){
		return getParser(moduleName.getValue(), start, loc, false, syntax);
	}

	private boolean isBootstrapper() { return false;  }
	
	/**
	 * Parse text from a string
	 * @param start		Start symbol
	 * @param input		Text to be parsed as string
	 * @param currentFrame TODO
	 * @return ParseTree or Exception
	 */
	public IValue parse(IString moduleName, IValue start, IString input, Frame currentFrame) {
		return parse(moduleName, start, vf.mapWriter().done(), URIUtil.invalidURI(), input.getValue().toCharArray(), currentFrame);
	}
	
	/**
	 * Parse text at a location
	 * @param moduleName Name of module in which grammar is defined
	 * @param start		Start symbol
	 * @param currentFrame TODO
	 * @param input		To be parsed as location
	 * @return ParseTree or Exception
	 */
	public IValue parse(IString moduleName, IValue start, ISourceLocation location, Frame currentFrame) {
		IRascalMonitor old = setMonitor(monitor);
		
		try{
			char[] input = getResourceContent(location.getURI());
			return parse(moduleName, start,  vf.mapWriter().done(), location.getURI(), input, currentFrame);
		}catch(IOException ioex){
			throw RascalRuntimeException.io(vf.string(ioex.getMessage()), currentFrame);
		} finally{
			setMonitor(old);
		}
	}

	/**
	 * The actual parse work horse
	 * @param moduleName	Name of module in which grammar is defined
	 * @param start			Start symbol
	 * @param robust		Error recovery map
	 * @param location		Location where input text comes from
	 * @param input			Input text as char array
	 * @param currentFrame 	Stacktrace of calling context
	 * @return
	 */
	public IValue parse(IString moduleName, IValue start, IMap robust, URI location, char[] input, Frame currentFrame) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified, currentFrame);
		
		IMap syntax = (IMap) ((IConstructor) start).get(1);
		try {
			IConstructor pt = parseObject(moduleName, startSort, robust, location, input, syntax);

			if (TreeAdapter.isAppl(pt)) {
				if (SymbolAdapter.isStart(TreeAdapter.getType(pt))) {
					pt = (IConstructor) TreeAdapter.getArgs(pt).get(1);
				}
			}
			return pt;
		}
		catch (ParseError pe) {
			ISourceLocation errorLoc = vf.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine() + 1, pe.getEndLine() + 1, pe.getBeginColumn(), pe.getEndColumn());
			throw RascalRuntimeException.parseError(errorLoc, currentFrame);
		}
		catch (UndeclaredNonTerminalException e){
			throw new CompilerError("Undeclared non-terminal: " + e.getName() + ", " + e.getClassName(), currentFrame);
		}
		catch (Exception e) {
			throw new CompilerError("Unexpected exception:" + e, currentFrame);
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
	
	/**
	 * Chek that start symbol is valid
	 * @param start			Start symbol, as IValue
	 * @param reified		Reified type, that shoud represent a non-terminal type
	 * @return Start symbol represented as Symbol
	 */
	private static IConstructor checkPreconditions(IValue start, Type reified, Frame currentFrame) {
		if (!(reified instanceof ReifiedType)) {
		   throw RascalRuntimeException.illegalArgument(start, currentFrame, "A reified type is required instead of " + reified);
		}
		
		Type nt = reified.getTypeParameters().getFieldType(0);
		
		if (!(nt instanceof NonTerminalType)) {
			throw RascalRuntimeException.illegalArgument(start, currentFrame, "A non-terminal type is required instead of  " + nt);
		}
		
		IConstructor symbol = ((NonTerminalType) nt).getSymbol();
		
		return symbol;
	}
	
	/**
	 * The actual parse object that is connected to a generated parser
	 * @param moduleName	Name of module in which grammar is defined
	 * @param startSort		Start symbol
	 * @param robust		Error recovery map
	 * @param location		Location where input text comes from
	 * @param input			Actual input text as char array
	 * @param syntax		Syntax as map[Symbol,Production]
	 * @return				ParseTree or Exception
	 */
	@SuppressWarnings("unchecked")
	public IConstructor parseObject(IString moduleName, IConstructor startSort, IMap robust, URI location, char[] input, IMap syntax){
		IGTD<IConstructor, IConstructor, ISourceLocation> parser = getObjectParser(moduleName, startSort, location, syntax);
		String name = ""; moduleName.getValue();
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
		IActionExecutor<IConstructor> exec = new RascalFunctionActionExecutor(rex.getEvaluatorContext());  // TODO: remove CTX
		
	      String className = name;
	      Class<?> clazz;
	      for (ClassLoader cl: classLoaders) {
	        try {
	          //stderr.println("Trying classloader: " + cl);
	          clazz = cl.loadClass(className);
	          parser =  (IGTD<IConstructor, IConstructor, ISourceLocation>) clazz.newInstance();
	          //stderr.println("succeeded!");
	          break;
	        } catch (ClassNotFoundException e) {
	          continue;
	        } catch (InstantiationException e) {
	          throw new CompilerError("could not instantiate " + className + " to valid IGTD parser: " + e);
	        } catch (IllegalAccessException e) {
	          throw new CompilerError("not allowed to instantiate " + className + " to valid IGTD parser: " + e);
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
	
	private ParserGenerator parserGenerator;
	
	public ParserGenerator getParserGenerator() {
		startJob("Loading parser generator", 40);
		if(parserGenerator == null ){
		  if (isBootstrapper()) {
		    throw new CompilerError("Cyclic bootstrapping is occurring, probably because a module in the bootstrap dependencies is using the concrete syntax feature.");
		  }
		 
		  parserGenerator = new ParserGenerator(rex.getMonitor(), rex.getStdErr(), classLoaders, vf, rex.getConfiguration());
		}
		endJob(true);
		return parserGenerator;
	}
	
	private char[] getResourceContent(URI location) throws IOException{
		char[] data;
		Reader textStream = null;
		
		try {
			textStream = rex.getResolverRegistry().getCharacterReader(location);
			data = InputConverter.toChar(textStream);
		}
		finally{
			if(textStream != null){
				textStream.close();
			}
		}
		
		return data;
	}
	  
	  public IGTD<IConstructor, IConstructor, ISourceLocation> getParser(String name, IValue start, URI loc, boolean force, IMap syntax) {
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
	    
	    Class<IGTD<IConstructor, IConstructor, ISourceLocation>> parser = getObjectParser(name, start);

	    if (parser == null || force) {
	      String parserName = name; // .replaceAll("::", ".");
	     //stderr.println("name = " + name);
	      parser = pg.getNewParser(rex.getMonitor(), loc, parserName, definitions);
	      storeObjectParser(name, start, parser);
	    }

	    try {
	      return parser.newInstance();
	    } catch (InstantiationException e) {
	      throw new CompilerError(e.getMessage() + e);
	    } catch (IllegalAccessException e) {
	      throw new CompilerError(e.getMessage() + e);
	    } catch (ExceptionInInitializerError e) {
	      throw new CompilerError(e.getMessage() + e);
	    }
	  }
	  
	  private boolean getBootstrap() { return false; }
	 
	  // Rascal library function (interpreter version)
	  public IConstructor parseFragment(IString name, IValue start, IConstructor tree, ISourceLocation loc, IMap grammar, IEvaluatorContext ctx){
		  if(rex == null){
			  rex = new RascalExecutionContext(vf, null, false, false, false, false, ctx, null);
		  }
		  return parseFragment(name, start, tree, loc.getURI(), grammar);
	  }
		
	// Rascal library function (compiler version)
	public IConstructor parseFragment(IString name, IValue start, IConstructor tree, ISourceLocation loc, IMap grammar, RascalExecutionContext rex){ 
		if(this.rex == null){
			this.rex = rex;
		}
		return parseFragment(name, start, tree, loc.getURI(), grammar);
	}
	
	/**
	 * This function will reconstruct a parse tree of a single nested concrete syntax fragment
	 * that has been parsed and its original flat literal string is replaced by a fully structured parse tree.
	 * 
	 */

	IConstructor parseFragment(IString name, IValue start, IConstructor tree, URI uri, IMap grammar) {
	    IConstructor symTree = TreeAdapter.getArg(tree, "symbol");
	    IConstructor lit = TreeAdapter.getArg(tree, "parts");
	    Map<String, IConstructor> antiquotes = new HashMap<String,IConstructor>();
	    
	    IGTD<IConstructor, IConstructor, ISourceLocation> parser = getBootstrap() ? new RascalParser() : getParser(name.getValue(), start, TreeAdapter.getLocation(tree).getURI(), false, grammar);
	    
	    try {
	      String parserMethodName = getParserGenerator().getParserMethodName(symTree);
	      DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation> converter = new DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation>();
	      UPTRNodeFactory nodeFactory = new UPTRNodeFactory();
	    
	      char[] input = replaceAntiQuotesByHoles(lit, antiquotes);
	      
	      IConstructor fragment = (IConstructor) parser.parse(parserMethodName, uri, input, converter, nodeFactory);
	      fragment = replaceHolesByAntiQuotes(fragment, antiquotes);
	      return fragment;
	      
//	      IConstructor prod = TreeAdapter.getProduction(tree);
//	      IConstructor sym = ProductionAdapter.getDefined(prod);
//	      sym = SymbolAdapter.delabel(sym); 
//	      prod = ProductionAdapter.setDefined(prod, vf.constructor(Factory.Symbol_Label, vf.string("$parsed"), sym));
//	      return TreeAdapter.setProduction(TreeAdapter.setArg(tree, "parts", fragment), prod);
	    }
	    catch (ParseError e) {
	      ISourceLocation loc = TreeAdapter.getLocation(tree);
	      ISourceLocation src = vf.sourceLocation(loc, loc.getOffset() + e.getOffset(), loc.getLength(), loc.getBeginLine() + e.getBeginLine() - 1, loc.getEndLine() + e.getEndLine() - 1, loc.getBeginColumn() + e.getBeginColumn(), loc.getBeginColumn() + e.getEndColumn());
	      rex.getStdErr().println("***** WARNING: parseFragment, parse error at " + src);
	      //getMonitor().warning("parse error in concrete syntax", src);
	      return tree.asAnnotatable().setAnnotation("parseError", src);
	    }
//	    catch (StaticError e) {
//	      ISourceLocation loc = TreeAdapter.getLocation(tree);
//	      ISourceLocation src = vf.sourceLocation(loc, loc.getOffset(), loc.getLength(), loc.getBeginLine(), loc.getEndLine(), loc.getBeginColumn(), loc.getBeginColumn());
//	      getMonitor().warning(e.getMessage(), e.getLocation());
//	      return tree.asAnnotatable().setAnnotation("can not parse fragment due to " + e.getMessage(), src);
//	    }
//	    catch (UndeclaredNonTerminalException e) {
//	      ISourceLocation loc = TreeAdapter.getLocation(tree);
//	      ISourceLocation src = vf.sourceLocation(loc, loc.getOffset(), loc.getLength(), loc.getBeginLine(), loc.getEndLine(), loc.getBeginColumn(), loc.getBeginColumn());
//	      getMonitor().warning(e.getMessage(), src);
//	      return tree.asAnnotatable().setAnnotation("can not parse fragment due to " + e.getMessage(), src);
//	    }
	  }
	  
	  private char[] replaceAntiQuotesByHoles(IConstructor lit, Map<String, IConstructor> antiquotes) {
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
	        b.append(createHole(part, antiquotes));
	      }
	    }
	    return b.toString().toCharArray();
	  }

	  private String createHole(IConstructor part, Map<String, IConstructor> antiquotes) {
	    String ph = getParserGenerator().createHole(part, antiquotes.size());
	    antiquotes.put(ph, part);
	    return ph;
	  }

	  private IConstructor replaceHolesByAntiQuotes(IConstructor fragment, final Map<String, IConstructor> antiquotes) {
		  return (IConstructor) fragment.accept(new IdentityTreeVisitor<CompilerError>() {

			  @Override
			  public IConstructor visitTreeAppl(IConstructor tree)  {
				  String cons = TreeAdapter.getConstructorName(tree);
				  if (cons == null || !cons.equals("$MetaHole") ) {
					  IListWriter w = vf.listWriter();
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

				  throw new CompilerError("expected to find a holeType, but did not: " + tree);
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
