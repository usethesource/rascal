/*******************************************************************************
 * Copyright (c) 2009-2025 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Davy Landman  - Davy.Landman@cwi.nl - CWI
 *   * Jurgen Vinju - Jurgem.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.infrastructure;

import org.jline.terminal.impl.DumbTerminal;
import org.rascalmpl.debug.IRascalMonitor;

import java.io.IOException;
import java.io.InputStream;

public class RascalJunitConsoleMonitor {
    private static class InstanceHolder {
        static IRascalMonitor sInstance;
       
        static {
            try {
                sInstance = IRascalMonitor.buildConsoleMonitor(new DumbTerminal(InputStream.nullInputStream(), System.out));
            }
            catch (IOException e) {
                throw new IllegalStateException("Could not create a terminal representation");
            }
        }
    }

    public static IRascalMonitor getInstance() {
        return InstanceHolder.sInstance;
    }
}
