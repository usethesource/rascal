package org.rascalmpl.value.fast;

import org.rascalmpl.value.BaseTestBasicValues;
import org.rascalmpl.value.impl.fast.ValueFactory;

public class TestBasicValues extends BaseTestBasicValues {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp(ValueFactory.getInstance());
	}
}
