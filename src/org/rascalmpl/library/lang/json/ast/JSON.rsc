@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::json::ast::JSON

data Value 
	= null() 
	| object(map[str memberName, Value memberValue] members) 
	| array(list[Value] values) 
	| integer(int n)
	| float(real r)
	| string(str s) 
	| boolean(bool b)
	;
