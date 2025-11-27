/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.lang.aterm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIResolverRegistry;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.ATermReader;
import io.usethesource.vallang.io.ATermWriter;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

public class IO {
	private final IValueFactory values;
	
	public IO(IValueFactory values){
		this.values = values;
	}
	
	public IValue readTextATermFile(IValue type, ISourceLocation loc) {
		TypeStore store = new TypeStore();
		Type start = new TypeReifier(values).valueToType((IConstructor) type, store);
		
		try (InputStream in = URIResolverRegistry.getInstance().getInputStream(loc)) {
			return new ATermReader().read(values, store, start, in);
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(e);
		}
	}
	
	public IValue readATermFromFile(IString fileName){
		try (FileInputStream stream = new FileInputStream(fileName.getValue())) {
			return new ATermReader().read(values, stream);
		} 
		catch (FactTypeUseException e) {
			throw RuntimeExceptionFactory.io(e.getMessage());
		} 
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(e);
		}
	}
	
	public void writeTextATermFile(ISourceLocation loc, IValue value){
		try (OutputStream out = URIResolverRegistry.getInstance().getOutputStream(loc, false)) {
			new ATermWriter().write(value, new OutputStreamWriter(out, "UTF8"));
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(e);
		}
	}
}
