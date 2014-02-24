package org.rascalmpl.library.lang.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.unicode.UnicodeOutputStreamWriter;
import org.rascalmpl.values.ValueFactoryFactory;

public class IO {
	private static final TypeFactory types = TypeFactory.getInstance();
	
	private final IValueFactory values;
	private int separator;  	// The separator to be used between fields
	private boolean header;		// Does the file start with a line defining field names?

	private TypeReifier tr;
	
	public IO(IValueFactory values){
		super();
		
		this.values = values;
		this.tr = new TypeReifier(values);
		separator = ',';
		header = true;
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
		return read(tr.valueToType((IConstructor) result, new TypeStore()), loc, header, separator, ctx);
	}


	/*
	 * Calculate the type of a CSV file, returned as the string 
	 */
	public IValue getCSVType(ISourceLocation loc, IBool header, IString separator, IEvaluatorContext ctx){
		return computeType(loc, header, separator, ctx);
	}
	
	//////
	
	private IValue read(Type resultType, ISourceLocation loc, IBool header, IString separator, IEvaluatorContext ctx) {
		setOptions(header, separator);
		Reader reader = null;
		try {
			reader = ctx.getResolverRegistry().getCharacterReader(loc.getURI(), "UTF8");
			List<Record> records = loadRecords(reader);
			if (resultType == null) {
				resultType = inferType(records, ctx);
				ctx.getStdOut().println("readCSV inferred the relation type: " + resultType);
				ctx.getStdOut().flush();
			}
			else if (this.header) {
				records.remove(0);
			}
			return buildCollection(resultType, records, ctx);
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

	private List<Record> loadRecords(Reader stream) throws IOException {
		FieldReader reader = new FieldReader(stream, separator);
		List<Record> records = new ArrayList<Record>();
		while (reader.hasRecord()) {
			// TODO: Record should not read from reader.
			records.add(new Record(reader));
		}
		return records;
	}
	

	private Type inferType(List<Record> records, IEvaluatorContext ctx) throws IOException {
		String[] labels = null;
		if (header) {
			labels = extractLabels(records.remove(0)); 
		}
		Type[] fieldTypes = null;
		for (Record ri: records) {
			List<Type> ftypes = ri.getFieldTypes();
			if (fieldTypes == null) {
				fieldTypes = new Type[ftypes.size()];
				Arrays.fill(fieldTypes, types.voidType());
			}
			else if (ftypes.size() != fieldTypes.length) {
				// We assume all records in the CSV file to have the same arity.
				throw RuntimeExceptionFactory.illegalArgument(
						//"Inconsistent tuple in CSV expected " + fieldTypes.length + " but was " + ftypes.size(),
						ctx.getCurrentAST(), ctx.getStackTrace());
				
			}
			for (int i = 0; i < ftypes.size(); i++){
				fieldTypes[i] = fieldTypes[i].lub(ftypes.get(i));
				if(fieldTypes[i].isTop()) {
					fieldTypes[i] = types.stringType();
				}
			}
		}
		if (labels == null) {
			labels = makeUpLabels(fieldTypes.length);
		}
		return types.setType(types.tupleType(fieldTypes, labels));
	}

	
	private String[] extractLabels(Record record)  {
		String[] labels = new String[record.getWidth()];
		int i = 0;
		for (String label: record) {
			label = normalizeLabel(label, i);
			labels[i] = label;
			i++;
		}
		return labels;
	}
	
	private String[] makeUpLabels(int n) {
		String labels[] = new String[n];
		for (int i = 0; i < n; i++) {
			labels[i] = "field" + i;
		}
		return labels;
	}

	
	public IValue buildCollection(Type type, List<Record> records, IEvaluatorContext ctx) {
		IWriter writer;
		while (type.isAliased()) {
			type = type.getAliased();
		}
		
		if (type.isList()) {
			writer = values.listWriter(type.getElementType());
		}
		else if (type.isRelation()) {
			writer = values.relationWriter(type.getElementType());
		}
		else {
			throw RuntimeExceptionFactory.illegalTypeArgument(
					"Invalid result type for CSV reading: " + type, 
					ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		// reverse traversal so that the order in case of ListWriter is correct
		// (IWriter only supports inserts at the front).
		Type eltType = type.getElementType();
		for (int i = records.size() - 1; i >= 0; i--) {
			Record record = records.get(i);
			checkRecord(eltType, record, ctx);
			writer.insert(record.getTuple(eltType));
		}
		return writer.done();
	}

	private void checkRecord(Type eltType, Record record, IEvaluatorContext ctx) {
		// TODO: not all inconsistencies are detected yet
		// probably because absent values get type void
		// but are nevertheless initialized eventually
		// to 0, false, or "".
		if (record.getType().isSubtypeOf(eltType)) {
			return;
		}
		if (eltType.isTuple()) {
			int expectedArity = eltType.getArity();
			int actualArity = record.getType().getArity();
			if (expectedArity == actualArity) {
				return;
			}
			throw RuntimeExceptionFactory.illegalTypeArgument(
					"Arities of actual type and requested type are different (" + actualArity + " vs " + expectedArity + ")", 
					ctx.getCurrentAST(), ctx.getStackTrace());
		}
		throw RuntimeExceptionFactory.illegalTypeArgument(
				"Invalid tuple " + record + " for requested field " + eltType, 
				ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	
	/*
	 * Write a CSV file.
	 */
	public void writeCSV(IValue rel, ISourceLocation loc, IBool header, IString separator, IEvaluatorContext ctx){
		setOptions(header, separator);
		write(rel, loc, ctx);
	}
	
	public void write(IValue rel, ISourceLocation loc, IEvaluatorContext ctx){

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
			
			String separatorAsString = new String(Character.toChars(separator));

			int nfields = isListRel ? lrel.asRelation().arity() : irel.asRelation().arity();
			if(header){
				for(int i = 0; i < nfields; i++){
					if(i > 0)
						out.write(separatorAsString);
					String label = paramType.getFieldName(i);
					if(label == null || label.isEmpty())
						label = "field" + i;
					out.write(label);
				}
				out.write('\n');
			}
			for(IValue v : (isListRel ? lrel : irel)){
				ITuple tup = (ITuple) v;
				boolean firstTime = true;
				for(IValue w : tup){
					if(firstTime)
						firstTime = false;
					else
						out.write(separatorAsString);
					if(w.getType().isString()){
						String s = ((IString)w).getValue();
						if(s.contains(separatorAsString) || s.contains("\n") || s.contains("\r") || s.contains("\"")){
							s = s.replaceAll("\"", "\"\"");
							out.write('"');
							out.write(s);
							out.write('"');
						} else
							out.write(s);
					} else {
						out.write(w.toString());
					}
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

/**
 * Auxiliary class to read and represent records in the file.
 *
 */
class Record implements Iterable<String> {
	private static final TypeFactory types = TypeFactory.getInstance();
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	ArrayList<IValue> rfields = new ArrayList<IValue>();
	ArrayList<Type> fieldTypes = new ArrayList<Type>();
	
	/**
	 * Create a record by reader all its fields using reader
	 * @param reader to be used.
	 * @throws IOException
	 */
	Record(FieldReader reader) throws IOException{
		
		while(reader.hasField()) {
			String field = reader.getField();
			//System.err.print("field = " + field);
			
			if(field.isEmpty()){
				rfields.add(null);
				fieldTypes.add(types.voidType());
				//System.err.println(" void");
			} else
			if(field.matches("^[+-]?[0-9]+$")){
				rfields.add(values.integer(field));
				fieldTypes.add(types.integerType());
				//System.err.println(" int");
			} else
			if(field.matches("[+-]?[0-9]+\\.[0-9]*")){
				rfields.add(values.real(field));
				fieldTypes.add(types.realType());
				//System.err.println(" real");
			} else
			if(field.equals("true") || field.equals("false")){
				rfields.add(values.bool(field.equals("true")));
				fieldTypes.add(types.boolType());
				//System.err.println(" bool");
			} else {
				rfields.add(values.string(field));
				fieldTypes.add(types.stringType());
				//System.err.println(" str");
			}
		}
	}
	
	
	
	/**
	 * @return the type of this record.
	 */
	Type getType(){
		Type[] typeArray = new Type[rfields.size()];
		for (int i = 0; i < rfields.size(); i++) {
			if (rfields.get(i) == null) {
				typeArray[i] = types.voidType();
			}
			else {
				typeArray[i] = rfields.get(i).getType();
			}
		}
		return types.tupleType(typeArray);
	}
	
	/**
	 * @return a list of the types of the fields of this record.
	 */
	ArrayList<Type> getFieldTypes(){
		return fieldTypes;
	}
	
	int getWidth() {
		return fieldTypes.size();
	}
	
	/**
	 * @param fieldTypes as inferred for the whole relation.
	 * @return The tuple value for this record
	 */
	ITuple getTuple(Type fieldTypes){
		IValue  fieldValues[] = new IValue[rfields.size()];
		for(int i = 0; i < rfields.size(); i++){
			if(rfields.get(i) == null){
				if(fieldTypes.getFieldType(i).isBool())
					rfields.set(i, values.bool(false));
				else if(fieldTypes.getFieldType(i).isInteger())
					rfields.set(i, values.integer(0));
				else if(fieldTypes.getFieldType(i).isReal())
					rfields.set(i, values.real(0));
				else
					rfields.set(i, values.string(""));
			}
			if(fieldTypes.getFieldType(i).isString() && !rfields.get(i).getType().isString())
				rfields.set(i, values.string(rfields.get(i).toString()));
			fieldValues[i] = rfields.get(i);
		}
		return values.tuple(fieldValues);
	}



	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {
			private final Iterator<IValue> iter = rfields.iterator();
			
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public String next() {
				IValue x = iter.next();
				if (x == null) {
					return "";
				}
				return x + "";
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}
	
}
