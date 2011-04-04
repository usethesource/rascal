@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::viz::Chart::barchart

import viz::Figure::Chart;

public void b1(){
  barChart("Sales Prognosis 1", 
                     ["First Quarter", "Second Quarter"],
           [<"2009", [20,              25]>,
            <"2010", [40,              60]>],
            xLabel("Quarters"), 
            yLabel("Sales")
            );
}

public void b2(){ 
  barChart("Sales Prognosis 2",  
                     ["First Quarter", "Second Quarter"],
           [<"2009", [20,              25]>,
            <"2010", [40,              60]>],
            xLabel("Quarters"), 
            yLabel("Sales"),
            dim3()
            );
}

public void b3(){   
  barChart("Sales Prognosis 3",  
                     ["First Quarter", "Second Quarter"],
           [<"2009", [20,              25]>,
            <"2010", [40,              60]>],
            xLabel("Quarters"), 
            yLabel("Sales"),
            dim3(),
            horizontal()
            );
}

public void b4(){   
  barChart("Sales Prognosis 4",  
                     ["First Quarter", "Second Quarter"],
           [<"2009", [20,              25]>,
            <"2010", [40,              60]>],
            xLabel("Quarters"), 
            yLabel("Sales"),
            dim3(),
            stacked()
            );
}
