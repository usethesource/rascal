package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

public class Types {
	private final TypeFactory tf = TypeFactory.getInstance();
    private final TypeReifier tr;
	
	public Types(IValueFactory vf) {
		this.tr = new TypeReifier(vf);
	}
	
	public IValue typeToValue(Type t, RascalExecutionContext rex) {
	    // TODO: check rex.getSymbolDefinitions(); don't know if this is correct
	    System.err.println("TODO: check correctness of typeToValue here");
		return tr.typeToValue(t, rex.getTypeStore(), rex.getSymbolDefinitions());
	}
	
	public Type symbolToType(IConstructor symbol) {
	    return tr.symbolToType(symbol);
	}
	
	public Type loadProduction(IConstructor prod) {
	    return tr.productionToConstructorType(prod);
        }

	private static Pattern identifier = Pattern.compile("\\w+");
	private static Pattern openBracket = Pattern.compile("\\[");
	private static Pattern closeBracket = Pattern.compile("\\]");
	private static Pattern openPar = Pattern.compile("\\(");
	private static Pattern closePar = Pattern.compile("\\)");
	private static Pattern comma = Pattern.compile(",");
	private static Pattern whiteSpace = Pattern.compile("\\s*");
	
	static String preprocess(String input){
		return input.replaceAll("(\\[|,|\\]|\\(|\\))", " $1 ");
	}
	Type parseType(String input){
		Scanner s = new Scanner(preprocess(input));
		return parseType(s);
	} 
	
	public Type getFunctionType(String signature){
		Scanner s = new Scanner(preprocess(signature));
		try {
			Type returnType = parseType(s);
			s.next(identifier);	// skip function name
			s.next(openPar);
			Object[] argumentTypes = parseFields(s);
			s.next(closePar);
			s.close();
			return RascalTypeFactory.getInstance().functionType(returnType, tf.tupleType(argumentTypes), null);
		} catch (NoSuchElementException e){
			if(s.hasNext()){
				String tok = s.next();
				s.close();
				throw new RuntimeException("Malformed function signature: " + signature + " at '" + tok + "'");
			} else {
				s.close();
				throw new RuntimeException("Malformed function signature: " + signature + " near end, may be missing ')'");
			}
		}
	}
	
	public String getFunctionName(String signature){
		Scanner s = new Scanner(preprocess(signature));
		try {
			parseType(s);	// skip function type
			String name = s.next(identifier);
			s.close();
			return name;
		} catch (NoSuchElementException e){
			if(s.hasNext()){
				String tok = s.next();
				s.close();
				throw new RuntimeException("Malformed function signature: " + signature + " at '" + tok + "'");
			} else {
				s.close();
				throw new RuntimeException("Malformed function signature: " + signature + " near end, may be missing ')'");
			}
		}
	}
	
	private Type parseType(Scanner s){
		String kw = s.next(identifier);
		Type res;
		switch(kw){
		case "int": return tf.integerType();
		case "real": return tf.realType();
		case "rat": return tf.rationalType();
		case "bool": return tf.boolType();
		case "datetime": return tf.dateTimeType();
		case "num": return tf.numberType();
		case "loc": return tf.sourceLocationType();
		case "void": return tf.voidType();
		case "value": return tf.valueType();
		case "node": return tf.nodeType();
		case "str": return tf.stringType();
		case "set":
		case "list":
				s.next(openBracket);
				Type elmType = parseType(s);
				s.skip(whiteSpace);
				s.next(closeBracket);
				return kw.equals("list") ? tf.listType(elmType) : tf.setType(elmType);
		case "tuple":
		case "rel":
		case "lrel":
			    s.next(openBracket);
			    Object[] flds = parseFields(s);
			    s.next(closeBracket);
			   switch(kw){
			   case "tuple": return tf.tupleType(flds);
			   case "rel": return tf.relType(flds);
			   case "lrel": return tf.lrelType(flds);
			   }
		case "map":
			s.next(openBracket);
		    flds = parseFields(s);
		    s.next(closeBracket);
		    if(flds.length == 4){
		    	return tf.mapType((Type)flds[0],  (Type)flds[2]);
		    } else {
		    	throw new NoSuchElementException();
		    }
		default:
			res = tf.abstractDataType(RascalValueFactory.getStore(), kw);
			return res;
			//throw new CompilerError("Unimplemented: " + kw);	   
		}
	}
	
	private Object[] parseFields(Scanner s){
		ArrayList<Object> flds = new ArrayList<>();
	    do {
	    	if(s.hasNext(comma)){
	    		s.next(comma);
	    	}
	    	if(s.hasNext(closeBracket) || s.hasNext(closePar)){
	    		break;
	    	}
	    	flds.add(parseType(s));
	    	if(s.hasNext(identifier)){
	    		flds.add(s.next());
	    	} else {
	    		flds.add("");
	    	}
	    }  while(s.hasNext(comma));
	    return flds.toArray();
	}

	
	// End deprecated

}
