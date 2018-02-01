package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class EvalExpr {
	
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private static final String intLiteral = "[0-9]+";
	private static final String strLiteral = "\"[^\"]*\"";
	private static final String variable = "[a-zA-Z][a-zA-Z0-9_]*";
	private static final String baseExpr = intLiteral + "|" + strLiteral + "|" + variable;
	
	private static final String subscript1 = "\\[(?<subscript1>" + baseExpr + ")\\]";
	private static final String subscript2 = "\\[(?<subscript2>" + baseExpr + ")\\]";
	private static final String field1 = "\\.(?<field1>" + baseExpr + ")";
	private static final String field2 = "\\.(?<field2>" + baseExpr + ")";
	private static final String expr = "(?<base>" + baseExpr + ")(" + subscript1 + "|" + field1 + ")?(" +  subscript2 + "|" + field2 + ")?" ;
	private static final Pattern exprPat = Pattern.compile(expr);
	
	private static IValue baseValue(String base, RVMCore rvm, Frame currentFrame){
		if(base.matches("[0-9]+")){
			return vf.integer(Integer.valueOf(base));
		}
		if(base.startsWith("\"")){
			return vf.string(base.substring(1, base.length()-1));
		}
		return getVar(base, rvm, currentFrame);
	}
	
	private static IValue getVar(String base, RVMCore rvm, Frame currentFrame){
		for(Frame f = currentFrame; f != null; f = f.previousCallFrame){
			IValue val = f.getVars().get(base);
			if(val != null){
				return val;
			}
		}
		
		Map<IValue, IValue> mvars = rvm.getModuleVariables();
		String moduleName = ":" + base;
		IValue foundKey = null;
		for(IValue ikey : mvars.keySet()){
			String key = ((IString) ikey).getValue();
			if(key.endsWith(moduleName)){
				if(foundKey != null){	// Same module var name can be used in different modules
					System.err.println(base + " is ambiguous, using " + foundKey);
				} else {
					foundKey = ikey;
				}
				return mvars.get(ikey);
			}
		}
		return foundKey == null ? null : mvars.get(foundKey);
	}
	
	private static IValue subscript(IValue base, IValue index){
		if(base.getType().isList()){
			IList lst = (IList) base;
			if(index.getType().isInteger()){
				int i = ((IInteger) index).intValue();
				return lst.get(i);
			}
		}
		if(base.getType().isMap()){
			IMap map = (IMap) base;
			return map.get(index);
		}
		throw new RuntimeException("subscript: " + base);
	}
	
	private static IValue select(IValue base, String field){
		if(base.getType().isAbstractData()){
			IConstructor cons = (IConstructor) base;
			return cons.get(field);
		}
		throw new RuntimeException("select: " + base);
	}
	
	static IValue eval(String expr, RVMCore rvm, Frame currentFrame){
		Matcher matcher = exprPat.matcher(expr);

		try {
			if(matcher.matches()){
				IValue base = baseValue(matcher.group("base"), rvm, currentFrame);

				if(matcher.end("subscript1") > 0){
					base = subscript(base, baseValue(matcher.group("subscript1"), rvm, currentFrame));
				}
				else if(matcher.end("field1") > 0){
					base = select(base, matcher.group("field1"));
				}
				
				if(matcher.end("subscript2") > 0){
					base = subscript(base, baseValue(matcher.group("subscript2"), rvm, currentFrame));
				}
				else if(matcher.end("field2") > 0){
					base = select(base, matcher.group("field2"));
				}
				
				return base;
			}
		}
		catch (Exception e){
			
		}
		return null;
	}
}
