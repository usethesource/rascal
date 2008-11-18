package org.meta_environment.rascal.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.IValueReader;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.ListType;
import org.eclipse.imp.pdb.facts.type.MapType;
import org.eclipse.imp.pdb.facts.type.RelationType;
import org.eclipse.imp.pdb.facts.type.SetType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.NamedTreeType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

// TODO: add support for values of type Value, for this we need overloading resolving
public class ATermReader implements IValueReader {
	private IValueFactory vf;
	private TypeFactory tf = TypeFactory.getInstance();

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

	
	// TODO add support for anonymous constructors (is already done for the parseNumber case)
	private IValue parse(SharingStream reader, Type expected)
			throws IOException {
		IValue result;
		int start, end;

		start = reader.getPosition();
		switch (reader.getLastChar()) {
		case -1:
			throw new FactTypeError("premature EOF encountered.");
		case '#':
			return parseAbbrev(reader);
		case '[':
			result = parseList(reader, expected);
			break;
		case '<':
			throw new FactTypeError("Placeholders are not supported");
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
				throw new FactTypeError("'}' expected");
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
			
			if (!expected.getBaseType().isNamedTreeType()) {
				throw new FactTypeError("Expected a " + expected + " but got a tree node");
			}
			
			List<TreeNodeType> nodes = tf.lookupTreeNodeType((NamedTreeType) expected.getBaseType(), funname);
			TreeNodeType node = nodes.get(0); // TODO deal with overloading
			
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
					
					result = node.make(vf, list);
				}
				c = reader.readSkippingWS();
			} else {
			    result = node.make(vf);
			}
		} else {
			throw new FactTypeError("illegal character: "
					+ (char) reader.getLastChar());
		}
		return result;
	}


	private IValue parseTuple(SharingStream reader, Type expected)
			throws IOException {
		int c;
		IValue result;
		c = reader.readSkippingWS();
		if (c == -1) {
			throw new FactTypeError("premature EOF encountered.");
		}
		if (reader.getLastChar() == ')') {
			result = expected.make(vf);
		} else {
			IValue[] list = parseFixedSizeATermsArray(reader, (TupleType) expected.getBaseType());

			if (reader.getLastChar() != ')') {
				throw new FactTypeError("expected ')' but got '"
						+ (char) reader.getLastChar() + "'");
			}

			result = expected.make(vf, list);
		}
		c = reader.readSkippingWS();
		if (c == -1) {
			throw new FactTypeError("premature EOF encountered.");
		}
		return result;
	}


	private IValue parseString(SharingStream reader, Type expected) throws IOException {
		int c;
		IValue result;
		String str = parseStringLiteral(reader);
		
		// note that we interpret all strings as strings, not possible function names.
		// this deviates from the ATerm library.
		
		result = expected.make(vf, str);
		
		c = reader.readSkippingWS();
		if (c == -1) {
			throw new FactTypeError("premature EOF encountered.");
		}
		return result;
	}


	private IValue parseList(SharingStream reader, Type expected)
			throws IOException {
		IValue result;
		int c;
		c = reader.readSkippingWS();
		if (c == -1) {
			throw new FactTypeError("premature EOF encountered.");
		}

		if (c == ']') {
			c = reader.readSkippingWS();

			if (expected.getBaseType().isListType()) {
				result = expected.make(vf);
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
				Type annoType = tf.getAnnotationType(result.getType(), key);

				if (reader.readSkippingWS() == ',') {
					reader.readSkippingWS();
					IValue value = parse(reader, annoType);
					result = result.setAnnotation(key, value);
					
					if (reader.getLastChar() != ']') {
						throw new FactTypeError("expected a ] but got a " + reader.getLastChar());
					}
					
					reader.readSkippingWS();
					return result;
				}
				else {
					throw new FactTypeError("expected a comma before the value of the annotation");
				}
			}
			else {
				throw new FactTypeError("expected a label for an annotation");
			}
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
				throw new FactTypeError("malformed int:" + str);
			}
			
			result = expected.make(vf, val);
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
				result = expected.make(vf, val);
			} catch (NumberFormatException e) {
				throw new FactTypeError("malformed real");
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
			IListWriter w = expected.writer(vf);

			for (int i = terms.length - 1; i >= 0; i--) {
				w.insert(terms[i]);
			}

			return w.done();
		} else if (base.isSetType()) {
			ISetWriter w = expected.writer(vf);
			w.insert(terms);
			return w.done();
		} else if (base.isMapType()) {
			IMapWriter w = expected.writer(vf);
			
			for (IValue elem : terms) {
				ITuple tuple = (ITuple) elem;
				w.put(tuple.get(0), tuple.get(1));
			}
			
			return w.done();
		} else if (base.isRelationType()) {
			ISetWriter w = expected.writer(vf);
			w.insert(terms);
			return w.done();
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
			return tf.tupleType(map.getKeyType(), map.getValueType());
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
