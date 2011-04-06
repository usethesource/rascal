@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module box::Latex
import IO;
import SystemAPI;
import box::Box;

public text intro(loc locationIntro) {
   println("INTRO:<locationIntro>");
   return getFileContent(locationIntro.path);
   }
   
public text finish(loc locationEnd) {
    println("FINISH:<locationEnd>");
   return getFileContent(locationEnd.path);
   }
