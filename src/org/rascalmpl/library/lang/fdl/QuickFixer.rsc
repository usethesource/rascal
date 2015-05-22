@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl}
module QuickFixer

import Syntax;

import ParseTree;
import IO;

str introduceFeature(Tree input, loc origin) {
	str toBeAddedFeature = getReferenceAt(input, origin);

	input = visit(input) {
		case FeatureDefinitions definitions => [FeatureDefinitions]"<definitions> 
			'	<toBeAddedFeature>: x"
	}
	
	return "<input>";
}

str getReferenceAt(&T<:Tree t, loc l) { 
	visit(t) {
		case FeatureName reference : if (reference@\loc == l) { return "<reference>"; }
	}
  
 	return "";
}

