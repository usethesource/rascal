package org.rascalmpl.library.lang.json.io;

import static org.rascalmpl.library.lang.json.Factory.JSON;
import static org.rascalmpl.library.lang.json.Factory.JSON_array;
import static org.rascalmpl.library.lang.json.Factory.JSON_boolean;
import static org.rascalmpl.library.lang.json.Factory.JSON_ivalue;
import static org.rascalmpl.library.lang.json.Factory.JSON_null;
import static org.rascalmpl.library.lang.json.Factory.JSON_number;
import static org.rascalmpl.library.lang.json.Factory.JSON_object;
import static org.rascalmpl.library.lang.json.Factory.JSON_string;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.Factory;

@SuppressWarnings("rawtypes")
public class JSONReadingTypeVisitor implements
		ITypeVisitor<IValue, IOException> {

	private static final TypeFactory tf = TypeFactory.getInstance();
	private final IValueFactory vf;
	private final TypeStore ts;
	private final Stack<Object> stack;

	public static IValue read(Object obj, IValueFactory vf, TypeStore ts, Type t) throws IOException {
		JSONReadingTypeVisitor v = new JSONReadingTypeVisitor(obj, vf, ts);
		return v.read(t);
	}
	
	private JSONReadingTypeVisitor(Object obj, IValueFactory vf, TypeStore ts) {
		this.stack = new Stack<Object>();
		this.stack.push(obj);
		this.vf = vf;
		this.ts = ts;
	}

	private IValue read(Type type) throws IOException {
		while (type.isAliased()) {
			type = type.getAliased();
		}

		if (type == tf.valueType()) {
			return visitValue(type);
		}

		if (type == tf.numberType()) {
			return visitNumber(type);
		}
		
		if (type == JSON) {
			return readPlainJSON();
		}

		// NB: why not treat isNode same as isNumber:
		// because nodes have values, but nums do not.

		stack.push(contents()); // skip tag
		IValue v = type.accept(this);
		stack.pop();
		return v;
	}


	private String tag() {
		assert stack.peek() instanceof List && ((List)stack.peek()).size() == 2;
		List m = (List)stack.peek();
		return (String) m.get(0);
	}
	
	private Object contents() {
		assert stack.peek() instanceof List && ((List)stack.peek()).size() == 2;
		List m = (List)stack.peek();
		return m.get(1);
	}
	
	@Override
	public IValue visitReal(Type type) throws IOException {
		return vf.real(((Double)stack.peek()).doubleValue());
	}

	@Override
	public IValue visitInteger(Type type) throws IOException {
		return vf.integer(((Double)stack.peek()).longValue());
	}

	@Override
	public IValue visitRational(Type type) throws IOException {
		// [ num, denom ]
		List l = (List)stack.peek();
		long num = ((Double)l.get(0)).longValue();
		long den = ((Double)l.get(1)).longValue();
		return vf.rational(num, den);
	}
	
	private Type elementType(Type t) {
		return t.isTop() ? tf.valueType() : t.getElementType();
	}
	
	private Type keyType(Type t) {
		return t.isTop() ? tf.valueType() : t.getKeyType();
	}
	
	private Type valueType(Type t) {
		return t.isTop() ? tf.valueType() : t.getValueType();
	}

	@SuppressWarnings("deprecation")
	@Override
	public IValue visitList(Type type) throws IOException {
		IListWriter w = vf.listWriter(elementType(type));
		List l = (List)stack.peek();
		for (Object e: l) {
			stack.push(e);
			w.append(read(elementType(type)));
			stack.pop();
		}
		return w.done();
	}

	@SuppressWarnings("deprecation")
	@Override
	public IValue visitMap(Type type) throws IOException {
		// [ [k, v], ... ]
		IMapWriter w = vf.mapWriter(keyType(type), valueType(type));
		List l = (List)stack.peek();
		for (Object e: l) {
			List pair = (List)e;
			stack.push(pair.get(0));
			IValue k = read(keyType(type));
			stack.pop();
			stack.push(pair.get(1));
			IValue v = read(valueType(type));
			stack.pop();
			w.put(k, v);
		}
		return w.done();
	}

	@Override
	public IValue visitNumber(Type type) throws IOException {
		IValue value = null;
		String tag = tag();
		stack.push(contents());
		switch (tag) {
		case "int":
			value = visitInteger(type);
			break;
		case "real":
			value = visitReal(type);
			break;
		case "rat":
			value = visitRational(type);
			break;
		default:
			throw new IOException("invalid tag for num: " + tag);
		}
		stack.pop();
		return value;
	}

	@Override
	public IValue visitAlias(Type type) throws IOException {
		throw new AssertionError("alias normalization should happen in read()");
	}

	@SuppressWarnings("deprecation")
	@Override
	public IValue visitSet(Type type) throws IOException {
		ISetWriter w = vf.setWriter(elementType(type));
		List l = (List)stack.peek();
		for (Object e: l) {
			stack.push(e);
			w.insert(read(elementType(type)));
			stack.pop();
		}
		return w.done();
	}

	@Override
	public IValue visitSourceLocation(Type type) throws IOException {
		String scheme = null;
		String authority = null;
		String path = null;
		String fragment = "";
		String query = "";
		int offset = -1;
		int length = -1;
		int beginLine = -1;
		int endLine = -1;
		int beginColumn = -1;
		int endColumn = -1;
		
		Map m = (Map)stack.peek();
		for (Object k: m.keySet()) {
			String name = (String)k;
			switch (name) {
			case "scheme":
				scheme = (String)m.get(name);
				break;
			case "authority":
				authority = (String)m.get(name);
				break;
			case "path":
				path = (String)m.get(name);
				break;
			case "fragment":
				fragment = (String)m.get(name);
				break;
			case "query":
				query = (String)m.get(name);
				break;
			case "offset":
				offset = ((Double)m.get(name)).intValue();
				break;
			case "length":
				length = ((Double)m.get(name)).intValue();
				break;
			case "beginLine":
				beginLine = ((Double)m.get(name)).intValue();
				break;
			case "endLine":
				endLine = ((Double)m.get(name)).intValue();
				break;
			case "beginColumn":
				beginColumn = ((Double)m.get(name)).intValue();
				break;
			case "endColumn":
				endColumn = ((Double)m.get(name)).intValue();
				break;
			default:
				throw new IOException(
						"invalid property name in SourceLocation serialization: "
								+ name);
			}
		}

		if (path != null && offset != -1 && length != -1 && beginLine != -1 && endLine != -1 && beginColumn != -1 && endColumn != -1) {
			return vf.sourceLocation(path, offset, length, beginLine, endLine, beginColumn, endColumn);
		}
		try {
			if (scheme != null && authority != null && query != null && fragment != null) {
				return vf.sourceLocation(scheme, authority, path, query, fragment);
			}
			if (scheme != null) {
				return vf.sourceLocation(scheme, authority == null ? "" : authority, path);
			}
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}

		if (path != null) {
			return vf.sourceLocation(path);
		}
		throw new IOException("Could not parse complete source location");
	}

	@Override
	public IValue visitString(Type type) throws IOException {
		return vf.string((String)stack.peek());
	}

	@Override
	public IValue visitNode(Type type) throws IOException {
		// ["name", [ ... ] ]

		List l = (List)stack.peek();
		
		String name = (String)l.get(0);
		int arity = ((Double)l.get(1)).intValue();
		
		List argsList = (List) l.get(2);
		
		IValue[] args = new IValue[arity];
		for (int i = 0; i < arity; i++) {
			stack.push(argsList.get(i));
			args[i] = read(tf.valueType());
			stack.pop();
		}
		
		Map<String, IValue> kwargs = null;
		if (l.size() > 3) {
			kwargs = new HashMap<>();
			Map kw = (Map)l.get(3);
			for (Object k: kw.keySet()) {
				String label = (String)k;
				stack.push(kw.get(label));
				kwargs.put(label, read(tf.valueType()));
				stack.pop();
			}
		}

		if (kwargs != null) {
			return vf.node(name, args, kwargs);
		}
		return vf.node(name, args);
	}

	@Override
	public IValue visitConstructor(Type type) throws IOException {
		throw new NotYetImplemented("constructor types");
	}

	@Override
	public IValue visitAbstractData(Type type) throws IOException {
		// [ "name", [ ... ], { ... } }
		
		if (type == JSON) {
			return readPlainJSON();
		}

		List l = (List)stack.peek();
		String name = (String)l.get(0);
		int arity = ((Double)l.get(1)).intValue();

		Set<Type> ctors = ts.lookupConstructor(type, name);
		Type ctor = null;
		for (Type t: ctors) {
			if (t.getArity() == arity) {
				ctor = t;
				break;
			}
		}
		
		if (ctor == null) {
			throw new IOException("no constructor " + name + "/" + arity+ " in " + type);
		}
		
		List argsList = (List) l.get(2);
	
		IValue[] args = new IValue[arity];
		for (int i = 0; i < arity; i++) {
			stack.push(argsList.get(i));
			args[i] = read(ctor.getFieldType(i));
			stack.pop();
		}

		Map<String, IValue> kwargs = null;
		if (ctor.hasKeywordParameters() && l.size() > 3) {
			kwargs = new HashMap<>();
			Map kw = (Map)l.get(3);
			for (Object k: kw.keySet()) {
				String label = (String)k;
				Type kwType = ctor.getKeywordParameterType(label);
				stack.push(kw.get(label));
				kwargs.put(label, read(kwType));
				stack.pop();
			}
		}

		if (kwargs != null) {
			return vf.constructor(ctor, args, kwargs);
		}

		return vf.constructor(ctor, args);
	}

	
	private IValue readPlainJSON() throws IOException {
		return convertToIValue(stack.peek());
	}

	private IValue convertToIValue(Object obj) throws IOException {
		if (obj == null) {
			return vf.constructor(JSON_null);
		}
		if (obj instanceof Double) {
			return vf.constructor(JSON_number, vf.real(((Double)obj).doubleValue()));
		}
		if (obj instanceof Boolean) {
			return vf.constructor(JSON_boolean, vf.bool(((Boolean)obj).booleanValue()));
		}
		if (obj instanceof String) {
			return vf.constructor(JSON_string, vf.string((String)obj));
		}
		if (obj instanceof Map) {
			Map map = (Map)obj;
			if (map.keySet().size() == 1) {
				for (Object k: map.keySet()) {
					if (k.equals("#value")) {
						stack.push(map.get(k));
						IValue v = read(tf.valueType());
						stack.pop();
						return vf.constructor(JSON_ivalue, v);
					}
				}
			}
			IMapWriter w = vf.mapWriter();
			for (Object k: map.keySet()) {
				w.put(vf.string((String) k), convertToIValue(map.get(k)));	
			}
			return vf.constructor(JSON_object, w.done());
		}
		if (obj instanceof List) {
			IListWriter w = vf.listWriter();
			for (Object k: (List)obj) {
				w.append(convertToIValue(k));	
			}
			return vf.constructor(JSON_array, w.done());
		}
		if (obj instanceof Double) {
			return vf.constructor(JSON_null);
			
		}
		throw new AssertionError("unhandled generic JSON object: " + obj);
	}

	@Override
	public IValue visitTuple(Type type) throws IOException {
		List l = (List)stack.peek();
		
		if (type.isTop()) {
			IValue[] args = new IValue[l.size()];
			for (int i = 0; i < l.size(); i++) {
				stack.push(l.get(i));
				args[i] = read(tf.valueType());
				stack.pop();
			}
			return vf.tuple(args);
		}

		IValue args[] = new IValue[type.getArity()];
		for (int i = 0; i < l.size(); i++) {
			stack.push(l.get(i));
			args[i] = read(type.getFieldType(i));
			stack.pop();
		}
		return vf.tuple(type, args);
	}

	@Override
	public IValue visitValue(Type type) throws IOException {
		String tag = tag();
		IValue value = null;
		stack.push(contents());
		switch (tag) {
		case "cons":
			value = visitNode(type);
			break;
		case "node":
			value = visitNode(type);
			break;
		case "int":
			value = visitInteger(type);
			break;
		case "rat":
			value = visitRational(type);
			break;
		case "real":
			value = visitReal(type);
			break;
		case "loc":
			value = visitSourceLocation(type);
			break;
		case "datetime":
			value = visitDateTime(type);
			break;
		case "bool":
			value = visitBool(type);
			break;
		case "list":
			value = visitList(type);
			break;
		case "set":
			value = visitSet(type);
			break;
		case "map":
			value = visitMap(type);
			break;
		case "str":
			value = visitString(type);
			break;
		case "tuple":
			value = visitTuple(type);
			break;
		default:
			throw new IOException("invalid tag for value: " + tag);
		}
		stack.pop();
		return value;
	}

	@Override
	public IValue visitDateTime(Type type) throws IOException {
		// {datetime: { }}
		int year = -1;
		int monthOfYear = -1;
		int dayOfMonth = -1;
				
		int hourOfDay = -1;
		int minuteOfHour = -1;
		int secondOfMinute = -1;
		int millisecondsOfSecond = -1;
		int timezoneOffsetHours = -1;
		int timezoneOffsetMinutes = -1;
		
		Map m = (Map)stack.peek();
		
		for (Object k: m.keySet()) {
			String fld = (String)k;
			int n = ((Double)m.get(fld)).intValue();
			switch (fld) {
			case "year": year = n; break;
			case "monthOfYear": monthOfYear = n; break;
			case "dayOfMonth": dayOfMonth = n; break;
			case "hourOfDay": hourOfDay = n; break;
			case "minuteOfHour": minuteOfHour = n; break;
			case "secondOfMinute": secondOfMinute = n; break;
			case "millisecondsOfSecond": millisecondsOfSecond = n; break;
			case "timezoneOffsetHours": timezoneOffsetHours = n; break;
			case "timezoneOffsetMinutes": timezoneOffsetMinutes = n; break;
			default: throw new IOException("invalid field for date time: " + fld);
			}
		}

		if (year != -1 && monthOfYear != -1 && dayOfMonth != -1 && hourOfDay != -1 
				&& minuteOfHour != -1 && secondOfMinute != -1 && millisecondsOfSecond != -1
				&& timezoneOffsetHours != -1 && timezoneOffsetMinutes != -1) {
			return vf.datetime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisecondsOfSecond, timezoneOffsetHours, timezoneOffsetMinutes);
		}
		if (year != -1 && monthOfYear != -1 && dayOfMonth != -1 && hourOfDay != -1 
				&& minuteOfHour != -1 && secondOfMinute != -1 && millisecondsOfSecond != -1) {
			return vf.datetime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisecondsOfSecond);
		}
		if (year != -1 && monthOfYear != -1 && dayOfMonth != -1) {
			return vf.date(year, monthOfYear, dayOfMonth);
		}
		throw new IOException("insufficient fields for making a datetime value");
	}

	@Override
	public IValue visitVoid(Type type) throws IOException {
		throw new AssertionError("cannot read values of type void");
	}

	@Override
	public IValue visitBool(Type type) throws IOException {
		return vf.bool((Boolean)stack.peek());
	}

	@Override
	public IValue visitParameter(Type type) throws IOException {
		throw new AssertionError("parameter types should have been bound");
	}

	@Override
	public IValue visitExternal(Type type) throws IOException {
		if (type instanceof NonTerminalType) {
			return Factory.Tree.accept(this);
		}
		throw new IOException("cannot deserialize external values");
	}

}