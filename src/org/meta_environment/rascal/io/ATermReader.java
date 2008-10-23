package org.meta_environment.rascal.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IValueReader;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.ListType;
import org.eclipse.imp.pdb.facts.type.MapType;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.RelationType;
import org.eclipse.imp.pdb.facts.type.SetType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.TreeSortType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class ATermReader implements IValueReader {
	private IValueFactory vf;
	private TypeFactory tf = TypeFactory.getInstance();
	private Type annoT = tf.listType(tf.listType(tf.valueType()));

	public IValue read(IValueFactory factory, Type type, InputStream stream)
			throws FactTypeError, IOException {
		this.vf = factory;

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

	private IValue parse(SharingStream reader, Type expected)
			throws IOException {
		IValue result;
		int c, start, end;

		start = reader.getPosition();
		switch (reader.getLastChar()) {
		case -1:
			throw new FactTypeError("premature EOF encountered.");

		case '#':
			return parseAbbrev(reader);

		case '[':

			c = reader.readSkippingWS();
			if (c == -1) {
				throw new FactTypeError("premature EOF encountered.");
			}

			if (c == ']') {
				c = reader.readSkippingWS();

				if (expected.isListType()) {
					result = vf.list((((ListType) expected).getElementType()));
					((IList) result).getWriter().done();
				} else if (expected.isNamedType()
						&& expected.getBaseType().isListType()) {
					result = vf.list(expected);
					((IList) result).getWriter().done();
				} else {
					throw new FactTypeError("Did not expect a list, rather a "
							+ expected);
				}
			} else {
				result = parseATerms(reader, expected);
				if (reader.getLastChar() != ']') {
					throw new FactTypeError("expected ']' but got '"
							+ (char) reader.getLastChar() + "'");
				}
				c = reader.readSkippingWS();
			}

			break;

		case '<':
			throw new FactTypeError("Placeholders are not supported");

		case '"':
			// note that we interpret all strings as strings, not possible function names.
			// this deviates from the ATerm library.
			String str = parseString(reader);
			
			if (expected.getBaseType().isStringType()) {
				if (expected.isNamedType()) {
					result = vf.string((NamedType) expected, str);
				}
				else {
					result = vf.string(str);
				}
			}
			else {
				throw new FactTypeError("Expected " + expected + " but got a string");
			}
			
			c = reader.readSkippingWS();
			if (c == -1) {
				throw new FactTypeError("premature EOF encountered.");
			}
			break;
		case '(':
			c = reader.readSkippingWS();
			if (c == -1) {
				throw new FactTypeError("premature EOF encountered.");
			}
			if (reader.getLastChar() == ')') {
				if (expected.getBaseType().isTupleType()) {
					if (expected.isNamedType()) {
					  result = vf.tuple((NamedType) expected, new IValue[0], 0);
					}
					else {
						result = vf.tuple(new IValue[0], 0);
					}
				}
				else {
					throw new FactTypeError("Expected " + expected + " but got an empty tuple");
				}
				
			} else {
				if (!expected.getBaseType().isTupleType()) {
					throw new FactTypeError("Expected a " + expected + " but got a tuple");
				}
				IValue[] list = parseFixedSizeATermsArray(reader, (TupleType) expected.getBaseType());

				if (reader.getLastChar() != ')') {
					throw new FactTypeError("expected ')' but got '"
							+ (char) reader.getLastChar() + "'");
				}

				if (expected.isNamedType()) {
					result = vf.tuple((NamedType) expected, list, list.length);
				} else {
					result = vf.tuple(list, list.length);
				}
			}
			c = reader.readSkippingWS();
			if (c == -1) {
				throw new FactTypeError("premature EOF encountered.");
			}

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
			c = reader.skipWS();
			break;

		default:
			c = reader.getLastChar();
			if (Character.isLetter(c)) {

				String funname = parseId(reader);
				
				if (!expected.getBaseType().isTreeSortType()) {
					throw new FactTypeError("Expected a " + expected + " but got a tree node");
				}
				
				TreeNodeType node = tf.signatureGet((TreeSortType) expected.getBaseType(), funname);
				
				c = reader.skipWS();
				if (reader.getLastChar() == '(') {
					c = reader.readSkippingWS();
					if (c == -1) {
						throw new FactTypeError("premature EOF encountered.");
					}
					if (reader.getLastChar() == ')') {
						result = vf
								.tree(node, new IValue[0]);
					} else {
						IValue[] list = parseFixedSizeATermsArray(reader, node.getChildrenTypes());

						if (reader.getLastChar() != ')') {
							throw new FactTypeError("expected ')' but got '"
									+ (char) reader.getLastChar() + "'");
						}
						
						result = vf.tree(node, list);
					}
					c = reader.readSkippingWS();
				} else {
				    result = vf.tree(node, new IValue[0]);
				}
			} else {
				throw new FactTypeError("illegal character: "
						+ (char) reader.getLastChar());
			}
		}

		
		// TODO add support for annotations
		if (reader.getLastChar() == '{') {

			IValue annos;
			if (reader.readSkippingWS() == '}') {
				reader.readSkippingWS();
				annos = vf.list(tf.integerType());
			} else {
				annos = parseATerms(reader, annoT);
				if (reader.getLastChar() != '}') {
					throw new FactTypeError("'}' expected");
				}
				reader.readSkippingWS();
			}

			System.err.println(annos);
//			result = result.setAnnotations(annos);

		}

		end = reader.getPosition();
		reader.storeNextTerm(result, end - start);

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
				throw new FactTypeError("malformed int");
			}
			
			if (expected.getBaseType().isIntegerType()) {
				if (expected.isNamedType()) {
					result = vf.integer((NamedType) expected, val);
				}
				else {
					result = vf.integer(val);
				}
			}
			else {
				throw new FactTypeError("Expected " + expected + " but got integer");
			}
		} else if (reader.getLastChar() == 'l' || reader.getLastChar() == 'L') {
			reader.read();
			throw new FactTypeError("No support for longs");
		} else {
			if (reader.getLastChar() == '.') {
				str.append('.');
				reader.read();
				if (!Character.isDigit(reader.getLastChar()))
					throw new FactTypeError("digit expected");
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
					throw new FactTypeError("digit expected!");
				do {
					str.append((char) reader.getLastChar());
				} while (Character.isDigit(reader.read()));
			}
			double val;
			try {
				val = Double.valueOf(str.toString()).doubleValue();
			} catch (NumberFormatException e) {
				throw new FactTypeError("malformed real");
			}
			
			if (expected.getBaseType().isDoubleType()) {
				if (expected.isNamedType()) {
					result = vf.dubble((NamedType) expected, val);
				}
				else {
					result = vf.dubble(val);
				}
			}
			else {
				throw new FactTypeError("Expected " + expected + " but got integer");
			}
		}
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

	private String parseString(SharingStream reader) throws IOException {
		boolean escaped;
		StringBuilder str = new StringBuilder();

		do {
			escaped = false;
			if (reader.read() == '\\') {
				reader.read();
				escaped = true;
			}

			if (escaped) {
				switch (reader.getLastChar()) {
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
					str.append('\\').append((char) reader.getLastChar());
				}
			} else if (reader.getLastChar() != '\"')
				str.append((char) reader.getLastChar());
		} while (escaped || reader.getLastChar() != '"');

		return str.toString();
	}

	private IValue parseATerms(SharingStream reader, Type expected)
			throws IOException {
		Type base = expected.getBaseType();
		Type elementType = getElementType(expected);
		IValue[] terms = parseATermsArray(reader, elementType);

		if (base.isListType()) {
			IList result;

			if (expected.isNamedType()) {
				result = vf.list((NamedType) expected);
			} else {
				result = vf.list(elementType);
			}

			IListWriter w = result.getWriter();

			for (int i = terms.length - 1; i >= 0; i--) {
				w.insert(terms[i]);
			}
			w.done();

			return result;
		} else if (base.isSetType()) {
			ISet result;

			if (expected.isNamedType()) {
				result = vf.set((NamedType) expected);
			} else {
				result = vf.set(elementType);
			}

			ISetWriter w = result.getWriter();

			for (IValue elem : terms) {
				w.insert(elem);
			}
			w.done();

			return result;
		} else if (base.isMapType()) {
			IMap result;
			
			if (expected.isNamedType()) {
				result = vf.map((NamedType) expected);
			}
			else {
				MapType mapType = (MapType) expected;
				result = vf.map(mapType.getKeyType(), mapType.getValueType());
			}
			
			IMapWriter w = result.getWriter();
			
			for (IValue elem : terms) {
				ITuple tuple = (ITuple) elem;
				w.put(tuple.get(0), tuple.get(1));
			}
			
			w.done();
			return result;
		} else if (base.isRelationType()) {
			IRelation result;
			
			if (expected.isNamedType()) {
				result = vf.relation((NamedType) expected);
			}
			else {
				RelationType relType = (RelationType) expected;
				result = vf.relation(relType.getFieldTypes());
			}
			
			IRelationWriter w = result.getWriter();
			
			for (IValue elem : terms) {
				ITuple tuple = (ITuple) elem;
				w.insert(tuple);
			}
			
			w.done();
			
			return result;
		}

		throw new FactTypeError("Unexpected type " + expected);
	}

	private Type getElementType(Type expected) {
		Type base = expected.getBaseType();

		if (base.isListType()) {
			return ((ListType) expected.getBaseType()).getElementType();
		} else if (base.isSetType()) {
			return ((SetType) expected.getBaseType()).getElementType();
		} else if (base.isMapType()) {
			MapType map = (MapType) base;
			return tf.tupleTypeOf(map.getKeyType(), map.getValueType());
		} else if (base.isRelationType()) {
			RelationType rel = (RelationType) base;
			return rel.getFieldTypes();
		} else {
			throw new FactTypeError("Found a list, set, relation or map instead of the expected "
					+ expected);
		}
	}

	private IValue[] parseATermsArray(SharingStream reader,
			Type elementType) throws IOException {
		List<IValue> list = new ArrayList<IValue>();

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
			TupleType elementTypes) throws IOException {
		List<IValue> list = new ArrayList<IValue>();
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


	class SharingStream {
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
				throw new FactTypeError("octal must have 3 octdigits.");
			}

			val += Character.digit(read(), 8);

			if (val < 0) {
				throw new FactTypeError("octal must have 3 octdigits");
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
