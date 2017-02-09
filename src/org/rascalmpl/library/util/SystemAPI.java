/*******************************************************************************
 * Copyright (c) 2009-2017 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.util;

import java.util.Map.Entry;

import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class SystemAPI {
	private final IValueFactory values;

	public SystemAPI(IValueFactory values) {
		this.values = values;
		
	}
	
	public IValue getSystemProperty(IString v) {
		return values.string(System.getProperty(v.getValue()));
	}

	public IMap getSystemProperties() {
	    IMapWriter w = values.mapWriter();
	    
	    for (Entry<Object, Object> p : System.getProperties().entrySet()) {
	        w.put(values.string(p.getKey().toString()), values.string(p.getValue().toString()));
	    }
	    
	    return w.done();
	}
	
	public IMap getSystemEnvironment() {
        IMapWriter w = values.mapWriter();
        
        for (Entry<String, String> p : System.getenv().entrySet()) {
            w.put(values.string(p.getKey()), values.string(p.getValue()));
        }
        
        return w.done();
    }
	
}
