@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module box::Tst
import ParseTree;
import IO;
import box::Box;


import languages::tst::syntax::Tst;

public text toList(loc asf){
     PROGRAM a = parse(#PROGRAM, asf);
     println(a);
     println(`->`:=a);
     println(`x`:=a);
     return ["finished"];
     }
