module experiments::Chart::barchart

import Chart;

public void b1(){
           
  barChart("Sales Prognosis 1",  
           [<"First Quarter", [20, 25]>,
            <"Second Quarter", [40, 60]>],
            domainLabel("Quarters"), 
            rangeLabel("Sales"),
            seriesLabels(["2009", "2010"])
            );
}

public void b2(){
           
  barChart("Sales Prognosis 2",  
           [<"First Quarter", [20, 25]>,
            <"Second Quarter", [40, 60]>],
            domainLabel("Quarters"), 
            rangeLabel("Sales"),
            seriesLabels(["2009", "2010"]),
            dim3()
            );
}

public void b3(){
           
  barChart("Sales Prognosis 3",  
           [<"First Quarter", [20, 25]>,
            <"Second Quarter", [40, 60]>],
            domainLabel("Quarters"), 
            rangeLabel("Sales"),
            seriesLabels(["2009", "2010"]),
            dim3(),
            horizontal()
            );
}