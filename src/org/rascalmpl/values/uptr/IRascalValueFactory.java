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
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

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
	
	static IRascalValueFactory getInstance() {
		return RascalValueFactory.getInstance();
	}
}
