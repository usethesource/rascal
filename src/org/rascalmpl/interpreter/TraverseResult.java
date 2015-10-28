/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter;

import org.rascalmpl.value.IValue;

public class TraverseResult {
	/*
	 * TraverseResult contains the value returned by a traversal
	 * and a changed flag that indicates whether the value itself or
	 * any of its children has been changed during the traversal.
	 */

	// TODO: can this be put in the result hierarchy?
		public boolean matched = false;   // Some rule matched;
		public IValue value; 		// Result<IValue> of the 
		public boolean changed = false;   // Original subject has been changed

		public TraverseResult() {
			// TODO Auto-generated constructor stub
		}
		
		public TraverseResult(boolean someMatch, IValue value){
			this.matched = someMatch;
			this.value = value;
			this.changed = false;
		}

		public TraverseResult(IValue value){
			this.matched = false;
			this.value = value;
			this.changed = false;
		}

		public TraverseResult(IValue value, boolean changed){
			this.matched = true;
			this.value   = value;
			this.changed = changed;
		}
		public TraverseResult(boolean someMatch, IValue value, boolean changed){
			this.matched = someMatch;
			this.value   = value;
			this.changed = changed;
		}

}
