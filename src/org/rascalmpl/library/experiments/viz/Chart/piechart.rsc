@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::viz::Chart::piechart

import viz::Chart;

public void p1(){
 	pieChart("p1", ("a" : 1, "b" : 2, "c" : 10, "z": 50));
}

public void p2(){
 	pieChart("p2", ("a" : 1, "b" : 2, "c" : 10, "z": 50),
 	         subtitle("A very, very, very long subtitle don't you think?")
 	
 	);
}

public void p3(){
 	pieChart("p3", ("a" : 1, "b" : 2, "c" : 10, "z": 50),
 					dim3()
 	);
}

public void p4(){
 	pieChart("p4", ("a" : 1, "b" : 2, "c" : 10, "z": 50),
 					ring()
 	);
}
