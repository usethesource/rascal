package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.util.ResizingArray;
import org.rascalmpl.values.ValueFactoryFactory;

import com.google.protobuf.CodedInputStream;

/**
 * RVMExecutableReader is a binary deserializer for RVMExecutable and related classes
 * such as Function, OverloadedFunction, CodeBlock.
 * 
 * Given an InputStream, this is wrapped as an RVMInputStream (by RVMIValueReader)
 * and both RVMExecutableReader and RVMIValueReader read from the latter stream.
 * 
 * To read Class instances read operations are provided for Java values that occur in 
 * the mentioned classes.
 * 
 * Each class should define a read function, e,g, Function Function.read(RVMExecutableReader in).
 *
 * Sharing is only done for Java values of type Map<String,Integer>, see readMapStringInt
 */
public class RVMExecutableReader {
	
	private final RVMIValueReader valueReader;
	
	private final CodedInputStream in;
	
	transient private final ResizingArray<Object> sharedObjectsList;
	transient private int currentSharedObjectId;

	public RVMExecutableReader(InputStream in){
		this.valueReader = new RVMIValueReader(in, ValueFactoryFactory.getValueFactory(), new TypeStore());
		this.in = valueReader.getIn();
		sharedObjectsList = new ResizingArray<>(1000);
		sharedObjectsList.set(new Boolean(false), currentSharedObjectId++); // make sure index 0 is not used
	}
	
	public void close() throws IOException {
		//in.close();
	}
	
	public ArrayList<String> readArrayListString() throws IOException {
		int n = in.readInt32();
		ArrayList<String> res = new ArrayList<String>(n);
		for(int i = 0; i < n; i++){
			String s = readJString();
			res.add(s);
		}
		return res;
	}
	
	public OverloadedFunction[] readArrayOverloadedFunctions() throws IOException {
		int n = in.readInt32();
		OverloadedFunction[] res = new OverloadedFunction[n];

		for(int i = 0; i < n; i++){
			//res[i] = OverloadedFunction.read(this);
		    throw new UnsupportedOperationException ("This should be changed after the RMV core stuff supports the rvm reader");
		}
		return res;
	}

	public boolean readBool() throws IOException {
		return in.readBool();
	}
	
	public byte[] readByteArray() throws IOException {
		int n = readInt();
		return in.readRawBytes(n);
	}
	
	public Integer readInt() throws IOException {
		return in.readInt32();
	}
	
	public Function[] readFunctionStore() throws IOException {
		int n = readInt();
		Function[] funs = new Function[n];
		for(int i = 0; i < n; i++){
			//funs[i] = Function.read(this);
		    throw new UnsupportedOperationException ("This should be changed after the RMV core stuff supports the rvm reader");
		}
		return funs;
	}
	
	public int[] readIntArray() throws IOException {
		int n = readInt();
		int[] ints = new int[n];
		for(int i = 0; i < n; i++){
			ints[i] = readInt();
		}
		return ints;
	}
	
	public long[] readLongArray() throws IOException {
		int n = readInt();
		long[] longs = new long[n];
		for(int i = 0; i < n; i++){
			longs[i] = valueReader.getIn().readInt64();
		}
		return longs;
	}
	
	public String readJString() throws IOException {
		return valueReader.readName();
	}

	public HashMap<Integer, int[]> readMapIntToIntArray() throws IOException {
		int n = readInt();
		HashMap<Integer, int[]> map = new HashMap<Integer, int[]>(n);
		for(int i = 0; i < n; i++){
			int key = readInt();
			int[] ints = readIntArray();
			map.put(key,  ints);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Integer> readMapStringInt() throws IOException {
		int n = readInt();
		if(n >= 0){
			HashMap<String, Integer> map = new HashMap<>(n);
			for(int i = 0; i < n; i++){
				String key = readJString();
				int val = readInt();
				map.put(key,  val);
			}
			sharedObjectsList.set(map, currentSharedObjectId++);
			return map;
		} else {
			return ( Map<String, Integer>) sharedObjectsList.get(-n);
		}
	}

	public Type readType() throws IOException {
		return valueReader.readType();
	}
	
	public IValue readValue() throws IOException {
		return valueReader.readValue();
	}
}
