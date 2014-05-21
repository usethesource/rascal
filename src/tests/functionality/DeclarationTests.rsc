module tests::functionality::DeclarationTests
/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/

   public test bool localTypeInferenceBottomScope() {
		x = 1;
		return x == 1;
	}
	
	public test bool localTypeInferenceNestedScope() {
		return { x = 1; x == 1; }
	}
	
	public test bool localTypeInferenceNoEscape() {
		{ x = 1; x == 1; }
	    x = "1"; return x == "1";
	}
