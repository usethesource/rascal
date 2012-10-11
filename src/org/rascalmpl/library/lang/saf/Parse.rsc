@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}

module lang::saf::Parse

import lang::saf::SAF;
import ParseTree;

public start[Fighter] parse(str src, loc l) = parse(#start[Fighter], src, l);
public start[Fighter] parse(str src) = parse(#start[Fighter], src);
public start[Fighter] parse(loc l) = parse(#start[Fighter], l);
