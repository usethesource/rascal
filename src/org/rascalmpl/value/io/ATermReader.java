/*******************************************************************************
* Copyright (c) INRIA-LORIA and CWI 2006-2009 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Jurgen Vinju (jurgenv@cwi.nl) - initial API and implementation

*******************************************************************************/
package org.rascalmpl.value.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactParseError;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.exceptions.UndeclaredAbstractDataTypeException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

// TODO: add support for values of type Value, for this we need overloading resolving
public class ATermReader extends AbstractBinaryReader {
	private IValueFactory vf;
	private TypeFactory tf = TypeFactory.getInstance();
	private TypeStore ts;

	public IValue read(IValueFactory factory, TypeStore store, Type type, InputStream stream)
			throws FactParseError, IOException {
		this.vf = factory;
		this.ts = store;

		int firstToken;
		do {
			firstToken = stream.read();
			if (firstToken == -1) {
				throw new IOException("Premature EOF.");
			}
		} while (Character.isWhitespace((char) firstToken));

		char typeByte = (char) firstToken;

		if (typeByte == '!') {
			SharingStream sreader = new SharingStream(stream);
			sreader.initializeSharing();
			sreader.readSkippingWS();
			return parse(sreader, type);
		} else if (typeByte == '?') {
			throw new UnsupportedOperationException("nyi");
		} else if (Character.isLetterOrDigit(typeByte) || typeByte == '_'
				|| typeByte == '[' || typeByte == '-') {
			SharingStream sreader = new SharingStream(stream);
			sreader.last_char = typeByte;
			return parse(sreader, type);
		} else {
			throw new RuntimeException("nyi");
		}
	}

	
	// TODO add support for anonymous constructors (is already done for the parseNumber case)
	private IValue parse(SharingStream reader, Type expected)
			throws IOException {
		IValue result;
		int start, end;

		start = reader.getPosition();
		switch (reader.getLastChar()) {
		case -1:
			throw new FactParseError("premature EOF encountered.", start);
		case '#':
			return parseAbbrev(reader);
		case '[':
			result = parseList(reader, expected);
			break;
		case '<':
			throw new FactParseError("Placeholders are not supported", start);
		case '"':
			result = parseString(reader, expected);
			break;
		case '(':
			result = parseTuple(reader, expected);
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
			result = parseAppl(reader, expected);
		}

		if (reader.getLastChar() == '{') {
			result = parseAnnotations(reader, result);
		}

		end = reader.getPosition();
		reader.storeNextTerm(result, end - start);

		return result;
	}


	private IValue parseAnnotations(SharingStream reader, IValue result)
			throws IOException {
		if (reader.readSkippingWS() == '}') {
			reader.readSkippingWS();
		} else {
			result = parseAnnos(reader, result);
			if (reader.getLastChar() != '}') {
				throw new FactParseError("'}' expected", reader.getPosition());
			}
		}
		return result;
	}


	private IValue parseAppl(SharingStream reader, Type expected)
			throws IOException {
		int c;
		IValue result;
		c = reader.getLastChar();
		if (Character.isLetter(c)) {

			String funname = parseId(reader);
			
			Type node;
			if (expected.isAbstractData()) {
				Set<Type> nodes = ts.lookupConstructor(expected, funname);
				// TODO deal with overloading
				Iterator<Type> iterator = nodes.iterator();
				if (!iterator.hasNext()) {
					throw new UndeclaredAbstractDataTypeException(expected);
				}
				node = iterator.next(); 
			}
			else {
				node = expected;
			}
			
			c = reader.skipWS();
			if (reader.getLastChar() == '(') {
				c = reader.readSkippingWS();
				if (c == -1) {
					throw new FactParseError("premature EOF encountered.", reader.getPosition());
				}
				if (reader.getLastChar() == ')') {
					
					// result = node.make(vf, ts, funname, new IValue[0]);
					if(node.isTop()) {
						Type constr = ts.lookupFirstConstructor(funname, tf.tupleType(new Type[0]));
						if(constr != null) node = constr;
					}
					if(node.isConstructor())
						result = vf.constructor(node, new IValue[0]);
					else
						result = vf.node(funname, new IValue[0]); 
					
				} else {
					IValue[] list;
					if (expected.isAbstractData()) {
						list = parseFixedSizeATermsArray(reader, node.getFieldTypes());
					}
					else {
						list = parseATermsArray(reader, TypeFactory.getInstance().valueType());
					}

					if (reader.getLastChar() != ')') {
						throw new FactParseError("expected ')' but got '"
								+ (char) reader.getLastChar() + "'", reader.getPosition());
					}
					
					if(node.isTop()) {
						Type constr = ts.lookupFirstConstructor(funname, tf.tupleType(list));
						if(constr != null) node = constr;
					}
					if (node.isConstructor())
						result = vf.constructor(node, list);
					else
						result = vf.node(funname, list);
				}
				c = reader.readSkippingWS();
			} else {
				if (node.isConstructor()) {
					result = vf.constructor(node);
				}
				else {
					result = vf.node(funname);
				}
			}
		} else {
			throw new FactParseError("illegal character: "
					+ (char) reader.getLastChar(), reader.getPosition());
		}
		return result;
	}


	private IValue parseTuple(SharingStream reader, Type expected)
			throws IOException {
		int c;
		IValue result;
		c = reader.readSkippingWS();
		if (c == -1) {
			throw new FactParseError("premature EOF encountered.", reader.getPosition());
		}
		if (reader.getLastChar() == ')') {
			result = vf.tuple();
		} else {
			IValue[] list = parseFixedSizeATermsArray(reader, expected);

			if (reader.getLastChar() != ')') {
				throw new FactParseError("expected ')' but got '"
						+ (char) reader.getLastChar() + "'", reader.getPosition());
			}

			result = vf.tuple(list);
		}
		c = reader.readSkippingWS();
		
		return result;
	}


	private IValue parseString(SharingStream reader, Type expected) throws IOException {
		int c;
		IValue result;
		String str = parseStringLiteral(reader);
		
		// note that we interpret all strings as strings, not possible function names.
		// this deviates from the ATerm library.
		
		result = vf.string(str);
		
		c = reader.readSkippingWS();
		if (c == -1) {
			throw new FactParseError("premature EOF encountered.", reader.getPosition());
		}
		return result;
	}


	private IValue parseList(SharingStream reader, Type expected)
			throws IOException {
		IValue result;
		int c;
		c = reader.readSkippingWS();
		if (c == -1) {
			throw new FactParseError("premature EOF encountered.", reader.getPosition());
		}

		if (c == ']') {
			c = reader.readSkippingWS();

			if (expected.isList()) {
				result = vf.list(expected.getElementType());
			} else if (expected.equivalent(TypeFactory.getInstance().valueType())) {
				result = vf.list(tf.valueType());
			}
			else {
				throw new FactParseError("Did not expect a list, rather a "
						+ expected, reader.getPosition());
			}
		} else {
			result = parseATerms(reader, expected);
			if (reader.getLastChar() != ']') {
				throw new FactParseError("expected ']' but got '"
						+ (char) reader.getLastChar() + "'", reader.getPosition());
			}
			c = reader.readSkippingWS();
		}
		return result;
	}

	private IValue parseAnnos(SharingStream reader, IValue result) throws IOException {
		result = parseAnno(reader, result);
		while (reader.getLastChar() == ',') {
			reader.readSkippingWS();
			result = parseAnno(reader, result);
		}
		
		return result;
	}
	
	private IValue parseAnno(SharingStream reader, IValue result) throws IOException {
		if (reader.getLastChar() == '[') {
			int c = reader.readSkippingWS();
			
			if (c == '"') {
				String key = parseStringLiteral(reader);
				Type annoType = ts.getAnnotationType(result.getType(), key);

				if (reader.readSkippingWS() == ',') {
					reader.readSkippingWS();
					IValue value = parse(reader, annoType);
					
					if (result.getType().isAbstractData()) {
						result = ((IConstructor) result).asAnnotatable().setAnnotation(key, value);
					}
					
					if (reader.getLastChar() != ']') {
						throw new FactParseError("expected a ] but got a " + reader.getLastChar(), reader.getPosition());
					}
					
					reader.readSkippingWS();
					return result;
				}
				
				throw new FactParseError("expected a comma before the value of the annotation", reader.getPosition());
			}
			
			throw new FactParseError("expected a label for an annotation", reader.getPosition());
		}
		
		// no annotations
		return result;
	}

	static private boolean isBase64(int c) {
		return Character.isLetterOrDigit(c) || c == '+' || c == '/';
	}

	private IValue parseAbbrev(SharingStream reader) throws IOException {
		IValue result;
		int abbrev;

		int c = reader.read();

		abbrev = 0;
		while (isBase64(c)) {
			abbrev *= 64;
			if (c >= 'A' && c <= 'Z') {
				abbrev += c - 'A';
			} else if (c >= 'a' && c <= 'z') {
				abbrev += c - 'a' + 26;
			} else if (c >= '0' && c <= '9') {
				abbrev += c - '0' + 52;
			} else if (c == '+') {
				abbrev += 62;
			} else if (c == '/') {
				abbrev += 63;
			} else {
				throw new RuntimeException("not a base-64 digit: " + c);
			}

			c = reader.read();
		}

		result = reader.getTerm(abbrev);

		return result;
	}

	private IValue parseNumber(SharingStream reader, Type expected) throws IOException {
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
				throw new FactParseError("malformed int:" + str, reader.getPosition());
			}
			
			result = vf.integer(val);
		} else if (reader.getLastChar() == 'l' || reader.getLastChar() == 'L') {
			reader.read();
			throw new FactParseError("No support for longs", reader.getPosition());
		} else {
			if (reader.getLastChar() == '.') {
				str.append('.');
				reader.read();
				if (!Character.isDigit(reader.getLastChar()))
					throw new FactParseError("digit expected", reader.getPosition());
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
					throw new FactParseError("digit expected!", reader.getPosition());
				do {
					str.append((char) reader.getLastChar());
				} while (Character.isDigit(reader.read()));
			}
			double val;
			try {
				val = Double.valueOf(str.toString());
				result = vf.real(val);
			} catch (NumberFormatException e) {
				throw new FactParseError("malformed real", reader.getPosition(), e);
			}
		}
		
		reader.skipWS();
		return result;
	}

	private String parseId(SharingStream reader) throws IOException {
		int c = reader.getLastChar();
		StringBuilder buf = new StringBuilder(32);

		do {
			buf.append((char) c);
			c = reader.read();
		} while (Character.isLetterOrDigit(c) || c == '_' || c == '-'
				|| c == '+' || c == '*' || c == '$' || c == '.');

		// slight deviation here, allowing . inside of identifiers
		return buf.toString();
	}

	private String parseStringLiteral(SharingStream reader) throws IOException {
		boolean escaped;
		StringBuilder str = new StringBuilder();

		do {
			escaped = false;
			if (reader.read() == '\\') {
				reader.read();
				escaped = true;
			}
			
			int lastChar = reader.getLastChar();
			if(lastChar == -1) throw new IOException("Premature EOF.");

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
			} else if (lastChar != '\"'){
				str.append((char) lastChar);
			}
		} while (escaped || reader.getLastChar() != '"');

		return str.toString();
	}

	private IValue parseATerms(SharingStream reader, Type expected)
			throws IOException {
		Type base = expected;
		Type elementType = getElementType(expected);
		IValue[] terms = parseATermsArray(reader, elementType);

		if (base.isList() || base.equivalent(tf.valueType())) {
			IListWriter w = vf.listWriter();

			for (int i = terms.length - 1; i >= 0; i--) {
				w.insert(terms[i]);
			}

			return w.done();
		} else if (base.isSet()) {
			ISetWriter w = vf.setWriter();
			w.insert(terms);
			return w.done();
		} else if (base.isMap()) {
			IMapWriter w = vf.mapWriter();
			
			for (IValue elem : terms) {
				ITuple tuple = (ITuple) elem;
				w.put(tuple.get(0), tuple.get(1));
			}
			
			return w.done();
		} else if (base.isRelation()) {
			ISetWriter w = vf.setWriter();
			w.insert(terms);
			return w.done();
		}

		throw new FactParseError("Unexpected type " + expected, reader.getPosition());
	}

	private Type getElementType(Type expected) {
		Type base = expected;
		
		if (base.isList()) {
			return base.getElementType();
		} else if (base.isSet()) {
			return base.getElementType();
		} else if (base.isMap()) {
			return tf.tupleType(base.getKeyType(), base.getValueType());
		} else if (base.isRelation()) {
			return base.getFieldTypes();
		} else if (base.isTop()) {
			return base;
		} 
		else {
			throw new IllegalOperationException("getElementType", expected);
		}
	}

	private IValue[] parseATermsArray(SharingStream reader,
			Type elementType) throws IOException {
		List<IValue> list = new ArrayList<>(2);

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
	
	private IValue[] parseFixedSizeATermsArray(SharingStream reader,
			Type elementTypes) throws IOException {
		List<IValue> list = new ArrayList<>(elementTypes.getArity());
		int i = 0;
		Type elementType = elementTypes.getFieldType(i++);

		IValue term = parse(reader, elementType);
		list.add(term);
		while (reader.getLastChar() == ',') {
			elementType = elementTypes.getFieldType(i++);
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


	private static class SharingStream {
		private static final int INITIAL_TABLE_SIZE = 2048;
		private static final int TABLE_INCREMENT = 4096;

		private static final int INITIAL_BUFFER_SIZE = 1024;

		private InputStream reader;

		int last_char;
		private int pos;

		private int nr_terms;
		private IValue[] table;

		private byte[] buffer;
		private int limit;
		private int bufferPos;

		public SharingStream(InputStream reader) {
			this(reader, INITIAL_BUFFER_SIZE);
		}

		public SharingStream(InputStream stream, int bufferSize) {
			this.reader = stream;
			last_char = -1;
			pos = 0;

			if (bufferSize < INITIAL_BUFFER_SIZE)
				buffer = new byte[bufferSize];
			else
				buffer = new byte[INITIAL_BUFFER_SIZE];
			limit = -1;
			bufferPos = -1;
		}

		public void initializeSharing() {
			table = new IValue[INITIAL_TABLE_SIZE];
			nr_terms = 0;
		}

		public void storeNextTerm(IValue t, int size) {
			if (table == null) {
				return;
			}

			if (nr_terms == table.length) {
				IValue[] new_table = new IValue[table.length + TABLE_INCREMENT];
				System.arraycopy(table, 0, new_table, 0, table.length);
				table = new_table;
			}

			table[nr_terms++] = t;
		}

		public IValue getTerm(int index) {
			if (index < 0 || index >= nr_terms) {
				throw new RuntimeException("illegal index");
			}
			return table[index];
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
				throw new FactParseError("octal must have 3 octdigits.", getPosition());
			}

			val += Character.digit(read(), 8);

			if (val < 0) {
				throw new FactParseError("octal must have 3 octdigits", getPosition());
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
}
