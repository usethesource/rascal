package org.rascalmpl.value.io;

import java.io.IOException;
import java.io.Reader;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

public abstract class AbstractTextReader implements IValueTextReader {
	public IValue read(IValueFactory factory, Type type, Reader reader)
			throws FactTypeUseException, IOException {
		return read(factory, new TypeStore(), type, reader);
	}

	public IValue read(IValueFactory factory, Reader reader)
			throws FactTypeUseException, IOException {
		return read(factory, new TypeStore(), TypeFactory.getInstance().valueType(), reader);
	}
}
