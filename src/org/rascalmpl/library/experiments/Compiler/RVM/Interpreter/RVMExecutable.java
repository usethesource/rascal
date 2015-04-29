package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.uri.CompressedStreamResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.uptr.Factory;

public class RVMExecutable implements Serializable{

	private static final long serialVersionUID = -8966920880207428792L;
	
	// transient fields
	transient static IValueFactory vf;
	transient static TypeStore store;
	transient private static TypeSerializer typeserializer;
	
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
		typeserializer = new TypeSerializer(ts); //new TypeSerializer(TypeFactory.getInstance());	//
	}
	
	
	private void writeObject(java.io.ObjectOutputStream stream)
			throws IOException {
		int n;
		
		// public String module_name;
		
		stream.writeObject(module_name);
		
		// public IMap moduleTags;
		
		stream.writeObject(new SerializableRascalValue<IMap>(moduleTags));
		
		// public IMap symbol_definitions;
		
		stream.writeObject(new SerializableRascalValue<IMap>(symbol_definitions));
		
		// public ArrayList<Function> functionStore;
		// public Map<String, Integer> functionMap;
		
		stream.writeObject(functionStore);

		// public ArrayList<Type> constructorStore;
		
		n = constructorStore.size();
		stream.writeObject(n);
		
		for(int i = 0; i < n; i++){
			typeserializer.writeType(stream, constructorStore.get(i));
		}
		
		// public Map<String, Integer> constructorMap;
		
		stream.writeObject(constructorMap);
		
		// public ArrayList<OverloadedFunction> overloadedStore;
		
		stream.writeObject(overloadedStore);
		
		// public Map<String, Integer> resolver;
		
		stream.writeObject(resolver);
		
		// ArrayList<String> initializers;
		
		stream.writeObject(initializers);
		
		// ArrayList<String> testsuites;
		
		stream.writeObject(testsuites);
		
		// public String uid_module_init;
		
		stream.writeObject(uid_module_init);
		
		// public String uid_module_main;
		
		stream.writeObject(uid_module_main);
		
		// public String uid_module_main_testsuite;
		
		stream.writeObject(uid_module_main_testsuite);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream) throws ClassNotFoundException, IOException{
		int n;
		
		// public String name;
		
		module_name = (String) stream.readObject();
		
		// public IMap moduleTags;
		
		moduleTags = ((SerializableRascalValue<IMap>) stream.readObject()).getValue();
		
		// public IMap symbol_definitions;
		
		symbol_definitions = ((SerializableRascalValue<IMap>) stream.readObject()).getValue();
		
		// public ArrayList<Function> functionStore;
		// public  Map<String, Integer> functionMap;
		
		functionStore = (ArrayList<Function>) stream.readObject();
		
		n = functionStore.size();
		functionMap = new HashMap<String, Integer>(n);
		for(int i = 0; i < n; i++){
			functionMap.put(functionStore.get(i).getName(), i);
		}
			
		// public ArrayList<Type> constructorStore;

		n = (Integer) stream.readObject();
		constructorStore = new ArrayList<Type>(n);
		
		for(int i = 0; i < n; i++){
			constructorStore.add(i, typeserializer.readType(stream));
		}
		
		// public Map<String, Integer> constructorMap;
		
		constructorMap = (Map<String, Integer>) stream.readObject();
		
		// public ArrayList<OverloadedFunction> overloadedStore;
		
		overloadedStore = (ArrayList<OverloadedFunction>) stream.readObject();
		
		// public Map<String, Integer> resolver;
		
		resolver = (HashMap<String, Integer>) stream.readObject();
		
		// ArrayList<String> initializers;
		
		initializers = (ArrayList<String>) stream.readObject();
		
		// ArrayList<String> testsuites;
		
		testsuites = (ArrayList<String>) stream.readObject();
		
		// public String uid_module_init;
		
		uid_module_init = (String) stream.readObject();
		
		// public String uid_module_main;
		
		uid_module_main = (String) stream.readObject();
		
		// public String uid_module_main_testsuite;
		
		uid_module_main_testsuite = (String) stream.readObject();
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
		SerializableRascalValue.initSerialization(vf, typeStore);
		Function.initSerialization(vf, typeStore);
		CodeBlock.initSerialization(vf, typeStore);
		
		try {
			ISourceLocation compOut = compressedLoc(rvmExecutable);
			fileOut = URIResolverRegistry.getInstance().getOutputStream(compOut, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			long before = Timing.getCpuTime();
			out.writeObject(this);
			fileOut.close();
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
		typeserializer = new TypeSerializer(typeStore); 
		SerializableRascalValue.initSerialization(vf, typeStore);
		Function.initSerialization(vf, typeStore);
		CodeBlock.initSerialization(vf, typeStore);
		
		try {
			ISourceLocation compIn = compressedLoc(rvmExecutable);
			InputStream fileIn = URIResolverRegistry.getInstance().getInputStream(compIn);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			long before = Timing.getCpuTime();
			executable = (RVMExecutable) in.readObject();
			in.close();
			fileIn.close();
			System.out.println("Reading: " + compIn.getPath() + " [" +  (Timing.getCpuTime() - before)/1000000 + " msec]");
		} catch (IOException i) {
			i.printStackTrace();

		} catch (ClassNotFoundException c) {
			System.out.println("Class not found: " + c.getMessage());
			c.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
