@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::logic::\syntax::Truth

extend lang::std::Whitespace;
extend lang::std::Layout;
extend lang::std::Comment;
  
// True only accepts true Boolean formulas              
syntax True 
  = "true"
  | bracket "(" True ")"
  | "not" False f
  > left True lt "and" True rt
  > left ( True lt "or" True rt
         | False lf "or" True rt
         | True lt "or" False rf
         )
  ;
  
// False only accepts false Boolean formulas         
syntax False 
  = "false"
  | bracket "(" False ")"
  | "not" True t
  > left ( False lf "and" False rf
         | True  lt "and" False rf
         | False lf "and" True  rt
         )
  > False lf "or" False  rf
  ;
  
