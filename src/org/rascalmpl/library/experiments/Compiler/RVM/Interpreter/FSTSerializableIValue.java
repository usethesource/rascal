package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.impl.AbstractValueFactoryAdapter;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.values.uptr.Factory;

import de.ruedigermoeller.serialization.FSTBasicObjectSerializer;
import de.ruedigermoeller.serialization.FSTClazzInfo;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTClazzInfo.FSTFieldInfo;
import de.ruedigermoeller.serialization.FSTObjectOutput;

public class FSTSerializableIValue extends FSTBasicObjectSerializer implements Serializable   {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6704614265562953320L;
	
	private transient static IValueFactory vf;
	private transient static TypeStore store;
	private transient static TypeReifier tr;

	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		vf = vfactory;
		store = ts;
		store.extendStore(Factory.getStore());
		tr = new TypeReifier(vf);
	}
	
	private transient IValue value;
	
	public FSTSerializableIValue(){
		value = null;
	}

	public FSTSerializableIValue(IValue value) {
		//System.out.println("FSTSerializableIValue.new: " + value);
		this.value = value;
	}
	
	IValue getValue(){
		return value;
	}

	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite,
			FSTClazzInfo clzInfo, FSTFieldInfo arg3, int arg4)
					throws IOException {
		StringWriter sw = new StringWriter();
		new StandardTextWriter().write(((FSTSerializableIValue)toWrite).getValue(), sw);
		out.writeObject(sw.toString());
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
			String s = (String) in.readObject();
			//System.out.println("FSTSerializableIValue read string: " + s);
			StringReader sr = new StringReader(s);
			IValue value = new StandardTextReader().read(new RascalValuesValueFactory(), store, TypeFactory.getInstance().valueType(), sr);
			this.value = value;
			res = new FSTSerializableIValue(value);
			res.value = value;
			in.registerObject(res,streamPosition,serializationInfo, referencee);
			//System.out.println("FSTSerializableIValue.instantiate: " + value);
			return res;
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
		public IConstructor constructor(Type constructor, IValue[] children,
				Map<String, IValue> keyArgValues) {
			IConstructor res = specializeType(constructor.getName(), children, keyArgValues);
			return res != null ? res: super.constructor(constructor, children, keyArgValues);
		}

		@Override
		public INode node(String name, IValue[] children,
				Map<String, IValue> keyArgValues) throws FactTypeUseException {
			IConstructor res = specializeType(name, children, keyArgValues);
			return res != null ? res: vf.node(name, children, keyArgValues);
		}

		public IConstructor specializeType(String name, IValue[] children,
				Map<String, IValue> keyArgValues) {
			if(name.equals("type") 
					&& children.length == 2
					&& children[0].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(0))
					&& children[1].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(1))) {

				java.util.Map<Type,Type> bindings = new HashMap<Type,Type>();
				bindings.put(Factory.TypeParam, tr.symbolToType((IConstructor) children[0], (IMap) children[1]));

				return vf.constructor(Factory.Type_Reified.instantiate(bindings), children[0], children[1]);
			}
			return null;
		}
	}
}
