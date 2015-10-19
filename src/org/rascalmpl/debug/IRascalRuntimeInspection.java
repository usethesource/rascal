/*******************************************************************************
 * Copyright (c) 2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 */
package org.rascalmpl.debug;

import java.util.Stack;

import org.rascalmpl.value.ISourceLocation;

public interface IRascalRuntimeInspection {
    /**
     * @return a reflection of the currently paused stack
     */
    Stack<IRascalFrame> getCurrentStack();
    
    /**
     * @return the top of the currently paused stack
     */
    IRascalFrame getTopFrame();
    
    /**
     * @return which statement or expression is now being executed (source location)
     */
    ISourceLocation getCurrentPointOfExecution();
    
    /**
     * @return a reflection of the singleton instance of the given module
     */
    IRascalFrame getModule(String name);
}
