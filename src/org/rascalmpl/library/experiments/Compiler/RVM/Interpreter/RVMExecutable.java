package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.XXXFSTIValueSerializer;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.uptr.Factory;

import de.ruedigermoeller.serialization.FSTBasicObjectSerializer;
import de.ruedigermoeller.serialization.FSTClazzInfo;
import de.ruedigermoeller.serialization.FSTClazzInfo.FSTFieldInfo;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;


/**
 * RVMExecutable contains all data needed for executing an RVM program.
 *
 * RVMExecutable is serialized by FSTRVMExecutableSerializer, make sure that
 * all fields declared here are synced with the serializer.
 */
public class RVMExecutable implements Serializable{

	private static final long serialVersionUID = -8966920880207428792L;
   
	
	// transient fields
	transient static IValueFactory vf;
	transient static TypeStore store;
	//transient static TypeSerializer typeserializer;
	
	// Serializable fields
	
	public String module_name;
	public IMap moduleTags;
	public IMap symbol_definitions;
	
	public ArrayList<Function> functionStore;
	public  Map<String, Integer> functionMap;
	
	// Constructors
	public ArrayList<Type> constructorStore;
	public Map<String, Integer> constructorMap;
	
	// Function overloading
	public ArrayList<OverloadedFunction> overloadedStore;
	public Map<String, Integer> resolver;
	
	public ArrayList<String> initializers;
	public ArrayList<String> testsuites;
	public String uid_module_init;
	public String uid_module_main;
	public String uid_module_main_testsuite;
	
	public RVMExecutable(
			final String module_name,
			final IMap moduleTags,
			
			final IMap symbol_definitions,
			final Map<String, Integer> functionMap,
			final ArrayList<Function> functionStore,
			
			final Map<String, Integer> constructorMap,
			final ArrayList<Type> constructorStore,
	
			final Map<String, Integer> resolver,
			final ArrayList<OverloadedFunction> overloadedStore,
			
			ArrayList<String> initializers,
			ArrayList<String> testsuites,
			String uid_module_init,
			String uid_module_main,
			String uid_module_main_testsuite,
			TypeStore ts,
			IValueFactory vfactory
			){
		
		this.module_name = module_name;
		this.moduleTags = moduleTags;
		this.symbol_definitions = symbol_definitions;
		
		this.functionMap = functionMap;
		this.functionStore = functionStore;
		
		this.constructorMap = constructorMap;
		this.constructorStore = constructorStore;

		this.resolver = resolver;
		this.overloadedStore = overloadedStore;
		
		this.initializers = initializers;
		this.testsuites = testsuites;
		
		this.uid_module_init = uid_module_init;
		this.uid_module_main = uid_module_main;
		this.uid_module_main_testsuite = uid_module_main_testsuite;
		
		vf = vfactory;
		store = ts;
	}
	
	private static ISourceLocation compressedLoc(ISourceLocation exec) throws URISyntaxException{
		String scheme = "compressed+" + exec.getScheme();
		String authority = exec.getAuthority();
		String path = exec.getPath() + ".xz";
		return vf.sourceLocation(scheme, authority, path);
	}
	
	public void write(ISourceLocation rvmExecutable){		
		OutputStream fileOut;
		
		TypeStore typeStore = new TypeStore(Factory.getStore());
		
		FSTSerializableType.initSerialization(vf, typeStore);
		FSTSerializableIValue.initSerialization(vf, typeStore);
		
		FSTRVMExecutableSerializer.initSerialization(vf, typeStore);
		FSTFunctionSerializer.initSerialization(vf, typeStore);
		FSTCodeBlockSerializer.initSerialization(vf, typeStore);

		try {
			ISourceLocation compOut = compressedLoc(rvmExecutable);
			fileOut = URIResolverRegistry.getInstance().getOutputStream(compOut, false);
			//ObjectOutputStream out = new ObjectOutputStream(fileOut);
			FSTObjectOutput out = new FSTObjectOutput(fileOut, RascalLinker.conf);
			long before = Timing.getCpuTime();
			out.writeObject(this);
			out.close();
			System.out.println("Writing: " + compOut.getPath() + " [" +  (Timing.getCpuTime() - before)/1000000 + " msec]");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static RVMExecutable read(ISourceLocation rvmExecutable) {
		RVMExecutable executable = null;
		
		TypeStore typeStore = new TypeStore(Factory.getStore());
		
		FSTSerializableType.initSerialization(vf, typeStore);
		FSTSerializableIValue.initSerialization(vf, typeStore);
	
		FSTRVMExecutableSerializer.initSerialization(vf, typeStore);
		FSTFunctionSerializer.initSerialization(vf, typeStore);
		FSTCodeBlockSerializer.initSerialization(vf, typeStore);
	
		FSTObjectInput in = null;
		try {
			ISourceLocation compIn = compressedLoc(rvmExecutable);
			InputStream fileIn = URIResolverRegistry.getInstance().getInputStream(compIn);
			in = new FSTObjectInput(fileIn, RascalLinker.conf);
			long before = Timing.getCpuTime();
			executable = (RVMExecutable) in.readObject(RVMExecutable.class);
			in.close();
			in = null;
			System.out.println("Reading: " + compIn.getPath() + " [" +  (Timing.getCpuTime() - before)/1000000 + " msec]");
		} catch (IOException i) {
			i.printStackTrace();

		} catch (ClassNotFoundException c) {
			System.out.println("Class not found: " + c.getMessage());
			c.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return executable;
	}
	
	public boolean comparable(RVMExecutable other){
		
		boolean nameOk = this.module_name.equals(other.module_name);
		boolean symbol_definitionsOk = true;
		
		IMap defs1 = this.symbol_definitions;
		IMap defs2 = other.symbol_definitions;
		
		if(defs1.size() != defs2.size()){
			System.err.println("symbol_definitions: different size" + defs1.size() + " vs " + defs2.size());
			symbol_definitionsOk = false;
		}
		
		for(IValue key : defs1){
			if(!defs2.containsKey(key)){
				System.err.println("symbol_definitions: key " + key + " not in defs2");
				symbol_definitionsOk = false;
			} else if(!defs1.get(key).equals(defs2.get(key))){
				System.err.println("symbol_definitions: different values for key " + key + ": " + defs1.get(key) + " vs " + defs2.get(key));
				IConstructor choice1 = (IConstructor) defs1.get(key);
				IConstructor choice2 = (IConstructor) defs2.get(key);
				
				IValue adt1 = choice1.get("def");
				IValue adt2 = choice2.get("def");
				
				boolean a1 = adt1.equals(adt2);
				boolean a2 = adt1 == adt2;
				
				ISet alts1 =  (ISet) choice1.get("alternatives");
				ISet alts2 =  (ISet) choice1.get("alternatives");
				
				boolean b = alts1.equals(alts2);
				boolean c = choice1.equals(choice2);
				
				System.err.println("symbol_definitions: a1=" + a1 + ", a2=" + a2 + ", b=" + b + ", c=" + c);
				
				symbol_definitionsOk = false;
			}
		}
		
		boolean functionMapOk = this.functionMap.equals(other.functionMap);
	
		boolean constructorMapOk = this.constructorMap.equals(other.constructorMap);
		boolean resolverOk = this.resolver.equals(other.resolver);
		boolean initializersOk = this.initializers.equals(other.initializers);
		boolean testsuitesOk = this.testsuites.equals(other.testsuites);
		boolean uidsOk = this.uid_module_init.equals(other.uid_module_init) &&
						 this.uid_module_main.equals(other.uid_module_main) &&
						 this.uid_module_main_testsuite.equals(other.uid_module_main_testsuite);
		
		boolean constructorStoreOk = true;
		ArrayList<Type> cs = this.constructorStore;
		ArrayList<Type> cs2 = other.constructorStore;
		
		if(cs.size() != cs2.size()){
			System.err.println("constructorStore: " + cs.size() + " vs " + cs2.size());
			constructorStoreOk = false;
		}
		
		for(int i = 0; i < Math.min(cs.size(),cs2.size()); i++){
			if(!cs.get(i).equals(cs2.get(i))){
				System.err.println(i + ": " + cs.get(i) + " vs " + cs2.get(i));
				constructorStoreOk = false;
			}
		}
		
		boolean overloadedStoreOk = true;
		
		ArrayList<OverloadedFunction> ols = this.overloadedStore;
		ArrayList<OverloadedFunction> ols2 = other.overloadedStore;
		
		if(ols.size() !=  ols2.size()){
			System.err.println("overloadedStores: " + ols.size() + " vs " + ols2.size());
			overloadedStoreOk = false;
		}
		
		for(int i = 0; i < Math.min(ols.size(),ols2.size()); i++){
			if(!ols.get(i).comparable(ols2.get(i))){
				System.err.println(i + ": " + ols.get(i) + " vs " + ols2.get(i));
				overloadedStoreOk = false;
			}
		}
		
		System.out.println("Checking original and copy of RVMExecutable:");
		System.out.println("\tname:                " + nameOk);
		System.out.println("\tsymbol_definitions: " + symbol_definitionsOk 	+ " [" + symbol_definitions.size() + "]");
		System.out.println("\tfunctionMap:        " + functionMapOk 		+ " [" + functionMap.size() + "]");
		System.out.println("\tconstructorStore:   " + constructorStoreOk 	+ " [" + constructorStore.size() + "]");
		System.out.println("\tconstructorMap:     " + constructorMapOk 		+ " [" + constructorStore.size() + "]");
		System.out.println("\tresolver:           " + resolverOk 			+ " [" + resolver.size() + "]");
		System.out.println("\tinitializers:       " + initializersOk 		+ " [" + initializers.size() + "]");
		System.out.println("\ttestsuites:         " + testsuitesOk 			+ " [" + testsuites.size() + "]");
		System.out.println("\tuids:               " + uidsOk);
		System.out.println("\toverloadedStore:    " + overloadedStoreOk 	+ " [" + overloadedStore.size() + "]");
		
		return functionMapOk && constructorStoreOk && constructorMapOk && 
			   resolverOk && overloadedStoreOk && initializersOk && testsuitesOk && uidsOk;
	}
}
	
class FSTRVMExecutableSerializer extends FSTBasicObjectSerializer {

	private static IValueFactory vf;
	private static TypeStore store;
	private static TypeReifier tr;
	//private static TypeSerializer typeserializer;

	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		vf = vfactory;
		store = ts;
		store.extendStore(Factory.getStore());
		tr = new TypeReifier(vf);
		//typeserializer = new TypeSerializer(ts);
	}

	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite,
			FSTClazzInfo arg2, FSTFieldInfo arg3, int arg4)
					throws IOException {

		int n;
		RVMExecutable ex = (RVMExecutable) toWrite;

		// public String module_name;

		out.writeObject(ex.module_name);

		// public IMap moduleTags;
	
		out.writeObject(new FSTSerializableIValue(ex.moduleTags));

		// public IMap symbol_definitions;

		out.writeObject(new FSTSerializableIValue(ex.symbol_definitions));

		// public ArrayList<Function> functionStore;
		// public Map<String, Integer> functionMap;

		out.writeObject(ex.functionStore);

		// public ArrayList<Type> constructorStore;

		n = ex.constructorStore.size();
		out.writeObject(n);

		for(int i = 0; i < n; i++){
			//typeserializer.writeType(stream, ex.constructorStore.get(i));
			out.writeObject(new FSTSerializableType(ex.constructorStore.get(i)));
		}

		// public Map<String, Integer> constructorMap;

		out.writeObject(ex.constructorMap);

		// public ArrayList<OverloadedFunction> overloadedStore;

		out.writeObject(ex.overloadedStore);

		// public Map<String, Integer> resolver;

		out.writeObject(ex.resolver);

		// ArrayList<String> initializers;

		out.writeObject(ex.initializers);

		// ArrayList<String> testsuites;

		out.writeObject(ex.testsuites);

		// public String uid_module_init;

		out.writeObject(ex.uid_module_init);

		// public String uid_module_main;

		out.writeObject(ex.uid_module_main);

		// public String uid_module_main_testsuite;

		out.writeObject(ex.uid_module_main_testsuite);
	}


	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo, FSTClazzInfo.FSTFieldInfo referencedBy)
	{
	}

	@SuppressWarnings("unchecked")
	public Object instantiate(@SuppressWarnings("rawtypes") Class objectClass, FSTObjectInput in, FSTClazzInfo serializationInfo, FSTClazzInfo.FSTFieldInfo referencee, int streamPosition) throws ClassNotFoundException, IOException 
	{
		int n;

		// public String name;

		String module_name = (String) in.readObject();

		// public IMap moduleTags;

		IMap moduleTags = (IMap) ((FSTSerializableIValue) in.readObject()).getValue();

		// public IMap symbol_definitions;

		IMap symbol_definitions = (IMap) ((FSTSerializableIValue) in.readObject()).getValue();

		// public ArrayList<Function> functionStore;
		// public  Map<String, Integer> functionMap;

		ArrayList<Function> functionStore = (ArrayList<Function>) in.readObject();

		n = functionStore.size();
		HashMap<String, Integer> functionMap = new HashMap<String, Integer>(n);
		for(int i = 0; i < n; i++){
			functionMap.put(functionStore.get(i).getName(), i);
		}

		// public ArrayList<Type> constructorStore;

		n = (Integer) in.readObject();
		ArrayList<Type> constructorStore = new ArrayList<Type>(n);

		for(int i = 0; i < n; i++){
			constructorStore.add(i, ((FSTSerializableType)in.readObject()).getType());
		}

		// public Map<String, Integer> constructorMap;

		Map<String, Integer> constructorMap = (Map<String, Integer>) in.readObject();

		// public ArrayList<OverloadedFunction> overloadedStore;

		ArrayList<OverloadedFunction> overloadedStore = (ArrayList<OverloadedFunction>) in.readObject();

		// public Map<String, Integer> resolver;

		HashMap<String, Integer> resolver = (HashMap<String, Integer>) in.readObject();

		// ArrayList<String> initializers;

		ArrayList<String> initializers = (ArrayList<String>) in.readObject();

		// ArrayList<String> testsuites;

		ArrayList<String> testsuites = (ArrayList<String>) in.readObject();

		// public String uid_module_init;

		String uid_module_init = (String) in.readObject();

		// public String uid_module_main;

		String uid_module_main = (String) in.readObject();

		// public String uid_module_main_testsuite;

		String uid_module_main_testsuite = (String) in.readObject();

		return new RVMExecutable(module_name, moduleTags, symbol_definitions, functionMap, functionStore, 
								constructorMap, constructorStore, resolver, overloadedStore, initializers, testsuites, 
								uid_module_init, uid_module_main, uid_module_main_testsuite, store, vf);
	
	}
}
