package org.eclipse.imp.pdb.facts.io;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IValueReader;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.TreeType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class ATermReader implements IValueReader {
	private IValueFactory vf;
	private TypeFactory tf;

	public IValue read(IValueFactory factory, Type type, Reader reader)
			throws FactTypeError, IOException {
		this.vf = factory;
		this.tf = TypeFactory.getInstance();

		int firstToken;
		do {
			firstToken = reader.read();
			if (firstToken == -1)
				throw new IOException("Premature EOF.");
		} while (Character.isWhitespace((char) firstToken));

		char typeByte = (char) firstToken;

		if (typeByte == '!') {
			SharingReader sreader = new SharingReader(reader);
			sreader.initializeSharing();
			sreader.readSkippingWS();
			return parse(sreader, type);
		} else if (typeByte == '?') {
			throw new UnsupportedOperationException("nyi");
		} else if (Character.isLetterOrDigit(typeByte) || typeByte == '_'
				|| typeByte == '[' || typeByte == '-') {
			SharingReader sreader = new SharingReader(reader);
			sreader.last_char = typeByte; 
			return parse(sreader, type);
		} else {
			throw new RuntimeException("nyi");
		}
	}

	private IValue parse(SharingReader reader, Type expected) throws IOException {
		IValue result;
		int c, start, end;
		String funname;

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
				result = vf.list(tf.integerType());
			} else {
				result = parseATerms(reader);
				if (reader.getLastChar() != ']') {
					throw new FactTypeError("expected ']' but got '"
							+ (char) reader.getLastChar() + "'");
				}
				c = reader.readSkippingWS();
			}

			break;

		case '<':

			c = reader.readSkippingWS();
			IValue ph = parse(reader, expected);

			if (reader.getLastChar() != '>') {
				throw new FactTypeError("expected '>' but got '"
						+ (char) reader.getLastChar() + "'");
			}

			c = reader.readSkippingWS();

//			result = makePlaceholder(ph);
			// TODO: what to do with placeholders???
			result = ph;

			break;

		case '"':

			funname = parseString(reader);

			c = reader.readSkippingWS();
			if (reader.getLastChar() == '(') {
				c = reader.readSkippingWS();
				if (c == -1) {
					throw new FactTypeError("premature EOF encountered.");
				}
				if (reader.getLastChar() == ')') {
					// TODO validation funname
					result = vf.tree((TreeType) expected, new IValue[0]);
				} else {
					IValue[] list = parseATermsArray(reader);

					if (reader.getLastChar() != ')') {
						throw new FactTypeError("expected ')' but got '"
								+ reader.getLastChar() + "'");
					}
					 
					// TODO validation
					result = vf.tree((TreeType) expected, list);
				}
				c = reader.readSkippingWS();
				if (c == -1) {
					throw new FactTypeError("premature EOF encountered.");
				}
			} else {
				// TODO validation funname
				result = vf.tree((TreeType) expected, new IValue[0]);
			}

			break;

		case '(':

			c = reader.readSkippingWS();
			if (c == -1) {
				throw new FactTypeError("premature EOF encountered.");
			}
			if (reader.getLastChar() == ')') {
				// TODO validation
				result = vf.tree((TreeType) expected, new IValue[0]);
			} else {
				IValue[] list = parseATermsArray(reader);

				if (reader.getLastChar() != ')') {
					throw new FactTypeError("expected ')' but got '"
							+ (char) reader.getLastChar() + "'");
				}
				
//				result = makeAppl(makeAFun("", list.length, false), list);
				result = vf.tree((TreeType) expected, list);
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
			result = parseNumber(reader);
			c = reader.skipWS();
			break;

		default:
			c = reader.getLastChar();
			if (Character.isLetter(c)) {

				funname = parseId(reader);
				c = reader.skipWS();
				if (reader.getLastChar() == '(') {
					c = reader.readSkippingWS();
					if (c == -1) {
						throw new FactTypeError("premature EOF encountered.");
					}
					if (reader.getLastChar() == ')') {
						// TODO validate
//						result = makeAppl(makeAFun(funname, 0, false));
						result = vf.tree((TreeType) expected, new IValue[0]);
					} else {
						IValue[] list = parseATermsArray(reader);

						if (reader.getLastChar() != ')') {
							throw new FactTypeError("expected ')' but got '"
									+ (char) reader.getLastChar() + "'");
						}
						// TODO validate
//						result = makeAppl(
//								makeAFun(funname, list.length, false), list);
						result = vf.tree((TreeType) expected, list);
					}
					c = reader.readSkippingWS();
				} else {
					// TODO validate
//					result = makeAppl(makeAFun(funname, 0, false));
					result = vf.tree((TreeType) expected, new IValue[0]);
				}

			} else {
				throw new FactTypeError("illegal character: "
						+ (char) reader.getLastChar());
			}
		}

		if (reader.getLastChar() == '{') {

			IList annos;
			if (reader.readSkippingWS() == '}') {
				reader.readSkippingWS();
				annos = vf.list(tf.integerType());
			} else {
				annos = parseATerms(reader);
				if (reader.getLastChar() != '}') {
					throw new FactTypeError("'}' expected");
				}
				reader.readSkippingWS();
			}
			
			// TODO: add annotations
//			result = result.setAnnotations(annos);

		}

		end = reader.getPosition();
		reader.storeNextTerm(result, end - start);

		return result;
	}

	static private boolean isBase64(int c) {
	    return Character.isLetterOrDigit(c) || c == '+' || c == '/';
	}
	
	private IValue parseAbbrev(SharingReader reader) throws IOException {
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

	  private IValue parseNumber(SharingReader reader) throws IOException {
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
	      result = vf.integer(val);
	    } else if (reader.getLastChar() == 'l' || reader.getLastChar() == 'L') {
	      reader.read();
	      long val;
	      try {
	        val = Long.parseLong(str.toString());
	      } catch (NumberFormatException e) {
	        throw new FactTypeError("malformed long");
	      }
	      // TODO: what to do with longs?
	      result = vf.integer((int) val);
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
	      result = vf.dubble(val);	    }
	    return result;
	  }

	  private String parseId(SharingReader reader) throws IOException {
	    int c = reader.getLastChar();
	    StringBuilder buf = new StringBuilder(32);

	    do {
	      buf.append((char) c);
	      c = reader.read();
	    } while (Character.isLetterOrDigit(c) || c == '_' || c == '-'
	        || c == '+' || c == '*' || c == '$');

	    return buf.toString();
	  }

	  private String parseString(SharingReader reader) throws IOException {
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

	  private IList parseATerms(SharingReader reader) throws IOException {
	    IValue[] terms = parseATermsArray(reader);
	    IList result = vf.list(tf.integerType());
	    IListWriter w = result.getWriter();
	    for (int i = terms.length - 1; i >= 0; i--) {
	      w.insert(terms[i]);
	    }
	    w.done();

	    return result;
	  }

	  private IValue[] parseATermsArray(SharingReader reader) throws IOException {
	    List<IValue> list = new ArrayList<IValue>();
	    
	    IValue term = parse(reader);
	    list.add(term);
	    while (reader.getLastChar() == ',') {
	      reader.readSkippingWS();
	      term = parse(reader);
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

	class SharingReader {
		private static final int INITIAL_TABLE_SIZE = 2048;
		private static final int TABLE_INCREMENT = 4096;

		private static final int INITIAL_BUFFER_SIZE = 1024;

		private Reader reader;

		int last_char;
		private int pos;

		private int nr_terms;
		private IValue[] table;

		private char[] buffer;
		private int limit;
		private int bufferPos;

		public SharingReader(Reader reader) {
			this(reader, INITIAL_BUFFER_SIZE);
		}

		public SharingReader(Reader reader, int bufferSize) {
			this.reader = reader;
			last_char = -1;
			pos = 0;

			if (bufferSize < INITIAL_BUFFER_SIZE)
				buffer = new char[bufferSize];
			else
				buffer = new char[INITIAL_BUFFER_SIZE];
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
