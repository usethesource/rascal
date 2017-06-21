package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.interpreter.IEvaluatorContext;				// TODO: remove import: YES
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.types.NonTerminalType;			// remove import: NO
import org.rascalmpl.interpreter.types.ReifiedType;				// remove import: NO
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.exception.UndeclaredNonTerminalException;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;
import org.rascalmpl.values.uptr.visitors.IdentityTreeVisitor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class ParsingTools {
	private final IValueFactory vf;
	
	//TODO this cache can move to RascalexecutionContext once we are fully boostrapped and independent of the Interpreter:
	// reason: parseFragment called from the interpreter creates a new REX and destroys caching.
	private Cache<IValue,  Class<IGTD<IConstructor, ITree, ISourceLocation>>> parserCache;
	private final int parserCacheSize = 30;
	private final boolean parserCacheEnabled = true;

    /**
     * @param vf    required to build parse trees
     * @param pcfg  required to load the generated code of the parser with the appropriate class loader
     */
	public ParsingTools(IValueFactory vf){
		super();
		this.vf = vf;
		parserCache = Caffeine.newBuilder()
			    .weakValues()
				.maximumSize(parserCacheEnabled ? parserCacheSize : 0)
				.build();
	}
	
	private IGTD<IConstructor, ITree, ISourceLocation> getObjectParser(IString moduleName, IValue start, ISourceLocation loc, IMap syntax, RascalExecutionContext rex) throws IOException{
		return getParser(moduleName.getValue(), start, loc, syntax, rex);
	}

	private boolean isBootstrapper() {
		return false;
	}
	
	/**
	 * Parse text from a string
	 * @param start			Start symbol
	 * @param input			Text to be parsed as string
	 * @param currentFrame	Frame that calls parse function
	 * @param rex 			RascalExecutionContext
	 * @return ParseTree or Exception
	 */
	public IValue parse(IString moduleName, IValue start, IString input, boolean allowAmbiguity, Frame currentFrame, RascalExecutionContext rex) {
		return parse(moduleName, start, vf.mapWriter().done(), URIUtil.invalidLocation(), input.getValue().toCharArray(), allowAmbiguity, currentFrame, rex);
	}
	
	/**
	 * Parse text from a string
	 * @param start			Start symbol
	 * @param input			Text to be parsed as string
	 * @param location		Location of that text
	 * @param currentFrame 	Frame that calls parse function
	 * @param rex 			RascalExecutionContext
	 * @return ParseTree or Exception
	 */
	public IValue parse(IString moduleName, IValue start, IString input, ISourceLocation location, boolean allowAmbiguity, Frame currentFrame, RascalExecutionContext rex) {
		return parse(moduleName, start, vf.mapWriter().done(), location, input.getValue().toCharArray(), allowAmbiguity, currentFrame, rex);
	}
	
	/**
	 * Parse text at a location
	 * @param moduleName 	Name of module in which grammar is defined
	 * @param start			Start symbol
	 * @param currentFrame 	Frame that calls parse function
	 * @param rex 			RascalExecutionContext
	 * @param input			To be parsed as location
	 * @return ParseTree or Exception
	 */
	public IValue parse(IString moduleName, IValue start, ISourceLocation location, boolean allowAmbiguity, Frame currentFrame, RascalExecutionContext rex) {
		try {
			char[] input = getResourceContent(location);
			return parse(moduleName, start,  vf.mapWriter().done(), location, input, allowAmbiguity, currentFrame, rex);
		}
		catch(IOException ioex){
			throw RascalRuntimeException.io(vf.string(ioex.getMessage()), currentFrame);
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
	 * @param rex 			RascalExecutionContext
	 * @return
	 */
	public IValue parse(IString moduleName, IValue start, IMap robust, ISourceLocation location, char[] input, boolean allowAmbiguity, Frame currentFrame, RascalExecutionContext rex) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified, currentFrame);
		
		IMap syntax = (IMap) ((IConstructor) start).get(1);
		try {
			return parseObject(moduleName, startSort, robust, location, input, syntax, allowAmbiguity, rex);
		}
		catch (ParseError pe) {
			ISourceLocation errorLoc = vf.sourceLocation(vf.sourceLocation(pe.getLocation()), pe.getOffset(), pe.getLength(), pe.getBeginLine() + 1, pe.getEndLine() + 1, pe.getBeginColumn(), pe.getEndColumn());
			throw RascalRuntimeException.parseError(errorLoc, currentFrame);
		}
		catch (Ambiguous e) {
			ITree tree = e.getTree();
			throw RascalRuntimeException.ambiguity(e.getLocation(), 
					vf.string(SymbolAdapter.toString(TreeAdapter.getType(tree), false)), 
					vf.string(TreeAdapter.yield(tree)), currentFrame);
		}
		catch (UndeclaredNonTerminalException e){
			throw new InternalCompilerError("Undeclared non-terminal: " + e.getName() + ", " + e.getClassName(), currentFrame);
		}
		catch (Exception e) {
		    e.printStackTrace();
			throw new InternalCompilerError("Unexpected exception:" + e, currentFrame);
		}
	}
	
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
		   throw RascalRuntimeException.invalidArgument(start, currentFrame, "A reified type is required instead of " + reified);
		}
		
		Type nt = reified.getTypeParameters().getFieldType(0);
		
		if (!(nt instanceof NonTerminalType)) {
			throw RascalRuntimeException.invalidArgument(start, currentFrame, "A non-terminal type is required instead of  " + nt);
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
	 * @param rex 			RascalExecutionContext
	 * @return				ParseTree or Exception
	 * @throws IOException 
	 */
	public ITree parseObject(IString moduleName, IConstructor startSort, IMap robust, ISourceLocation location, char[] input, IMap syntax, boolean allowAmbiguity, RascalExecutionContext rex) throws IOException{
		IGTD<IConstructor, ITree, ISourceLocation> parser = getObjectParser(moduleName, startSort, location, syntax, rex);
		
		String name = ""; moduleName.getValue();
		if (SymbolAdapter.isStartSort(startSort)) {
			name = "start__";
			startSort = SymbolAdapter.getStart(startSort);
		}
		
		if (SymbolAdapter.isSort(startSort) || SymbolAdapter.isLex(startSort) || SymbolAdapter.isLayouts(startSort)) {
			name += SymbolAdapter.getName(startSort);
		}

		return (ITree) parser.parse(
		    name, 
		    location.getURI(), 
		    input, 
		    new RascalFunctionActionExecutor(rex), 
		    new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), 
		    new UPTRNodeFactory(allowAmbiguity), (IRecoverer<IConstructor>) null
		    );
	}
	
	private ParserGenerator parserGenerator;
	
	public ParserGenerator getParserGenerator(RascalExecutionContext rex) throws IOException {
		//rex.startJob("Compiled -- Loading parser generator", 40);
		if(parserGenerator == null ){
		  if (isBootstrapper()) {
		     throw new InternalCompilerError("Cyclic bootstrapping is occurring, probably because a module in the bootstrap dependencies is using the concrete syntax feature.");
		  }
		 
		  parserGenerator = new ParserGenerator(rex);
		}
		//rex.endJob(true);
		return parserGenerator;
	}
	
	private char[] getResourceContent(ISourceLocation location) throws IOException{
		try (Reader in = URIResolverRegistry.getInstance().getCharacterReader(location)) {
			return InputConverter.toChar(in);
		}
	}
	  
	  public IGTD<IConstructor, ITree, ISourceLocation> getParser(String name, IValue start, ISourceLocation loc, IMap syntax, RascalExecutionContext rex) throws IOException {
		if(getBootstrap(name, rex)){
			return new RascalParser();
		}
		
	    ParserGenerator pg = getParserGenerator(rex);
	   
	    try {
	        return parserCache.get(syntax, k -> pg.getNewParser(rex.getMonitor(), loc, name, syntax, rex))
	            .newInstance();
	    } 
	    catch (InstantiationException | IllegalAccessException | ExceptionInInitializerError e) {
	        throw new InternalCompilerError(e.getMessage() + e);
	    } 
	  }
	  
	  private boolean getBootstrap(String moduleName, RascalExecutionContext rex) { 
		  return rex.bootstrapParser(moduleName); 
	  }
	 
	  // Rascal library function (interpreter version)
	  public ITree parseFragment(IString name, IMap moduleTags, IValue start, IConstructor tree, ISourceLocation loc, IMap grammar, IEvaluatorContext ctx) throws IOException{
	      IMapWriter w = vf.mapWriter();
	      w.insert(vf.tuple(name, moduleTags));

	      RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(new PathConfig(), ctx.getStdOut(), ctx.getStdErr())
	              .withModuleTags(w.done())
	              .build();

	      rex.getConfiguration().setRascalJavaClassPathProperty(ctx.getConfiguration().getRascalJavaClassPathProperty());

	      return parseFragment1(name, start, tree, loc, grammar, rex);
	  }
		
	  // Rascal library function (compiler version)
	  // TODO moduleTags is only needed in interpreted version
	  public ITree parseFragment(IString name, IMap moduleTags, IValue start, IConstructor tree, ISourceLocation loc, IMap grammar, RascalExecutionContext rex) throws IOException{
	      IMapWriter w = vf.mapWriter();
          w.insert(vf.tuple(name, moduleTags));
          
	      RascalExecutionContext rex2 = RascalExecutionContextBuilder.normalContext(rex.getPathConfig(), rex.getStdOut(), rex.getStdErr())
	              .withModuleTags(w.done())
	              .build();
	      
		  return parseFragment1(name, start, tree, loc, grammar, rex2);
	  }
	
	/**
	 * This function will reconstruct a parse tree of a single nested concrete syntax fragment
	 * that has been parsed and its original flat literal string is replaced by a fully structured parse tree.
	 * @param moduleName	Name of module in which grammar is defined
	 * @param start			Start symbol
	 * @param tree			Paree tree to be reconstructed
	 * @param uri			Location where input text comes from
	 * @param syntax		Syntax as map[Symbol,Production]
	 * @param rex 			RascalExecutionContext
	 * @return				ParseTree or Exception
	 * @throws IOException 
	 * 
	 */

	ITree parseFragment1(IString name, IValue start, IConstructor tree, ISourceLocation uri, IMap grammar, RascalExecutionContext rex) throws IOException {
		IConstructor prod = (IConstructor) tree.get("prod");
		IConstructor def = (IConstructor) prod.get("def");
		if(def.getName().equals("label")){
			String defName = ((IString) def.get("name")).getValue();
			boolean b = defName.equals("$parsed");
			if(b) return (ITree) tree;
		}
		
	    ITree symTree = TreeAdapter.getArg((ITree) tree, "symbol");
	    ITree lit = TreeAdapter.getArg((ITree) tree, "parts");
	    Map<String, ITree> antiquotes = new HashMap<String,ITree>();
	    
	    IGTD<IConstructor, ITree, ISourceLocation> parser = getBootstrap(name.getValue(), rex) ? new RascalParser() : getParser(name.getValue(), start, TreeAdapter.getLocation((ITree) tree), grammar, rex);
	    
	    try {
	      String parserMethodName = getParserGenerator(rex).getParserMethodName(symTree, rex);
	      DefaultNodeFlattener<IConstructor, ITree, ISourceLocation> converter = new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>();
	      UPTRNodeFactory nodeFactory = new UPTRNodeFactory(false);
	    
	      char[] input = replaceAntiQuotesByHoles(lit, antiquotes, rex);
	      
	      ITree fragment = (ITree) parser.parse(parserMethodName, uri.getURI(), input, converter, nodeFactory);
	      fragment = replaceHolesByAntiQuotes(fragment, antiquotes);
	      return fragment;
	    }
	    catch (ParseError e) {
	      ISourceLocation loc = TreeAdapter.getLocation((ITree) tree);
	      ISourceLocation src = vf.sourceLocation(loc, loc.getOffset() + e.getOffset(), loc.getLength(), loc.getBeginLine() + e.getBeginLine() - 1, loc.getEndLine() + e.getEndLine() - 1, loc.getBeginColumn() + e.getBeginColumn(), loc.getBeginColumn() + e.getEndColumn());
	      throw RascalRuntimeException.parseError(src, null);
	    }
	    catch (Ambiguous e) {
            ITree tree1 = e.getTree();
            throw RascalRuntimeException.ambiguity(e.getLocation(), 
                    vf.string(SymbolAdapter.toString(TreeAdapter.getType(tree1), false)), 
                    vf.string(TreeAdapter.yield(tree)), null);
        }
	  }
	  
	  private char[] replaceAntiQuotesByHoles(ITree lit, Map<String, ITree> antiquotes, RascalExecutionContext rex) throws IOException {
	    IList parts = TreeAdapter.getArgs(lit);
	    StringBuilder b = new StringBuilder();
	    
	    for (IValue elem : parts) {
	    	ITree part = (ITree) elem;
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
	        b.append(createHole(part, antiquotes, rex));
	      }
	    }
	    return b.toString().toCharArray();
	  }

	  public String createHole(ITree part, Map<String, ITree> antiquotes, RascalExecutionContext rex) throws IOException {
	    String ph = getParserGenerator(rex).createHole(part, antiquotes.size(), rex);
	    antiquotes.put(ph, part);
	    return ph;
	  }

	  private ITree replaceHolesByAntiQuotes(ITree fragment, final Map<String, ITree> antiquotes) {
		  return (ITree) fragment.accept(new IdentityTreeVisitor<InternalCompilerError>() {

			  @Override
			  public ITree visitTreeAppl(ITree tree)  {
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
				  return (ITree) antiquotes.get(TreeAdapter.yield(tree)).asAnnotatable().setAnnotation("holeType", type);
			  }

			  private IConstructor retrieveHoleType(ITree tree) {
				  IConstructor prod = TreeAdapter.getProduction(tree);
				  ISet attrs = ProductionAdapter.getAttributes(prod);

				  for (IValue attr : attrs) {
					  if (((IConstructor) attr).getConstructorType() == RascalValueFactory.Attr_Tag) {
						  IValue arg = ((IConstructor) attr).get(0);

						  if (arg.getType().isNode() && ((INode) arg).getName().equals("holeType")) {
							  return (IConstructor) ((INode) arg).get(0);
						  }
					  }
				  }

				  throw new InternalCompilerError("expected to find a holeType, but did not: " + tree);
			  }

			  @Override
			  public ITree visitTreeAmb(ITree arg)  {
				  ISetWriter w = vf.setWriter();
				  for (IValue elem : TreeAdapter.getAlternatives(arg)) {
					  w.insert(elem.accept(this));
				  }
				  return (ITree) arg.set("alternatives", w.done());
			  }
		  });
	  }
}
