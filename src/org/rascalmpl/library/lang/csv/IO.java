package org.rascalmpl.library.lang.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import org.eclipse.imp.pdb.facts.IConstructor;
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
	private String separator;

	private TypeReifier tr;
	
	public IO(IValueFactory values){
		super();
		
		this.values = values;
		this.tr = new TypeReifier(values);
		separator = ";";
	}
	
	public IValue readCSV(IValue result, ISourceLocation loc, IString sep, IEvaluatorContext ctx){
		separator = sep.getValue();
		return readCSV(result, loc, ctx);
	}
	
	public IValue readCSV(IValue result, ISourceLocation loc, IEvaluatorContext ctx){
		TypeStore store = new TypeStore();
		Type requestedType = tr.valueToType((IConstructor) result, store);

		InputStream in = null;
		System.err.println("requestedType = " + requestedType + ": " + requestedType.isRelationType());
		if(!requestedType.isRelationType()){
			throw RuntimeExceptionFactory.illegalTypeArgument("A relation type is required instead of " + requestedType.toString(), ctx.getCurrentAST(), ctx.getStackTrace(), "XXX");
		}
	
		try{
			in = ctx.getResolverRegistry().getInputStream(loc.getURI());
			String header = readLine(in);
			java.lang.String[] labels = header.split(separator);
			int nfields = labels.length;
			Type fieldType[] = new Type[nfields];
			
			for(int i = 0; i < nfields; i++){
				fieldType[i] = types.voidType();
				labels[i] = normalizeLabel(labels[i], i);
			}
			
			ArrayList<Record> records = new ArrayList<Record>();
			String line = readLine(in);
			while(line != null){
				Record ri = new Record(nfields, separator, line);
				Type ftypes[] = ri.getFieldTypes();
				for(int i = 0; i < nfields; i++){
					fieldType[i] = fieldType[i].lub(ftypes[i]);
					if(fieldType[i].isValueType())
						fieldType[i] = types.stringType();
				}
				records.add(ri);
				line = readLine(in);
			}
			Type tupleType = types.tupleType(fieldType, labels);
			Type relType = types.relType(tupleType);
			
			if(!relType.isSubtypeOf(requestedType)){
				int ra = requestedType.getArity();
				if(ra != nfields)
					throw RuntimeExceptionFactory.illegalTypeArgument("Arities of actual type and requested type are different (" + nfields + " vs " + ra + ")", ctx.getCurrentAST(), ctx.getStackTrace());
				for(int i = 0; i < nfields; i++)
					if(!fieldType[i].isSubtypeOf(requestedType.getFieldType(i)))
						throw RuntimeExceptionFactory.illegalTypeArgument("Actual field " + fieldType[i] + " " + labels[i] +" is not a subtype of requested field " + requestedType.getFieldType(i) + " " + requestedType.getFieldName(i), ctx.getCurrentAST(), ctx.getStackTrace());
			}
			System.err.println("tupleType = " + tupleType);
			//IRelationWriter rw = values.relationWriter(tupleType);
			IRelationWriter rw = values.relationWriter(tupleType);
			
			for(Record r : records){
				ITuple tp = r.getTuple(fieldType);
				rw.insert(tp);
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
	
	private String normalizeLabel(String label, int pos){
		label = label.replaceAll("[^a-zA-Z0-9]+", "");
		if(label.isEmpty())
			return "field" + pos;
		return label;
	}
	
	private String readLine(InputStream in) throws IOException{
		StringWriter sw = new StringWriter();
		
		int lastChar = in.read();
		
		while (lastChar != -1 && lastChar != '\n'){
			sw.append((char)lastChar);
			lastChar = in.read();
		}
		if(lastChar == -1)
			return null;
		return sw.toString();
	}
}

class Record {
	private static final TypeFactory types = TypeFactory.getInstance();
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	IValue rfields [];
	Type fieldTypes[];
	
	Record(int nfields, String separator, String line){
		rfields = new IValue[nfields];
		fieldTypes = new Type[nfields];
		for(int i = 0; i < nfields; i++)
			fieldTypes[i] = types.voidType();
		
		Scanner scan = new Scanner(line);
		scan.useDelimiter(separator);
		
		int i = 0;
		do {
			if(scan.hasNextInt()){
				rfields[i] = values.integer(scan.nextInt());
				fieldTypes[i] = types.integerType();
			} else
			if(scan.hasNextFloat()){
				rfields[i] = values.real(scan.nextFloat());
				fieldTypes[i] = types.realType();
			} else
			if(scan.hasNextBoolean()){
				rfields[i] = values.bool(scan.nextBoolean());
				fieldTypes[i] = types.boolType();
			} else {
				rfields[i] = values.string(scan.next());
				fieldTypes[i] = types.stringType();
			}
			if(i == 3 && !fieldTypes[i].isIntegerType()) System.err.println(line + ": i = " + i + ", type = " + fieldTypes[i] + ", value = " + rfields[i]);
			i++;
		} while(i < nfields && scan.hasNext());
	}
	
	Type getType(){
		return types.tupleType(fieldTypes);
	}
	
	Type[] getFieldTypes(){
		return fieldTypes;
	}
	
	ITuple getTuple(Type[] fieldTypes){
		for(int i = 0; i < rfields.length; i++){
			if(rfields[i] == null){
				if(fieldTypes[i].isBoolType())
					rfields[i] = values.bool(false);
				else if(fieldTypes[i].isIntegerType())
					rfields[i] = values.integer(0);
				else if(fieldTypes[i].isRealType())
					rfields[i] = values.real(0);
				else
					rfields[i] = values.string("");
			}
			if(fieldTypes[i].isStringType() && !rfields[i].getType().isStringType())
				rfields[i] = values.string(rfields[i].toString());
		}
		return values.tuple(rfields);
	}
	
}
