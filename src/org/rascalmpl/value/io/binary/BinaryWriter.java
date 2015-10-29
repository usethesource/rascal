/*******************************************************************************
* Copyright (c) 2009-2015 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*    Anya Helene Bagge - labeled map types
*    Jurgen Vinju - externa; types
*******************************************************************************/
package org.rascalmpl.value.io.binary;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
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
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.type.ExternalType;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.util.IndexedSet;
import org.rascalmpl.value.visitors.IValueVisitor;

// TODO Change this thing so it doesn't use recursion.
/**
 * @author Arnold Lankamp
 */
public class BinaryWriter{
	public static final class IdentityValue implements IValue {
	  private final IValue wrapped;
    public IdentityValue(IValue toWrap) {
	    wrapped = toWrap;
    }

    @Override
    public int hashCode() {
      return System.identityHashCode(wrapped);
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof IdentityValue && wrapped == ((IdentityValue)obj).wrapped;
    }

    @Override
    public Type getType() {
      throw new UnsupportedOperationException();
    }
    @Override
    public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean isEqual(IValue other) {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean isAnnotatable() {
      throw new UnsupportedOperationException();
    }
    @Override
    public IAnnotatable<? extends IValue> asAnnotatable() {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean mayHaveKeywordParameters() {
      throw new UnsupportedOperationException();
    }
    @Override
    public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
      throw new UnsupportedOperationException();
    }

  }

  /*package*/ static final String CharEncoding = "UTF8";
	private final static int BOOL_HEADER = 0x01;
//	private final static int INTEGER_HEADER = 0x02;
	private final static int BIG_INTEGER_HEADER = 0x03; // Special case of INTEGER_HEADER (flags for alternate encoding).
	private final static int DOUBLE_HEADER = 0x04;
	private final static int STRING_HEADER = 0x05;
	private final static int SOURCE_LOCATION_HEADER = 0x06;
	private final static int DATE_TIME_HEADER = 0x10;
	private final static int TUPLE_HEADER = 0x07;
	private final static int NODE_HEADER = 0x08;
	private final static int ANNOTATED_NODE_HEADER = 0x09;
	private final static int CONSTRUCTOR_HEADER = 0x0a;
	private final static int ANNOTATED_CONSTRUCTOR_HEADER = 0x0b;
	private final static int LIST_HEADER = 0x0c;
	private final static int SET_HEADER = 0x0d;
	private final static int MAP_HEADER = 0x0f;
	private final static int RATIONAL_HEADER = 0x11;
	private final static int KEYWORDED_NODE_HEADER = 0x12;
	private final static int KEYWORDED_CONSTRUCTOR_HEADER = 0x13;


	private final static int VALUE_TYPE_HEADER = 0x01;
	private final static int VOID_TYPE_HEADER = 0x02;
	private final static int BOOL_TYPE_HEADER = 0x03;
	private final static int INTEGER_TYPE_HEADER = 0x04;
	private final static int DOUBLE_TYPE_HEADER = 0x05;
	private final static int STRING_TYPE_HEADER = 0x06;
	private final static int SOURCE_LOCATION_TYPE_HEADER = 0x07;
	private final static int DATE_TIME_TYPE_HEADER = 0x14;
	private final static int NODE_TYPE_HEADER = 0x08;
	private final static int TUPLE_TYPE_HEADER = 0x09;
	private final static int LIST_TYPE_HEADER = 0x0a;
	private final static int SET_TYPE_HEADER = 0x0b;
	private final static int MAP_TYPE_HEADER = 0x0d;
	private final static int PARAMETER_TYPE_HEADER = 0x0e;
	private final static int ADT_TYPE_HEADER = 0x0f;
	private final static int CONSTRUCTOR_TYPE_HEADER = 0x10;
	private final static int ALIAS_TYPE_HEADER = 0x11;
	private final static int ANNOTATED_NODE_TYPE_HEADER = 0x12;
	private final static int ANNOTATED_CONSTRUCTOR_TYPE_HEADER = 0x13;
	private final static int RATIONAL_TYPE_HEADER = 0x15;
	private final static int NUM_TYPE_HEADER = 0x16;
	
	private final static int SHARED_FLAG = 0x80;
	private final static int TYPE_SHARED_FLAG = 0x40;
	private final static int URL_SHARED_FLAG = 0x20;
	private final static int NAME_SHARED_FLAG = 0x20;
	
	private final static int HAS_FIELD_NAMES = 0x20;
	
	private final static int DATE_TIME_INDICATOR = 0x01;
	private final static int DATE_INDICATOR = 0x02;
	private final static int TIME_INDICATOR = 0x03;
	
	private final IndexedSet<IValue> sharedValues;
	private final IndexedSet<Type> sharedTypes;
	private final IndexedSet<String> sharedPaths;
	private final IndexedSet<String> sharedNames;
	
	private final IValue value;
	private final OutputStream out;
	private final TypeStore typeStore;
	private final boolean maximalSharing;
	
	public BinaryWriter(IValue value, OutputStream outputStream, TypeStore typeStore){
		this(value, outputStream, true, typeStore);
	}
	public BinaryWriter(IValue value, OutputStream outputStream, boolean maximalSharing, TypeStore typeStore){
		super();
		
		this.value = value;
		this.out = outputStream;
		this.typeStore = typeStore;
		this.maximalSharing = maximalSharing;
		
		sharedValues = new IndexedSet<>();
		sharedTypes = new IndexedSet<>();
		sharedPaths = new IndexedSet<>();
		sharedNames = new IndexedSet<>();
	}
	
	public void serialize() throws IOException{
		doSerialize(value);
	}
	
	private void doSerialize(IValue value) throws IOException{
		// This special cases the hashing logic: if we have a constructor with
		// at least one location annotation, don't try to hash it
		boolean tryHashing = true;
		
		if (tryHashing && value.getType().isAbstractData()) {
			IConstructor consValue = (IConstructor)value;
			if (consValue.isAnnotatable() && consValue.asAnnotatable().hasAnnotations()) {
				Map<String,IValue> amap = consValue.asAnnotatable().getAnnotations();
				for (Entry<String, IValue> aEntry : amap.entrySet()) {
					Type aType = aEntry.getValue().getType();
					if (!aType.isBottom() && aType.isSourceLocation()) {
						tryHashing = false;
						break;
					}
				}
			}
		}
		
		boolean alwaysMaximallyShare = value.getType().isString() || value.getType().isNumber() || value.getType().isSourceLocation();
		if (tryHashing) {
		  int valueId;
		  if (maximalSharing || alwaysMaximallyShare) {
		    valueId = sharedValues.get(value);
		  }
		  else {
		    valueId = sharedValues.get(new IdentityValue(value));
		  }
		  if(valueId != -1){
		    out.write(SHARED_FLAG);
		    printInteger(valueId);
		    return;
		  }
		}
		
		// This sucks and is order dependent :-/.
		if(value instanceof IBool){
			writeBool((IBool) value);
		}else if(value instanceof IInteger){
			writeInteger((IInteger) value);
		}else if(value instanceof IRational){
			writeRational((IRational) value);
		}else if(value instanceof IReal){
			writeDouble((IReal) value);
		}else if(value instanceof IString){
			writeString((IString) value);
		}else if(value instanceof ISourceLocation){
			writeSourceLocation((ISourceLocation) value);
		}else if(value instanceof IDateTime){
			writeDateTime((IDateTime) value);
		}else if(value instanceof ITuple){
			writeTuple((ITuple) value);
		}else if(value instanceof IConstructor){
			IConstructor constructor = (IConstructor) value;
			if (constructor.mayHaveKeywordParameters() && constructor.asWithKeywordParameters().hasParameters()) {
				writeKeywordedConstructor(constructor);
			}
			else if (!constructor.mayHaveKeywordParameters() && constructor.isAnnotatable() && constructor.asAnnotatable().hasAnnotations()) {
				writeAnnotatedConstructor(constructor);
			}
			else {
				writeConstructor(constructor);
			}
		}else if(value instanceof INode){
			INode node = (INode) value;
			if (node.mayHaveKeywordParameters() && node.asWithKeywordParameters().hasParameters()) {
				writeKeywordedNode(node);
			}
			else if (!node.mayHaveKeywordParameters() && node.asAnnotatable().hasAnnotations()) {
				writeAnnotatedNode(node);
			}
			else {
				writeNode(node);
			}
		}else if(value instanceof IList){
			writeList((IList) value);
		}else if(value instanceof ISet){
				writeSet((ISet) value);
		}else if(value instanceof IMap){
			writeMap((IMap) value);
		}
		
		if (tryHashing) {
		  if (maximalSharing || alwaysMaximallyShare) {
		    sharedValues.store(value);
		  }
		  else {
		    sharedValues.store(new IdentityValue(value));
		  }
		}
	}
	
	private void doWriteType(Type type) throws IOException{
		// This sucks and is order dependent :-/.
	  type.accept(new ITypeVisitor<Type,IOException>() {

      @Override
      public Type visitReal(Type type) throws IOException {
        writeDoubleType();
        return type;
      }

      @Override
      public Type visitInteger(Type type) throws IOException {
        writeIntegerType();
        return type;
      }

      @Override
      public Type visitRational(Type type) throws IOException {
        writeRationalType();
        return type;
      }

      @Override
      public Type visitList(Type type) throws IOException {
        writeListType(type);
        return type;
      }

      @Override
      public Type visitMap(Type type) throws IOException {
        writeMapType(type);
        return type;
      }

      @Override
      public Type visitNumber(Type type) throws IOException {
        writeNumType();
        return type;
      }

      @Override
      public Type visitAlias(Type type) throws IOException {
        writeAliasType(type);
        return type;
      }

      @Override
      public Type visitSet(Type type) throws IOException {
        writeSetType(type);
        return type;
      }

      @Override
      public Type visitSourceLocation(Type type) throws IOException {
        writeSourceLocationType();
        return type;
      }

      @Override
      public Type visitString(Type type) throws IOException {
        writeStringType();
        return type;
      }

      @Override
      public Type visitNode(Type type) throws IOException {
        writeNodeType(type);
        return type;
      }

      @Override
      public Type visitConstructor(Type type) throws IOException {
        writeConstructorType(type);
        return type;
      }

      @Override
      public Type visitAbstractData(Type type) throws IOException {
        writeADTType(type);
        return type;
      }

      @Override
      public Type visitTuple(Type type) throws IOException {
        writeTupleType(type);
        return type;
      }

      @Override
      public Type visitValue(Type type) throws IOException {
        writeValueType();
        return type;
      }

      @Override
      public Type visitVoid(Type type) throws IOException {
        writeVoidType();
        return type;
      }

      @Override
      public Type visitBool(Type type) throws IOException {
        writeBoolType();
        return type;
      }

      @Override
      public Type visitParameter(Type type) throws IOException {
        writeParameterType(type);
        return type;
      }

      @Override
      public Type visitExternal(Type type) throws IOException {
    	  return visitAbstractData(((ExternalType) type).asAbstractDataType());
      }

      @Override
      public Type visitDateTime(Type type) throws IOException {
        writeDateTimeType(type);
        return type;
      }
    });
	}
	
	private void writeType(Type type) throws IOException{
		int typeId = sharedTypes.get(type);
		if(typeId != -1){
			out.write(SHARED_FLAG);
			printInteger(typeId);
			return;
		}
		
		doWriteType(type);
		
		sharedTypes.store(type);
	}
	
	private void writeBool(IBool bool) throws IOException{
		out.write(BOOL_HEADER);
		
		if(bool.getValue()){
			out.write(1);
		}else{
			out.write(0);
		}
	}
	
	private void writeInteger(IInteger integer) throws IOException{
		byte[] valueData = integer.getTwosComplementRepresentation();
		int length = valueData.length;
		out.write(BIG_INTEGER_HEADER);
		printInteger(length);
		out.write(valueData, 0, length);
	}

	/**
	 *  Format:
	 *    header
	 *    length of numerator
	 *    numerator byte[]
	 *    length of denominator
	 *    denominator byte[]
	 */
	private void writeRational(IRational rational) throws IOException{
		out.write(RATIONAL_HEADER);
		
		byte[] valueData = rational.numerator().getTwosComplementRepresentation();
		int length = valueData.length;
		printInteger(length);
		out.write(valueData, 0, length);

		valueData = rational.denominator().getTwosComplementRepresentation();
		length = valueData.length;
		printInteger(length);
		out.write(valueData, 0, length);
		
	}
	private void writeDouble(IReal real) throws IOException{
		out.write(DOUBLE_HEADER);
		
		byte[] valueData = real.unscaled().getTwosComplementRepresentation();
		int length = valueData.length;
		printInteger(length);
		out.write(valueData, 0, length);
		
		printInteger(real.scale());
	}
	
	private void writeString(IString string) throws IOException{
		out.write(STRING_HEADER);
		
		String theString = string.getValue();
		
		byte[] stringData = theString.getBytes(CharEncoding);
		printInteger(stringData.length);
		out.write(stringData);
	}
	
	private void writeSourceLocation(ISourceLocation sourceLocation) throws IOException{
		URI uri = sourceLocation.getURI();
		String path = uri.toString();
		int id = sharedPaths.store(path);
		
		int header = SOURCE_LOCATION_HEADER;
		
		if(id == -1){
			out.write(header);
			
			byte[] pathData = path.getBytes(CharEncoding);
			printInteger(pathData.length);
			out.write(pathData);
		} else{
			out.write(header | URL_SHARED_FLAG);
			
			printInteger(id);
		}
		
		int beginLine, beginColumn, endLine, endColumn;
		
		if (!sourceLocation.hasLineColumn()) {
			beginLine = -1;
			endLine = -1;
			beginColumn = -1;
			endColumn = -1;
		}
		else {
			beginLine = sourceLocation.getBeginLine();
			endLine = sourceLocation.getEndLine();
			beginColumn = sourceLocation.getBeginColumn();
			endColumn = sourceLocation.getEndColumn();
		}
		
		int offset, length;
		if (!sourceLocation.hasOffsetLength()) {
			offset = -1;
			length = -1;
		}
		else {
			offset = sourceLocation.getOffset();
			length = sourceLocation.getLength();
		}
		
		printInteger(offset);
		printInteger(length);
		printInteger(beginLine);
		printInteger(endLine);
		printInteger(beginColumn);
		printInteger(endColumn);
	}
	
	private void writeDateTime(IDateTime dateTime) throws IOException{
		out.write(DATE_TIME_HEADER);
		
		if(dateTime.isDateTime()){
			out.write(DATE_TIME_INDICATOR);
			
			printInteger(dateTime.getYear());
			printInteger(dateTime.getMonthOfYear());
			printInteger(dateTime.getDayOfMonth());
			
			printInteger(dateTime.getHourOfDay());
			printInteger(dateTime.getMinuteOfHour());
			printInteger(dateTime.getSecondOfMinute());
			printInteger(dateTime.getMillisecondsOfSecond());
			
			printInteger(dateTime.getTimezoneOffsetHours());
			printInteger(dateTime.getTimezoneOffsetMinutes());
		}else if(dateTime.isDate()){
			out.write(DATE_INDICATOR);
			
			printInteger(dateTime.getYear());
			printInteger(dateTime.getMonthOfYear());
			printInteger(dateTime.getDayOfMonth());
		}else{
			out.write(TIME_INDICATOR);
			
			printInteger(dateTime.getHourOfDay());
			printInteger(dateTime.getMinuteOfHour());
			printInteger(dateTime.getSecondOfMinute());
			printInteger(dateTime.getMillisecondsOfSecond());
			
			printInteger(dateTime.getTimezoneOffsetHours());
			printInteger(dateTime.getTimezoneOffsetMinutes());
		}
	}
	
	private void writeTuple(ITuple tuple) throws IOException{
		out.write(TUPLE_HEADER);
		
		int arity = tuple.arity();
		printInteger(arity);
		
		for(int i = 0; i < arity; i++){
			doSerialize(tuple.get(i));
		}
	}
	
	private void writeNode(INode node) throws IOException{
		String nodeName = node.getName();
		int nodeNameId = sharedNames.store(nodeName);
		
		if(nodeNameId == -1){
			out.write(NODE_HEADER);
			
			byte[] nodeData = nodeName.getBytes(CharEncoding);
			printInteger(nodeData.length);
			out.write(nodeData);
		}else{
			out.write(NODE_HEADER | NAME_SHARED_FLAG);
			
			printInteger(nodeNameId);
		}
		
		int arity = node.arity();
		printInteger(arity);
		
		for(int i = 0; i < arity; i++){
			doSerialize(node.get(i));
		}
	}
	
	private void writeKeywordedNode(INode node) throws IOException{
		String nodeName = node.getName();
		int nodeNameId = sharedNames.store(nodeName);
		
		if(nodeNameId == -1){
			out.write(KEYWORDED_NODE_HEADER);
			
			byte[] nodeData = nodeName.getBytes(CharEncoding);
			printInteger(nodeData.length);
			out.write(nodeData);
		}else{
			out.write(KEYWORDED_NODE_HEADER | NAME_SHARED_FLAG);
			
			printInteger(nodeNameId);
		}
		
		int arity = node.arity();
		printInteger(arity);
		
		for(int i = 0; i < arity; i++){
			doSerialize(node.get(i));
		}
		
		Map<String, IValue> kwParams = node.asWithKeywordParameters().getParameters();
		
		printInteger(kwParams.size());
		
		for (Map.Entry<String, IValue> param : kwParams.entrySet()) {
			String label = param.getKey();
			byte[] labelData = label.getBytes(CharEncoding);
			printInteger(labelData.length);
			out.write(labelData);
			
			IValue value = param.getValue();
			doSerialize(value);
		}
	}
	private void writeAnnotatedNode(INode node) throws IOException{
		String nodeName = node.getName();
		int nodeNameId = sharedNames.store(nodeName);
		
		if(nodeNameId == -1){
			out.write(ANNOTATED_NODE_HEADER);
			
			byte[] nodeData = nodeName.getBytes(CharEncoding);
			printInteger(nodeData.length);
			out.write(nodeData);
		}else{
			out.write(ANNOTATED_NODE_HEADER | NAME_SHARED_FLAG);
			
			printInteger(nodeNameId);
		}
		
		int arity = node.arity();
		printInteger(arity);
		
		for(int i = 0; i < arity; i++){
			doSerialize(node.get(i));
		}
		
		Map<String, IValue> annotations = node.asAnnotatable().getAnnotations();
		
		printInteger(annotations.size());
		
		Iterator<Map.Entry<String, IValue>> annotationsIterator = annotations.entrySet().iterator();
		while(annotationsIterator.hasNext()){
			Map.Entry<String, IValue> annotation = annotationsIterator.next();
			String label = annotation.getKey();
			byte[] labelData = label.getBytes(CharEncoding);
			printInteger(labelData.length);
			out.write(labelData);
			
			IValue value = annotation.getValue();
			doSerialize(value);
		}
	}
	
	private void writeConstructor(IConstructor constructor) throws IOException{
		Type constructorType = constructor.getUninstantiatedConstructorType();
		int constructorTypeId = sharedTypes.get(constructorType);
		
		if(constructorTypeId == -1){
			out.write(CONSTRUCTOR_HEADER);
			
			doWriteType(constructorType);
			
			sharedTypes.store(constructorType);
		}else{
			out.write(CONSTRUCTOR_HEADER | TYPE_SHARED_FLAG);
			
			printInteger(constructorTypeId);
		}
		
		int arity = constructor.arity();
		printInteger(arity);
		
		for(int i = 0; i < arity; i++){
			doSerialize(constructor.get(i));
		}
	}

	private void writeKeywordedConstructor(IConstructor constructor) throws IOException{
		Type constructorType = constructor.getConstructorType();
		int constructorTypeId = sharedTypes.get(constructorType);
		
		if(constructorTypeId == -1){
			out.write(KEYWORDED_CONSTRUCTOR_HEADER);
			
			doWriteType(constructorType);
			
			sharedTypes.store(constructorType);
		}else{
			out.write(KEYWORDED_CONSTRUCTOR_HEADER | TYPE_SHARED_FLAG);
			
			printInteger(constructorTypeId);
		}
		
		int arity = constructor.arity();
		printInteger(arity);
		
		for(int i = 0; i < arity; i++){
			doSerialize(constructor.get(i));
		}
		
		Map<String, IValue> kwParams = constructor.asWithKeywordParameters().getParameters();
		
		printInteger(kwParams.size());
		
		for (Map.Entry<String, IValue> param: kwParams.entrySet()) {
			String label = param.getKey();
			byte[] labelData = label.getBytes(CharEncoding);
			printInteger(labelData.length);
			out.write(labelData);
			
			IValue value = param.getValue();
			doSerialize(value);
		}
	}
	
	private void writeAnnotatedConstructor(IConstructor constructor) throws IOException{
		Type constructorType = constructor.getConstructorType();
		int constructorTypeId = sharedTypes.get(constructorType);
		
		if(constructorTypeId == -1){
			out.write(ANNOTATED_CONSTRUCTOR_HEADER);
			
			doWriteType(constructorType);
			
			sharedTypes.store(constructorType);
		}else{
			out.write(ANNOTATED_CONSTRUCTOR_HEADER | TYPE_SHARED_FLAG);
			
			printInteger(constructorTypeId);
		}
		
		int arity = constructor.arity();
		printInteger(arity);
		
		for(int i = 0; i < arity; i++){
			doSerialize(constructor.get(i));
		}
		
		Map<String, IValue> annotations = constructor.asAnnotatable().getAnnotations();
		
		printInteger(annotations.size());
		
		Iterator<Map.Entry<String, IValue>> annotationsIterator = annotations.entrySet().iterator();
		while(annotationsIterator.hasNext()){
			Map.Entry<String, IValue> annotation = annotationsIterator.next();
			String label = annotation.getKey();
			byte[] labelData = label.getBytes(CharEncoding);
			printInteger(labelData.length);
			out.write(labelData);
			
			IValue value = annotation.getValue();
			doSerialize(value);
		}
	}
	
	private void writeList(IList list) throws IOException{
		Type elementType = list.getElementType();
		int elementTypeId = sharedTypes.get(elementType);
		
		if(elementTypeId == -1){
			out.write(LIST_HEADER);
			
			doWriteType(elementType);
			
			sharedTypes.store(elementType);
		}else{
			out.write(LIST_HEADER | TYPE_SHARED_FLAG);
			
			printInteger(elementTypeId);
		}
		
		int length = list.length();
		printInteger(length);
		for(int i = 0; i < length; i++){
			doSerialize(list.get(i));
		}
	}
	
	private void writeSet(ISet set) throws IOException{
		Type elementType = set.getElementType();
		int elementTypeId = sharedTypes.get(elementType);
		
		if(elementTypeId == -1){
			out.write(SET_HEADER);
			
			doWriteType(elementType);
			
			sharedTypes.store(elementType);
		}else{
			out.write(SET_HEADER | TYPE_SHARED_FLAG);
			
			printInteger(elementTypeId);
		}

		printInteger(set.size());
		
		Iterator<IValue> content = set.iterator();
		while(content.hasNext()){
			doSerialize(content.next());
		}
	}
	
	private void writeMap(IMap map) throws IOException{
		Type mapType = map.getType();
		int mapTypeId = sharedTypes.get(mapType);
		
		if(mapTypeId == -1){
			out.write(MAP_HEADER);
			
			doWriteType(mapType);
			
			sharedTypes.store(mapType);
		}else{
			out.write(MAP_HEADER | TYPE_SHARED_FLAG);
			
			printInteger(mapTypeId);
		}
		
		printInteger(map.size());
		
		Iterator<Map.Entry<IValue, IValue>> content = map.entryIterator();
		while(content.hasNext()){
			Map.Entry<IValue, IValue> entry = content.next();
			
			doSerialize(entry.getKey());
			doSerialize(entry.getValue());
		}
	}
	
	private void writeValueType() throws IOException{
		out.write(VALUE_TYPE_HEADER);
	}
	
	private void writeVoidType() throws IOException{
		out.write(VOID_TYPE_HEADER);
	}
	
	private void writeBoolType() throws IOException{
		out.write(BOOL_TYPE_HEADER);
	}
	
	private void writeIntegerType() throws IOException{
		out.write(INTEGER_TYPE_HEADER);
	}
	
	private void writeNumType() throws IOException {
    out.write(NUM_TYPE_HEADER);
  }
	
	private void writeRationalType() throws IOException{
		out.write(RATIONAL_TYPE_HEADER);
	}
	
	private void writeDoubleType() throws IOException{
		out.write(DOUBLE_TYPE_HEADER);
	}
	
	private void writeStringType() throws IOException{
		out.write(STRING_TYPE_HEADER);
	}
	
	private void writeSourceLocationType() throws IOException{
		out.write(SOURCE_LOCATION_TYPE_HEADER);
	}
	
	private void writeDateTimeType(Type dateTimeType) throws IOException{
		out.write(DATE_TIME_TYPE_HEADER);
	}
	
	private void writeNodeType(Type nodeType) throws IOException{
		Map<String, Type> declaredAnnotations = typeStore.getAnnotations(nodeType);
		if(declaredAnnotations.isEmpty()){
			out.write(NODE_TYPE_HEADER);
		}else{
			out.write(ANNOTATED_NODE_TYPE_HEADER);
			
			// Annotations.
			int nrOfAnnotations = declaredAnnotations.size();
			printInteger(nrOfAnnotations);
			
			Iterator<Map.Entry<String, Type>> declaredAnnotationsIterator = declaredAnnotations.entrySet().iterator();
			while(declaredAnnotationsIterator.hasNext()){
				Map.Entry<String, Type> declaredAnnotation = declaredAnnotationsIterator.next();
				
				String label = declaredAnnotation.getKey();
				byte[] labelBytes = label.getBytes(CharEncoding);
				printInteger(labelBytes.length);
				out.write(labelBytes);
				
				writeType(declaredAnnotation.getValue());
			}
		}
	}
	
	private void writeTupleType(Type tupleType) throws IOException{
		boolean hasFieldNames = tupleType.hasFieldNames();
		
		if(hasFieldNames){
			out.write(TUPLE_TYPE_HEADER | HAS_FIELD_NAMES);
			
			int arity = tupleType.getArity();
			printInteger(arity);
			for(int i = 0; i < arity; i++){
				writeType(tupleType.getFieldType(i));
				
				String name = tupleType.getFieldName(i);
				byte[] nameData = name.getBytes(CharEncoding);
				printInteger(nameData.length);
				out.write(nameData);
			}
		}else{
			out.write(TUPLE_TYPE_HEADER);
			
			int arity = tupleType.getArity();
			printInteger(arity);
			for(int i = 0; i < arity; i++){
				writeType(tupleType.getFieldType(i));
			}
		}
	}
	
	private void writeListType(Type listType) throws IOException{
		out.write(LIST_TYPE_HEADER);
		
		writeType(listType.getElementType());
	}
	
	private void writeSetType(Type setType) throws IOException{
		out.write(SET_TYPE_HEADER);
		
		writeType(setType.getElementType());
	}
	
	private void writeMapType(Type mapType) throws IOException{
		boolean hasFieldNames = mapType.hasFieldNames();
		
		if(hasFieldNames){
			out.write(MAP_TYPE_HEADER | HAS_FIELD_NAMES);

			String name;
			byte[] nameData;

			writeType(mapType.getKeyType());
			name = mapType.getKeyLabel();
			nameData = name.getBytes(CharEncoding);
			printInteger(nameData.length);
			out.write(nameData);
			
			writeType(mapType.getValueType());
			name = mapType.getValueLabel();
			nameData = name.getBytes(CharEncoding);
			printInteger(nameData.length);
			out.write(nameData);
		}
		else {
			out.write(MAP_TYPE_HEADER);
			writeType(mapType.getKeyType());
			writeType(mapType.getValueType());
		}
	}
	
	private void writeParameterType(Type parameterType) throws IOException{
		out.write(PARAMETER_TYPE_HEADER);
		
		String name = parameterType.getName();
		byte[] nameData = name.getBytes(CharEncoding);
		printInteger(nameData.length);
		out.write(nameData);
		
		writeType(parameterType.getBound());
	}
	
	private void writeADTType(Type adtType) throws IOException{
		out.write(ADT_TYPE_HEADER);
		
		String name = adtType.getName();
		byte[] nameData = name.getBytes(CharEncoding);
		printInteger(nameData.length);
		out.write(nameData);
		
		writeType(adtType.getTypeParameters());
	}
	
	private void writeConstructorType(Type constructorType) throws IOException{
		Map<String, Type> declaredAnnotations = typeStore.getAnnotations(constructorType);
		if(declaredAnnotations.isEmpty()){
			out.write(CONSTRUCTOR_TYPE_HEADER);
			
			String name = constructorType.getName();
			byte[] nameData = name.getBytes(CharEncoding);
			printInteger(nameData.length);
			out.write(nameData);
			
			writeType(constructorType.getFieldTypes());
			
			writeType(constructorType.getAbstractDataType());
		}else{
			out.write(ANNOTATED_CONSTRUCTOR_TYPE_HEADER);
			
			String name = constructorType.getName();
			byte[] nameData = name.getBytes(CharEncoding);
			printInteger(nameData.length);
			out.write(nameData);
			
			writeType(constructorType.getFieldTypes());
			
			writeType(constructorType.getAbstractDataType());
			
			// Annotations.
			int nrOfAnnotations = declaredAnnotations.size();
			printInteger(nrOfAnnotations);
			
			Iterator<Map.Entry<String, Type>> declaredAnnotationsIterator = declaredAnnotations.entrySet().iterator();
			while(declaredAnnotationsIterator.hasNext()){
				Map.Entry<String, Type> declaredAnnotation = declaredAnnotationsIterator.next();
				
				String label = declaredAnnotation.getKey();
				byte[] labelBytes = label.getBytes(CharEncoding);
				printInteger(labelBytes.length);
				out.write(labelBytes);
				
				writeType(declaredAnnotation.getValue());
			}
		}
	}
	
	private void writeAliasType(Type aliasType) throws IOException{
		out.write(ALIAS_TYPE_HEADER);
		
		String name = aliasType.getName();
		byte[] nameData = name.getBytes(CharEncoding);
		printInteger(nameData.length);
		out.write(nameData);
		
		writeType(aliasType.getAliased());
		
		writeType(aliasType.getTypeParameters());
	}
	
	private final static int SEVENBITS = 0x0000007f;
	private final static int SIGNBIT = 0x00000080;
	
	private void printInteger(int value) throws IOException{
		int intValue = value;
		
		if((intValue & 0xffffff80) == 0){
			out.write((byte) (intValue & SEVENBITS));
			return;
		}
		out.write((byte) ((intValue & SEVENBITS) | SIGNBIT));
		
		if((intValue & 0xffffc000) == 0){
			out.write((byte) ((intValue >>> 7) & SEVENBITS));
			return;
		}
		out.write((byte) (((intValue >>> 7) & SEVENBITS) | SIGNBIT));
		
		if((intValue & 0xffe00000) == 0){
			out.write((byte) ((intValue >>> 14) & SEVENBITS));
			return;
		}
		out.write((byte) (((intValue >>> 14) & SEVENBITS) | SIGNBIT));
		
		if((intValue & 0xf0000000) == 0){
			out.write((byte) ((intValue >>> 21) & SEVENBITS));
			return;
		}
		out.write((byte) (((intValue >>> 21) & SEVENBITS) | SIGNBIT));
		
		out.write((byte) ((intValue >>> 28) & SEVENBITS));
	}
}
