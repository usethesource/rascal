@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::rascalcore::grammar::definition::Productions
     
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
 
//extend analysis::typepal::TypePal;
import lang::rascalcore::check::TypePalConfig;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Attributes;
import lang::rascalcore::grammar::definition::Names;
extend lang::rascalcore::grammar::definition::Grammar;
import List; 
import Set;
import String;      
import IO;  
import util::Math;
import util::Maybe;
import Message;