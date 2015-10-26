package org.rascalmpl.value.persistent;

import org.rascalmpl.value.BaseTestBasicValues;
import org.rascalmpl.value.impl.persistent.ValueFactory;

public class TestBasicValues extends BaseTestBasicValues {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp(ValueFactory.getInstance());
	}
}
