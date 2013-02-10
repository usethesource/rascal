@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl}

module Plugin

import lang::dot::\syntax::Dot;

import util::IDE;
import ParseTree;
import IO;

public void main() {
    println("RegisterLanguage");
	registerLanguage("Dot", "dot", DOT(str input, loc org) {
		return parse(#DOT, input, org);
	});
}