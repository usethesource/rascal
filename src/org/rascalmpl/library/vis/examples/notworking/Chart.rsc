@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::Chart

import vis::Figure;
import vis::Render;
import vis::Chart;

import Number;
import List;

import IO;
import Real;
import util::Math;




public void nominalKeyTest(){
	render(
		vcat([
			hcat( [ box(text(n),fillColor(convert(t,"type"))) | <n,t> <- 
				[<"equals","public">,<"intersects","protected">,<"toString","public">,<"getPassWord","private">,<"union","protected">]]) 
			, palleteKey("Types","type")])
				);
}

public Figure hBarChart(list[tuple[str,num]] vals,FProperty props...){
	return bottomScreen("l",leftAxis("y",
			hcat([box(height(convert(n,"y")),project(text(s),"l")) | <s,n> <- vals],hgrow(1.2),hcapGaps(true))
		),props);
}


public void testBarChart(){
	render(title("Coolness of programming languages",
		hBarChart([<"Rascal",140>,<"Java",50>,<"C",70>,<"C++",22>,<"Haskell",100>,<"PHP",1>,<"Scala",90>],stdFillColor(color("red",0.8)))));
}

public Figure hGroupedStackedBarChart(list[tuple[str,list[tuple[str,list[tuple[str,num]]]]]] vals,FProperty props...){
	return vcat([
			bottomScreen("group",
			bottomScreen("elem",
				leftAxis("y",
			 		hcat([
			   			hcat([
			   				vstack([box(height(convert(n,"y")),fillColor(convert(sub,"sub"))) | <sub,n> <- elem]
			   				,project(text(elemLabel),"elem"))
			   			| <elemLabel,elem> <- e],project(text(group,fontSize(17)),"group"),hgrow(1.05))
			   		| <group,e> <- vals ],hgrow(1.2),hcapGaps(true))
			   	)
			)
		,vshrink(0.8)),
		box(palleteKey("Components","sub"))
		],props)
		;
}


public void testGSBarChart(){
	render(title("Detailed Coolness of programming languages",
		hGroupedStackedBarChart(
			[<"Functional languages",
				[<"Clean",[<"type system",100.0>, <"syntax",60.0>,<"speed",40>]>,
				 <"Haskell",[<"type system",120>, <"syntax",70>,<"speed",60>]>,
				 <"Agda",[<"type system",160>, <"syntax",10>,<"speed",5>]>
				]>,
			<"Imperative languages",
				[<"C",[<"type system",40>, <"syntax",60>,<"speed",100>]>,
				 <"C++",[<"type system",70>, <"syntax",20>,<"speed",95>]>,
				 <"Java",[<"type system",40>, <"syntax",40>,<"speed",50>]>
				]>,
			<"Semi-functional languages",
				[<"Rascal",[<"type system",90>, <"syntax",200>,<"speed",10>]>,
				 <"Scala",[<"type system",100>, <"syntax",50>,<"speed",50>]>,
				 <"Erlang",[<"type system",40>, <"syntax",40>,<"speed",50>]>
				]>
			]
		,vshrink(0.9))));	
}

public void graph(int n){
	render(
		 title("Graph Demo",
			bottomAxis("Time since epoch","x",
			leftAxis("Lines of code","y",
				overlay([
					ellipse(shrink(0.02),fillColor("blue"),hpos(convert( toReal(x) , "x")),vpos(convert(((x == 0) ? 0 :arbReal()),"y")))
					| x <- [0..n]],shapeConnected(true),shapeCurved(true))
			) )			
		));
}

/*

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
/*
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
*/
