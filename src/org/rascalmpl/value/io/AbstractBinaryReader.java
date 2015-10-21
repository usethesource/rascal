package org.rascalmpl.value.io;

import java.io.IOException;
import java.io.InputStream;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

public abstract class AbstractBinaryReader implements IValueBinaryReader {
	public IValue read(IValueFactory factory, Type type, InputStream stream)
			throws FactTypeUseException, IOException {
		return read(factory, new TypeStore(), type, stream);
	}

	public IValue read(IValueFactory factory, InputStream stream)
			throws FactTypeUseException, IOException {
		return read(factory, new TypeStore(), TypeFactory.getInstance().valueType(), stream);
	}
}
