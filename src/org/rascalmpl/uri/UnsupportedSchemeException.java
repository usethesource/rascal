/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.uri;

import java.io.IOException;

public class UnsupportedSchemeException extends IOException {
	private static final long serialVersionUID = -6623574531009224681L;
	
	public UnsupportedSchemeException(String scheme) {
		super("Unsupported scheme '" + scheme + "'");
	}
}
