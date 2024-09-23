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

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;

public interface IParserTest{
	public final static IValueFactory VF = ValueFactoryFactory.getValueFactory();

	@SafeVarargs
	public static AbstractStackNode<IConstructor>[] createExpectArray(IConstructor prod, AbstractStackNode<IConstructor>... nodes) {
		@SuppressWarnings({"unchecked", "cast"})
		AbstractStackNode<IConstructor>[] expectArray = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[nodes.length];
		
		int index = 0;
		for (AbstractStackNode<IConstructor> node : nodes) {
			expectArray[index] = node;
			node.setProduction(expectArray);
			index++;
		}

		expectArray[index-1].setAlternativeProduction(prod);

		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{expectArray[0]};
	}
	
	ITree executeParser();
	
	IValue getExpectedResult() throws IOException;
}
