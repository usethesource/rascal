@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::viz::Chart::xychart

import viz::Chart;

public void p1(){

	xyChart("test", [<"f", [1,1, 2,2, 3, 5]>, 
                     <"g", [2,50, 5,100] >],
                     domainLabel("X-axis"),
                     rangeLabel("Y-axis")
           );
}

public void p2(){

	xyChart("test", [<"f", [1,1, 2,2, 3,5]>, 
                     <"g", [2,50, 5,100] >],
                     area(),
                     domainLabel("X-axis"),
                     rangeLabel("Y-axis")
           );
}

public void p3(){

	xyChart("test", [<"f", [1,1, 2,2, 3,5]>, 
                     <"g", [2,50, 5,100] >],
                     scatter(),
                     domainLabel("X-axis"),
                     rangeLabel("Y-axis")
           );
}
