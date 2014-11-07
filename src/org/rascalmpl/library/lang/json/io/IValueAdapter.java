package org.rascalmpl.library.lang.json.io;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IValue;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class IValueAdapter extends TypeAdapter<IValue> {
	@Override
	public void write(JsonWriter out, IValue value) throws IOException {
		value.accept(new JSONWritingValueVisitor(out));
	}

	@Override
	public IValue read(JsonReader in) throws IOException {
		throw new AssertionError("should not be used");
	}

	

	
}
