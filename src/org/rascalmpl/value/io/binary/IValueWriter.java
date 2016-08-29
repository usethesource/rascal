package org.rascalmpl.value.io.binary;

import java.io.IOException;
import java.io.OutputStream;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.io.binary.util.MapLastWritten;
import org.rascalmpl.value.io.binary.util.PrePostIValueIterator;
import org.rascalmpl.value.io.binary.util.PrePostTypeIterator;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;
import org.rascalmpl.value.io.binary.util.Types;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.visitors.IValueVisitor;
import org.rascalmpl.values.ValueFactoryFactory;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;
        	
/**
 * RSFIValueWriter is a binary serializer for IValues and Types. The main public functions is:
 * - writeValue
 */
	        
public class IValueWriter {
    

    public enum CompressionRate {
        None(0,0,0, 0),
        //TypesOnly(10,0,0),
        //ValuesOnly(0,10,10),
        Fast(10,10,10, 1),
        Normal(50,100,50,3),
        Extreme(50,250,100, 6)
        ;

        private final int uriWindow;
        private final int typeWindow;
        private final int valueWindow;
        private int xzMode;

        CompressionRate(int typeWindow, int valueWindow, int uriWindow, int xzMode) {
            this.typeWindow = typeWindow;
            this.valueWindow = valueWindow;
            this.uriWindow = uriWindow;
            this.xzMode = xzMode;
        } 
    }
    
	protected static final byte[] header = { 'R', 'V', 1,0,0 };
	
	static final class CompressionHeader {
	    public static final byte NONE = 0;
	    public static final byte GZIP = 1;
	    public static final byte XZ = 2;
	}
	
	private static <T> TrackLastWritten<T> getWindow(int size) {
	    if (size == 0) {
	        return new TrackLastWritten<T>() {
	            public int howLongAgo(T obj) {
	                return -1;
	            }
	            public void write(T obj) {};
	        };
	    }
	    return new MapLastWritten<>(size * 1024); 
	}
	
	public static void write(OutputStream out, IValue value, CompressionRate compression, boolean shouldClose) throws IOException {
        out.write(header);
    	out.write(compression.typeWindow);
    	out.write(compression.valueWindow);
    	out.write(compression.uriWindow);
    	out.write(compression.xzMode == 0 ? CompressionHeader.NONE : CompressionHeader.XZ);
    	if (compression.xzMode > 0) {
    	    out = new XZOutputStream(out, new LZMA2Options(compression.xzMode));
    	}
    	ValueWireOutputStream writer =  new ValueWireOutputStream(out);
    	try {
    	    TrackLastWritten<Type> typeCache = getWindow(compression.typeWindow);
    	    TrackLastWritten<IValue> valueCache = getWindow(compression.valueWindow);
    	    TrackLastWritten<ISourceLocation> uriCache = getWindow(compression.uriWindow);
    	    write(writer, value, typeCache, valueCache, uriCache);
    	}
    	finally {
    	    writer.flush();
    	    if (shouldClose) {
    	        writer.close();
    	    }
    	    else {
                if (compression.xzMode > 0) {
                    ((XZOutputStream)out).finish();
                }
    	    }
    	}
	}
	
	private static void writeNames(final ValueWireOutputStream writer, int fieldId, String[] names) throws IOException{
		writer.writeField(fieldId, names.length);
		for(int i = 0; i < names.length; i++){
		    writer.writeField(fieldId, names[i]);
		}
	}
	
    private static final class TypeWriterVisitor
            implements ITypeVisitor<Boolean, IOException> {
        private final TrackLastWritten<IValue> valueCache;
        private final TrackLastWritten<Type> typeCache;
        private final PrePostTypeIterator iter;
        private final ValueWireOutputStream writer;
        private final TrackLastWritten<ISourceLocation> uriCache;

        private TypeWriterVisitor(TrackLastWritten<IValue> valueCache,
                TrackLastWritten<Type> typeCache, PrePostTypeIterator iter,
                ValueWireOutputStream writer, TrackLastWritten<ISourceLocation> uriCache) {
            this.valueCache = valueCache;
            this.typeCache = typeCache;
            this.iter = iter;
            this.writer = writer;
            this.uriCache = uriCache;
        }

        private boolean writeFromCache(Type type) throws IOException {
            int lastSeen = typeCache.howLongAgo(type);
            if (lastSeen != -1) { 
                writeSingleValueMessage(writer, IValueIDs.PreviousType.ID, IValueIDs.PreviousType.HOW_LONG_AGO, lastSeen);
                iter.skipItem();
                return true;
            }
            return false;
        }

        @Override
        public Boolean visitAbstractData(Type type) throws IOException {
            assert IValueKinds.ADT_COMPOUND;
            if (writeFromCache(type) || iter.atBeginning()) {
                return false;
            }
            writeSingleValueMessage(writer, IValueIDs.ADTType.ID, IValueIDs.ADTType.NAME, type.getName());
            return true;
        }

        @Override
        public Boolean visitAlias(Type type) throws IOException {
            assert IValueKinds.ALIAS_COMPOUND;
            if (writeFromCache(type) || iter.atBeginning()) {
                return false;
            }
            writeSingleValueMessage(writer, IValueIDs.AliasType.ID, IValueIDs.AliasType.NAME, type.getName());
            return true;
        }

        @Override
        public Boolean visitConstructor(Type type) throws IOException {
            assert IValueKinds.CONSTRUCTOR_COMPOUND;
            if (writeFromCache(type) || iter.atBeginning()) {
                return false;
            }
            writeSingleValueMessage(writer, IValueIDs.ConstructorType.ID, IValueIDs.ConstructorType.NAME, type.getName());
            return true;
        }

        @Override
        public Boolean visitExternal(Type type) throws IOException {
            assert IValueKinds.EXTERNAL_COMPOUND;
            if (writeFromCache(type) || iter.atBeginning()) {
                return false;
            }
            // TODO this should be here, but on the external type callback 
            if(type instanceof FunctionType){
                writer.writeEmptyMessage(IValueIDs.ConstructorType.ID);
            } else if(type instanceof ReifiedType){
                writer.writeEmptyMessage(IValueIDs.ReifiedType.ID);
            } else if(type instanceof OverloadedFunctionType){
                writeSingleValueMessage(writer, IValueIDs.OverloadedType.ID, IValueIDs.OverloadedType.SIZE, ((OverloadedFunctionType) type).getAlternatives().size());
            } else if(type instanceof NonTerminalType){
                write(writer, ((NonTerminalType)type).getSymbol(), typeCache, valueCache, uriCache);
                writer.writeEmptyMessage(IValueIDs.NonTerminalType.ID);
            } else {
                throw new RuntimeException("External type not supported: " + type);
            }
            return true;
        }

        @Override
        public Boolean visitList(Type type) throws IOException {
            assert IValueKinds.LIST_COMPOUND;
            if (writeFromCache(type) || iter.atBeginning()) {
                return false;
            }
            writer.writeEmptyMessage(IValueIDs.ListType.ID);
            return true;
        }

        @Override
        public Boolean visitMap(Type type) throws IOException {
            assert IValueKinds.MAP_COMPOUND;
            if (writeFromCache(type) || iter.atBeginning()) {
                return false;
            }
            writer.writeEmptyMessage(IValueIDs.MapType.ID);
            return true;
        }

        @Override
        public Boolean visitParameter(Type type) throws IOException {
            assert IValueKinds.PARAMETER_COMPOUND;
            if (writeFromCache(type) || iter.atBeginning()) {
                return false;
            }
            writeSingleValueMessage(writer, IValueIDs.ParameterType.ID, IValueIDs.ParameterType.NAME,type.getName());
            return true;
        }

        @Override
        public Boolean visitSet(Type type) throws IOException {
            assert IValueKinds.SET_COMPOUND;
            if (writeFromCache(type) || iter.atBeginning()) {
                return false;
            }
            writer.writeEmptyMessage(IValueIDs.SetType.ID);
            return true;
        }

        @Override
        public Boolean visitTuple(Type type) throws IOException {
            assert IValueKinds.TUPLE_COMPOUND;
            if (writeFromCache(type) || iter.atBeginning()) {
                return false;
            }
            writer.startMessage(IValueIDs.TupleType.ID);
            writer.writeField(IValueIDs.TupleType.ARITY, type.getArity());
            String[] fieldNames = type.getFieldNames();
            if(fieldNames != null){
                writeNames(writer, IValueIDs.TupleType.NAMES, fieldNames);
            }
            writer.endMessage();
            return true;
        }

        @Override
        public Boolean visitBool(Type t) throws IOException {
            assert !IValueKinds.BOOLEAN_COMPOUND;
            writer.writeEmptyMessage(IValueIDs.BoolType.ID);
            return false;
        }

        @Override
        public Boolean visitDateTime(Type t) throws IOException {
            assert !IValueKinds.DATETIME_COMPOUND;
            writer.writeEmptyMessage(IValueIDs.DateTimeType.ID);
            return false;
        }

        @Override
        public Boolean visitInteger(Type t) throws IOException {
            assert !IValueKinds.INTEGER_COMPOUND;
            writer.writeEmptyMessage(IValueIDs.IntegerType.ID);
            return false;
        }

        @Override
        public Boolean visitNode(Type t) throws IOException {
            assert IValueKinds.NODE_COMPOUND; // node types have no nested types
            writer.writeEmptyMessage(IValueIDs.NodeType.ID);
            return false;
        }

        @Override
        public Boolean visitNumber(Type t) throws IOException {
            assert !IValueKinds.NUMBER_COMPOUND;
            writer.writeEmptyMessage(IValueIDs.NumberType.ID);
            return false;
        }

        @Override
        public Boolean visitRational(Type t) throws IOException {
            assert IValueKinds.RATIONAL_COMPOUND; // rational has no children
            writer.writeEmptyMessage(IValueIDs.RationalType.ID);
            return false;
        }

        @Override
        public Boolean visitReal(Type t) throws IOException {
            assert !IValueKinds.REAL_COMPOUND;
            writer.writeEmptyMessage(IValueIDs.RealType.ID);
            return false;
        }

        @Override
        public Boolean visitSourceLocation(Type t) throws IOException {
            assert !IValueKinds.SOURCELOCATION_COMPOUND;
            writer.writeEmptyMessage(IValueIDs.SourceLocationType.ID);
            return false;
        }

        @Override
        public Boolean visitString(Type t) throws IOException {
            assert !IValueKinds.STRING_COMPOUND;
            writer.writeEmptyMessage(IValueIDs.StringType.ID);
            return false;
        }

        @Override
        public Boolean visitValue(Type t) throws IOException {
            assert !IValueKinds.VALUE_COMPOUND;
            writer.writeEmptyMessage(IValueIDs.ValueType.ID);
            return false;
        }

        @Override
        public Boolean visitVoid(Type t) throws IOException {
            assert !IValueKinds.VOID_COMPOUND;
            writer.writeEmptyMessage(IValueIDs.VoidType.ID);
            return false;
        }
    }
	private static void write(final ValueWireOutputStream writer, final Type type, final TrackLastWritten<Type> typeCache, final TrackLastWritten<IValue> valueCache, final TrackLastWritten<ISourceLocation> uriCache) throws IOException {
	    final PrePostTypeIterator iter = new PrePostTypeIterator(type);
	    
	    ITypeVisitor<Boolean, IOException> typeWriter = new TypeWriterVisitor(valueCache, typeCache, iter, writer, uriCache);
	    while(iter.hasNext()){
	        iter.next();
	        final Type currentType = iter.getItem();
	        if (currentType.accept(typeWriter)) {
                typeCache.write(currentType);
	        }
	    }
	}
	
	private static void writeSingleValueMessage(final ValueWireOutputStream writer, int messageID, int fieldId, long fieldValue) throws IOException {
	    writer.startMessage(messageID);
	    writer.writeField(fieldId, fieldValue);
	    writer.endMessage();
	}
	
	private static void writeSingleValueMessage(final ValueWireOutputStream writer, int messageID, int fieldId, String fieldValue) throws IOException {
	    writer.startMessage(messageID);
	    writer.writeField(fieldId, fieldValue);
	    writer.endMessage();
	}

	private static final IInteger MININT =ValueFactoryFactory.getValueFactory().integer(Integer.MIN_VALUE);
	private static final IInteger MAXINT =ValueFactoryFactory.getValueFactory().integer(Integer.MAX_VALUE);
	
	private static void write(final ValueWireOutputStream writer, final IValue value, final TrackLastWritten<Type> typeCache, final TrackLastWritten<IValue> valueCache, final TrackLastWritten<ISourceLocation> uriCache) throws IOException {
		PrePostIValueIterator iter = new PrePostIValueIterator(value);
		
		// returns if the value should be put into the cache or not
		IValueVisitor<Boolean, IOException> visitWriter = new IValueVisitor<Boolean, IOException>() {

		    private boolean writeFromCache(IValue val) throws IOException {
		        int lastSeen = valueCache.howLongAgo(val);
		        if (lastSeen != -1) {
		            writeSingleValueMessage(writer, IValueIDs.PreviousValue.ID, IValueIDs.PreviousValue.HOW_FAR_BACK, lastSeen);
		            iter.skipItem();
		            return true;
		        }
		        return false;
		    }
		    @Override
		    public Boolean visitConstructor(IConstructor cons) throws IOException {
		        assert IValueKinds.CONSTRUCTOR_COMPOUND;
		        if (writeFromCache(cons) || iter.atBeginning()) {
		            return false;
		        }
		        write(writer, cons.getUninstantiatedConstructorType(), typeCache, valueCache, uriCache);

		        writer.startMessage(IValueIDs.ConstructorValue.ID);
		        writer.writeField(IValueIDs.ConstructorValue.ARITY, cons.arity());
		        if(cons.mayHaveKeywordParameters()){
		            if(cons.asWithKeywordParameters().hasParameters()){
		                writer.writeField(IValueIDs.ConstructorValue.KWPARAMS, cons.asWithKeywordParameters().getParameters().size());
		            }
		        } else {
		            if(cons.asAnnotatable().hasAnnotations()){
		                writer.writeField(IValueIDs.ConstructorValue.ANNOS, cons.asAnnotatable().getAnnotations().size());
		            }
		        }
		        writer.endMessage();
		        return true;
		    }
		    @Override
		    public Boolean visitNode(INode node) throws IOException {
		        assert IValueKinds.NODE_COMPOUND;
		        if (writeFromCache(node) || iter.atBeginning()) {
		            return false;
		        }
		        writer.startMessage(IValueIDs.NodeValue.ID);
		        writer.writeField(IValueIDs.NodeValue.NAME,  node.getName());
		        writer.writeField(IValueIDs.NodeValue.ARITY, node.arity());
		        if(node.mayHaveKeywordParameters()){
		            if(node.asWithKeywordParameters().hasParameters()){
		                writer.writeField(IValueIDs.NodeValue.KWPARAMS, node.asWithKeywordParameters().getParameters().size());
		            }
		        } else {
		            if(node.asAnnotatable().hasAnnotations()){
		                writer.writeField(IValueIDs.NodeValue.ANNOS, node.asAnnotatable().getAnnotations().size());
		            }
		        }
		        writer.endMessage();
		        return true;
		    }
		    @Override
		    public Boolean visitList(IList o) throws IOException {
		        assert IValueKinds.LIST_COMPOUND;
		        if (writeFromCache(o) || iter.atBeginning()) {
		            return false;
		        }
		        writeSingleValueMessage(writer, IValueIDs.ListValue.ID, IValueIDs.ListValue.SIZE, o.length());
		        return true;
		    }
		    @Override
		    public Boolean visitMap(IMap o) throws IOException {
		        assert IValueKinds.MAP_COMPOUND;
		        if (writeFromCache(o) || iter.atBeginning()) {
		            return false;
		        }
		        writeSingleValueMessage(writer, IValueIDs.MapValue.ID, IValueIDs.MapValue.SIZE, o.size());
		        return true;
		    }
		    @Override
		    public Boolean visitSet(ISet o) throws IOException {
		        assert IValueKinds.SET_COMPOUND;
		        if (writeFromCache(o) || iter.atBeginning()) {
		            return false;
		        }
		        writeSingleValueMessage(writer, IValueIDs.SetValue.ID, IValueIDs.SetValue.SIZE, o.size());
		        return true;
		    }
		    @Override
		    public Boolean visitRational(IRational o) throws IOException {
		        assert IValueKinds.RATIONAL_COMPOUND;
		        if (writeFromCache(o) || iter.atBeginning()) {
		            return false;
		        }
		        writer.writeEmptyMessage(IValueIDs.RationalValue.ID);
		        return true;
		    }
		    @Override
		    public Boolean visitTuple(ITuple o) throws IOException {
		        assert IValueKinds.TUPLE_COMPOUND;
		        if (writeFromCache(o) || iter.atBeginning()) {
		            return false;
		        }
			     writeSingleValueMessage(writer, IValueIDs.TupleValue.ID, IValueIDs.TupleValue.SIZE, o.arity());
			     return true;
		    }

		    @Override
		    public Boolean visitBoolean(IBool boolValue) throws IOException {
		        assert !IValueKinds.BOOLEAN_COMPOUND;
		        writeSingleValueMessage(writer, IValueIDs.BoolValue.ID, IValueIDs.BoolValue.VALUE, boolValue.getValue() ? 1: 0);
		        return false;
		    }

		    @Override
		    public Boolean visitDateTime(IDateTime dateTime) throws IOException {
		        assert !IValueKinds.DATETIME_COMPOUND;
		        writer.startMessage(IValueIDs.DateTimeValue.ID);

		        if (!dateTime.isTime()) {
		            writer.writeField(IValueIDs.DateTimeValue.YEAR, dateTime.getYear());
		            writer.writeField(IValueIDs.DateTimeValue.MONTH, dateTime.getMonthOfYear());
		            writer.writeField(IValueIDs.DateTimeValue.DAY, dateTime.getDayOfMonth());
		        }

		        if (!dateTime.isDate()) {
		            writer.writeField(IValueIDs.DateTimeValue.HOUR, dateTime.getHourOfDay());
		            writer.writeField(IValueIDs.DateTimeValue.MINUTE, dateTime.getMinuteOfHour());
		            writer.writeField(IValueIDs.DateTimeValue.SECOND, dateTime.getSecondOfMinute());
		            writer.writeField(IValueIDs.DateTimeValue.MILLISECOND, dateTime.getMillisecondsOfSecond());

		            writer.writeField(IValueIDs.DateTimeValue.TZ_HOUR, dateTime.getTimezoneOffsetHours());
		            writer.writeField(IValueIDs.DateTimeValue.TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
		        }
		        writer.endMessage();
		        return false;
		    }
		    @Override
		    public Boolean visitInteger(IInteger ii) throws IOException {
		        assert !IValueKinds.INTEGER_COMPOUND;
		        writer.startMessage(IValueIDs.IntegerValue.ID);
		        if(ii. greaterEqual(MININT).getValue() && ii.lessEqual(MAXINT).getValue()){
		            writer.writeField(IValueIDs.IntegerValue.INTVALUE, ii.intValue());
		        } 
		        else {
		            writer.writeField(IValueIDs.IntegerValue.BIGVALUE, ii.getTwosComplementRepresentation());
		        }
		        writer.endMessage();
		        return false;
		    }


		    @Override
		    public Boolean visitReal(IReal o) throws IOException {
		        assert !IValueKinds.REAL_COMPOUND;
		        writer.startMessage(IValueIDs.RealValue.ID);
		        writer.writeField(IValueIDs.RealValue.CONTENT, o.unscaled().getTwosComplementRepresentation());
		        writer.writeField(IValueIDs.RealValue.SCALE, o.scale());
		        writer.endMessage();
		        return false;
		    }

		    @Override
		    public Boolean visitSourceLocation(ISourceLocation loc) throws IOException {
		        assert !IValueKinds.SOURCELOCATION_COMPOUND;
		        writer.startMessage(IValueIDs.SourceLocationValue.ID);
		        ISourceLocation uriPart = loc.top();
		        int alreadyWritten = uriCache.howLongAgo(uriPart);
		        if (alreadyWritten == -1) {
		            writer.writeField(IValueIDs.SourceLocationValue.SCHEME, uriPart.getScheme());
		            if (uriPart.hasAuthority()) {
		                writer.writeField(IValueIDs.SourceLocationValue.AUTHORITY, uriPart.getAuthority());
		            }
		            if (uriPart.hasPath()) {
		                writer.writeField(IValueIDs.SourceLocationValue.PATH, uriPart.getPath());
		            }
		            if (uriPart.hasQuery()) {
		                writer.writeField(IValueIDs.SourceLocationValue.QUERY,  uriPart.getQuery());
		            }
		            if (uriPart.hasFragment()) {
		                writer.writeField(IValueIDs.SourceLocationValue.FRAGMENT,  uriPart.getFragment());
		            }
		            uriCache.write(uriPart);
		        }
		        else {
		            writer.writeField(IValueIDs.SourceLocationValue.PREVIOUS_URI, alreadyWritten);
		        }

		        if(loc.hasOffsetLength()){
		            writer.writeField(IValueIDs.SourceLocationValue.OFFSET, loc.getOffset());
		            writer.writeField(IValueIDs.SourceLocationValue.LENGTH, loc.getLength());
		        } 
		        if(loc.hasLineColumn()){
		            writer.writeField(IValueIDs.SourceLocationValue.BEGINLINE, loc.getBeginLine());
		            writer.writeField(IValueIDs.SourceLocationValue.ENDLINE, loc.getEndLine());
		            writer.writeField(IValueIDs.SourceLocationValue.BEGINCOLUMN, loc.getBeginColumn());
		            writer.writeField(IValueIDs.SourceLocationValue.ENDCOLUMN, loc.getEndColumn());
		        }
		        writer.endMessage();
		        return false;
		    }

		    @Override
		    public Boolean visitString(IString o) throws IOException {
		        assert !IValueKinds.SOURCELOCATION_COMPOUND;
		        writeSingleValueMessage(writer, IValueIDs.StringValue.ID, IValueIDs.StringValue.CONTENT, o.getValue());
		        return false;
		    }
		    @Override
		    public Boolean visitExternal(IExternalValue externalValue) throws IOException {
		        throw new RuntimeException("Not supported yet");
		    }
		    @Override
		    public Boolean visitListRelation(IList o) throws IOException {
		        return visitList(o);
		    }
		    @Override
		    public Boolean visitRelation(ISet o) throws IOException {
		        return visitSet(o);
		    }
		};

		while(iter.hasNext()){
		    iter.next();
		    if (iter.getItem().accept(visitWriter)) {
			        valueCache.write(iter.getItem());
		    }
		}
	}
}
