package org.rascalmpl.library.lang.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
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
	
	/**
	 * @param options	map with options may contain:
	 * 					- "header" (Possible values "true" or "false", as string!)
	 * 					- "separator" (Possible value a single character string)
	 * 					When map is null, the defaults are reset.
	 */
	private void setOptions(IMap options){
		
		IString separatorKey = values.string("separator");
		IString headerKey = values.string("header");
		
		IString iseparator = null;
		IString iheader = null;
		
		if(options != null){
			iseparator = (IString)options.get(separatorKey);
			iheader = (IString)options.get(headerKey);
		}
			
		separator = (iseparator == null) ? ',' : iseparator.getValue().charAt(0);
		header = (iheader == null) ? true : iheader.toString().equals("true");
	}
	
	/*
	 * Read a CSV file
	 */
	
	public IValue readCSV(IValue result, ISourceLocation loc, IMap options, IEvaluatorContext ctx){
		setOptions(options);
		return read(result, loc, ctx);
	}
	
	public IValue readCSV(IValue result, ISourceLocation loc, IEvaluatorContext ctx){
		setOptions(null);
		return read(result, loc, ctx);
	}
	
	public IValue read(IValue result, ISourceLocation loc, IEvaluatorContext ctx){
		TypeStore store = new TypeStore();
		Type requestedType = tr.valueToType((IConstructor) result, store);

		InputStream in = null;
	
		try{
			in = ctx.getResolverRegistry().getInputStream(loc.getURI());
			FieldReader reader = new FieldReader(in, separator);
			ArrayList<String> labels = new ArrayList<String>();
			ArrayList<Record> records = new ArrayList<Record>();
			int nfields = 0;
			if(header){
				while(reader.hasField()){
					String label = normalizeLabel(reader.getField(), nfields);
					labels.add(label);
					nfields++;
				}
			}
	
			ArrayList<Type> fieldType = new ArrayList<Type>();
			
			while(reader.hasRecord()){
				Record ri = new Record(reader);
				ArrayList<Type> ftypes = ri.getFieldTypes();
				for(int i = 0; i < ftypes.size(); i++){
					if(i < fieldType.size()){
						fieldType.set(i, fieldType.get(i).lub(ftypes.get(i)));
						if(fieldType.get(i).isValueType())
							fieldType.set(i, types.stringType());
					} else {
						fieldType.add(ftypes.get(i));
					}
				}
				records.add(ri);
			}
			nfields = fieldType.size();
			
			/*
			 * Lift inferred field types to string when that was requested
			 */
			if(requestedType.isRelationType()){
				for(int i = 0; i < nfields; i++){
					if(!fieldType.get(i).isSubtypeOf(requestedType.getFieldType(i)) &&
							requestedType.getFieldType(i).isStringType()){
						fieldType.set(i, types.stringType());
					}
				}
			}
			
			Type fieldTypeArray[] = new Type[nfields];
			String labelArray[] = new String[nfields];
			for(int i = 0; i < nfields; i++){
				fieldTypeArray[i] = fieldType.get(i);
				if(header)
					labelArray[i] = labels.get(i);
				else
					labelArray[i] = "field" + i;
			}
			Type tupleType = types.tupleType(fieldTypeArray, labelArray);
			Type relType = types.setType(tupleType);
			
			ctx.getStdOut().println("readCSV inferred the relation type: " + relType);
			ctx.getStdOut().flush();
			
			if(!relType.isSubtypeOf(requestedType)){
				int ra = requestedType.getArity();
				if(ra != nfields)
					throw RuntimeExceptionFactory.illegalTypeArgument("Arities of actual type and requested type are different (" + nfields + " vs " + ra + ")", ctx.getCurrentAST(), ctx.getStackTrace());
				for(int i = 0; i < nfields; i++)
					if(!fieldType.get(i).isSubtypeOf(requestedType.getFieldType(i)))
						throw RuntimeExceptionFactory.illegalTypeArgument("Actual field " + fieldType.get(i) + " " + labels.get(i) +" is not a subtype of requested field " + requestedType.getFieldType(i) + " " + requestedType.getFieldName(i), ctx.getCurrentAST(), ctx.getStackTrace());
			}
			IRelationWriter rw = values.relationWriter(tupleType);
			
			for(Record r : records){
				rw.insert(r.getTuple(fieldTypeArray));
			}
			
			return rw.done();
			
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	/*
	 * Write a CSV file.
	 */
	public void writeCSV(IValue rel, ISourceLocation loc, IMap options, IEvaluatorContext ctx){
		setOptions(options);
		write(rel, loc, ctx);
	}
	
	public void writeCSV(IValue rel, ISourceLocation loc, IEvaluatorContext ctx){
		setOptions(null);
		write(rel, loc, ctx);
	}
	
	public void write(IValue rel, ISourceLocation loc, IEvaluatorContext ctx){

		OutputStream out = null;
		
		if(!rel.getType().isRelationType()){
			throw RuntimeExceptionFactory.illegalTypeArgument("A relation type is required instead of " + rel.getType(),ctx.getCurrentAST(), 
					ctx.getStackTrace());
		}
		
		try{
			out = ctx.getResolverRegistry().getOutputStream(loc.getURI(), false);
			IRelation irel = (IRelation) rel;
			
			int nfields = irel.arity();
			if(header){
				for(int i = 0; i < nfields; i++){
					if(i > 0)
						out.write(separator);
					String label = irel.getType().getFieldName(i);
					if(label == null || label.isEmpty())
						label = "field" + i;
					writeString(out, label);
				}
				out.write('\n');
			}
			
			for(IValue v : irel){
				ITuple tup = (ITuple) v;
				int sep = 0;
				for(IValue w : tup){
					if(sep == 0)
						sep = separator;
					else
						out.write(sep);
					if(w.getType().isStringType()){
						String s = ((IString)w).getValue();
						if(s.contains("\n") || s.contains("\"")){
							s = s.replaceAll("\"", "\"\"");
							out.write('"');
							writeString(out,s);
							out.write('"');
						} else
							writeString(out, s);
					} else {
						writeString(out, w.toString());
					}
				}
				out.write('\n');
			}
			out.flush();
			out.close();
		}
		catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(out != null){
				try{
					out.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	/**
	 * Normalize a label in the header for use in the relation type.
	 * @param label	The string found in the header
	 * @param pos	Position in the header
	 * @return		The label (with non-fieldname characters removed) or "field<pos>" when empty
	 */
	private String normalizeLabel(String label, int pos){
		label = label.replaceAll("[^a-zA-Z0-9]+", "");
		if(label.isEmpty())
			return "field" + pos;
		return label;
	}
	
	/**
	 * Write a string to the output stream.
	 * @param out	The output stream.
	 * @param txt	The string to be written.
	 * @throws IOException
	 */
	private void writeString(OutputStream out, String txt) throws IOException{
		for(char c : txt.toCharArray()){
			out.write((byte)c);
		}
	}
}

/**
 * Auxiliary class to read fields from an input stream.
 *
 */
class FieldReader {
	int lastChar = ';';
	int separator = ';';
	InputStream in;
	boolean startOfLine = true;
	
	FieldReader(InputStream in, int sep) throws IOException{
		this.in = in;
		this.separator = sep;
		startOfLine = true;
		lastChar = in.read();
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
			return lastChar != -1;
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
		while(lastChar == '\n' || lastChar == '\r')
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
			assert lastChar == separator || lastChar == '\n' || lastChar == -1;
			return sw.toString();
		}
		while ((lastChar != -1) && (lastChar != separator) && (lastChar != '\n')){
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
class Record {
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
			if(field.matches("[+-]?[0-9]+")){
				int n = Integer.parseInt(field);
				rfields.add(values.integer(n));
				fieldTypes.add(types.integerType());
				//System.err.println(" int");
			} else
			if(field.matches("[+-]?[0-9]+\\.[0-9]*")){
				double d = Double.parseDouble(field);
				rfields.add(values.real(d));
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
		return types.tupleType(fieldTypes);
	}
	
	/**
	 * @return a list of the types of the fields of this record.
	 */
	ArrayList<Type> getFieldTypes(){
		return fieldTypes;
	}
	
	/**
	 * @param fieldTypes as inferred for the whole relation.
	 * @return The tuple value for this record
	 */
	ITuple getTuple(Type[] fieldTypes){
		IValue  fieldValues[] = new IValue[rfields.size()];
		for(int i = 0; i < rfields.size(); i++){
			if(rfields.get(i) == null){
				if(fieldTypes[i].isBoolType())
					rfields.set(i, values.bool(false));
				else if(fieldTypes[i].isIntegerType())
					rfields.set(i, values.integer(0));
				else if(fieldTypes[i].isRealType())
					rfields.set(i, values.real(0));
				else
					rfields.set(i, values.string(""));
			}
			if(fieldTypes[i].isStringType() && !rfields.get(i).getType().isStringType())
				rfields.set(i, values.string(rfields.get(i).toString()));
			fieldValues[i] = rfields.get(i);
		}
		return values.tuple(fieldValues);
	}
	
}
