package org.rascalmpl.library.lang.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IWriter;
import org.eclipse.imp.pdb.facts.exceptions.FactParseError;
import org.eclipse.imp.pdb.facts.exceptions.UnexpectedTypeException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.type.DefaultTypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.unicode.UnicodeOutputStreamWriter;
import org.rascalmpl.uri.URIUtil;

public class IO {
	private static final TypeFactory types = TypeFactory.getInstance();
	
	private final IValueFactory values;
	private final StandardTextReader pdbReader;
	private int separator;  	// The separator to be used between fields
	private boolean header;		// Does the file start with a line defining field names?

	private TypeReifier tr;
	
	public IO(IValueFactory values){
		super();
		
		this.values = values;
		this.tr = new TypeReifier(values);
		separator = ',';
		header = true;
		this.pdbReader = new StandardTextReader();
	}
	
	private void setOptions(IBool header, IString separator) {
		this.separator = separator == null ? ',' : separator.charAt(0);
		this.header = header == null ? true : header.getValue();
	}
	
	/*
	 * Read a CSV file
	 */
	public IValue readCSV(ISourceLocation loc, IBool header, IString separator, IEvaluatorContext ctx){
		return read(null, loc, header, separator, ctx);
	}
	
	public IValue readCSV(IValue result, ISourceLocation loc, IBool header, IString separator, IEvaluatorContext ctx){
		return read(result, loc, header, separator, ctx);
	}


	/*
	 * Calculate the type of a CSV file, returned as the string 
	 */
	public IValue getCSVType(ISourceLocation loc, IBool header, IString separator, IEvaluatorContext ctx){
		return computeType(loc, header, separator, ctx);
	}
	
	//////
	
	private IValue read(IValue resultTypeConstructor, ISourceLocation loc, IBool header, IString separator, IEvaluatorContext ctx) {
		setOptions(header, separator);
		Type resultType = types.valueType();
		TypeStore store = new TypeStore();
		if (resultTypeConstructor != null && resultTypeConstructor instanceof IConstructor) {
			resultType = tr.valueToType((IConstructor)resultTypeConstructor, store);
		}
		Type actualType = resultType;
		while (actualType.isAliased()) {
			actualType = actualType.getAliased();
		}
		Reader reader = null;
		try {
			reader = ctx.getResolverRegistry().getCharacterReader(loc.getURI(), "UTF8");
			if (actualType.isTop()) {
				return readInferAndBuild(reader, store, ctx);
			}
			else {
				return readAndBuild(reader, actualType, store, ctx);
			}
		}
		catch (IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e){
					throw RuntimeExceptionFactory.io(values.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
				}
			}
		}
	}


	private IValue computeType(ISourceLocation loc, IBool header, IString separator, IEvaluatorContext ctx) {
		IValue csvResult = this.read(null, loc, header, separator, ctx);
		return ((IConstructor) new TypeReifier(values).typeToValue(csvResult.getType(), ctx).getValue());
	}
	
	private String[] readFirstRecord(FieldReader reader) throws IOException {
		List<String> result = new ArrayList<String>();
		if (reader.hasRecord()) {
			while (reader.hasField()) {
				result.add(reader.getField());
			}
			return result.toArray(new String[0]);
		}
		return new String[0];
	}

	private void collectFields(FieldReader reader, final String[] currentRecord, IEvaluatorContext ctx) throws IOException {
		int recordIndex = 0;
		while (reader.hasField()) {
			if (recordIndex < currentRecord.length) {
				currentRecord[recordIndex++] = reader.getField();
			}
			else {
				recordIndex++;  // count how many collumns to many we have
			}
		}
		if (recordIndex != currentRecord.length) {
			throw RuntimeExceptionFactory.illegalTypeArgument("Arities of actual type and requested type are different (expected: " + currentRecord.length + ", found: " + recordIndex + ")", ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}

	private IValue readInferAndBuild(Reader stream, TypeStore store, IEvaluatorContext ctx) throws IOException {
		FieldReader reader = new FieldReader(stream, separator);

		boolean first = header;
		final String[] currentRecord = readFirstRecord(reader);

		final String[] labels = new String[currentRecord.length]; 
		for (int l =0; l < labels.length; l++) {
			if (header) {
				labels[l] = normalizeLabel(currentRecord[l], l);
			}
			else {
				labels[l] = "field" + l;
			}
		}

		final Type[] expectedTypes = new Type[currentRecord.length];
		Arrays.fill(expectedTypes, types.valueType());
		
		final Type[] currentTypes = new Type[currentRecord.length]; 
		Arrays.fill(currentTypes, types.voidType());

		List<IValue[]> records = new LinkedList<>();

		do {
			if (first) {
				first = false;
				continue;
			}
			collectFields(reader, currentRecord, ctx);

			IValue[] tuple = new IValue[currentRecord.length];
			parseRecordFields(currentRecord, expectedTypes, store, tuple, false, ctx);
			records.add(tuple);

			// update infered type
			for (int i = 0; i < currentTypes.length; i++){
				if (tuple[i] == null) continue;
				currentTypes[i] = currentTypes[i].lub(tuple[i].getType());
				if(currentTypes[i].isTop()) {
					currentTypes[i] = types.stringType();
				}
			}
		} while(reader.hasRecord());
		
		// done reading records, lets infer and build
		for (int i = 0; i < currentTypes.length; i++){
			if(currentTypes[i].isBottom()) {
				// in case of an completly empty row
				currentTypes[i] = types.stringType();
			}
		}
		Type tupleType = types.tupleType(currentTypes, labels);
		Type resultType = types.setType(tupleType);
		ctx.getStdOut().println("readCSV inferred the relation type: " + resultType);
		ctx.getStdOut().flush();
		
		IWriter result = values.setWriter();
		for (IValue[] rec : records) {
			result.insert(createTuple(tupleType, rec));
		}
		return result.done();
	}
	private IValue createTuple(Type tupleType, IValue[] rec) {
		// do some fixing of empties and value => string
		for (int i = 0 ; i < rec.length; i++) {
			if (rec[i] == null) {
				rec[i] = defaultValue(tupleType.getFieldType(i));
			}
			else if (tupleType.getFieldType(i).isString() && !rec[i].getType().isString()) {
				rec[i] = values.string(rec[i].toString());
			}
		}
		return values.tuple(tupleType, rec);
	}

	private IValue readAndBuild(Reader stream, Type actualType, TypeStore store, IEvaluatorContext ctx) throws IOException {
		FieldReader reader = new FieldReader(stream, separator);
		IWriter result = actualType.isListRelation() ? values.listWriter() : values.setWriter();

		boolean first = header;
		Type tupleType = actualType.getElementType();
		Type[] expectedTypes = new Type[tupleType.getArity()];
		for (int i = 0; i < expectedTypes.length; i++) {
			expectedTypes[i] = tupleType.getFieldType(i);
		}

		final String[] currentRecord = new String[expectedTypes.length];
		final IValue[] tuple = new IValue[expectedTypes.length];
		while (reader.hasRecord()) {
			collectFields(reader, currentRecord, ctx);
			if (first) {
				first = false;
				continue;
			}
			parseRecordFields(currentRecord, expectedTypes, store, tuple, true, ctx);
			if (result instanceof IListWriter) {
				((IListWriter)result).append(values.tuple(tuple));
			}
			else {
				result.insert(values.tuple(tuple));
			}
		}
		return result.done();
	}

	private void parseRecordFields(final String[] fields, final Type[] expectedTypes, TypeStore store, IValue[] result, boolean replaceEmpty, final IEvaluatorContext ctx) throws IOException  {
		for (int i=0; i < fields.length; i++) {
			final String field = fields[i];
			final Type currentType = expectedTypes[i];

			if (field.isEmpty()) {
				if (replaceEmpty) {
					result[i] = defaultValue(currentType);
				}
				else {
					result[i] = null;
				}
				continue;
			}

			result[i] = currentType.accept(new DefaultTypeVisitor<IValue, RuntimeException>((IValue)null) {
				@Override
				public IValue visitString(Type type) throws RuntimeException {
					return values.string(field);
				}
				@Override
				public IValue visitInteger(Type type) throws RuntimeException {
					try {
						return values.integer(field);
					}
					catch (NumberFormatException nfe) {
						throw RuntimeExceptionFactory.illegalTypeArgument("Invalid int \"" + field + "\" for requested field " + currentType, ctx.getCurrentAST(), ctx.getStackTrace());
					}
				}
				@Override
				public IValue visitReal(Type type) throws RuntimeException {
					try {
						return values.real(field);
					}
					catch (NumberFormatException nfe) {
						throw RuntimeExceptionFactory.illegalTypeArgument("Invalid real \"" + field + "\" for requested field " + currentType, ctx.getCurrentAST(), ctx.getStackTrace());
					}
				}

			});
			
			if (result[i] == null) {
				// use pdb reader to deserialize the other types
				StringReader in = new StringReader(field);
				try {
					result[i] = pdbReader.read(values, store, currentType, in);
					if (currentType.isTop() && result[i].getType().isString()) {
						result[i] = values.string(field);
					}
				}
				catch (UnexpectedTypeException ute) {
					throw RuntimeExceptionFactory.illegalTypeArgument("Invalid field \"" + field + "\" (" + ute.getExpected() + ") for requested field " + ute.getGiven(), ctx.getCurrentAST(), ctx.getStackTrace());
				}
				catch (FactParseError ex) {
					if (currentType.isTop()) {
						result[i] = values.string(field);
					}
					else {
						throw RuntimeExceptionFactory.illegalTypeArgument("Invalid field \"" + field + "\" is not a " + currentType, ctx.getCurrentAST(), ctx.getStackTrace());
					}
				}
				finally {
					in.close();
				}
			}
		}
	}

	private IValue defaultValue(Type targetType) {
		IValue result = targetType.accept(new DefaultTypeVisitor<IValue, RuntimeException>(null) {
			@Override
			public IValue visitBool(Type type) throws RuntimeException {
				return values.bool(false);
			}
			@Override
			public IValue visitDateTime(Type type) throws RuntimeException {
				return values.datetime(-1);
			}
			@Override
			public IValue visitInteger(Type type) throws RuntimeException {
				return values.integer(0);
			}
			@Override
			public IValue visitList(Type type) throws RuntimeException {
				return values.list(type.getElementType());
			}
			@Override
			public IValue visitMap(Type type) throws RuntimeException {
				return values.mapWriter().done();
			}
			@Override
			public IValue visitNumber(Type type) throws RuntimeException {
				return values.integer(0);
			}
			@Override
			public IValue visitRational(Type type) throws RuntimeException {
				return values.rational(0, 0);
			}
			@Override
			public IValue visitReal(Type type) throws RuntimeException {
				return values.real(0);
			}
			@Override
			public IValue visitSet(Type type) throws RuntimeException {
				return values.set(type.getElementType());
			}
			@Override
			public IValue visitSourceLocation(Type type) throws RuntimeException {
				return values.sourceLocation(URIUtil.invalidURI());
			}
			@Override
			public IValue visitString(Type type) throws RuntimeException {
				return values.string("");
			}
			@Override
			public IValue visitTuple(Type type) throws RuntimeException {
				IValue[] elems = new IValue[type.getArity()];
				for (int i =0; i < elems.length; i++) {
					elems[i] = type.getFieldType(i).accept(this);
					if (elems[i] == null)
						return null;
				}
				return values.tuple(elems);
			}
			@Override
			public IValue visitValue(Type type) throws RuntimeException {
				return values.string("");
			}
		});
		if (result != null)
			return result;
		throw RuntimeExceptionFactory.illegalTypeArgument("Cannot create a default value for an empty field of type " + targetType, null, null);
	}
	
	
	/*
	 * Write a CSV file.
	 */
	public void writeCSV(IValue rel, ISourceLocation loc, IBool header, IString separator, IEvaluatorContext ctx){
		String sep = separator != null ? separator.getValue() : ",";
		Boolean head = header != null ? header.getValue() : true;
		Writer out = null;
		
		Type paramType = ctx.getCurrentEnvt().getTypeBindings().get(types.parameterType("T"));
		if(!paramType.isRelation() && !paramType.isListRelation()){
			throw RuntimeExceptionFactory.illegalTypeArgument("A relation type is required instead of " + paramType,ctx.getCurrentAST(), 
					ctx.getStackTrace());
		}
		
		try{
			boolean isListRel = rel instanceof IList;
			out = new UnicodeOutputStreamWriter(ctx.getResolverRegistry().getOutputStream(loc.getURI(), false), "UTF8", false);
			out = new BufferedWriter(out); // performance
			ISet irel = null;
			IList lrel = null;
			if (isListRel) {
				lrel = (IList)rel;
			}
			else {
				irel = (ISet) rel;
			}
			
			int nfields = isListRel ? lrel.asRelation().arity() : irel.asRelation().arity();
			if(head){
				for(int i = 0; i < nfields; i++){
					if(i > 0)
						out.write(sep);
					String label = paramType.getFieldName(i);
					if(label == null || label.isEmpty())
						label = "field" + i;
					out.write(label);
				}
				out.write('\n');
			}
			
			Pattern escapingNeeded = Pattern.compile("[\\n\\r\"\\x" + Integer.toHexString(separator.charAt(0)) + "]");
			
			for(IValue v : (isListRel ? lrel : irel)){
				ITuple tup = (ITuple) v;
				boolean firstTime = true;
				for(IValue w : tup){
					if(firstTime)
						firstTime = false;
					else
						out.write(sep);

					String s;
					if(w.getType().isString()){
						s = ((IString)w).getValue();
					}
					else {
						s = w.toString();
					}
					if(escapingNeeded.matcher(s).find()){
						s = s.replaceAll("\"", "\"\"");
						out.write('"');
						out.write(s);
						out.write('"');
					} else
						out.write(s);
				}
				out.write('\n');
			}
		}
		catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(out != null){
				try{
					out.flush();
					out.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	/**
	 * Normalize a label in the header for use in the relation type.
	 * The name is escaped to avoid conflicts with Rascal keywords.
	 * @param label	The string found in the header
	 * @param pos	Position in the header
	 * @return		The label (with non-fieldname characters removed) or "field<pos>" when empty
	 */
	private String normalizeLabel(String label, int pos){
		label = label.replaceAll("[^a-zA-Z0-9]+", "");
		if(label.isEmpty())
			return "field" + pos;
		else 
		  return "\\" + label;
	}
}

/**
 * Auxiliary class to read fields from an input stream.
 *
 */
class FieldReader {
	int lastChar = ';';
	int separator = ';';
	Reader in;
	boolean startOfLine = true;
	
	FieldReader(Reader reader, int sep) throws IOException{
		this.in = reader;
		this.separator = sep;
		startOfLine = true;
		lastChar = reader.read();
	}
	
	private boolean isEOL(int c) {
		return c == '\n' || c == '\r';
	}
	
	/**
	 * @return true if the current record has another field left to be read.
	 * @throws IOException
	 */
	boolean hasField() throws IOException{
		if(startOfLine)
			return true;
		if(lastChar == separator){
			lastChar = in.read();
			return true; //lastChar != -1;
		}
		return false;
	}
	
	/**
	 * @return true if the current stream has another record to be read.
	 * @throws IOException
	 */
	boolean hasRecord() throws IOException{
		if(startOfLine)
			return true;
		while(isEOL(lastChar))
			lastChar = in.read();
		startOfLine = true;
		return lastChar != -1;
	}
	
	/**
	 * @return The next field from the input stream.
	 * @throws IOException
	 */
	String getField() throws IOException{
		startOfLine = false;
		StringWriter sw = new StringWriter();
		if(lastChar == '"'){
			lastChar = in.read();
			while (lastChar != -1){
				if(lastChar == '"'){
					lastChar = in.read();
					if(lastChar == '"'){
						sw.append('"');
						lastChar = in.read();
					} else
						break;
				} else {
					sw.append((char)lastChar);
					lastChar = in.read();
				}
			}
			assert lastChar == separator || isEOL(lastChar) || lastChar == -1;
			return sw.toString();
		}
		while ((lastChar != -1) && (lastChar != separator) && !isEOL(lastChar)){
			sw.append((char)lastChar);
			lastChar = in.read();
		}
		return sw.toString();
	}
}
