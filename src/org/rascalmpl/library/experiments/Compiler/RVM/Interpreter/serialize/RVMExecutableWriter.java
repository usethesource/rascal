package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.IndexedSet;

/**
 * RVMExecutableWriter is a binary serializer for RVMExecutable and related classes
 * such as Function, OverloadedFunction, CodeBlock.
 * 
 * Given an OutputStream, this is wrapped as an RVMOutputStream (by RVMIValueWriter)
 * and both RVMExecutableWriter and RVMIValueWriter write to the latter stream.
 * 
 * To write Class instances write operations are provided for Java values that occur in 
 * the mentioned classes.
 * 
 * Each class should define a write function, e,g, void Function.write(RVMExecutableWriter out).
 * 
 * Sharing is only done for Java values of type Map<String,Integer>, see writeMapStringInt
 */
public class RVMExecutableWriter {
	
	private final RVMIValueWriter valueWriter;
	transient private static IndexedSet<Object> sharedObjects;

	public RVMExecutableWriter(OutputStream out){
		valueWriter = new RVMIValueWriter(out);
		sharedObjects = new IndexedSet<>();
		sharedObjects.store(new Boolean(false));	// make sure index 0 will not occur
	}
	
	public void close() throws IOException{
		valueWriter.close();
	}
	
	public void writeArrayListString(ArrayList<String> initializers) throws IOException {
		int n = initializers == null ? 0 : initializers.size();
		//if(n>10)System.out.println("*** writeArrayListString: " + n);
		writeInt(n);
		for(int i = 0; i < n; i++){
			writeJString(initializers.get(i));
		}
	}
	
	public void writeArrayOverloadedFunctions(OverloadedFunction[] overloadedStore) throws IOException {
		int n = overloadedStore == null ? 0 : overloadedStore.length;
		//if(n>10)System.out.println("*** writeArrayOverloadedFunctions: " + n);
		writeInt(n);
		for(int i = 0; i < n; i++){
			overloadedStore[i].write(this);
		}
	}
	
	public void writeBool(boolean b) throws IOException {
		valueWriter.getOut().writeBool(b);
	}
	
	public void writeByteArray(byte[] jvmByteCode) throws IOException {
		int n = jvmByteCode.length;
		writeInt(n);
		valueWriter.getOut().write(jvmByteCode, 0, n);
	}

	
	public void writeConstructorStore(ArrayList<Type> constructorStore) throws IOException {
		int n = constructorStore == null ? 0 : constructorStore.size();
		//if(n>10)System.out.println("*** writeConstructorStore: " + n);
		
		writeInt(n);

		for(int i = 0; i < n; i++){
			valueWriter.writeType(constructorStore.get(i));
		}
	}
	
	public void writeInt(int n) throws IOException {
		valueWriter.getOut().writeInt(n);
	}
	
	public void writeIntArray(int[] ia) throws IOException {
		int n = ia == null ? 0 : ia.length;
		writeInt(n);
		for(int i = 0; i < n; i++){
			writeInt(ia[i]);
		}
	}
	
	public void writeFunctionStore(Function[] functionStore) throws IOException {
		int n = functionStore == null ? 0 : functionStore.length;
		//if(n>10)System.out.println("*** writeFunctionStore: " + n);
		writeInt(n);
		
		for(int i = 0; i < n; i++){
			functionStore[i].write(this);
		}
	}
	
	public void writeJString(String s) throws IOException {
		valueWriter.writeName(s);
	}

	private void writeLong(long l) throws IOException {
		valueWriter.getOut().writeLong(l);
	}
	
	public void writeLongArray(long[] la) throws IOException {
		int n = la == null ? 0 : la.length;
		writeInt(n);
		//if(n>10)System.out.println("*** writeLongArray: " + n);
		
		for(int i = 0; i < n; i++){
			writeLong(la[i]);
		}
	}

	public void writeMapStringInt(Map<String, Integer> map) throws IOException {

		int mapId = sharedObjects.store(map);
		int n = map == null ? 0 : map.size();
		if(mapId == -1){
			writeInt(n);
			//if(n>10)System.out.println("*** writeMapStringInt: " + n);

			if(n == 0) return;
			for(String key : map.keySet()){
				writeJString(key);
				writeInt(map.get(key));
			}
		} else {
			writeInt(-mapId);
		}
	}

	public void writeMapIntToIntArray(HashMap<Integer, int[]> filteredFunctions) throws IOException {
		int n = filteredFunctions == null ? 0 : filteredFunctions.size();
		writeInt(n);
		
		//if(n>10)System.out.println("*** writeMapIntToIntArray: " + n);
		
		if(n == 0) return;
		for(Integer key : filteredFunctions.keySet()){
			writeInt(key);
			writeIntArray(filteredFunctions.get(key));
		}
	}
	
	public void writeType(Type type) throws IOException{
		valueWriter.writeType(type);
	}
	
	public void writeValue(IValue value) throws IOException{
		valueWriter.writeValue(value);
	}
}
