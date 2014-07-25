package org.rascalmpl.library.lang.json.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.Factory;

import com.google.gson.stream.JsonReader;

public class JSONReadingTypeVisitor implements
		ITypeVisitor<IValue, IOException> {

	private static final Type VALUE_TYPE = TypeFactory.getInstance()
			.valueType();
	private final JsonReader in;
	private final IValueFactory vf;
	private final TypeStore ts;

	public static IValue read(JsonReader in, IValueFactory vf, TypeStore ts, Type t) throws IOException {
		JSONReadingTypeVisitor v = new JSONReadingTypeVisitor(in, vf, ts);
		return v.read(t);
	}
	
	private JSONReadingTypeVisitor(JsonReader in, IValueFactory vf, TypeStore ts) {
		this.in = in;
		this.vf = vf;
		this.ts = ts;
	}

	private IValue read(Type type) throws IOException {
		while (type.isAliased()) {
			type = type.getAliased();
		}

		if (type.isTop()) {
			return visitValue(type);
		}

		if (type.isNumber()) {
			return visitNumber(type);
		}

		// NB: why not treat isNode same as isNumber:
		// because nodes have values, but nums do not.

		in.beginObject();
		// TODO check that the name is good (perhaps push this unwrapping into the visit methods.
		in.nextName(); // ignored
		IValue v = type.accept(this);
		in.endObject();
		return v;
	}

	@Override
	public IValue visitReal(Type type) throws IOException {
		double d = in.nextDouble();
		return vf.real(d);
	}

	@Override
	public IValue visitInteger(Type type) throws IOException {
		long l = in.nextLong();
		return vf.integer(l);
	}

	@Override
	public IValue visitRational(Type type) throws IOException {
		// [ num, denom ]
		in.beginArray();
		long num = in.nextLong();
		long den = in.nextLong();
		in.endArray();
		return vf.rational(num, den);
	}
	
	private Type elementType(Type t) {
		return t.isTop() ? VALUE_TYPE : t.getElementType();
	}
	
	private Type keyType(Type t) {
		return t.isTop() ? VALUE_TYPE : t.getKeyType();
	}
	
	private Type valueType(Type t) {
		return t.isTop() ? VALUE_TYPE : t.getValueType();
	}

	@Override
	public IValue visitList(Type type) throws IOException {
		IListWriter w = vf.listWriter(elementType(type));
		in.beginArray();
		while (in.hasNext()) {
			w.append(read(elementType(type)));
		}
		in.endArray();
		return w.done();
	}

	@Override
	public IValue visitMap(Type type) throws IOException {
		// [ [k, v], ... ]
		IMapWriter w = vf.mapWriter(keyType(type), valueType(type));
		in.beginArray();
		while (in.hasNext()) {
			in.beginArray();
			IValue k = read(keyType(type));
			IValue v = read(valueType(type));
			in.endArray();
			w.put(k, v);
		}
		in.endArray();
		return w.done();
	}

	@Override
	public IValue visitNumber(Type type) throws IOException {
		in.beginObject();
		String tag = in.nextName();
		IValue value = null;
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
		in.endObject();
		return value;
	}

	@Override
	public IValue visitAlias(Type type) throws IOException {
		throw new AssertionError("alias normalization should happen in read()");
	}

	@Override
	public IValue visitSet(Type type) throws IOException {
		ISetWriter w = vf.setWriter(elementType(type));
		in.beginArray();
		while (in.hasNext()) {
			w.insert(read(elementType(type)));
		}
		in.endArray();
		return w.done();
	}

	@Override
	public IValue visitSourceLocation(Type type) throws IOException {
		in.beginObject();

		String scheme = null;
		String authority = null;
		String path = null;
		String fragment = null;
		String query = null;
		int offset = -1;
		int length = -1;
		int beginLine = -1;
		int endLine = -1;
		int beginColumn = -1;
		int endColumn = -1;

		while (in.hasNext()) {
			String name = in.nextName();
			switch (name) {
			case "scheme":
				scheme = in.nextString();
				break;
			case "authority":
				authority = in.nextString();
				break;
			case "path":
				path = in.nextString();
				break;
			case "fragment":
				fragment = in.nextString();
				break;
			case "query":
				query = in.nextString();
				break;
			case "offset":
				offset = in.nextInt();
				break;
			case "beginLine":
				beginLine = in.nextInt();
				break;
			case "endLine":
				endLine = in.nextInt();
				break;
			case "beginColumn":
				beginColumn = in.nextInt();
				break;
			case "endColumn":
				endColumn = in.nextInt();
				break;
			default:
				throw new IOException(
						"invalid property name in SourceLocation serialization: "
								+ name);
			}
		}

		in.endObject();

		if (path != null && offset != -1 && length != -1 && beginLine != -1
				&& endLine != -1 && beginColumn != -1 && endColumn != -1) {
			return vf.sourceLocation(path, offset, length, beginLine, endLine,
					beginColumn, endColumn);
		}
		try {
			if (scheme != null && authority != null && query != null
					&& fragment != null) {
				return vf.sourceLocation(scheme, authority, path, query,
						fragment);
			}
			if (scheme != null && authority != null) {
				return vf.sourceLocation(scheme, authority, path);
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
		return vf.string(in.nextString());
	}

	@Override
	public IValue visitNode(Type type) throws IOException {
		// ["name", [ ... ] ]

		in.beginArray();
		
		String name = in.nextString();
		int arity = in.nextInt();
		
		IValue[] args = new IValue[arity];
		in.beginArray();
		for (int i = 0; i < arity; i++) {
			args[i] = read(VALUE_TYPE);
		}
		in.endArray();
		
		Map<String, IValue> kwargs = null;
		if (in.hasNext()) {
			kwargs = new HashMap<>();
			in.beginObject();
			while (in.hasNext()) {
				String label = in.nextName();
				kwargs.put(label, read(VALUE_TYPE));
			}
			in.endObject();
		}

		in.endArray();

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

		in.beginArray();
		String name = in.nextString();
		int arity = in.nextInt();

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
		
		IValue[] args = new IValue[arity];
		in.beginArray();
		for (int i = 0; i < arity; i++) {
			args[i] = read(ctor.getFieldType(i));
		}
		in.endArray();

		Map<String, IValue> kwargs = null;
		if (in.hasNext()) {
			kwargs = new HashMap<>();
			in.beginObject();
			while (in.hasNext()) {
				String label = in.nextName();
				kwargs.put(label, read(VALUE_TYPE));
			}
			in.endObject();
		}

		in.endArray();

		
		if (ctor.hasKeywordParameters() && kwargs != null) {
			return vf.constructor(ctor, args, kwargs);
		}

		return vf.constructor(ctor, args);
	}

	
	@Override
	public IValue visitTuple(Type type) throws IOException {
		if (type.isTop()) {
			List<IValue >args = new ArrayList<IValue>();
			in.beginArray();
			while (in.hasNext()) {
				args.add(read(VALUE_TYPE));
			}
			in.endArray();
			return vf.tuple(args.toArray(new IValue[]{}));
		}
		
		IValue args[] = new IValue[type.getArity()];
		in.beginArray();
		int i = 0;
		while (in.hasNext()) {
			args[i] = read(type.getFieldType(i));
			i++;
		}
		in.endArray();
		return vf.tuple(type, args);
	}

	@Override
	public IValue visitValue(Type type) throws IOException {
		in.beginObject();
		String tag = in.nextName();
		IValue value = null;
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
		in.endObject();
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
		
		in.beginObject();
		while (in.hasNext()) {
			String fld = in.nextName();
			switch (fld) {
			case "year": year = in.nextInt(); break;
			case "monthOfYear": monthOfYear = in.nextInt(); break;
			case "dayOfMonth": dayOfMonth = in.nextInt(); break;
			case "hourOfDay": hourOfDay = in.nextInt(); break;
			case "minuteOfHour": minuteOfHour = in.nextInt(); break;
			case "secondOfMinute": secondOfMinute = in.nextInt(); break;
			case "millisecondsOfSecond": millisecondsOfSecond = in.nextInt(); break;
			case "timezoneOffsetHours": timezoneOffsetHours = in.nextInt(); break;
			case "timezoneOffsetMinutes": timezoneOffsetMinutes = in.nextInt(); break;
			default: throw new IOException("invalid field for date time: " + fld);
			}
		}
		in.endObject();
		
		if (year != -1 && monthOfYear != -1 && dayOfMonth != -1 && hourOfDay != -1 
				&& minuteOfHour != -1 && secondOfMinute != -1 && millisecondsOfSecond != -1
				&& timezoneOffsetHours != -1 && timezoneOffsetMinutes != -1) {
			return vf.datetime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisecondsOfSecond);
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
		return vf.bool(in.nextBoolean());
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