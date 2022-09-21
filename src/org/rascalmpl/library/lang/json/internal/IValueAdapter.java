package org.rascalmpl.library.lang.json.io;

import java.io.IOException;

import io.usethesource.vallang.IValue;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class IValueAdapter extends TypeAdapter<IValue> {
	private final boolean compact;
	
	@Override
	public void write(JsonWriter out, IValue value) throws IOException {
		value.accept(new JSONWritingValueVisitor(out, compact));
	}

	@Override
	public IValue read(JsonReader in) throws IOException {
		throw new AssertionError("should not be used");
	}

	public IValueAdapter(boolean compact) {
	   this.compact = compact;
	}
}
