/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.core.uri;

import java.io.IOException;
import java.nio.charset.Charset;

import io.usethesource.vallang.ISourceLocation;

public interface ISourceLocationInputOutput extends 
        ISourceLocationInput,
		ISourceLocationOutput {

	@Override
	default Charset getCharset(ISourceLocation uri) throws IOException {
		return ISourceLocationOutput.super.getCharset(uri);
	}
}
