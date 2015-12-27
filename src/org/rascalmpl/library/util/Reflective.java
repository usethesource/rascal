/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.SourceLocationListContributor;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ToplevelType;
import org.rascalmpl.repl.LimitedLineWriter;
import org.rascalmpl.repl.LimitedWriter;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.StandardTextWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class Reflective {
	protected final IValueFactory values;
	private Evaluator cachedEvaluator;
	private int robin = 0;
	protected final Prelude prelude;
	private static final int maxCacheRounds = 500;

	public Reflective(IValueFactory values){
		super();
		this.values = values;
		prelude = new Prelude(values);
	}
	
	
	IEvaluator<?> getDefaultEvaluator(PrintWriter stdout, PrintWriter stderr) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		Evaluator evaluator = new Evaluator(vf, stderr, stdout, root, heap);
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		evaluator.setMonitor(new ConsoleRascalMonitor());
		return evaluator;
	}
    
    
	public IList evalCommands(IList commands, ISourceLocation loc, IEvaluatorContext ctx) {
		StringWriter out = new StringWriter();
		StringWriter err = new StringWriter();
		IListWriter result = values.listWriter();
		IEvaluator<?> evaluator = getDefaultEvaluator(new PrintWriter(out), new PrintWriter(err));
		int outOffset = 0;
		int errOffset = 0;
		
		for (IValue v: commands) {
			String errOut = "";
			boolean exc = false;
			Result<IValue> x = null;
			try {
				x = evaluator.eval(evaluator.getMonitor(), ((IString)v).getValue(), loc);
			}
			catch (Throwable e) {
				errOut = err.getBuffer().substring(errOffset);
				errOffset += errOut.length();
				errOut += e.getMessage();
				exc = true;
			}
			String output = out.getBuffer().substring(outOffset);
			outOffset += output.length();
			if (!exc) {
				errOut += err.getBuffer().substring(errOffset);
				errOffset += errOut.length();
			}
			String s;
			try {
				s = printResult(x);
				ITuple tuple = values.tuple(values.string(s), values.string(output), values.string(errOut));
				result.append(tuple);
			} catch (IOException e) {
				continue;
			}
		}
		IList results = result.done();
		return results;
	}
	
	private final static int LINE_LIMIT = 200;
	private final static int CHAR_LIMIT = LINE_LIMIT * 20;
	  
	private String printResult(IRascalResult result) throws IOException {
	    if (result == null) {
	      return "";
	    }
	    
	    StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);
	    IValue value = result.getValue();
	    
	    if (value == null) {
	    	return "";
	    }
	    
	    Type type = result.getType();

	    StandardTextWriter indentedPrettyPrinter = new StandardTextWriter();
	      
		if (type.isAbstractData() && type.isSubtypeOf(RascalValueFactory.Tree)) {
	    	out.print(type.toString());
	        out.print(": ");
	      // we first unparse the tree
	      out.print("`");
	      TreeAdapter.yield((IConstructor)result.getValue(), true, out);
	      out.println("`");
	      // write parse tree out one a single line for reference
	      out.print("Tree: ");
	      
	      StandardTextWriter singleLinePrettyPrinter = new StandardTextWriter(false);
	      try (Writer wrt = new LimitedWriter(out, CHAR_LIMIT)) {
	    	  singleLinePrettyPrinter.write(value, wrt);
	      }
	      catch (IOLimitReachedException e) {
	          // ignore since this is what we wanted
	      }
	    }
	    else {
	    	out.print(type.toString());
	    	out.print(": ");
	    	// limit both the lines and the characters
	    	try (Writer wrt = new LimitedWriter(new LimitedLineWriter(out,LINE_LIMIT), CHAR_LIMIT)) {
	    		indentedPrettyPrinter.write(value, wrt);
	    	}
	    	catch (IOLimitReachedException e) {
	    	    // ignore since this is what we wanted
	    	}
	    }
	    out.flush();
	    return sw.toString();
	  }
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseCommand(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		IEvaluator<?> evaluator = ctx.getEvaluator();
		return evaluator.parseCommand(evaluator.getMonitor(), str.getValue(), loc);
	}

	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseCommands(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		IEvaluator<?> evaluator = ctx.getEvaluator();
		return evaluator.parseCommands(evaluator.getMonitor(), str.getValue(), loc);
	}
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseModule(ISourceLocation loc, IEvaluatorContext ctx) {
		try {
			Evaluator ownEvaluator = getPrivateEvaluator(ctx);
			return ownEvaluator.parseModule(ownEvaluator.getMonitor(), loc);
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		catch (Throwable e) {
		  throw RuntimeExceptionFactory.javaException(e, null, null);
		}
	}

	private Evaluator getPrivateEvaluator(IEvaluatorContext ctx) {
		if (cachedEvaluator == null || robin++ > maxCacheRounds) {
			robin = 0;
			IEvaluator<?> callingEval = ctx.getEvaluator();
			
			
			GlobalEnvironment heap = new GlobalEnvironment();
			ModuleEnvironment root = heap.addModule(new ModuleEnvironment("$parser$", heap));
			cachedEvaluator = new Evaluator(callingEval.getValueFactory(), callingEval.getStdErr(), callingEval.getStdOut(), root, heap);
			
			// Update the classpath so it is the same as in the context interpreter.
			cachedEvaluator.getConfiguration().setRascalJavaClassPathProperty(ctx.getConfiguration().getRascalJavaClassPathProperty());
		  // clone the classloaders
	    for (ClassLoader loader : ctx.getEvaluator().getClassLoaders()) {
	      cachedEvaluator.addClassLoader(loader);
	    }
		}
		
		return cachedEvaluator;
	}
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseModule(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		Evaluator ownEvaluator = getPrivateEvaluator(ctx);
		return ownEvaluator.parseModule(ownEvaluator.getMonitor(), str.getValue().toCharArray(), loc);
	}
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseModule(ISourceLocation loc, final IList searchPath, IEvaluatorContext ctx) {
    final Evaluator ownEvaluator = getPrivateEvaluator(ctx);

    // add the given locations to the search path
    SourceLocationListContributor contrib = new SourceLocationListContributor("reflective", searchPath);
    ownEvaluator.addRascalSearchPathContributor(contrib);
    
    try { 
      return ownEvaluator.parseModule(ownEvaluator.getMonitor(), loc);
    } catch (IOException e) {
      throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
    }
    catch (Throwable e) {
      throw RuntimeExceptionFactory.javaException(e, null, null);
    }
    finally {
      ownEvaluator.removeSearchPathContributor(contrib);
    }
  }
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue getModuleLocation(IString modulePath, IEvaluatorContext ctx) {
		ISourceLocation uri = ctx.getEvaluator().getRascalResolver().resolveModule(modulePath.getValue());
		if (uri == null) {
		  throw RuntimeExceptionFactory.moduleNotFound(modulePath, ctx.getCurrentAST(), null);
		}
		return uri;
	}
	
	// REFLECT -- copy in ReflectiveCompiled
	public ISourceLocation getSearchPathLocation(IString path, IEvaluatorContext ctx) {
		String value = path.getValue();
		
		if (path.length() == 0) {
			throw RuntimeExceptionFactory.io(values.string("File not found in search path: [" + path + "]"), null, null);
		}
		
		if (!value.startsWith("/")) {
			value = "/" + value;
		}
		
		try {
			ISourceLocation uri = ctx.getEvaluator().getRascalResolver().resolvePath(value);
			if (uri == null) {
				URI parent = URIUtil.getParentURI(URIUtil.createFile(value));
				
				if (parent == null) {
					// if the parent does not exist we are at the root and we look up the first path contributor:
					parent = URIUtil.createFile("/"); 
				}
				
				// here we recurse on the parent to see if it might exist
				ISourceLocation result = getSearchPathLocation(values.string(parent.getPath()), ctx);
				
				if (result != null) {
					String child = URIUtil.getURIName(URIUtil.createFile(value));
					return URIUtil.getChildLocation(result, child);
				}
				
				throw RuntimeExceptionFactory.io(values.string("File not found in search path: " + path), null, null);
			}

			return uri;
		} catch (URISyntaxException e) {
			throw  RuntimeExceptionFactory.malformedURI(value, null, null);
		}
	}
	
	// Note -- copy in ReflectiveCompiled
	
	public IBool inCompiledMode() { return values.bool(false); }
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue watch(IValue tp, IValue val, IString name, IEvaluatorContext ctx){
		return watch(tp, val, name, values.string(""), ctx);
	}
	
	protected String stripQuotes(IValue suffixVal){
		String s1 = suffixVal.toString();
		if(s1.startsWith("\"")){
			s1 = s1.substring(1, s1.length() - 1);
		}
		return s1;
	}
	
	public IString diff(IValue oldVal, IValue newVal){
		return values.string(idiff("", oldVal, newVal));
	}
	
	private String preview(IValue v){
		String s = v.toString();
		if(s.length() < 80){
			return s;
		}
		return s.substring(0, 76) + " ...";
	}
	
	protected String idiff(String indent, IValue oldVal, IValue newVal){
		
		if(!oldVal.getType().equals(newVal.getType())){
			return indent + "old " + oldVal.getType() + ",  new " + newVal.getType();
		}
		if(oldVal.equals(newVal)){
			return "no diff";
		}
		if(oldVal.getType().isString()){
			IString ov = (IString) oldVal;
			IString nv = (IString) newVal;
			String ldiff = (ov.length() == nv.length()) ? "" : ("string length " + ov.length() + " vs " +  nv.length() + "; ");
			for(int i = 0; i < ov.length() && i < nv.length(); i++){
				if(ov.charAt(i) != nv.charAt(i)){
					return indent + ldiff + "diff at index " + i + " in " + preview(ov) + ": " + ov.charAt(i) + " vs " + nv.charAt(i) + "\n" +
						   indent + "old: " + ov + "\n" +
						   indent + "new: " + nv;
				}
			}
		}
		if(oldVal.getType().isSourceLocation()){
			return indent + "old " + oldVal + "\n" +
		           indent + "new " + newVal;
		}
		if(oldVal.getType().isList()){
			IList ov = (IList) oldVal;
			IList nv = (IList) newVal;
			String ldiff = (ov.length() == nv.length()) ? "" : ("size " + ov.length() + " vs " +  nv.length() + "; ");
			for(int i = 0; i < ov.length() && i < nv.length(); i++){
				if(!ov.get(i).equals(nv.get(i))){
					return indent + ldiff + "diff at index " + i + " in list " + preview(ov) + ":\n"
				                  +  idiff(indent + " ", ov.get(i), nv.get(i));
				}
			}
		}
		if(oldVal.getType().isTuple()){
			ITuple ov = (ITuple) oldVal;
			ITuple nv = (ITuple) newVal;
			for(int i = 0; i < ov.arity(); i++){
				if(!ov.get(i).equals(nv.get(i))){
					return indent + "diff at index " + i + " in tuple " + preview(ov) + ":\n"
				                  + idiff(indent + " ", ov.get(i), nv.get(i));
				}
			}
		}
		if(oldVal.getType().isSet()){
			ISet ov = (ISet) oldVal;
			ISet nv = (ISet) newVal;
			String ldiff = (ov.size() == nv.size()) ? "" : ("size " + ov.size() + " vs " +  nv.size() + "; ");
			
			ISet diff1 = ov.subtract(nv);
			String msg1 = diff1.size() == 0 ? "" : indent + "only in old set: " + diff1 + "\n"; //"; ";
			ISet diff2 = nv.subtract(ov);
			String msg2 = diff2.size() == 0 ? "" : indent + "only in new set: " + diff2;
			return ldiff + msg1 + msg2 + "\n";
		}
		
		if(oldVal.getType().isMap()){
			IMap ov = (IMap) oldVal;
			IMap nv = (IMap) newVal;
			String ldiff = (ov.size() == nv.size()) ? "" : ("size " + ov.size() + " vs " +  nv.size() + "; ");
			
			IMap all = ov.join(nv);
			
			String onlyInOld = "";
			String onlyInOldCurrent = "";
			String onlyInNew = "";
			String onlyInNewCurrent = "";
			String diffVal = "";
			String diffValCurrent = "";
			int nDiff = 0;
			for(IValue key : all){
				if(!nv.containsKey(key)){
					if(onlyInOldCurrent.length() > 80){
						onlyInOld += onlyInOldCurrent + "\n" + indent + key;
						onlyInOldCurrent = "";
					} else {
					  onlyInOldCurrent += " " + key;
					}
					continue;
				}
				if(!ov.containsKey(key)){
					if(onlyInNewCurrent.length() > 80){
						onlyInNew += onlyInNewCurrent + "\n" + indent + key;
						onlyInNewCurrent = "";
					} else {
					  onlyInNewCurrent += " " + key;
					}
					continue;
				}
				if(!ov.get(key).equals(nv.get(key))){
					if(nDiff < 10){
						if(diffValCurrent.length() > 80){
							diffVal += diffValCurrent + "\n" + indent + key;
							diffValCurrent = "";
						} else {
							diffValCurrent += " " + key;
						}
						nDiff++;
					}
				}
			}
			
			onlyInOld += onlyInOldCurrent;
			onlyInNew += onlyInNewCurrent;
			diffVal += diffValCurrent;
			String msg1 = onlyInOld.length() == 0 ? "" : "keys only in old map:" + onlyInOld + "; ";
			String msg2 = onlyInNew.length() == 0 ? "" : "keys only in new map:" + onlyInNew + "; ";
			String msg3 = diffVal.length() == 0 ? "" : "some keys with different values:" + diffVal;
			return indent + ldiff + msg1 + msg2 + msg3;
		}
		
		if(oldVal.getType().isNode()){
			INode ov = (INode) oldVal;
			INode nv = (INode) newVal;
			String oldName = ov.getName();
			String newName = nv.getName();
			if(!oldName.equals(newName)){
				return indent + "diff in function symbol: " + oldName + " vs " + newName;
			}
			int oldArity = ov.arity();
			int newArity = nv.arity();
			if(oldArity != newArity){
				return indent + "diff in arity for function symbol " + oldName + ": "+ oldArity + " vs " + newArity;
			}
			for(int i = 0; i < oldArity; i++){
				if(!ov.get(i).equals(nv.get(i))){
					String argId = Integer.toString(i);
					if(ov instanceof IConstructor){
						IConstructor cov = (IConstructor) ov;
						argId = cov.getChildrenTypes().getFieldName(i);
					}
					return indent + "diff at arg " + argId + " for function symbol " + oldName + ": " + preview(ov) + "\n" + idiff(indent + " ", ov.get(i), nv.get(i));
				}
			}
		}
		String sOld = oldVal.toString();
		if(sOld.length() > 20){
			sOld = sOld.substring(0, 20) + "...";
		}
		String sNew = newVal.toString();
		if(sNew.length() > 20){		
			sNew = sNew.substring(0, 20) + "...";
		}
		return indent + "old " + sOld + ", new " + sNew;
		
	}

	// REFLECT -- copy in ReflectiveCompiled
	public IValue watch(IValue tp, IValue val, IString name, IValue suffixVal, IEvaluatorContext ctx){
		ISourceLocation watchLoc;
		String suffix = stripQuotes(suffixVal);
		String name1 = stripQuotes(name);

		String path = "watchpoints/" + (suffix.length() == 0 ? name1 : (name1 + "-" + suffix)) + ".txt";
		try {
			watchLoc = values.sourceLocation("home", null, path, null, null);
		} catch (URISyntaxException e) {
			throw RuntimeExceptionFactory.io(values.string("Cannot create |home:///" + name1 + "|"), null, null);
		}
		prelude.writeTextValueFile(watchLoc, val);
		return val;
	}

	public IInteger getFingerprint(IValue val, IBool concretePatterns){
		return values.integer(ToplevelType.getFingerprint(val, concretePatterns.getValue()));
	}
	
	public IInteger getFingerprint(IValue val, IInteger arity, IBool concretePatterns){
		return values.integer(ToplevelType.getFingerprint(val, concretePatterns.getValue()) << 2 + arity.intValue());
	}
	
	public IInteger getFingerprintNode(INode nd){
		return values.integer(ToplevelType.getFingerprintNode(nd));
	}

}
