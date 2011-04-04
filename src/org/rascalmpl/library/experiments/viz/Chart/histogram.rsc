@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::viz::Chart::histogram

import viz::Chart;

public void p1(){

   histogram("p1", [<"a", [10.0, 11.0, 12.0, 20.0, 23.0, 30.0, 40.0, 70.0]>], 6);

}
