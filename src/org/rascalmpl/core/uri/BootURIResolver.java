/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Paul Klint  - Paul.Klint@cwi.nl - CWI
*******************************************************************************/

package org.rascalmpl.core.uri;

import org.rascalmpl.core.uri.libraries.ClassResourceInput;

public class BootURIResolver extends ClassResourceInput {

    public BootURIResolver() {
        super("boot", BootURIResolver.class, "/boot");
    }

}
