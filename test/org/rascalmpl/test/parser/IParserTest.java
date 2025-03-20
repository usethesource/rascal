/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.parser;

import java.io.IOException;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;

public interface IParserTest{
	public final static IValueFactory VF = ValueFactoryFactory.getValueFactory();

	ITree executeParser();
	
	IValue getExpectedResult() throws IOException;
}
