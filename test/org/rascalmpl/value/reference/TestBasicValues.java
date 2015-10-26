package org.rascalmpl.value.reference;

import org.rascalmpl.value.BaseTestBasicValues;
import org.rascalmpl.value.impl.reference.ValueFactory;

public class TestBasicValues extends BaseTestBasicValues {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp(ValueFactory.getInstance());
	}
}
