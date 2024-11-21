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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.library.util.PathConfig.RascalConfigMode;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.repl.LimitedLineWriter;
import org.rascalmpl.repl.LimitedWriter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;

public class Reflective {
	protected final IValueFactory values;
	private final IRascalMonitor monitor;

	public Reflective(IValueFactory values, IRascalMonitor monitor) {
		super();
		this.values = values;
		this.monitor = monitor;
	}
	
	public IString getRascalVersion() {
	    return values.string(RascalManifest.getRascalVersionNumber());
	}
	
	public IString getLineSeparator() {
        return values.string(System.lineSeparator());
    }
	
	public IConstructor getProjectPathConfig(ISourceLocation projectRoot, IConstructor mode) {
	    try {
	        if (URIResolverRegistry.getInstance().exists(projectRoot)) {
	            return PathConfig.fromSourceProjectRascalManifest(projectRoot, mode.getName().equals("compiler") ? RascalConfigMode.COMPILER :  RascalConfigMode.INTERPETER).asConstructor();
	        }
	        else {
	            throw new FileNotFoundException(projectRoot.toString());
	        }
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
        }
    }
	
	IEvaluator<?> getDefaultEvaluator(OutputStream stdout, OutputStream stderr) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		Evaluator evaluator = new Evaluator(vf, System.in, stderr, stdout, root, heap, monitor);
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		return evaluator;
	}
    
    
	public IList evalCommands(IList commands, ISourceLocation loc) {
	    OutputStream out = new ByteArrayOutputStream();
	    OutputStream err = new ByteArrayOutputStream();
		IListWriter result = values.listWriter();
		IEvaluator<?> evaluator = getDefaultEvaluator(out, err);
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
				errOut = err.toString().substring(errOffset);
				errOffset += errOut.length();
				errOut += e.getMessage();
				exc = true;
			}
			String output = out.toString().substring(outOffset);
			outOffset += output.length();
			if (!exc) {
				errOut += err.toString().substring(errOffset);
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
	    
	    Type type = result.getStaticType();

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
	      catch (/*IOLimitReachedException*/ RuntimeException e) {
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
	
	protected char[] getResourceContent(ISourceLocation location) throws IOException{
		char[] data;
		Reader textStream = null;
		
		URIResolverRegistry resolverRegistry = URIResolverRegistry.getInstance();
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
	
	public IValue parseModuleWithSpaces(ISourceLocation loc) {
		IActionExecutor<ITree> actions = new NoActionExecutor();	
		try {
			return new RascalParser().parse(Parser.START_MODULE, loc.getURI(), getResourceContent(loc), actions, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}

	// Note -- copy in ReflectiveCompiled
	
	public IBool inCompiledMode() { return values.bool(false); }
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue watch(IValue tp, IValue val, IString name){
		return watch(tp, val, name, values.string(""));
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
	public IValue watch(IValue tp, IValue val, IString name, IValue suffixVal){
		ISourceLocation watchLoc;
		String suffix = stripQuotes(suffixVal);
		String name1 = stripQuotes(name);

		String path = "watchpoints/" + (suffix.length() == 0 ? name1 : (name1 + "-" + suffix)) + ".txt";
		try {
			watchLoc = values.sourceLocation("home", null, path, null, null);
		} catch (URISyntaxException e) {
			throw RuntimeExceptionFactory.io(values.string("Cannot create |home:///" + name1 + "|"), null, null);
		}
		Prelude.writeTextValueFile(values, false, watchLoc, val);
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

	public IInteger getHashCode(IValue v) {
		return values.integer(v.hashCode());
	}
	
	public void throwNullPointerException() {
        throw new NullPointerException();
    }
}
