package org.rascalmpl.test.infrastructure;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RecursiveTest {
	public abstract String[] value();
}
