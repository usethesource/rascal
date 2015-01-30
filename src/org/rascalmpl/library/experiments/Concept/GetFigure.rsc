@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module experiments::Concept::GetFigure

import analysis::formalconcepts::FCA;
import experiments::Concept::Types;
import IO;
import vis::Figure; 

public Figure getFigure(loc f, str layout_alg) {
   property_table vb = readCxt(f);
   println("getFigure:<layout_alg>");
   // return use(createLatticeFigure(vb, layout_alg), [height(600), width(600)]);
   return createLatticeFigure(vb, layout_alg);
   }
