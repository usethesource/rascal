package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.impl.AbstractValueFactoryAdapter;
import org.rascalmpl.value.io.BinaryValueReader;
import org.rascalmpl.value.io.BinaryValueWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

import org.nustaq.serialization.FSTBasicObjectSerializer;
import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTClazzInfo.FSTFieldInfo;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

/**
 * FSTSerializableIValue acts as a serializer and wrapper for IValues
 * - On writing a FSTSerializableIValue oject is written
 * - On reading, the wrapped IValue is returned.
 *
 */
@SuppressWarnings("deprecation")
public class FSTSerializableIValue extends FSTBasicObjectSerializer implements Serializable   {
	
	private static final long serialVersionUID = 6704614265562953320L;
	
	private transient static IValueFactory vf;
	private transient static TypeStore store;
	private transient static TypeReifier tr;
	private transient static ByteArrayOutputStream byteStream;

	private transient static BinaryValueWriter binaryWriter;

	private transient static BinaryValueReader binaryReader;

	private transient static Type valueType;

	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		vf = vfactory;
		store = ts;
		store.extendStore(RascalValueFactory.getStore());
		tr = new TypeReifier(vf);
		byteStream = new ByteArrayOutputStream();	// TODO should we remove it as well?
		binaryWriter = new BinaryValueWriter();
		binaryReader = new BinaryValueReader();
		valueType = TypeFactory.getInstance().valueType();
	}
	
	private transient IValue value;
	
	/**
	 *  Constructor used for registration of this serializer
	 */
	public FSTSerializableIValue() {
	}


	/**
	 * Constructor used to wrap an IValue to be serialized
	 */
	public FSTSerializableIValue(IValue value) {
		this.value = value;
	}
	
	IValue getValue(){
		return value;
	}

	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite,
			FSTClazzInfo clzInfo, FSTFieldInfo arg3, int arg4)
					throws IOException {
		byteStream.reset();
		binaryWriter.write(((FSTSerializableIValue)toWrite).getValue(), byteStream, false);
		byte[] bytes = byteStream.toByteArray();
		out.writeObject(bytes);
	}

	@Override
	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo, FSTClazzInfo.FSTFieldInfo referencedBy)
	{
			System.out.println("FSTSerializableIValue.readObject");
	}

	@Override
	public Object instantiate(@SuppressWarnings("rawtypes") Class objectClass, FSTObjectInput in, FSTClazzInfo serializationInfo, FSTClazzInfo.FSTFieldInfo referencee, int streamPosition) 
	{		
		FSTSerializableIValue res = null;
		try {
			byte[] bytes = (byte[]) in.readObject();
			ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
			
			IValue v = binaryReader.read(new RascalValuesValueFactory(), store, valueType, bs);
			bs.close();
			
			// TODO: why is this not handled by RascalValuesValueFactory????
			if(v.getType().isNode()){
				INode nd = (INode) v;
				if(nd.getName().equals("type")){
					//System.out.println("FSTSerializableIValue.instantiate: " + v);
					java.util.Map<Type,Type> bindings = new HashMap<Type,Type>();
					bindings.put(RascalValueFactory.TypeParam, tr.symbolToType((IConstructor) nd.get(0), (IMap) nd.get(1)));
					
					v = vf.constructor(RascalValueFactory.Type_Reified.instantiate(bindings), nd.get(0), nd.get(1));
				}
			}
			in.registerObject(v,streamPosition,serializationInfo, referencee);
			return v;
		} catch (FactTypeUseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private class RascalValuesValueFactory extends AbstractValueFactoryAdapter {
		public RascalValuesValueFactory() {
			super(vf);
		}
		
		@Override
		public INode node(String name, IValue... children) {
			
//			System.out.println("node1: " + name);
			IConstructor res = specializeType(name, children);
			
			return res != null ? res: vf.node(name, children);
		}
		
		@Override
		public INode node(String name, Map<String, IValue> annotations, IValue... children) throws FactTypeUseException {
			
//			System.out.println("node2: " + name);
			IConstructor res = specializeType(name, children);
			
			return res != null ? res: vf.node(name, annotations, children);
		}
		
		@Override
		public INode node(String name, IValue[] children, Map<String, IValue> keyArgValues) throws FactTypeUseException {
//			System.out.println("node3: " + name);
			IConstructor res = specializeType(name, children);
			
			return res != null ? res: vf.node(name, children, keyArgValues);
		}

		private IConstructor specializeType(String name, IValue... children) {
			if ("type".equals(name) 
					//&& children.length == 2
					//&& children[0].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(0))
					//&& children[1].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(1))
					) {
				java.util.Map<Type,Type> bindings = new HashMap<Type,Type>();
				bindings.put(RascalValueFactory.TypeParam, tr.symbolToType((IConstructor) children[0], (IMap) children[1]));
				
				return vf.constructor(RascalValueFactory.Type_Reified.instantiate(bindings), children[0], children[1]);
			}
			
			return null;
		}
		
		@Override
		public IConstructor constructor(Type constructor, IValue[] children,
				Map<String, IValue> keyArgValues) {
			System.out.println("constructor1: " + constructor.getName());
			IConstructor res = specializeType(constructor.getName(), children);
			return res != null ? res: super.constructor(constructor, children, keyArgValues);
		}
		
		 @Override
		  public IConstructor constructor(Type constructor, Map<String, IValue> annotations, IValue... children)
		      throws FactTypeUseException {
			 System.out.println("constructor2: " + constructor.getName());
			 IConstructor res = specializeType(constructor.getName(), children);
			 return res != null ? res: super.constructor(constructor, children, annotations);
		  }
//	
//
//		@Override
//		public INode node(String name, IValue[] children,
//				Map<String, IValue> keyArgValues) throws FactTypeUseException {
//			System.out.println("node: " + name);
//			IConstructor res = specializeType(name, children, keyArgValues);
//			return res != null ? res: vf.node(name, children, keyArgValues);
//		}
//
//		public IConstructor specializeType(String name, IValue[] children,
//				Map<String, IValue> keyArgValues) {
//			System.out.println("specializeType: " + name);
//			if(name.equals("type") 
//					&& children.length == 2
//					&& children[0].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(0))
//					&& children[1].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(1))) {
//
//				java.util.Map<Type,Type> bindings = new HashMap<Type,Type>();
//				bindings.put(Factory.TypeParam, tr.symbolToType((IConstructor) children[0], (IMap) children[1]));
//
//				return vf.constructor(Factory.Type_Reified.instantiate(bindings), children[0], children[1]);
//			}
//			return null;
//		}
	}
	
	static private final FSTSerializableIValue serializableValue;
	
	static private final FSTSerializableType serializableType;
	
	static {
		// set up FST serialization in gredients that will be reused across read/write calls

		// PDB Types
		serializableType = new FSTSerializableType();
		serializableValue = new FSTSerializableIValue();
	} 
	
	/**
	 * Create an FSTConfiguration depending on the used extension: ".json" triggers the JSON reader/writer.
	 * Note: the JSON version is somewhat larger and slower but is usefull for recovery during bootstrapping incidents.
	 * @param source or desination of executable
	 * @return an initialized FSTConfiguration
	 */
	private static FSTConfiguration makeFSTConfig(ISourceLocation path){
		FSTConfiguration config = path.getURI().getPath().contains(".json") ?
				FSTConfiguration.createJsonConfiguration() : FSTConfiguration.createDefaultConfiguration(); 
		config.registerSerializer(FSTSerializableType.class, serializableType, false);
		config.registerSerializer(FSTSerializableIValue.class, serializableValue, false);
		return config;
	}
	
	public static void main(String[] args) throws IOException {

		int N = 100000;

		OutputStream fileOut;

		TypeStore typeStore = RascalValueFactory.getStore();
		TypeFactory tf = TypeFactory.getInstance();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();

		FSTSerializableIValue.initSerialization(vf, typeStore);

		ISourceLocation fileLoc = null;
		try {
			fileLoc = vf.sourceLocation("home", "", "file.fst");
		} catch (URISyntaxException e) {
			System.err.println("Cannot create default location: " + e.getMessage());
		}

		fileOut = URIResolverRegistry.getInstance().getOutputStream(fileLoc, false);
		FSTObjectOutput out = new FSTObjectOutput(fileOut, makeFSTConfig(fileLoc));

		ISetWriter w = vf.setWriter();
		w.insert(vf.string("abc"));
		w.insert(vf.integer(42));
		
		Type adt = tf.abstractDataType(typeStore, "D");
		Type fcons = tf.constructor(typeStore, adt, "f", tf.integerType(), "n");
		
		HashMap<String,IValue> kwParams = new HashMap<>();
		kwParams.put("zzz", vf.string("pqr"));
		
		IValue start = vf.constructor(fcons, vf.integer(42)).asWithKeywordParameters().setParameters(kwParams);
		
		
		long startTime = Timing.getCpuTime();

		FSTSerializableIValue startFST = new FSTSerializableIValue(start);

		for(int i = 0; i < N; i++){
			out.writeObject(startFST);
		}
		out.close();

		long endWrite = Timing.getCpuTime();

		System.out.println("Writing " + N + " values " +  (endWrite - startTime)/1000000 + " msec");


		FSTSerializableIValue.initSerialization(vf, typeStore);

		FSTObjectInput in = null;
		IValue outVal = start;
		try {
			ISourceLocation compIn = fileLoc;
			InputStream fileIn = URIResolverRegistry.getInstance().getInputStream(compIn);
			in = new FSTObjectInput(fileIn, makeFSTConfig(fileLoc));
			for(int i = 0; i < N; i++){
				outVal = (IValue) in.readObject(IValue.class);
				//System.out.println(outVal);
			}
			in.close();
			in = null;

		} catch (ClassNotFoundException c) {
			throw new IOException("Class not found: " + c.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
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
		long endRead = Timing.getCpuTime();
		System.out.println(outVal);
		System.out.println("Reading " + N + " value " +  (endRead - endWrite)/1000000 + " msec");


	}
}
