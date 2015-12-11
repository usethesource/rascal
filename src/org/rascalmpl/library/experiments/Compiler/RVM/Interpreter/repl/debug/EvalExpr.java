package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
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
	
    
	private static IValue baseValue(String base, Frame currentFrame){
		if(base.matches("[0-9]+")){
			return vf.integer(Integer.valueOf(base));
		}
		if(base.startsWith("\"")){
			return vf.string(base.substring(1, base.length()-1));
		}
		return currentFrame.getVars().get(base);
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
	
	static IValue eval(String expr, Frame currentFrame){
		Matcher matcher = exprPat.matcher(expr);

		try {
			if(matcher.matches()){
				IValue base = baseValue(matcher.group("base"), currentFrame);

				if(matcher.end("subscript1") > 0){
					base = subscript(base, baseValue(matcher.group("subscript1"), currentFrame));
				}
				else if(matcher.end("field1") > 0){
					base = select(base, matcher.group("field1"));
				}
				
				if(matcher.end("subscript2") > 0){
					base = subscript(base, baseValue(matcher.group("subscript2"), currentFrame));
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
