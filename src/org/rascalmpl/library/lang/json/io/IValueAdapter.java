package org.rascalmpl.library.lang.json.io;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.ibm.icu.text.DateFormat;

public class IValueAdapter extends TypeAdapter<IValue> {

	public static void main(String[] args) {
		IValueFactory vf = ValueFactoryFactory.getValueFactory();

		INode n = vf.node("\uD83C\uDF5D", vf.integer(34), vf.real(1979.0));
		IWithKeywordParameters<? extends INode> kw = n
				.asWithKeywordParameters();
		n = kw.setParameter("keyword", vf.integer(-1));
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("hello"), vf.integer(43));
		ISet x = vf.set(vf.list(n), w.done());

		IValueAdapter iValueAdapter = new IValueAdapter(x.getType(), vf, new TypeStore());
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(IValue.class,
						iValueAdapter)
				.enableComplexMapKeySerialization()
				// .serializeNulls()
				.setDateFormat(DateFormat.LONG)
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
//				.setPrettyPrinting()
				.setVersion(1.0)
				.create();

		iValueAdapter.setGson(gson);
		
		String json = gson.toJson(x, new TypeToken<IValue>() {}.getType());
		System.out.println(json);
		
		IValue newValue = gson.fromJson(json, IValue.class);
		
		
		System.out.println("Old = " + x);
		System.out.println("New = " + newValue);
		System.out.println(newValue.isEqual(x));
		
		
		Object obj = gson.fromJson("[1,2,3]", Object.class);
		System.out.println(obj);
	}

	private final Type type;
	private final IValueFactory vf;
	private final TypeStore ts;
	private Gson gson;

	public IValueAdapter(Type type, IValueFactory vf, TypeStore ts) {
		this.type = type;
		this.vf = vf;
		this.ts = ts;
	}
	
	public void setGson(Gson gson) {
		this.gson = gson;
	}

	@Override
	public void write(JsonWriter out, IValue value) throws IOException {
		value.accept(new JSONWritingValueVisitor(out));
	}

	@Override
	public IValue read(JsonReader in) throws IOException {
		return null;
		//return JSONReadingTypeVisitor.read(gson, in, vf, ts, type);
	}

	

	
}
