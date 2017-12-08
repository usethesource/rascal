/*******************************************************************************
 * Copyright (c) 2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.uptr;

import java.util.Map;

import org.rascalmpl.parser.gtd.util.ArrayList;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

/**
 * See {@link RascalValueFactory} for documentation.
 */
public interface IRascalValueFactory extends IValueFactory {
	IConstructor reifiedType(IConstructor symbol, IMap definitions);
	
	ITree appl(Map<String,IValue> annos, IConstructor prod, IList args);
	ITree appl(IConstructor prod, IList args);
	ITree appl(IConstructor prod, IValue... args);
	@Deprecated IConstructor appl(IConstructor prod, ArrayList<ITree> args);
	
	ITree cycle(IConstructor symbol, int cycleLength);

	ITree amb(ISet alternatives);
	
	ITree character(int ch);
	
	ITree character(byte ch);
	
	IConstructor grammar(IMap rules);
	
	static IRascalValueFactory getInstance() {
		return RascalValueFactory.getInstance();
	}
}
