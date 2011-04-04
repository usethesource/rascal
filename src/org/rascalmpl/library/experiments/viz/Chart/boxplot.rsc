@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::viz::Chart::boxplot

import viz::Chart;         

public void p1(){
  D = [ <"series1", [<"Type 0", [1, 5, 4, 3, 5, 5, 6, 5, 6, 5, 10, 20, 30]>, 
                     <"Type 1", [5, 19, 20, 20, 20, 20, 21, 18, 18, 22, 25, 18]>,
                     <"Type 2", [1, 5, 10, 15, 20]>
                    ]>,
           
        <"series2", [<"Type 0", [ 1, 4, 4, 5, 3, 5, 6, 8, 20]>, 
                     <"Type 1", [5, 20, 20, 19, 25, 30]>,
                     <"Type 2", [0,7,7,7,7, 14]>
                    ]>];

  boxplot("BoxPlot P1", D);
}
