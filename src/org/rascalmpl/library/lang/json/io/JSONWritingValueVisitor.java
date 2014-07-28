package org.rascalmpl.library.lang.json.io;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.lang.json.Factory;

import com.google.gson.stream.JsonWriter;

public class JSONWritingValueVisitor implements IValueVisitor<Void, IOException> {

	private final JsonWriter out;

	public JSONWritingValueVisitor(JsonWriter out) {
		this.out = out;
	}

	public static void write(JsonWriter out, IValue value)
			throws IOException {
		value.accept(new JSONWritingValueVisitor(out));
	}

	@Override
	public Void visitReal(IReal value) throws IOException {
		// {real: n}
		out.beginObject()
			.name("real")
			.value(value.doubleValue())
			.endObject();
		return null;
	}

	@Override
	public Void visitInteger(IInteger value) throws IOException {
		// {int: n}
		out.beginObject()
			.name("int")
			.value(((IInteger) value).intValue())
			.endObject();
		return null;
	}

	@Override
	public Void visitRational(IRational value) throws IOException {
		// {rat: [n, d] }
		out.beginObject()
			.name("rat")
			.beginArray()
			.value(((IRational) value).numerator().longValue())
			.value(((IRational) value).denominator().longValue())
			.endArray()
			.endObject();
		return null;
	}

	@Override
	public Void visitList(IList value) throws IOException {
		// {list: [ ... ] }
		out.beginObject()
			.name("list")
			.beginArray();
		for (IValue v : (IList) value) {
			write(out, v);
		}
		out.endArray()
			.endObject();
		return null;
	}

	@Override
	public Void visitMap(IMap value) throws IOException {
		// {map: [ [k, v], [k, v] ] }
		out.beginObject()
			.name("map")
			.beginArray();
		for (IValue k : (IMap) value) {
			out.beginArray();
			write(out, k);
			write(out, ((IMap) value).get(k));
			out.endArray();
		}
		out.endArray()
			.endObject();
		return null;
	}

	@Override
	public Void visitSet(ISet value) throws IOException {
		// {set: [.... ]}
		out.beginObject();
		out.name("set");
		out.beginArray();
		for (IValue v : (ISet) value) {
			write(out, v);
		}
		out.endArray();
		out.endObject();
		return null;
	}

	@Override
	public Void visitSourceLocation(ISourceLocation value) throws IOException {
		// {loc: {...} }
		out.beginObject();
		out.name("loc");
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
		out.endObject();
		return null;
	}

	@Override
	public Void visitString(IString value) throws IOException {
		// {str: ""}
		out.beginObject()
			.name("str")
			.value(((IString) value).getValue())
			.endObject();
		return null;
	}

	@Override
	public Void visitNode(INode value) throws IOException {
		// {node: ["name", arity, [...]] }
		out.beginObject();
		out.name("node");
		out.beginArray();
		INode n = (INode) value;
		out.value(n.getName());
		out.value(n.arity());
		out.beginArray();
		for (IValue v : n.getChildren()) {
			write(out, v);
		}
		out.endArray();
		
		if (!value.asAnnotatable().hasAnnotations()) {
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
		
		out.endArray();
		out.endObject();
		return null;
	}

	@Override
	public Void visitConstructor(IConstructor value) throws IOException {
		if (value.getType().getAbstractDataType() == Factory.JSON) {
			return writePlainJSON(value);
		}
		
		
		// {cons: ["name", arity, [...], { }]}
		out.beginObject();
		out.name("cons");
		out.beginArray();
		out.value(value.getName());
		out.value(value.arity());
		out.beginArray();
		for (IValue v : value.getChildren()) {
			write(out, v);
		}
		out.endArray();

		if (!value.asAnnotatable().hasAnnotations()) {
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

		out.endArray();

		out.endObject();
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
		case "integer":
			out.value(((IInteger)value.get(0)).longValue());
			break;
		case "float":
			out.value(((IReal)value.get(0)).doubleValue());
			break;
		case "string":
			out.value(((IString)value.get(0)).getValue());
			break;
		case "boolean":
			out.value(((IBool)value.get(0)).getValue());
			break;
		case "ivalue":
			value.get(0).accept(this);
			break;
		default:
			throw new IOException("invalid JSON constructor " + value);
		}
		return null;
	}

	@Override
	public Void visitTuple(ITuple value) throws IOException {
		// {tuple: [ ... ]}
		out.beginObject()
			.name("tuple");
		out.beginArray();
		ITuple t = (ITuple) value;
		for (int i = 0; i < t.arity(); i++) {
			write(out, t.get(i));
		}
		out.endArray();
		out.endObject();
		return null;
	}

	
	
	@Override
	public Void visitBoolean(IBool value) throws IOException {
		// {bool: ..}
		out.beginObject()
			.name("bool")
			.value(((IBool) value).getValue())
			.endObject();
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
		IDateTime dt = (IDateTime) value;
		out.beginObject();
		out.name("datetime");
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
		out.endObject();
		return null;
	}

	@Override
	public Void visitRelation(ISet o) throws IOException {
		visitSet(o);
		return null;
	}

	@Override
	public Void visitListRelation(IList o) throws IOException {
		visitList(o);
		return null;
	}


}
