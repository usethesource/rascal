package org.rascalmpl.library.lang.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Types;
import org.rascalmpl.unicode.UnicodeOutputStreamWriter;
import org.rascalmpl.uri.URIUtil;

public class IOCompiled extends IO {
	private static final TypeFactory types = TypeFactory.getInstance();
	private Types types2;
	
	private final IValueFactory values;
	private final StandardTextReader pdbReader;
	private int separator;  	// The separator to be used between fields
	private boolean header;		// Does the file start with a line defining field names?
	
	private TypeReifier tr;
	
	private boolean printInferredType;
	
	public IOCompiled(IValueFactory values){
		super(values);
		
		this.values = values;
		this.types2 = new Types(values);
		this.tr = new TypeReifier(values);
		separator = ',';
		header = true;
		this.pdbReader = new StandardTextReader();
	}
	
	private void setOptions(IBool header, IString separator, IBool printInferredType) {
		this.separator = separator == null ? ',' : separator.charAt(0);
		this.header = header == null ? true : header.getValue();
		this.printInferredType = printInferredType == null ? false : printInferredType.getValue();
	}
	
	/*
	 * Read a CSV file
	 */
	public IValue readCSV(ISourceLocation loc, IBool header, IString separator, IString encoding, IBool printInferredType, RascalExecutionContext rex){
		return read(null, loc, header, separator, encoding, printInferredType, rex);
	}
	
	public IValue readCSV(IValue result, ISourceLocation loc, IBool header, IString separator, IString encoding, RascalExecutionContext rex){
		return read(result, loc, header, separator, encoding, values.bool(false), rex);
	}


	/*
	 * Calculate the type of a CSV file, returned as the string 
	 */
	public IValue getCSVType(ISourceLocation loc, IBool header, IString separator, IString encoding, IBool printInferredType, RascalExecutionContext rex){
		return computeType(loc, header, separator, encoding, rex);
	}
	
	//////
	
	private IValue read(IValue resultTypeConstructor, ISourceLocation loc, IBool header, IString separator, IString encoding, IBool printInferredType, RascalExecutionContext rex) {
		setOptions(header, separator, printInferredType);
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
			reader = rex.getResolverRegistry().getCharacterReader(loc.getURI(), encoding.getValue());
			if (actualType.isTop()) {
				return readInferAndBuild(reader, store, rex);
			}
			else {
				return readAndBuild(reader, actualType, store, rex);
			}
		}
		catch (IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e){
					throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
				}
			}
		}
	}


	private IValue computeType(ISourceLocation loc, IBool header, IString separator, IString encoding, RascalExecutionContext rex) {
		IValue csvResult = this.read(null, loc, header, separator, encoding, values.bool(true), rex);
		return types2.typeToValue(csvResult.getType(), rex);
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

	private void collectFields(FieldReader reader, final String[] currentRecord, RascalExecutionContext rex) throws IOException {
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
			throw RuntimeExceptionFactory.illegalTypeArgument("Arities of actual type and requested type are different (expected: " + currentRecord.length + ", found: " + recordIndex + ")", null, null);
		}
	}

	private IValue readInferAndBuild(Reader stream, TypeStore store, RascalExecutionContext rex) throws IOException {
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
			collectFields(reader, currentRecord, rex);

			IValue[] tuple = new IValue[currentRecord.length];
			parseRecordFields(currentRecord, expectedTypes, store, tuple, false, rex);
			records.add(tuple);

			// update inferred type
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
				// in case of an completely empty row
				currentTypes[i] = types.stringType();
			}
		}
		Type tupleType = types.tupleType(currentTypes, labels);
		Type resultType = types.setType(tupleType);
		if (this.printInferredType) {
			rex.getStdOut().println("readCSV inferred the relation type: " + resultType);
			rex.getStdOut().flush();
		}
		
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

	private IValue readAndBuild(Reader stream, Type actualType, TypeStore store, RascalExecutionContext rex) throws IOException {
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
			collectFields(reader, currentRecord, rex);
			if (first) {
				first = false;
				continue;
			}
			parseRecordFields(currentRecord, expectedTypes, store, tuple, true, rex);
			if (result instanceof IListWriter) {
				((IListWriter)result).append(values.tuple(tuple));
			}
			else {
				result.insert(values.tuple(tuple));
			}
		}
		return result.done();
	}

	private void parseRecordFields(final String[] fields, final Type[] expectedTypes, TypeStore store, IValue[] result, boolean replaceEmpty, final RascalExecutionContext rex) throws IOException  {
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
						throw RuntimeExceptionFactory.illegalTypeArgument(currentType.toString(),null, null, "Invalid int \"" + field + "\" for requested field " + currentType);
					}
				}
				@Override
				public IValue visitReal(Type type) throws RuntimeException {
					try {
						return values.real(field);
					}
					catch (NumberFormatException nfe) {
						throw RuntimeExceptionFactory.illegalTypeArgument("Invalid real \"" + field + "\" for requested field " + currentType, null, null);
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
					throw RuntimeExceptionFactory.illegalTypeArgument("Invalid field \"" + field + "\" (" + ute.getExpected() + ") for requested field " + ute.getGiven(), null, null);
				}
				catch (FactParseError ex) {
					if (currentType.isTop()) {
						result[i] = values.string(field);
					}
					else {
						throw RuntimeExceptionFactory.illegalTypeArgument("Invalid field \"" + field + "\" is not a " + currentType, null, null);
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
	
	public void writeCSV(IValue rel, ISourceLocation loc, IBool header, IString separator, IString encoding, RascalExecutionContext rex){
		String sep = separator != null ? separator.getValue() : ",";
		Boolean head = header != null ? header.getValue() : true;
		Writer out = null;
		
		//Type paramType = ctx.getCurrentEnvt().getTypeBindings().get(types.parameterType("T"));
		
		Type paramType = rel.getType();
		if(!paramType.isRelation() && !paramType.isListRelation()){
			throw RuntimeExceptionFactory.illegalTypeArgument("A relation type is required instead of " + paramType, null, null);
		}
		
		try{
			boolean isListRel = rel instanceof IList;
			out = new UnicodeOutputStreamWriter(rex.getResolverRegistry().getOutputStream(loc.getURI(), false), encoding.getValue(), false);
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
