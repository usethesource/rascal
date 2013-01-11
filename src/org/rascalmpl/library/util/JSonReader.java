/*******************************************************************************
 * Copyright (c) INRIA-LORIA and CWI 2006-2009 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jurgen Vinju (jurgenv@cwi.nl) - initial API and implementation
 *    Bert Lisser  (Bert.Lisser@cwi.nl)
 *******************************************************************************/
package org.rascalmpl.library.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactParseError;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.io.AbstractBinaryReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.unicode.UnicodeInputStreamReader;
import org.rascalmpl.uri.URIUtil;

import com.ibm.icu.text.SimpleDateFormat;

// TODO: add support for values of type Value, for this we need overloading resolving
public class JSonReader extends AbstractBinaryReader {
	private IValueFactory vf;
	private TypeFactory tf = TypeFactory.getInstance();
	private TypeStore ts;

	private IString nameKey, argKey, annoKey;

	static final String name = "#name", args = "#args", annos = "#annos";

	final boolean debug = false;

	public IValue read(IValueFactory factory, TypeStore store, Type type,
			Reader read) throws FactParseError, IOException {
		this.vf = factory;
		this.ts = store;
		if (debug)
			System.err.println("read1:" + type);
		nameKey = (IString) tf.stringType().make(vf, name);
		argKey = (IString) tf.stringType().make(vf, args);
		annoKey = (IString) tf.stringType().make(vf, annos);
		int firstToken;
		do {
			firstToken = read.read();
			if (firstToken == -1) {
				throw new IOException("Premature EOF.");
			}
		} while (Character.isWhitespace((char) firstToken));

		char typeByte = (char) firstToken;
		if (Character.isLetterOrDigit(typeByte) || typeByte == '_'
				|| typeByte == '[' || typeByte == '{' || typeByte == '-'
				|| typeByte == '.' || typeByte == '"' || typeByte == '#') {
			JSonStream sreader = new JSonStream(read);
			sreader.last_char = typeByte;
			if (debug)
				System.err.println("read2:" + type);
			IValue result = parse(sreader, type);
			if (debug)
				System.err.println("PARSED:" + result);
			if (type.isAbstractDataType()) {
				result = buildTerm((IMap) result, type);
			} else
				result = buildTerm(result, type);
			return result;
		} else {
			throw new RuntimeException("nyi");
		}
	}

	private IValue parse(JSonStream reader, Type expected) throws IOException {
		IValue result;
		int start;
		if (debug)
			System.err
					.println("Parse:" + expected + " " + reader.getLastChar());
		start = reader.getPosition();
		switch (reader.getLastChar()) {
		case -1:
			throw new FactParseError("premature EOF encountered.", start);
		case '{':
			result = parseMap(reader, expected);
			break;
		case '[':
			if (expected.isTupleType()) {
				result = parseTuple(reader, expected);
			} else
				result = parseList(reader, expected);
			break;
		case 't':
		case 'f':
			result = parseBoolean(reader);
			break;
		case 'n':
			result = parseNull(reader);
			break;
		case '"':
			result = parseString(reader, expected);
			break;
		case '-':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			result = parseNumber(reader, expected);
			break;
		default:
			throw new FactParseError("Illegal symbol", reader.getPosition());
		}
		return result;
	}

	private IValue parseTuple(JSonStream reader, Type expected)
			throws IOException {
		if (debug)
			System.err.println("ParseTuple:" + expected);
		final int c = reader.readSkippingWS();
		if (c == -1) {
			throw new FactParseError("premature EOF encountered.",
					reader.getPosition());
		}
		IValue[] a = new IValue[expected.getArity()];
		Iterator<Type> it = expected.iterator();
		int i = 0;
		if (it.hasNext()) {
			Type typ = it.next();
			IValue term = parse(reader, typ);
			a[i] = term;
			if (debug)
				System.err.println("ParseTuple:" + a[i] + " " + i);
			i++;
		}
		while (reader.getLastChar() == ',' && it.hasNext()) {
			reader.readSkippingWS();
			IValue term = parse(reader, it.next());
			a[i] = term;
			if (debug)
				System.err.println("ParseTuple:" + a[i] + " " + i);
			i++;
		}
		IValue result = vf.tuple(a);
		if (debug)
			System.err.println("result=" + result);
		if (reader.getLastChar() != ']') {
			throw new FactParseError("expected ']' but got '"
					+ (char) reader.getLastChar() + "'", reader.getPosition());
		}
		reader.readSkippingWS();
		return result;
	}

	private ITuple parseEntry(JSonStream reader, Type mapType)
			throws IOException {
		IValue[] array = new IValue[2];
		if (debug)
			System.err.println("ParseEntry:" + mapType.getKeyType() + " "
					+ mapType.getValueType());
		array[0] = parse(reader, mapType.getKeyType());
		if (debug)
			System.err.println("ParseEntry2:" + array[0] + " "
					+ reader.getLastChar());
		if (reader.getLastChar() == ':') {
			reader.readSkippingWS();
			if (debug)
				System.err.println("ParseEntry3:" + mapType.getKeyType() + " "
						+ mapType.getValueType());
			array[1] = parse(reader, mapType.getValueType());
			if (debug)
				System.err.println("ParseEntry4:" + array[1]);
		} else
			throw new FactParseError("In map ':' expected",
					reader.getPosition());
		Type t = tf.tupleType(mapType.getKeyType(), mapType.getValueType());
		return (ITuple) t.make(vf, array[0], array[1]);
	}

	private IValue parseMap(JSonStream reader, Type expected)
			throws IOException {
		final int c = reader.readSkippingWS();
		if (debug)
			System.err.println("ParseMap1:" + expected);
		if (!expected.isMapType())
			expected = tf.mapType(tf.stringType(), tf.valueType());
		if (debug)
			System.err.println("ParseMap2:" + expected);
		IMapWriter w = expected.writer(vf);
		if (c == -1) {
			throw new FactParseError("premature EOF encountered.",
					reader.getPosition());
		}
		if (reader.getLastChar() == '}') {
			reader.readSkippingWS();
			return w.done();
		}
		ITuple term = parseEntry(reader, expected);
		w.put(term.get(0), term.get(1));
		while (reader.getLastChar() == ',') {
			reader.readSkippingWS();
			term = parseEntry(reader, expected);
			w.put(term.get(0), term.get(1));
		}
		if (reader.getLastChar() != '}') {
			throw new FactParseError("expected '}' but got '"
					+ (char) reader.getLastChar() + "'", reader.getPosition());
		}
		reader.readSkippingWS();
		return w.done();
	}

	private IValue parseString(JSonStream reader, Type expected)
			throws IOException {
		IValue result;
		String str = parseStringLiteral(reader);
		result = expected.make(vf, str);
		reader.readSkippingWS(); /* " */
		return result;
	}

	private IValue parseBoolean(JSonStream reader)
			throws IOException {
		IValue result;
		String str = parseBooleanLiteral(reader);
		if (!str.equalsIgnoreCase("true") && !str.equalsIgnoreCase("false"))
			throw new FactParseError("true or false expected but found:" + str
					+ ".", reader.getPosition());
		result = vf.bool(str.equalsIgnoreCase("true") ? true : false);
		reader.readSkippingWS(); /* e */
		return result;
	}
	
	private IValue parseNull(JSonStream reader)
			throws IOException {
		IValue result;
		String str = parseNullLiteral(reader);
		if (!str.equalsIgnoreCase("null"))
			throw new FactParseError("null expected but found:" + str
					+ ".", reader.getPosition());
		result = vf.string(str);
		reader.readSkippingWS(); /* l */
		return result;
	}


	private IValue parseList(JSonStream reader, Type expected)
			throws IOException {
		IValue result;
		if (debug)
			System.err.println("ParseList:" + expected);
		final int c = reader.readSkippingWS();
		if (c == -1) {
			throw new FactParseError("premature EOF encountered.",
					reader.getPosition());
		}
		if (c == ']') {
			reader.readSkippingWS();
			if (expected.isListType() || expected.isSetType()
					|| expected.isRelationType() || expected.isTupleType()) {
				result = expected.make(vf);
			} else if (expected.isValueType()) {
				result = tf.listType(tf.valueType()).make(vf);
			} else {
				throw new FactParseError("Did not expect a list, rather a "
						+ expected, reader.getPosition());
			}
		} else {
			result = parseTerms(reader, expected);
			if (reader.getLastChar() != ']' && reader.getLastChar() != '}') {
				throw new FactParseError("expected ']' or '}' but got '"
						+ (char) reader.getLastChar() + "'",
						reader.getPosition());
			}
			reader.readSkippingWS();
		}
		return result;
	}

	private IValue parseNumber(JSonStream reader, Type expected)
			throws IOException {
		StringBuilder str = new StringBuilder();
		IValue result;
		do {
			str.append((char) reader.getLastChar());
		} while (Character.isDigit(reader.read()));
		if (reader.getLastChar() != '.' && reader.getLastChar() != 'e'
				&& reader.getLastChar() != 'E' && reader.getLastChar() != 'l'
				&& reader.getLastChar() != 'L') {
			int val;
			try {
				val = Integer.parseInt(str.toString());
			} catch (NumberFormatException e) {
				throw new FactParseError("malformed int:" + str,
						reader.getPosition());
			}
			result = !expected.isValueType() ? expected.make(vf, ts, val) : tf
					.integerType().make(vf, ts, val);
		} else if (reader.getLastChar() == 'l' || reader.getLastChar() == 'L') {
			reader.read();
			throw new FactParseError("No support for longs",
					reader.getPosition());
		} else {
			if (reader.getLastChar() == '.') {
				str.append('.');
				reader.read();
				if (!Character.isDigit(reader.getLastChar()))
					throw new FactParseError("digit expected",
							reader.getPosition());
				do {
					str.append((char) reader.getLastChar());
				} while (Character.isDigit(reader.read()));
			}
			if (reader.getLastChar() == 'e' || reader.getLastChar() == 'E') {
				str.append((char) reader.getLastChar());
				reader.read();
				if (reader.getLastChar() == '-' || reader.getLastChar() == '+') {
					str.append((char) reader.getLastChar());
					reader.read();
				}
				if (!Character.isDigit(reader.getLastChar()))
					throw new FactParseError("digit expected!",
							reader.getPosition());
				do {
					str.append((char) reader.getLastChar());
				} while (Character.isDigit(reader.read()));
			}
			double val;
			try {
				val = Double.valueOf(str.toString()).doubleValue();
				result = !expected.isValueType() ? expected.make(vf, ts, val)
						: tf.realType().make(vf, ts, val);
			} catch (NumberFormatException e) {
				throw new FactParseError("malformed real",
						reader.getPosition(), e);
			}
		}
		reader.skipWS();
		return result;
	}

	private String parseBooleanLiteral(JSonStream reader) throws IOException {
		StringBuilder str = new StringBuilder();
		str.append((char) reader.getLastChar());
		do {
			reader.read();
			int lastChar = reader.getLastChar();
			if (lastChar == -1)
				throw new IOException("Premature EOF.");
			str.append((char) lastChar);

		} while (reader.getLastChar() != 'e');
		return str.toString();
	}
	
	private String parseNullLiteral(JSonStream reader) throws IOException {
		StringBuilder str = new StringBuilder();
		str.append((char) reader.getLastChar());
		do {
			reader.read();
			int lastChar = reader.getLastChar();
			if (lastChar == -1)
				throw new IOException("Premature EOF.");
			str.append((char) lastChar);

		} while (reader.getLastChar() != 'l');
		reader.read();
		int lastChar = reader.getLastChar();
		if (lastChar == -1)
			throw new IOException("Premature EOF.");
		str.append((char) lastChar);
		return str.toString();
	}

	private String parseStringLiteral(JSonStream reader) throws IOException {
		boolean escaped;
		StringBuilder str = new StringBuilder();
		do {
			escaped = false;
			if (reader.read() == '\\') {
				reader.read();
				escaped = true;
			}
			int lastChar = reader.getLastChar();
			if (lastChar == -1)
				throw new IOException("Premature EOF.");
			if (escaped) {
				switch (lastChar) {
				case 'n':
					str.append('\n');
					break;
				case 't':
					str.append('\t');
					break;
				case 'b':
					str.append('\b');
					break;
				case 'r':
					str.append('\r');
					break;
				case 'f':
					str.append('\f');
					break;
				case '\\':
					str.append('\\');
					break;
				case '\'':
					str.append('\'');
					break;
				case '\"':
					str.append('\"');
					break;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
					str.append(reader.readOct());
					break;
				default:
					str.append('\\').append((char) lastChar);
				}
			} else if (lastChar != '\"') {
				str.append((char) lastChar);
			}
		} while (escaped || reader.getLastChar() != '"');

		return str.toString();
	}

	IValue dateTime(String s) {
		final String formatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat(formatString);
			Date dt = fmt.parse(s);
			return vf.datetime(dt.getTime());
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError(
					"Cannot parse input datetime: " + s
							+ " using format string: " + formatString, null,
					null);
		} catch (ParseException e) {
			throw RuntimeExceptionFactory.dateTimeParsingError(
					"Cannot parse input datetime: " + s
							+ " using format string: " + formatString, null,
					null);
		}
	}

	private IValue buildTerm(IList t, Type type) {
		IValue[] a = new IValue[t.length()];
		if (debug)
			System.err.println("buildTermList");
		for (int i = 0; i < t.length(); i++) {
			if (debug)
				System.err.println(t.get(i));
			a[i] = buildTerm(t.get(i), t.getElementType());
			if (debug)
				System.err.println("R:" + a[i]);
		}
		if (type.isTupleType())
			return vf.tuple(a);
		if (type.isSetType())
			return t.isEmpty() ? vf.set(t.getElementType()) : vf.set(a);
		if (type.isRelationType())
			return t.isEmpty() ? vf.set(t.getElementType()) : vf.set(a);
		return t.isEmpty() ? vf.list(t.getElementType()) : vf.list(a);
	}

	private IValue buildTerm(ISet t, Type type) {
		if (debug)
			System.err.println("buildTermSet" + " " + t.size());
		IValue[] a = new IValue[t.size()];
		Iterator<IValue> it = t.iterator();
		for (int i = 0; i < t.size(); i++) {
			a[i] = buildTerm(it.next(), type.isValueType() ? t.getElementType()
					: type.getElementType());
		}
		return t.isEmpty() ? vf.set(t.getElementType()) : vf.set(a);
	}

	private IValue buildTerm(ITuple t, Type type) {
		IValue[] a = new IValue[t.arity()];
		for (int i = 0; i < t.arity(); i++) {
			a[i] = buildTerm(t.get(i), type.isValueType() ? t.get(i).getType()
					: type.getFieldType(i));
		}
		return vf.tuple(a);
	}

	private IValue _buildTerm(IMap t, Type type) {
		IValue[] a1 = new IValue[t.size()];
		IValue[] a2 = new IValue[t.size()];
		Type keyType = type.isMapType() ? type.getKeyType() : t.getType()
				.getKeyType();
		Type valueType = type.isMapType() ? type.getValueType() : t.getType()
				.getValueType();
		Iterator<IValue> it = t.iterator();
		for (int i = 0; i < t.size(); i++) {
			a1[i] = buildTerm(it.next(), keyType);
			a2[i] = buildTerm(t.get(a1[i]), valueType);
		}
		IMapWriter w = vf.mapWriter();
		for (int i = 0; i < t.size(); i++) {
			w.put(a1[i], a2[i]);
		}
		return w.done();
	}

	@SuppressWarnings("unused")
	private IValue buildTerm(IMap t, Type type) {
		INode result;
		IValue key = t.get(nameKey);
		if (debug && key != null)
			System.err.println("builterm key=" + key);
		if (key == null) {
			if (type.isMapType() || t.getType().isMapType())
				return _buildTerm(t, type);
			else
				return t;
		}
		/* Abstract Data Type */
		final String funname = ((IString) key).getValue();
		if (debug && key != null)
			System.err.println("builterm funname=" + funname);
		IList rs = (IList) tf.listType(tf.valueType()).make(vf);
		final Iterator<IValue> args = ((IList) t.get(argKey)).iterator();
		while (args.hasNext()) {
			IValue arg = (IValue) args.next();
			arg = buildTerm(arg, type);
			rs = rs.append(arg);
		}
		IValue[] a = new IValue[rs.length()];
		Type[] b = new Type[rs.length()];
		{
			int i = 0;
			for (IValue c : rs) {
				a[i] = c;
				b[i] = c.getType();
				i++;
			}
		}
		HashMap<String, IValue> map = new HashMap<String, IValue>();
		IMap annoMap = (IMap) t.get(annoKey);
		if (annoMap != null) {
			Iterator<IValue> iterator = annoMap.iterator();		
			while (iterator.hasNext()) {
				IValue k = iterator.next();
				String ky = ((IString) k).getValue();
				IValue v = annoMap.get(k);
				map.put(ky, v);
			}
		}
		if (funname.equals("#rat")) {
			int dn = ((IInteger) a[0]).intValue(), nm = ((IInteger) a[1])
					.intValue();
			return vf.rational(dn, nm);
		}
		if (funname.equals("#tuple")) {
			return vf.tuple(a);
		}
		if (funname.equals("#loc")) {
			try {
				final URI uri = URIUtil.createFromEncoded(((IString) a[0]).getValue());
				return vf.sourceLocation(uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if (funname.equals("#set")) {
			return a.length == 0 ? vf.set(tf.valueType()) : vf.set(a);
		}
		if (funname.equals("#map")) {
			IMapWriter w = vf.mapWriter();
			for (int i = 0; i < a.length; i++) {
				w.put(((ITuple) a[i]).get(0), ((ITuple) a[i]).get(1));
			}
			return w.done();
		}
		if (funname.equals("#datetime"))
			return dateTime(((IString) a[0]).getValue());

		Type types = tf.tupleType(b);
		if (debug)
			System.err.println("lookupFirstConstructor:" + funname + " "
					+ types + " " + type);
		Type node = null;
		if (type.isAbstractDataType())
			node = ts.lookupConstructor(type, funname, types);
		if (node == null)
			node = ts.lookupFirstConstructor(funname, types);
		if (node == null) {
			Set<Type> nodes = ts.lookupConstructors(funname);
			Iterator<Type> it = nodes.iterator();
			while (it.hasNext()) {
				node = it.next();
				if (types.getArity() == node.getArity())
					break;
				node = null;
			}
		}
		if (debug)
			System.err.println("node2=" + node);
		if (node == null) {
			result = vf.node(funname, a);
		} else {
			if (node.isAliasType())
				node = node.getAliased();
			result = (INode) node.make(vf, a);
		}
		if (annoMap != null)
			result=result.setAnnotations(map);
		return result;
	}

	private IValue buildTerm(IValue t, Type type) {
		if (debug)
			System.err.println("BuildTerm:" + t + " " + type);
		if (t instanceof IMap)
			return buildTerm((IMap) t, type);
		if (t instanceof IList)
			return buildTerm((IList) t, type);
		if (t instanceof ISet)
			return buildTerm((ISet) t, type);
		if (t instanceof ITuple)
			return buildTerm((ITuple) t, type);
		return t;
	}

	private IValue parseTerms(JSonStream reader, Type expected)
			throws IOException {
		Type base = expected;
		Type elementType = getElementType(expected);
		IValue[] terms = parseTermsArray(reader, elementType);
		if (debug)
			System.err.println("ParseTerms2:" + base + " " + elementType);
		if (base.isListType() || base.isValueType()) {
			IListWriter w = expected.writer(vf);
			for (int i = terms.length - 1; i >= 0; i--) {
				w.insert(terms[i]);
			}
			return w.done();
		} else if (base.isRelationType()) {
			IRelationWriter w = expected.writer(vf);
			w.insert(terms);
			return w.done();

		} else if (base.isSetType()) {
			ISetWriter w = expected.writer(vf);
			w.insert(terms);
			return w.done();
		}
		throw new FactParseError("Unexpected type " + expected,
				reader.getPosition());
	}

	private Type getElementType(Type expected) {
		Type base = expected;
		if (base.isTupleType()) {
			return base;
		} else if (base.isRelationType()) {
			return base.getFieldTypes();
		} else if (base.isListType()) {
			return base.getElementType();
		} else if (base.isSetType()) {
			return base.getElementType();
		} else if (base.isMapType()) {
			return base;
		} else if (base.isAbstractDataType()) {
			return tf.tupleType(tf.stringType(), tf.valueType());
		} else if (base.isValueType()) {
			return base;
		} else {
			throw new IllegalOperationException("getElementType", expected);
		}
	}

	private IValue[] parseTermsArray(JSonStream reader, Type elementType)
			throws IOException {
		List<IValue> list = new ArrayList<IValue>(2);
		IValue term = parse(reader, elementType);
		list.add(term);
		while (reader.getLastChar() == ',') {
			reader.readSkippingWS();
			term = parse(reader, elementType);
			list.add(term);
		}
		IValue[] array = new IValue[list.size()];
		ListIterator<IValue> iter = list.listIterator();
		int index = 0;
		while (iter.hasNext()) {
			array[index++] = iter.next();
		}
		return array;
	}

	class JSonStream {

		private static final int INITIAL_BUFFER_SIZE = 1024;

		private BufferedReader reader;

		int last_char;
		private int pos;

		private char[] buffer;
		private int limit;
		private int bufferPos;

		public JSonStream(Reader read) {
			this(read, INITIAL_BUFFER_SIZE);
		}

		public JSonStream(Reader read, int bufferSize) {
			this.reader = new BufferedReader(read);
			last_char = -1;
			pos = 0;

			if (bufferSize < INITIAL_BUFFER_SIZE)
				buffer = new char[bufferSize];
			else
				buffer = new char[INITIAL_BUFFER_SIZE];
			limit = -1;
			bufferPos = -1;
		}

		public int read() throws IOException {
			if (bufferPos == limit) {
				limit = reader.read(buffer);
				bufferPos = 0;
			}

			if (limit == -1) {
				last_char = -1;
			} else {
				last_char = buffer[bufferPos++];
				pos++;
			}

			return last_char;
		}

		public int readSkippingWS() throws IOException {
			do {
				last_char = read();
			} while (Character.isWhitespace(last_char));
			return last_char;
		}

		public int skipWS() throws IOException {
			while (Character.isWhitespace(last_char)) {
				last_char = read();
			}
			return last_char;
		}

		public int readOct() throws IOException {
			int val = Character.digit(last_char, 8);
			val += Character.digit(read(), 8);

			if (val < 0) {
				throw new FactParseError("octal must have 3 octdigits.",
						getPosition());
			}

			val += Character.digit(read(), 8);

			if (val < 0) {
				throw new FactParseError("octal must have 3 octdigits",
						getPosition());
			}

			return val;
		}

		public int getLastChar() {
			return last_char;
		}

		public int getPosition() {
			return pos;
		}
	}

	@Override
	public IValue read(IValueFactory factory, TypeStore store, Type type,
			InputStream stream) throws FactTypeUseException, IOException {
		return read(factory, store, type, new UnicodeInputStreamReader(stream));
	}
}
