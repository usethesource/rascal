/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.library.experiments.Compiler.Rascal2muRascal;

import java.net.URISyntaxException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import org.rascalmpl.library.cobra.RandomType;
import org.rascalmpl.library.cobra.util.RandomUtil;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

/*
 * Generate random value for a given type. Used in random testing framework
 *
 */
public class RandomValueTypeVisitor implements ITypeVisitor<IValue, RuntimeException> {

	private static final Random stRandom = new Random();

	private final IValueFactory vf;
	private final TypeFactory tf = TypeFactory.getInstance();
	//private final ModuleEnvironment rootEnv;
	TypeStore definitions;
	private final int maxDepth;
	private final Map<Type, Type> typeParameters;

	public RandomValueTypeVisitor(IValueFactory vf, int maxDepth, Map<Type, Type> typeParameters, TypeStore definitions) {
		this.vf = vf;
		//this.rootEnv = rootEnv;
		this.maxDepth = maxDepth;
		//this.generators = generators;
		this.typeParameters = typeParameters;
		this.definitions = definitions;
	}

	private RandomValueTypeVisitor descend() {
		RandomValueTypeVisitor visitor = new RandomValueTypeVisitor(vf, maxDepth - 1, typeParameters, definitions);
		return visitor;
	}

	public IValue generate(Type t) {
		return t.accept(this);
	}

	private IValue genSet(Type type) {
		ISetWriter writer = vf.setWriter(); // type.writer(vf);

		if (maxDepth <= 0 || (stRandom.nextInt(2) == 0)) {
			return writer.done();
		} else {
			RandomValueTypeVisitor visitor = descend();
			ISet set = (ISet) visitor.generate(type);

			IValue element = null;
			int recursionGuard = 0; // Domain of set can be small.
			while ((element == null || set.contains(element))
					&& recursionGuard < 1000) {
				recursionGuard += 1;
				element = visitor.generate(type.getElementType());
			}

			writer.insertAll(set);

			if (element != null) {
				writer.insert(element);
			}
			return writer.done();
		}
	}

	@Override
	public IValue visitAbstractData(Type type) {
		LinkedList<Type> alternatives = new LinkedList<Type>();
		alternatives.addAll(definitions.lookupAlternatives(type));				
		Collections.shuffle(alternatives);
		for (Type pick : alternatives) {
			IConstructor result = (IConstructor) this.generate(pick);
			if (result != null) {
				RandomValueTypeVisitor visitor = descend();
				Map<String, Type> annotations = definitions.getAnnotations(type);
				for (Map.Entry<String, Type> entry : annotations.entrySet()) {
					IValue value = visitor.generate(entry.getValue());
					if (value == null) {
						return null;
					}
					result = result.asAnnotatable().setAnnotation(entry.getKey(), value);
				}

				return result;

			}
		}
		return null;
	}

	@Override
	public IValue visitAlias(Type type) {
		// Het is niet mogelijk om een circulaire alias te maken dus dit kost
		// geen diepte.
		return this.generate(type.getAliased());
	}

	@Override
	public IValue visitBool(Type boolType) {
		return vf.bool(stRandom.nextBoolean());
	}

	@Override
	public IValue visitConstructor(Type type) {
		/*
		 * Following the common definition of depth of tree, the depth of an
		 * algebraic datatype with zero arguments is 0 and the depth of an
		 * alternative with more than 0 arguments is defined as the maximum
		 * depth of the list of arguments plus 1.
		 */
		Map<String, Type> kwParams = definitions.getKeywordParameters(type);
		
		if (type.getArity() == 0 && kwParams.size() == 0) { 
			return vf.constructor(type);
		} 
		else if (this.maxDepth <= 0) {
			return null;
		}

		RandomValueTypeVisitor visitor = descend();

		LinkedList<IValue> values = new LinkedList<IValue>();
		for (int i = 0; i < type.getArity(); i++) {
			Type fieldType = type.getFieldType(i);
			IValue argument = visitor.generate(fieldType);
			if (argument == null) {
				return null;
			}
			values.add(argument);
		}
		IValue[] params = values.toArray(new IValue[values.size()]);
		if (stRandom.nextBoolean() && kwParams.size() > 0) {
			Map<String,IValue> kwp = new HashMap<>();
			for (String kw:  kwParams.keySet()) {
				if (stRandom.nextBoolean()) {
					continue;
				}
				Type fieldType = kwParams.get(kw);
				IValue argument = visitor.generate(fieldType);
				if (argument == null) {
					return null;
				}
				kwp.put(kw, argument);
			}
			return vf.constructor(type, params, kwp);
		}
		else {
			return vf.constructor(type, params);
		}
	}

	@Override
	public IValue visitDateTime(Type type) {
		Calendar cal = Calendar.getInstance();
		int milliOffset = stRandom.nextInt(1000) * (stRandom.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.MILLISECOND, milliOffset);
		int second = stRandom.nextInt(60) * (stRandom.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.SECOND, second);
		int minute = stRandom.nextInt(60) * (stRandom.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.MINUTE, minute);
		int hour = stRandom.nextInt(60) * (stRandom.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.HOUR_OF_DAY, hour);
		int day = stRandom.nextInt(30) * (stRandom.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.DAY_OF_MONTH, day);
		int month = stRandom.nextInt(12) * (stRandom.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.MONTH, month);
		
		// make sure we do not go over the 4 digit year limit, which breaks things
		int year = stRandom.nextInt(5000) * (stRandom.nextBoolean() ? -1 : 1);
		
		// make sure we don't go into negative territory
		if (cal.get(Calendar.YEAR) + year < 1)
			cal.add(Calendar.YEAR, 1);
		else
			cal.add(Calendar.YEAR, year);
		
		return vf.datetime(cal.getTimeInMillis());
	}

	@Override
	public IValue visitExternal(Type externalType) {
		throw new CompilerError("Can't handle ExternalType.");
	}

	@Override
	public IValue visitInteger(Type type) {
	  if (stRandom.nextFloat() > 0.6) {
	    return vf.integer(stRandom.nextInt());
	  }
	  if (stRandom.nextFloat() > 0.6) {
	    return vf.integer(stRandom.nextInt(10));
	  }
	  if (stRandom.nextFloat() > 0.6) {
	    return vf.integer(-stRandom.nextInt(10));
	  }
	  return vf.integer(0);
	}
	
	private IValue genList(Type type){
		IListWriter writer = vf.listWriter(); // type.writer(vf);

		if (maxDepth <= 0 || (stRandom.nextInt(2) == 0)) {
			return writer.done();
		} else {
			RandomValueTypeVisitor visitor = descend();
			IValue element = visitor.generate(type.getElementType());
			if (element != null) {
				writer.append(element);
			}
			writer.appendAll((IList) visitor.generate(type));
			return writer.done();
		}
	}

	@Override
	public IValue visitList(Type type) {
		return genList(type);
	}

	@Override
	public IValue visitMap(Type type) {
		IMapWriter writer = vf.mapWriter(); // type.writer(vf);

		if (maxDepth <= 0 || (stRandom.nextInt(2) == 0)) {
			return writer.done();
		} else {

			RandomValueTypeVisitor visitor = descend();
			IValue key = visitor.generate(type.getKeyType());
			IValue value = visitor.generate(type.getValueType());

			if (key != null && value != null) {
				writer.put(key, value);
			}

			writer.putAll((IMap) visitor.generate(type));
			return writer.done();
		}
	}

	@Override
	public IValue visitNode(Type type) {
	  String str = stRandom.nextBoolean() ? RandomUtil.string(stRandom, stRandom.nextInt(5)) : RandomUtil.stringAlpha(stRandom, stRandom.nextInt(5));


	  int arity = maxDepth <= 0 ? 0: stRandom.nextInt(5);
		IValue[] args = new IValue[arity];
		for (int i = 0; i < arity; i++) {
			args[i] = descend().generate(tf.valueType());
		}
		if (stRandom.nextBoolean()) {
			int kwArity = 1 + stRandom.nextInt(5);
			Map<String, IValue> kwParams = new HashMap<>(kwArity);
			for (int i = 0; i < kwArity; i++) {
				String name = "";
				while (name.isEmpty()) {
					// names have to start with alpha character
					name = RandomUtil.stringAlpha(stRandom, 3); 
				}
				name += RandomUtil.stringAlphaNumeric(stRandom, 4);
				IValue argument = descend().generate(tf.valueType());
				if (argument == null) {
					return null;
				}
				kwParams.put(name, argument);
			}
			return vf.node(str, args, kwParams);
		}
		return vf.node(str, args);	
	}

	@Override
	public IValue visitNumber(Type type) {
		switch (stRandom.nextInt(3)) {
		case 0:
			return this.visitInteger(type);
		case 1:
			return this.visitReal(type);
		default:
			return this.visitRational(type);
		}
	}

	@Override
	public IValue visitParameter(Type parameterType) {
		// FIXME Type parameters binden aan echte type van actual value in call.
		Type type = typeParameters.get(parameterType);
		if(type == null){
			throw new IllegalArgumentException("Unbound type parameter " + parameterType);
		}
		return this.generate(type);
	}

	@Override
	public IValue visitRational(Type type) {
		return vf.rational(stRandom.nextInt(), stRandom.nextInt());
	}

	@Override
	public IValue visitReal(Type type) {
	  if (stRandom.nextDouble() > 0.6) {
	    return vf.real(stRandom.nextDouble());
	  }
	  if (stRandom.nextDouble() > 0.6) {
	    return vf.real(-stRandom.nextDouble());
	  }
	  return vf.real(0.0);
	}

	@Override
	public IValue visitSet(Type type) {
		return genSet(type);
	}

	@Override
	public IValue visitSourceLocation(Type type) {
	  try {
	    if (maxDepth <= 0) {
	      return vf.sourceLocation("tmp","","");
	    }
	    else {

	      String path = stRandom.nextDouble() < 0.9 ? RandomUtil.stringAlphaNumeric(stRandom, stRandom.nextInt(5)) : RandomUtil.string(stRandom, stRandom.nextInt(5));
	      String nested = "";

	      if (stRandom.nextBoolean()) {
	        RandomValueTypeVisitor visitor = descend();
	        ISourceLocation loc = (ISourceLocation) visitor.generate(type);
	        nested = loc.getPath();
	      }

	      path = path.startsWith("/") ? path : "/" + path;
	      if (nested.length() > 0 && !nested.equals("/")) {
	        path = nested + path;
	      }
	      return vf.sourceLocation("tmp","",path);
	    }
	  } catch (URISyntaxException e) {
	    // generated illegal URI?
	    try {
	      return vf.sourceLocation("tmp","","");
	    } catch (URISyntaxException e1) {
	      throw new RuntimeException(e1); // this sloc should never throw.
	    }
	  }

	}

	@Override
	public IValue visitString(Type type) {
		if (stRandom.nextBoolean() || maxDepth <= 0) {
			return vf.string("");
		}
		String result = RandomUtil.string(stRandom, 1 + stRandom.nextInt(maxDepth + 3));
		// make sure we are not generating very strange sequences
		result = Normalizer.normalize(result, Form.NFC);
		return vf.string(result);
	}

	@Override
	public IValue visitTuple(Type type) {
		RandomValueTypeVisitor visitor = descend();

		IValue[] elems = new IValue[type.getArity()];
		for (int i = 0; i < type.getArity(); i++) {
			Type fieldType = type.getFieldType(i);
			IValue element = visitor.generate(fieldType);
			if (element == null) {
				return null;
			}
			elems[i] = visitor.generate(fieldType);
		}
		return vf.tuple(elems);
	}

	@Override
	public IValue visitValue(Type type) {
		RandomType rt = new RandomType();
		return this.generate(rt.getType(maxDepth));
	}

	@Override
	public IValue visitVoid(Type type) {
		throw new CompilerError("void has no values.");
	}
	
	public static void main(String[] args) {
		RandomValueTypeVisitor r = new RandomValueTypeVisitor(ValueFactoryFactory.getValueFactory(), 3, null, null);
		
	    Type intType = r.tf.integerType();
	    
		System.out.println(r.generate(r.tf.setType(intType)));
	}

}
