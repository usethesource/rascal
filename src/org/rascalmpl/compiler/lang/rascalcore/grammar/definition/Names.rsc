@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascalcore::grammar::definition::Names

import lang::rascalcore::check::ATypeBase;
//import lang::rascalcore::grammar::definition::Grammar;


public str unescape(str name) {
  if (/\\<rest:.*>/ := name) return rest; 
  return name;
}
