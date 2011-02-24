module vis::examples::Chart

import vis::Figure;
import vis::Render;
import vis::Chart;

import Number;
import List;
import Set;
import IO;

// Simple bar charts

public void bar1(){
    dt1 = [10, 12, 17, 0, 15, 7, 20, 40, 60];  
    colors = colorScale(dt1, color("blue"), color("red"));  
	b = hcat( [ box([height(d * 8), fillColor(colors(d))]) | d <- dt1 ],	
                lineColor(0),
                lineWidth(1),
	            fillColor(125),
	            width(10),
	            bottom()
               );
    render(b);
}

public void bar2(){
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [ hcat( [ box(fillColor("green"), height((d1[i] ? 0) * 8)),
                     box(fillColor("red"), height((d2[i] ? 0) * 8))
                   ],
                   gap(5), top())
           | int i <- [0 .. m]
           ];
    
	b = hcat(bars,
             lineColor(0),
             lineWidth(1),
	         width(10),
	         top(),
	         gap(10)
             );
    render(b);
}

public void bar3(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [vcat( [ box(fillColor("green"), height((d1[i] ? 0) * 8)),
                    box(fillColor("red"), height((d2[i] ? 0) * 8))
                  ],
                  gap(0))
           | int i <- [0 .. m]
           ];
           
	b = hcat(bars, 
             lineColor(0),
             lineWidth(1),
	         width(10),
	         gap(5),
	         bottom()
            );
    render(b);
}

// Barchart: Horizontal composition of vertically stacked boxes
public void bar4(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [vcat( [ box(fillColor("green"), height((d1[i] ? 0) * 8)),
                    box(fillColor("red"), height((d2[i] ? 0) * 8))
                  ],
                  gap(0))
           | int i <- [0 .. m]
           ];
           
	b = hcat(bars,
             lineColor(0),
             lineWidth(1),
	         width(10),
	         gap(5),
	         bottom()
            );
    render(b);
}



private list[NamedPairSeries] pdata =
        [ <"f", [<0, 50>, <10,50>, <20,50>, <30, 50>, <40, 50>, <50, 50>, <60,50>]>, 
          <"g", [<50,0>, <50,50>, <50,100>]>,
          <"h", [<0,0>, <10,10>, <20,20>, <30,30>, <40,40>, <50,50>, <60,60>]>,
          <"i", [<0, 60>, <10, 50>, <20, 40>, <30, 30>, <40, 20>, <50, 10>, <60, 0>]>,
          <"j", [< -20, 20>, < -10, 10>, <0,0>, <10, -10>, <20, -20>]>,
          <"k", [< -20, 40>, < -10, 10>, <0, 0>, <10, 10>, <20, 40>, <30, 90>]>                
        ];
        
public void p0(){
     mydata = [
               <"h", [<0,0>, <10,10>, <20,20>, <30,30>, <40,40>, <50,50>, <60,60>]>
               ];
     render(xyChart("P0", 
	                 mydata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis")
                  )
           );
}

// Scatter plot

public void p1(){
	render(xyChart("P1", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis")
                  )
           );
}

// Line plot

public void p2(){
	render(xyChart("P2", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis"),
	                 linePlot()
                  )
           );
}

// Curve plot

public void p3(){
	render(xyChart("P3", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis"),
	                 linePlot(), curvePlot()
                  )
           );
}

// Line/area plot

public void p4(){
	render(xyChart("Test Title P4", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis"),
	                 linePlot(), areaPlot()
                  )
           );
}

// Curve/area plot

public void p5(){
	render(xyChart("Test Title P5", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis"),
	                 linePlot(), curvePlot(), areaPlot()
                  )
           );
}

public void b0(){
  render(barChart("Sales Prognosis 0", [<"a", 10>, <"b", 20>, <"c", 30>],
                 xLabel("Item"), 
                  yLabel("Value")
            ));
}


public void b1a(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20]>,
                    <"2010", [40]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales")
            ));
}

public void b1(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20,              25]>,
                    <"2010", [40,              60]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales")
            ));
}

public void b2(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20,              25]>,
                    <"2010", [40,              60]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales"),
                  stackedBars()
            ));
}

public void b3(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20,              25]>,
                    <"2010", [40,              60]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales"),
                  horizontal()
            ));
}

public void b4(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20,              25]>,
                    <"2010", [40,              60]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales"),
                  stackedBars(),
                  horizontal()
            ));
}

// pieCharts

public void pie0(){
 	render(pieChart("pie0", ("a" : 1, "b" : 1, "c" : 1, "z": 1)));
}

public void pie1(){
 	render(pieChart("pie1", ("a" : 1, "b" : 2, "c" : 10, "z": 50)));
}

public void pie2(){
 	render(pieChart("pie2", ("a" : 1, "b" : 2, "c" : 10, "z": 50),
 	         subTitle("A very, very, very long subtitle dont you think?"))
 	
 	);
}

public void pie3(){
 	render(pieChart("pie3", ("a" : 1, "b" : 2, "c" : 10, "z": 50),
 					ring(20)
 	));
}