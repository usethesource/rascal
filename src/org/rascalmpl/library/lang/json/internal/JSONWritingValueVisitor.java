package org.rascalmpl.library.lang.json.io;

import java.io.IOException;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.lang.json.Factory;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.impl.fields.ConstructorWithKeywordParametersFacade;
import io.usethesource.vallang.impl.fields.NodeWithKeywordParametersFacade;
import io.usethesource.vallang.visitors.IValueVisitor;

import com.google.gson.stream.JsonWriter;

public class JSONWritingValueVisitor implements IValueVisitor<Void, IOException> {

	private final JsonWriter out;
	
	private final boolean compact;

	public JSONWritingValueVisitor(JsonWriter out, boolean compact) {
		this.out = out;
		this.compact = compact;
	}

	public static void write(JsonWriter out, IValue value, boolean compact)
			throws IOException {
		value.accept(new JSONWritingValueVisitor(out, compact));
	}
	
	public static void write(JsonWriter out, IValue value)
			throws IOException {
		   write(out, value, false);
	}

	@Override
	public Void visitReal(IReal value) throws IOException {
		// {real: n}
		if (compact) {
			out.value(value.doubleValue()); 
		} else {
		out.beginArray()
			.value("real")
			.value(value.doubleValue())
			.endArray();
		}
		return null;
	}

	@Override
	public Void visitInteger(IInteger value) throws IOException {
		// {int: n}
		if (compact) {
			out.value(value.intValue()); 
		} else {
		out.beginArray()
			.value("int")
			.value(((IInteger) value).intValue())
			.endArray();
		}
		return null;
	}

	@Override
	public Void visitRational(IRational value) throws IOException {
	    // {rat: [n, d] }
	    if (!compact) {
	        out.beginArray()
	        .value("rat");
	    }
	    
	    out.beginArray()
	    .value(((IRational) value).numerator().longValue())
	    .value(((IRational) value).denominator().longValue())
	    .endArray();

	    if (!compact) {
	        out.endArray();
	    }
	    return null;
	}

	@Override
	public Void visitList(IList value) throws IOException {
		// {list: [ ... ] }
		if (compact) {
			out.beginArray();
			for (IValue v : (IList) value) {
				write(out, v, compact);
			}
			out.endArray();
		}
		else
		{
		out.beginArray()
			.value("list")
			.beginArray();
		for (IValue v : (IList) value) {
			write(out, v);
		}
		out.endArray()
			.endArray();
		}
		return null;
	}

	@Override
	public Void visitMap(IMap value) throws IOException {
		// {map: [ [k, v], [k, v] ] }
		if (compact) {
			out.beginObject();
			for (IValue k : (IMap) value) {
				if (k.getType().isString()) {
					String s = ((IString) k).getValue();
					out.name(s);
					write(out, ((IMap) value).get(k), compact);				
				} else
					throw new IOException("Not possible to translate: " + value);
			}
			out.endObject();
		} else {
		out.beginArray()
			.value("map")
			.beginArray();
		for (IValue k : (IMap) value) {
			out.beginArray();
			write(out, k);
			write(out, ((IMap) value).get(k));
			out.endArray();
		}
		out.endArray()
			.endArray();
		}
		return null;
	}

	@Override
	public Void visitSet(ISet value) throws IOException {
		if (compact) {
		    out.beginArray();
            for (IValue v : (ISet) value) {
                write(out, v, compact);
            }
            out.endArray();
		} else {
		 // {set: [.... ]}
		out.beginArray();
		out.value("set");
		out.beginArray();
		for (IValue v : (ISet) value) {
			write(out, v);
		}
		out.endArray();
		out.endArray();
		}
		return null;
	}

	@Override
	public Void visitSourceLocation(ISourceLocation value) throws IOException {
		// {loc: {...} }
	    if (!compact) {
	        out.beginArray();
	        out.value("loc");
	    }
	    
		out.beginObject();
		ISourceLocation loc = (ISourceLocation) value;

		out.name("scheme");
		out.value(loc.getScheme());

		if (loc.hasAuthority()) {
			out.name("authority");
			out.value(loc.getAuthority());
		}
		if (loc.hasPath()) {
			out.name("path");
			out.value(loc.getPath());
		}
		if (loc.hasFragment()) {
			out.name("fragment");
			out.value(loc.getFragment());
		}
		if (loc.hasQuery()) {
			out.name("query");
			out.value(loc.getQuery());
		}

		if (loc.hasOffsetLength()) {
			out.name("offset");
			out.value(loc.getOffset());
			out.name("length");
			out.value(loc.getLength());
		}

		if (loc.hasLineColumn()) {
			out.name("beginLine");
			out.value(loc.getBeginLine());
			out.name("endLine");
			out.value(loc.getEndLine());
			out.name("beginColumn");
			out.value(loc.getBeginColumn());
			out.name("endColumn");
			out.value(loc.getEndColumn());
		}

		out.endObject();
		
		if (!compact) {
		    out.endArray();
		}
		
		return null;
	}

	@Override
	public Void visitString(IString value) throws IOException {
		if (compact) {
			out.value(((IString) value).getValue());
		} else {
		out.beginArray()
			.value("str")
			.value(((IString) value).getValue())
			.endArray();
		}
		return null;
	}

	@Override
	public Void visitNode(INode value) throws IOException {
		// ["node", ["name", arity, [...]] ]
		if (compact) {
			throw RuntimeExceptionFactory.illegalTypeArgument(value.toString(),
					null, null, "cannot serialize node types");
		} else {
		out.beginArray();
		out.value("node");
		out.beginArray();
		INode n = (INode) value;
		out.value(n.getName());
		out.value(n.arity());
		out.beginArray();
		for (IValue v : n.getChildren()) {
			write(out, v);
		}
		out.endArray();
		
//		if (!value.asAnnotatable().hasAnnotations()) {
		// temp hack
		if (value instanceof NodeWithKeywordParametersFacade) {
			IWithKeywordParameters<? extends INode> kw = value.asWithKeywordParameters();
			if (kw.hasParameters()) {
				out.beginObject();
				for (String k : kw.getParameterNames()) {
					out.name(k);
					write(out, kw.getParameter(k));
				}
				out.endObject();
			}
		}
//		}
			
		
		out.endArray();
		out.endArray();
		}
		return null;
	}

	@Override
	public Void visitConstructor(IConstructor value) throws IOException {
		if (compact) {
				throw RuntimeExceptionFactory.illegalTypeArgument(value.toString(),
						null, null, "cannot serialize constructor types");
			}
		else {
		if (value.getType().getAbstractDataType() == Factory.JSON) {
			return writePlainJSON(value);
		}
		
		
		// ["cons", ["name", arity, [...], { }]]
		out.beginArray();
		out.value("cons");
		out.beginArray();
		out.value(value.getName());
		out.value(value.arity());
		out.beginArray();
		for (IValue v : value.getChildren()) {
			write(out, v);
		}
		out.endArray();

		//if (!value.asAnnotatable().hasAnnotations()) {
		// temp hack
		if (value instanceof ConstructorWithKeywordParametersFacade) {
			IWithKeywordParameters<? extends INode> kw = value.asWithKeywordParameters();
			if (kw.hasParameters()) {
				out.beginObject();
				for (String k : kw.getParameterNames()) {
					out.name(k);
					write(out, kw.getParameter(k));
				}
				out.endObject();
			
			}
		}
//		}

		out.endArray();

		out.endArray();
		}
		return null;
	}

	private Void writePlainJSON(IConstructor value) throws IndexOutOfBoundsException, IOException {
		switch (value.getName()) {
		case "null":
			out.nullValue();
			break;
		case "object":
			IMap props = (IMap) value.get(0);
			out.beginObject();
			for (IValue k: props) {
				out.name(((IString)k).getValue());
				writePlainJSON((IConstructor) props.get(k));
			}
			out.endObject();
			break;
		case "array":
			IList vals = (IList) value.get(0);
			out.beginArray();
			for (IValue v: vals) {
				writePlainJSON((IConstructor) v);
			}
			out.endArray();
			break;
		case "number":
			out.value(((IReal)value.get(0)).doubleValue());
			break;
		case "string":
			out.value(((IString)value.get(0)).getValue());
			break;
		case "boolean":
			out.value(((IBool)value.get(0)).getValue());
			break;
		case "ivalue":
			out.beginObject();
			out.name("#value");
			value.get(0).accept(this);
			out.endObject();
			break;
		default:
			throw new IOException("invalid JSON constructor " + value);
		}
		return null;
	}

	@Override
	public Void visitTuple(ITuple value) throws IOException {
		// {tuple: [ ... ]}
		if (compact) {
			throw RuntimeExceptionFactory.illegalTypeArgument(value.toString(),
					null, null, "cannot serialize tuple types");
			
		} else {
		out.beginArray()
			.value("tuple");
		out.beginArray();
		ITuple t = (ITuple) value;
		for (int i = 0; i < t.arity(); i++) {
			write(out, t.get(i));
		}
		out.endArray();
		out.endArray();
		}
		return null;
	}

	
	
	@Override
	public Void visitBoolean(IBool value) throws IOException {
		// {bool: ..}
		if (compact) {
			out.value(((IBool) value).getValue());
		} else {
		out.beginArray()
			.value("bool")
			.value(((IBool) value).getValue())
			.endArray();
		}
		return null;
	}


	@Override
	public Void visitExternal(IExternalValue value) throws IOException {
		throw RuntimeExceptionFactory.illegalTypeArgument(value.toString(),
				null, null, "cannot serialize external types");
	}

	@Override
	public Void visitDateTime(IDateTime value) throws IOException {
		// {datetime: { }}
		if (compact) {
			throw RuntimeExceptionFactory.illegalTypeArgument(value.toString(),
					null, null, "cannot serialize datetime types");
		} else {
		IDateTime dt = (IDateTime) value;
		out.beginArray();
		out.value("datetime");
		out.beginObject();
		if (dt.isDate() || dt.isDateTime()) {
			out.name("year");
			out.value(dt.getYear());
			out.name("monthOfYear");
			out.value(dt.getMonthOfYear());
			out.name("dayOfMonth");
			out.value(dt.getDayOfMonth());

		}
		if (dt.isTime() || dt.isDateTime()) {
			out.name("hourOfDay");
			out.value(dt.getHourOfDay());
			out.name("minuteOfHour");
			out.value(dt.getMinuteOfHour());
			out.name("secondOfMinute");
			out.value(dt.getSecondOfMinute());
			out.name("millisecondsOfSecond");
			out.value(dt.getMillisecondsOfSecond());
			out.name("timezoneOffsetHours");
			out.value(dt.getTimezoneOffsetHours());
			out.name("timezoneOffsetMinutes");
			out.value(dt.getTimezoneOffsetMinutes());
		}
		out.endObject();
		out.endArray();
		}
		return null;
	}
}
