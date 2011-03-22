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
