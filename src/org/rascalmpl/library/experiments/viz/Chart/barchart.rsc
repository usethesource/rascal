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