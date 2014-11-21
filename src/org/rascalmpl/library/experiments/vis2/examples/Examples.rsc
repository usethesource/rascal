module experiments::vis2::examples::Examples

import experiments::vis2::Figure;
import experiments::vis2::FigureServer; 
import IO;

import String;
import List;
import util::Math;
import experiments::vis2::\data::Nederland;
import experiments::vis2::\data::Steden;
import experiments::vis2::vega::VegaChart;

      					  

// ********************** Examples **********************

void ex(str title, Figure f){
	render(title, f);
}

void ex(str title, value model, Figure f){
	render(title, model, f);
}

// single box

public Figure box0 = box();
void tbox0(){ ex("box0", box0); }

public Figure box1 = box(fillColor="red", size=<200,200>);
void tbox1(){ ex("box1", box1); }  

public Figure box2 = box(fillColor="red", size=<200,200>, lineWidth=10);
void tbox2(){ ex("box2", box2); } 

public Figure box3 = box(fillColor="red", lineColor="blue", lineWidth=10, lineDashing= [10,20,10,10], size=<200,200>);
void tbox3(){ ex("box3", box3); }

// Nested box

Figure WB = box(fillColor="white", size=<50,100>);
Figure RB = box(fillColor="red", size=<20,20>);

public Figure box4 =  box(fig=WB, fillColor="blue", size=<200,200>);
void tbox4(){ ex("box4", box4); } 

public Figure box5 = box(fig=WB, fillColor="blue", size=<200,200>, align=topLeft);
void tbox5(){ ex("box5", box5); } 

public Figure box6 = box( fig=WB, fillColor="blue", size=<200,200>, align=topRight);
void tbox6(){ ex("box6", box6); } 

public Figure box7 = box(fig=WB, fillColor="blue", size=<200,200>, align=bottomRight);
void tbox7(){ ex("box7", box7); } 

public Figure box8 = box(fig=WB, fillColor="blue", size=<200,200>, align=bottomLeft);
void tbox8(){ ex("box8", box8); } 

public Figure box9 = box(fig=WB, fillColor="lightblue", size=<200,200>, gap=<10,10>, align=topLeft);
void tbox9(){ ex("box9", box9); } 

public Figure box10 = box(fig=WB, fillColor="lightblue", size=<200,200>, gap=<10,10>, align=topRight);
void tbox10(){ ex("box10", box10); } 

public Figure box11 = box(fig=WB, fillColor="lightblue", size=<200,200>, gap=<10,10>, align=bottomRight);
void tbox11(){ ex("box11", box11); } 

public Figure box12 = box(fig=WB, fillColor="lightblue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox12(){ ex("box12", box12); } 

public Figure box13 = box(fig=box(fig=RB, fillColor="white", size=<50,100>), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox13(){ ex("box13", box13); } 

public Figure box14 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=topLeft), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox14(){ ex("box14", box14); }

public Figure box15 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=topRight), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox15(){ ex("box15", box15); }

public Figure box16 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=bottomRight), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox16(){ ex("box16", box16); }

public Figure box17 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=bottomLeft), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox17(){ ex("box17", box17); }

public Figure box18 = box(fig=RB, fillColor="blue", grow=3);
void tbox18(){ ex("box18", box18); }

public Figure box19 = box(fig=text("Hello", fontSize=20), grow=2);
void tbox19(){ ex("box19", box19); }

public Figure box20 = box(fig=box(lineWidth=20, lineColor="silver", lineOpacity=0.5, size=<200,200>), lineColor="red");
void tbox20(){ ex("box20", box20); }

public Figure box21 = box(fig=box(lineWidth=20, lineColor="silver", lineOpacity=0.5, size=<200,200>), lineColor="red", lineWidth=15);
void tbox21(){ ex("box21", box21); }

void boxes(){
	ex("boxes", grid(gap=<10,10>,
					figArray=[ //[box1, box2, box3],
						       //[box4, box5, box6, box7, box8],
						       [box4, box9, box10, box11, box12],
						       
						       [box13, box14, box15, box16, box17],
						       //[box18, box19]
						       [box20]
						      // [box21]
						  ], align=center));
}

// ellipse

public Figure ellipse1 = ellipse(cx=100, cy=100, rx=100, ry=75);
void tellipse1(){ ex("ellipse1", ellipse1); }

public Figure ellipse2 = ellipse(cx=100, cy=100, rx=100, ry=75, fillColor="red");
void tellipse2(){ ex("ellipse2", ellipse2); }

public Figure ellipse3 = ellipse(cx=100, cy=100, fillColor="red", fig=box(size=<50,80>, fillColor="yellow"));
void tellipse3(){ ex("ellipse3", ellipse3); }

public Figure ellipse4 = box(lineColor="red", fig=ellipse(cx=100, cy=100, rx=100, ry=75, lineWidth=1, lineColor="silver", lineOpacity=0.5));
void tellipse4(){ ex("ellipse4", ellipse4); }

public Figure ellipse5 = box(lineColor="red", fig=ellipse(cx=100, cy=100, rx=100, ry=75, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tellipse5(){ ex("ellipse5", ellipse5); }

public Figure ellipse6 = box(lineColor="red", lineWidth=15, fig=ellipse(cx=100, cy=100, rx=100, ry=75, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tellipse6(){ ex("ellipse6", ellipse6); }

void ellipses() {
   ex("ellipses", vcat(figs=[ellipse1, ellipse2, ellipse3, ellipse4, ellipse5, ellipse6]));
}

// circle

public Figure circle1 = circle(r=100);
void tcircle1(){ ex("circle1", circle1); }

public Figure circle2 = circle(r=100, fillColor="red");
void tcircle2(){ ex("circle2", circle2); }

public Figure circle3 = circle(fillColor="red", fig=box(size=<50,80>, fillColor="yellow"));
void tcircle3(){ ex("circle3", circle3); }

public Figure circle4 = box(lineColor="red", fig=circle(cx=100, cy=100, r=100, lineWidth=1, lineColor="silver", lineOpacity=0.5));
void tcircle4(){ ex("circle4", circle4); }

public Figure circle5 = box(lineColor="red", fig=circle(cx=100, cy=100, r=100, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tcircle5(){ ex("circle5", circle5); }


public Figure circle6 = box(lineColor="red", lineWidth=15, fig=circle(cx=100, cy=100, r=100, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tcircle6(){ ex("circle6", circle6); }

void circles() {
   ex("circles", vcat(figs=[circle1, circle2, circle3, circle4, circle5, circle6]));
}
// ngon

public Figure ngon1 = ngon(n = 3, r=100);
void tngon1(){ ex("ngon1", ngon1); }

public Figure ngon2 = ngon(n = 4, r=100);
void tngon2(){ ex("ngon2", ngon2); }

public Figure ngon3 = ngon(n = 5, r=100);
void tngon3(){ ex("ngon3", ngon3); }

public Figure ngon4 = ngon(n = 6, r=100);
void tngon4(){ ex("ngon4", ngon4); }

public Figure ngon5 = ngon(n = 7, r=100);
void tngon5(){ ex("ngon5", ngon5); }

public Figure ngon6 = ngon(n = 8, r=100);
void tngon6(){ ex("ngon6", ngon6); }

public Figure ngon7 = box(fig= ngon(n = 3, fillColor="red", r = 100));
void tngon7(){	ex("ngon7", ngon7); }

public Figure ngon8 = box(fig= ngon(n = 4, fillColor="red", r = 100));
void tngon8(){	ex("ngon8", ngon8); }

public Figure ngon9 = box(fig= ngon(n = 5, fillColor="red", r = 100));
void tngon9(){	ex("ngon9", ngon9); }

public Figure ngon10 = box(fig= ngon(n = 6, fillColor="red", r = 100));
void tngon10(){	ex("ngon10", ngon10); }

public Figure ngon11 = box(fig= ngon(n = 7, fillColor="red", r = 100));
void tngon11(){	ex("ngon11", ngon11); }

public Figure ngon12 = box(fig= ngon(n = 8, fillColor="red", r = 100));
void tngon12(){	ex("ngon12", ngon12); }

public Figure ngon13 = ngon(n = 3, fillColor="red", fig=box(size=<250,180>, fillColor="yellow"));
void tngon13(){ ex("ngon13", ngon13); }

public Figure ngon14 = ngon(n = 4, fillColor="red", fig=box(size=<250,180>, fillColor="yellow"));
void tngon14(){ ex("ngon14", ngon14); }

public Figure ngon15 = ngon(n = 5, fillColor="red", fig=box(size=<250,180>, fillColor="yellow"));
void tngon15(){ ex("ngon15", ngon15); }

public Figure ngon16 = ngon(n = 6, fillColor="red", fig=box(size=<250,180>, fillColor="yellow"));
void tngon16(){ ex("ngon16", ngon16); }

public Figure ngon17 =  ngon(n = 7, fillColor="red", fig=box(size=<250,180>, fillColor="yellow"));
void tngon17(){ ex("ngon17", ngon17); }

public Figure ngon18 = ngon(n = 10, fillColor="red", fig=box(size=<250,180>, fillColor="yellow"));
void tngon18(){ ex("ngon18", ngon18); }

void ngons(){
  ex("ngons" , grid(gap=<10,10>,
                    figArray=[ 
							[ngon1, ngon2, ngon3, ngon4, ngon5, ngon6],
							[ngon7, ngon8, ngon9, ngon10, ngon11, ngon12],
							[ngon13, ngon14, ngon15, ngon16, ngon17, ngon18]			    
  						 ], align=topLeft));
}

/********************** polygon ******************************/

public Figure polygon1 = polygon(points=[<100,100>, <100,200>, <200,200>]);
void tpolygon1(){ ex("polygon1", polygon1); }

public Figure polygon2 =  polygon(points=[<100,100>, <100,200>, <200,200>], fillColor="red", lineWidth=4, lineDashing=[1,1,1,1,1,1]);
void tpolygon2(){ ex("polygon2", polygon2); }

public Figure polygon3 =  polygon(points=[<100,10>, <40,198>, <190,78>, <10,78>, <160,198>], fillColor="green", lineWidth=4);
void tpolygon3(){ ex("polygon3", polygon3); }

public Figure polygon4 = polygon(points=[<200,10>,<250,190>, <160,210>], fillColor="pink", lineWidth=1);
void tpolygon4(){ ex("polygon4", polygon4); }

public Figure polygon5 = polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=4);
void tpolygon5(){ ex("polygon5", polygon5); }

public Figure polygon6 = box(fig=polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=1));
void tpolygon6(){ ex("polygon6", polygon6); }

public Figure polygon7 =  box(fig=polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=4));
void tpolygon7(){ ex("polygon7", polygon7); }

public Figure polygon8 = box(fig=polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=10));
void tpolygon8(){ ex("polygon8", polygon8); }

public Figure polygon9 = box(lineColor="red", lineWidth=10, fig=polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=10));
void tpolygon9(){ ex("polygon9", polygon9); }

void polygons() {
  ex("polygons",  grid(gap=<10,10>,
  					   figArray=[
							[polygon1, polygon2, polygon3, polygon4, polygon5],
                            [polygon6, polygon7, polygon8, polygon9]
                            ], align=topLeft));
}

/********************** shape ******************************/

public Figure shape1 = shape([line(100,100), line(100,200), line(200,200)], shapeClosed=true);
void tshape1(){	ex("shape1", shape1); }

public Figure shape2 = shape([line(30,100), line(100, 100), line(200,80)], shapeClosed=true);
void tshape2(){ ex("shape2", shape2); }

public Figure shape3 = hcat(figs=[ shape([line(100,100), line(100, 200), line(200,200)], shapeClosed=true, fillColor="red"),
	
							 shape([line(100,100), line(100, 200), line(200,200)], shapeClosed=true, fillColor="blue")
							 ]);
void tshape3(){ ex("shape3", shape3); }

public Figure shape4 = shape([line(0,0), line(50, 50), line(80,50), line(100,0) ], shapeClosed = true,  fillColor = "yellow");
void tshape4(){	ex("shape4", shape4); }

public Figure shape5 = shape([line(0,0), line(50, 50), line(80,50), line(100,0) ], shapeCurved=true, shapeClosed = true, fillColor = "yellow");
void tshape5(){	ex("shape5", shape5); }

public Figure shape6 = box(lineColor="red", fig=shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
					                      shapeClosed=true, fillRule="evenodd", fillColor = "grey"));
void tshape6(){	ex("shape6", shape6); }

public Figure shape7 = box(lineColor="red", lineWidth=10, fig=shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
					                      shapeClosed=true, fillRule="evenodd", fillColor = "grey"));
					                      
void tshape7(){	ex("shape7", shape7); }

// SVG Essentials, p95.

public Figure fillRule1 = grid(fillColor="yellow",
						figArray=[ [ shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
					                      shapeClosed=true, fillRule="nonzero", fillColor = "grey"),
					           
					                 shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(15,45), line(45,45), line(45, 15)], 	// clockwise/counter clockwise
					                       shapeClosed=true, fillRule="nonzero", fillColor = "grey")
					               ],
					               
					               [ shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
					                      shapeClosed=true, fillRule="evenodd", fillColor = "grey"),
					           
					                 shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(15,45), line(45,45), line(45, 15)], 	// clockwise/counter clockwise
					                       shapeClosed=true, fillRule="evenodd", fillColor = "grey")
					               ] ]);
void tfillRule1(){ ex("fillRule1", fillRule1); }

void shapes(){
	ex("shapes", grid(gap=<10,10>,
					figArray=[ [shape1, shape2, shape3, shape4, shape5 ],
							   [shape6, shape7, fillRule1]
							 ]));

}


// hcat  

Figures rgbFigs = [box(fillColor="red",size=<50,100>), box(fillColor="green", size=<200,200>), box(fillColor="blue", size=<10,10>)];

public Figure hcat1 = hcat(figs=rgbFigs, align=top);    
void thcat1(){ ex("hcat1", hcat1); }

public Figure hcat2 = hcat(figs=rgbFigs, align=center);  
void thcat2(){ ex("hcat2", hcat2); }

public Figure hcat3 = hcat(figs=rgbFigs, align=bottom);  
void thcat3(){ ex("hcat3", hcat3); }

public Figure hcat4 = hcat(figs=rgbFigs, align=bottom, gap=<10,10>);
void thcat4(){ ex("hcat4", hcat4); }

// hcat in box

Figure tlFigs = hcat(align=topLeft, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);
				             
Figure ctFigs = hcat(align=center, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);
public Figure box_hcat1 = box(fig=tlFigs, fillColor="grey");
void tbox_hcat1(){ ex("box_hcat1", box_hcat1); }

public Figure box_hcat2 = box(fig=tlFigs, fillColor="grey", size=<400,400>, align=topLeft);
void tbox_hcat2(){ ex("box_hcat2", box_hcat2); }

public Figure box_hcat3 = box(fig=tlFigs, fillColor="grey", size=<400,400>, align=topRight);
void tbox_hcat3(){ ex("box_hcat3", box_hcat3); }

public Figure box_hcat4 = box(fig=tlFigs,fillColor="grey", size=<400,400>, align=bottomRight);
void tbox_hcat4(){ ex("box_hcat4", box_hcat4); }

public Figure box_hcat5 = box(fig=tlFigs, fillColor="grey", size=<400,400>, align=bottomLeft);
void tbox_hcat5(){ ex("box_hcat5", box_hcat5); }

public Figure box_hcat6 = box(fig=tlFigs, fillColor="grey", size=<400,400>, align=topLeft);
void tbox_hcat6(){ ex("box_hcat6", box_hcat6); }

public Figure box_hcat7 = box(fig=ctFigs, fillColor="grey", size=<400,400>, align=topRight);
void tbox_hcat7(){ ex("box_hcat7", box_hcat7); }

public Figure box_hcat8 = box(fig=ctFigs, fillColor="grey", size=<400,400>, align=bottomRight);
void tbox_hcat8(){ ex("box_hcat8", box_hcat8); }

public Figure box_hcat9 = box(fig=ctFigs, fillColor="grey", size=<400,400>, align=bottomLeft);
void tbox_hcat9(){ ex("box_hcat9", box_hcat9); }

// hcat flex

public Figure hflex1 = hcat(size = <200,200>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);
void thflex1(){	ex("hflex1", hflex1); }

public Figure hflex2 = hcat(size = <200,200>,
					        figs = [ box(fillColor="red", height=10), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);
void thflex2(){	ex("hflex2", hflex2); }

public Figure hflex3 = hcat(size = <200,200>,
					        figs = [ box(fillColor="red", width=10), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);
void thflex3(){	ex("hflex3", hflex3); }	

public Figure hflex4 = hcat(size = <200,200>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue")
				                   ]);
void thflex4(){	ex("hflex4", hflex4); }

public Figure hflex5 = hcat(size = <400,400>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue")
				                   ]);
void thflex5(){ ex("hflex5", hflex5); }						               

	
void hcats(){
	ex("hcats", grid(gap=<10,10>,
	                 figArray=[
							[hcat1, hcat2, hcat3, hcat4, box_hcat1],
							[box_hcat2,box_hcat3,box_hcat4,box_hcat5],
							[box_hcat6,box_hcat7,box_hcat8, box_hcat9],
							[hflex1,hflex2,hflex3, hflex4,hflex5]
						  ], align=topLeft));
}		               

// vcat

public Figure vcat1 = vcat(figs=rgbFigs, align=topLeft);
void tvcat1(){ ex("vcat1", vcat1); }

public Figure vcat2 = vcat(figs=rgbFigs, align=top);
void tvcat2(){ ex("vcat2", vcat2); }

public Figure vcat3 = vcat(figs=rgbFigs, align=topRight);
void tvcat3(){ ex("vcat3", vcat3); }

public Figure vcat4 = vcat(figs=rgbFigs, align=topRight, gap=<10,10>);
void tvcat4(){ ex("vcat4", vcat4); }


// vcat in box

Figure vtlFigs = vcat(align=topLeft, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);
				             
Figure vctFigs = vcat(align=center, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);

public Figure box_vcat1 = box(fig=vtlFigs, fillColor="grey");
void tbox_vcat1(){ ex("box_vcat1", box_vcat1); }

public Figure box_vcat2 =  box(fig=vtlFigs, size=<400,400>, align=topLeft, fillColor="grey");
void tbox_vcat2(){ ex("box_vcat2", box_vcat2); }

public Figure box_vcat3 = box(fig=vtlFigs, size=<400,400>, align=topRight, fillColor="grey");
void tbox_vcat3(){ ex("box_vcat3", box_vcat3); }

public Figure box_vcat4 = box(fig=vtlFigs, size=<400,400>, align=bottomRight, fillColor="grey");
void tbox_vcat4(){ ex("box_vcat4", box_vcat4); }

public Figure box_vcat5 = box(fig=vtlFigs, size=<400,400>, align=bottomLeft, fillColor="grey");
void tbox_vcat5(){ ex("box_vcat5", box_vcat5); }

public Figure box_vcat6 = box(fig=vctFigs, size=<400,400>, align=topLeft, fillColor="grey");
void tbox_vcat6(){ ex("box_vcat6", box_vcat6); }

public Figure box_vcat7 = box(fig=vctFigs, size=<400,400>, align=topRight, fillColor="grey");
void tbox_vcat7(){ ex("box_vcat7", box_vcat7); }

public Figure box_vcat8 = box(fig=vctFigs, size=<400,400>, align=bottomRight, fillColor="grey");
void tbox_vcat8(){ ex("box_vcat8", box_vcat8); }

public Figure box_vcat9 = box(fig=vctFigs, size=<400,400>, align=bottomLeft, fillColor="grey");
void tbox_vcat9(){ ex("box_vcat9", box_vcat9);
}

// vcat flex

public Figure vflex1 = vcat(size = <200,200>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);

void tvflex1(){ ex("vflex1", vflex1); }

public Figure vflex2 = vcat(size = <200,200>,
					        figs = [ box(fillColor="red", height=10), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);
void tvflex2(){ ex("vflex2", vflex2); }

public Figure vflex3 = vcat(size = <200,200>,
					        figs = [ box(fillColor="red", width=10), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);
void tvflex3(){	ex("vflex3",vflex3); }	

public Figure vflex4 = vcat(size = <200,200>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue")
				                   ]);
void tvflex4(){ ex("vflex4", vflex4); }

public Figure vflex5 = vcat(size = <400,400>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue")
				                   ]);
void tvflex5(){	ex("vflex5", vflex5);
}	


// hvcat flex

void hvflex1(){
	ex("hvflex1", hcat(size=<600,600>,
					   figs= [ vcat(width=200, height=300,
	                                figs= [ 
	                                       box(fillColor="red"), 
				                           box(fillColor="green", width=50), 
				                           box(fillColor="blue")
				                          ]),
				               vcat(//size = <400,400>,
					               figs = [ box(fillColor="yellow", height=50), 
				                            box(fillColor="purple"), 
				                            box(fillColor="orange")
				                          ])
				            ]));
}	

void vcats(){
	ex("vcats", grid(gap=<10,10>, 
	                 figArray=[
							[vcat1, vcat2, vcat3, vcat4, box_vcat1],
							[box_vcat2,box_vcat3,box_vcat4,box_vcat5],
							[box_vcat6,box_vcat7,box_vcat8, box_vcat9],
							[vflex1,vflex2,vflex3, vflex4,vflex5]
						  ], align=center));
}	

/********************** grid ******************************/

Figure RedBox = box(fillColor="red", size=<50,50>);
Figure BlueBox = box(fillColor="blue", size=<100,30>);
Figure GreenBox = box(fillColor="green", size=<40,60>);


void grid1(){
	ex("grid1", grid(figArray=[ [RedBox],
							    [BlueBox]
							  ], gap=<10,10>));
}

void grid2(){
	ex("grid2", grid(figArray=[ [RedBox, GreenBox],
							    [BlueBox, RedBox, RedBox]
							  ], gap=<10,10>));
}

void grid3(){
	ex("grid3", grid(figArray=[ [box(fillColor="red", size=<50,50>, align=topLeft), GreenBox],
							    [BlueBox, RedBox, RedBox]
							  ], gap=<10,10>));
}
void grid4(){
	ex("grid4", grid(figArray=[ [box(fillColor="red", size=<50,50>, align=bottomRight), GreenBox],
							    [BlueBox, RedBox, RedBox]
							  ], gap=<10,10>));
}

// grid flex

void gflex1(){
	ex("gflex1", grid(size=<600,600>,
					  figArray= [ [box(fillColor="red"),               box(fillColor="green", width=50), box(fillColor="blue")],
				                  [box(fillColor="yellow", height=50), box(fillColor="purple"),          box(fillColor="mediumspringgreen") ]
				                ]));
}

void gflex2(){
	ex("gflex2", grid(size=<600,600>,
					  figArray= [ [box(fillColor="red"),               box(fillColor="green", size=<50,50>), box(fillColor="blue")],
				                  [box(fillColor="yellow", height=50), box(fillColor="purple"),          box(fillColor="mediumspringgreen") ]
				                ]));
}

void gflex3(){
	ex("gflex3", grid(size=<600,600>,
					  figArray= [ [box(fillColor="red"),               box(fillColor="green", size=<50,50>, align=topRight), box(fillColor="blue")],
				                  [box(fillColor="yellow", size=<50,50>, align=bottomLeft), box(fillColor="purple"),          box(fillColor="mediumspringgreen") ]
				                ]));
}

/********************** overlay ******************************/

public Figure overlay1 = overlay(align=topLeft, figs= [box(fillColor="red", size=<50,50>), box(fillColor="green", size=<100,20>)]);
void toverlay1(){ ex("overlay1", overlay1); }

public Figure overlay2 = overlay(align=center, figs= [box(fillColor="red", size=<50,50>), box(fillColor="green", size=<100,20>)]);
void toverlay2(){ ex("overlay2", overlay2); }

public Figure overlay3 = overlay(align=bottomLeft, figs= [box(fillColor="red", size=<50,50>), box(fillColor="green", size=<100,20>)]);
void toverlay3(){ ex("overlay3", overlay3); }

public Figure overlay4 = overlay(align=topRight, figs= [box(fillColor="red", size=<50,50>), box(fillColor="green", size=<100,20>)]);
void toverlay4(){ ex("overlay4", overlay4); }

public Figure overlay5 = overlay(align=center, figs= [box(fillColor="red", size=<50,50>), box(fillColor="green", size=<100,20>)]);
void toverlay5(){ ex("overlay5", overlay5); }

public Figure overlay6 = overlay(align=bottomRight, figs= [box(fillColor="red", size=<50,50>), box(fillColor="green", size=<100,20>)]);
void toverlay6(){ ex("overlay6", overlay6); }


//public Figure overlay5 = box(fig=overlay(align=topLeft, figs=[at(x, y, box(size=<10,10>)) | <x, y> <- [<0,0>, <100,100>, <200,200>]]));
//void toverlay5(){ ex("overlay5", overlay5); }
//
//public Figure overlay6 = box(fig=overlay(align=topLeft, figs= at(0,100, box(size = <100,1>)) + [at(toInt(x * 10), toInt(100 + 100 * sin(x)), box(size=<2,2>))| real x <- [0.0, 0.1 .. 10.0]]));
//void toverlay6(){ ex("overlay6", overlay6); } 

void overlays(){
	ex("overlays", grid(gap=<10,10>,
						figArray=[ 
							[overlay1, overlay2, overlay3],
							[overlay4, overlay5, overlay6]
						]));
}


/********************** at ********************************/

public Figure at1 = at(100, 100, box(fillColor="red", size=<50,50>));
void tat1(){ ex("at1", at1); }

public Figure at2 = overlay(align=topLeft,
						figs= [at(100,100, box(fillColor="red", size=<50,50>)),
							   at(200,200, box(fillColor="green", size=<50,50>))
							  ]);
void tat2(){ ex("at2", at2); }

public Figure at3 = box(fig=overlay(align=topLeft,
	                          figs= [at(100,100, box(fillColor="red", size=<50,50>)),
								     at(200,200, box(fillColor="green", size=<50,50>)),
								     at(0,0, box(fillColor="blue", size=<50,50>))
								]));
void tat3(){ ex("at3", at3); }

public Figure at4 = hcat(figs= [
						box(fillColor="red", size=<50,50>),
								
						box(fig=overlay(align=topLeft,
						                figs= [at(200,200, box(fillColor="yellow", size=<50,50>)),
								               at(20, 0, box(fillColor="gray", size=<50,50>))
								]))
						]);
void tat4(){ ex("at4", at4); }

public Figure at5 = hcat(figs= [
						overlay(align=topLeft,
						        figs= [at(100, 100, box(fillColor="red", size=<50,50>)),
							           at(200, 200, box(fillColor="green", size=<50,50>))
								]),
								
						box(fig=overlay(align=topLeft,
						                figs= [at(100, 100, box(fillColor="purple", size=<50,50>)),
								               at(200, 200, box(fillColor="yellow", size=<50,50>)),
								               at(0, 0, box(fillColor="gray", size=<50,50>))
								]))
						]);
void tat5(){ ex("at5", at5); }

public Figure at6 = box(fig=overlay(align=topLeft,
	                          figs= [at(100, 100, box(fillColor="red", size=<50,50>)),
								       at(100, -50, box(fillColor="green", size=<50,50>)),
								       at(0, 0, box(fillColor="blue", size=<50,50>))
								]));
void tat6(){ ex("at6", at6); }

void ats(){
	ex("ats", hcat(gap=<10,10>, align=topLeft,
				   figs=[
				   			at1, at2, at3 //, at4, at5, at6
				   ]));
}

/********************** scale ******************************/

public Figure scale1 = SCALE(0.5, box(size=<200,300>, lineWidth=4));
void tscale1(){ ex("scale1", scale1); }

public Figure scale2 = SCALE(1, box(size=<200,300>, lineWidth=4));
void tscale2(){	ex("scale2", scale2); }

public Figure scale3 = SCALE(2, box(size=<200,300>, lineWidth=4));
void tscale3(){	ex("scale3", scale3); }

void scales(){ ex("scales", hcat(gap=<10,10>, figs=[ scale1, scale2, scale3])); }

/********************** rotate ******************************/

public Figure rotate1 = rotate(0, box(size=<100,150>));
void trotate1(){ ex("rotate1", rotate1); }

public Figure rotate2 = box(fig=rotate(45, box(size=<100,150>)));
void trotate2(){ ex("rotate2", rotate2); }

public Figure rotate3 = box(fig=rotate(90, box(size=<100,150>)));
void trotate3(){ ex("rotate3", rotate3); }

public Figure rotate4 = box(fig=rotate(135, box(size=<100,150>)));
void trotate4(){ ex("rotate4", rotate4); }

public Figure rotate5 = box(fig=rotate(180, box(size=<100,150>)));
void trotate5(){ ex("rotate5", rotate5); }

public Figure rotate6 = box(fig=rotate(225, box(size=<100,150>)));
void trotate6(){ ex("rotate6", rotate6); }

public Figure rotate7 = box(fig=rotate(270, box(size=<100,150>)));
void trotate7(){ ex("rotate7", rotate7); }

public Figure rotate8 = box(fig=rotate(360, box(size=<100,150>)));
void trotate8(){ ex("rotate8", rotate8); }

void rotates(){
	ex("rotates", hcat(gap=<10,10>,
					   figs= [
					   				rotate1, rotate2 //, rotate3, rotate4,	rotate5, rotate6, rotate7, rotate8
					   ]));
}

/********************** image ******************************/

public Figure image1 = image(url=|file:///lib/favicon.ico|);
void timage1(){	ex("image1", image1); }

public Figure image2 = image(url=|file:///lib/favicon.ico|, size=<80,80>);
void timage2(){	ex("image2", image2); }

public Figure image3 = image(url=|http:///www.wiskgenoot.nl/watis/1b_CWI_LogoCMYK.png|);
void timage3(){	ex("image2", image2); }

public Figure image4 = hcat(figs = [ image(url=|file:///lib/favicon.ico|, size=<50,50>),
							   image(url=|file:///lib/favicon.ico|, size=<100,100>)
							 ]);
void timage4(){	ex("image4",image4); }

public Figure image5 =  rotate(45, image(url=|file:///lib/favicon.ico|, size=<50,50>));
void timage5(){	ex("image5",image5); }

void images(){
	ex("images", grid(gap=<10,10>,
					  figArray= [
					  			[image1, image2, image3],
					  			[image4, image5]
					  ]));
}


// http://www.soc.napier.ac.uk/~cs66/hilbert.html

Vertices hilbert(num x0, num y0, num xis, num xjs, num yis, num yjs, int n){
	/* x0 and y0 are the coordinates of the bottom left corner */
	/* xis & xjs are the i & j components of the unit x vector this frame */
	/* similarly yis and yjs */
	if (n<= 0){
   	return [line(x0+(xis+yis)/2, y0+(xjs+yjs)/2)];
	} else {
		return [ *hilbert(x0,             y0,             yis/2,  yjs/2,  xis/2,  xjs/2,  n-1),
   				 *hilbert(x0+xis/2,       y0+xjs/2,       xis/2,  xjs/2,  yis/2,  yjs/2,  n-1),
  				 *hilbert(x0+xis/2+yis/2, y0+xjs/2+yjs/2, xis/2,  xjs/2,  yis/2,  yjs/2,  n-1),
   				 *hilbert(x0+xis/2+yis,   y0+xjs/2+yjs,   -yis/2, -yjs/2, -xis/2, -xjs/2, n-1) ];
   	}
}

void hilbert1(){
	ex("hilbert1", shape(hilbert(0, 0, 300, 0, 0, 300, 5)));
}

void hilbert2(){
	ex("hilbert2", shape(hilbert(0, 0, 300, 0, 0, 300, 5), 
								startMarker=box(size=<10,10>,fillColor="red"),
								midMarker=box(size=<3,3>,fillColor="blue"),
								endMarker=box(size=<10,10>,fillColor="green")
					   ));
}

void hilbert3(){
	ex("hilbert3", hcat(,
						figs = [ box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 1))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 2))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 3))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 4))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 5)))
							 ]));
}

void hilberts(){
	ex("hilberts", grid(figArray=[
								   [text("1   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 1))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 1), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("2   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 2))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 2), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("3   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 3))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 3), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("4   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 4))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 4), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("5   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 5))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 5), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("6   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 6))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 6), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("7   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 7))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 7), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ]
								   ]));
}


/********************** shape with markers ******************************/

public Figure marker1 =  box(align=topLeft, fig=shape([move(100,100), line(200,200)], startMarker=box(size=<10,10>,fillColor="red")));
void tmarker1(){ ex("marker1",marker1); }

public Figure marker2 = box(align=topLeft, fig=shape([move(100,100), line(100,200), line(200,200)], shapeClosed=true, startMarker=box(size=<10,10>,fillColor="red")));
void tmarker2(){ ex("marker2", marker2); }

public Figure marker3 = box(align=topLeft, fig=shape([move(100,100), line(100,200), line(200,200)], 
												shapeClosed=true,
												startMarker=box(size=<10,10>,fillColor="red"),
												midMarker=box(size=<20,20>,fillColor="blue")
												));
void tmarker3(){ ex("marker3", marker3); }

public Figure marker4 = box(align=topLeft, fig=shape([move(100,100), line(150,30), line(200,100), line(150,150)],
												shapeClosed=true, shapeCurved=true,
												startMarker=box(size=<10,10>,fillColor="red"),
												midMarker=box(size=<20,20>,fillColor="blue")
												//endMarker=box(size=<20,20>,fillColor="yellow")
												));
void tmarker4(){ ex("marker4", marker4); }

Figure arrow(int side, str color, bool rightDir=true) =
	rightDir ? shape([line(0,0), line(side,side), line(0, 2*side)], shapeClosed=true, fillColor=color)
			 : shape([line(side,0), line(0,side), line(side, 2*side)], shapeClosed=true, fillColor=color);

public Figure arrow1 = box(align=topLeft, fig= shape([move(100,100), line(200,200)], endMarker=arrow(10, "red")));
void tarrow1(){ ex("arrow1", arrow1); }

public Figure arrow2 = box(align=topLeft, fig= shape([move(100,100), line(200,200)], startMarker = arrow(10, "green",rightDir=false), endMarker=arrow(10, "red")));
void tarrow2(){	ex("arrow2", arrow2); }

public Figure arrow3 = box(align=topLeft, fillColor="silver", fig= shape([line(100,100), line(200,150), line(100,200), line(250,250)], 
	                                            shapeCurved=true, fillColor="silver",startMarker = arrow(10, "green",rightDir=false), endMarker=arrow(10, "red")));
void tarrow3(){	ex("arrow3", arrow3); }

void markers(){
	ex("markers", grid(gap=<10,10>,
					   figArray=[ [marker1, marker2, marker3, marker4],
					   			  [arrow1, arrow2, arrow3]
					   			]));
}


/********************* barChart ******************************/

Datasets[LabeledData] exampleBarData() =
	("Cumulative Return": [	<"A Label" , -29.765957771107>,
          					<"B Label" , 0>,
       						<"C Label" , 32.807804682612>,
          					<"D Label" , 196.45946739256>,
     						<"E Label" , 0.19434030906893>,
     						<"F Label" , -98.079782601442>,
      						<"G Label" , -13.925743130903>,
      						<"H Label" , -5.1387322875705>
      				   ]);
      				   
 public map[str, lrel[str, int]] stackedData = (
   "0": [
    <"0", 28>,
    <"1", 43>,
    <"2", 81>,
    <"3", 19>,
    <"4", 52>,
    <"5", 24>,
    <"6", 87>,
    <"7", 17>,
    <"8", 68>,
    <"9", 49>
    ],
   "1": [
     <"0", 55>,
     <"1", 91>,
     <"2", 53>,
     <"3", 87>,
     <"4", 48>,
     <"5", 49>,
     <"6", 66>,
     <"7", 27>,
     <"8", 16>,
     <"9", 15>
   ]
);


void stackedBarChart(){       
        ex("stackedBarChart", vega(size=<500,200>, padding = <10,100,10,10>
             ,datasets=stackedData,  command= 
              stackedBar(
                title=("x":"index", "y":"N","color":"p") 
               ,legends = ("color":"fill")
               ,tickLabels=("x":tickLabels(angle=45))          
               ,palette =  color12     
              )
         ));        
}

void groupedBarChart(){
        ex("groupedBarChart", vega(
           size=<500,200>, datasets=stackedData, command=
           groupedBar(
              title=("x":"index", "y":"N","color":"p") 
             ,legends = ("color":"fill")
             ,xTickLabels=tickLabels(angle=90)
             )
           )
          );
}

void stackedAreaChart(){
        ex("stackedAreaChart", vega(size=<500,200>, datasets=stackedData, command=stackedArea()));
}

void graphSetChart(){
        ex("graphSet", vega(size=<500,200>, datasets=stackedData, command=graphSet(grid=true,legends = ("color":"fill"))));
}

void symbolSetChart(){
        ex("symbolSet", vega(size=<500,200>, datasets=stackedData, command=graphSet(shape=("all":"circle"))));
}

void stedenBarChart(){
        ex("stedenBarChart", vega(size=<1000,300>, datasets=exampleSteden(), command=
        groupedBar(
             title=("x":"steden", "y":"N","color":"p") 
             ,legends = ("color":"fill")
             ,tickLabels=("x": tickLabels(angle=90, title_dy = 60, dx = 1),
                          "y": tickLabels(title_dy = -40)
                          )        
             ,palette =  color12
             ,format = ("y": "3s")
             )
        ));
}
     

void barChart1(){
	ex("barChart1", barChart(datasets=exampleBarData()));
}

void barChart2(){
	ex("barChart2", barChart(datasets=exampleBarData(), size=<600,600>));
}

void barChart3(){
	ex("barChart3", hcat(figs=[  box(fillColor="red",size=<100,100>), barChart(size=<400,300>, datasets=exampleBarData())]));
}

/*
map[str, lrel[str, num]] sincos = ("Sine Wave": [<"<x>", round(sin(x/10),0.01)>| x <- [0.0, 1.0 .. 100.0]],
	 "Cosine Wave":       [<"<x>", round(0.5 * cos(x/10), 0.01)>        | x <- [0.0, 1.0 .. 100.0]],
	 "Another sine wave": [<"<x>", round(0.25 * sin(x/10) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 100.0]]
	);

void graphSetChart2(){
        ex("graphSet2", vega(size=<500,200>, datasets=sincos, command=graphSet(shape=("all":"circle"),grid=true)));
}
*/



/********************* lineChart ******************************/

Datasets[XYData] sinAndCos() =
	("Sine Wave":         [<x, round(sin(x/10),0.01)>               | x <- [0.0, 1.0 .. 100.0]],
	 "Cosine Wave":       [<x, round(0.5 * cos(x/10), 0.01)>        | x <- [0.0, 1.0 .. 100.0]],
	 "Another sine wave": [<x, round(0.25 * sin(x/10) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 100.0]]
	);

void lineChart1(){
	ex("lineChart1", lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   yAxis=axis(label="Volutage (v)", tick=".02f"),	
							   datasets= sinAndCos()));
}

void lineChart2(){
	ex("lineChart2", lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   yAxis=axis(label="Volutage (v)", tick=".02f"),	
							   datasets= sinAndCos(), size=<600,600>));
}

void lineChart3(){
	ex("lineChart3", hcat(figs=[box(fillColor="yellow", size=<200,100>),
								lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   			  yAxis=axis(label="Volutage (v)", tick=".02f"),	
							   			  datasets= sinAndCos(), 
							   			  size=<400,400>)
	]));
}

void lineChart4(){
	ex("lineChart4", box(fillColor="whitesmoke", lineWidth=4, lineColor="blue",
					     fig=hcat(figs=[barChart(size=<400,300>, dataset=exampleBarData()),
								lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   			  yAxis=axis(label="Volutage (v)", tick=".02f"),	
							   			  datasets= sinAndCos(), 
							   			  size=<400,400>)
	])));
}

void lineChart5(){
	ex("lineChart5", lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   yAxis=axis(label="Volutage (v)", tick=".02f"),	
							   datasets= sinAndCos(), 
							   flavor="nvLineWithFocusChart",
							   size=<400,400>));
}

/********************* graph ******************************/

lrel[str,Figure] nodes1 = 
			     [ <"N0",    ngon(n=5, fig=text("N0"), fillColor="yellow", lineWidth=1)>,
          		   <"N1" ,   //SCALE(0.5, polygon(points=[<200,10>,<250,190>, <160,210>], fillColor="pink", lineWidth=1))>,
          		   			//SCALE(0.5, polygon(points=[<100,10>, <40,198>, <190,78>, <10,78>, <160,198>], fillColor="green", lineWidth=4))>,
          		   			polygon(points=[<70.0,15>, <75.8,32.2>, <93.8,32.2>, <79.4,43>, <84.6,60.2>, <70,50>, <55.4,60.2>, <60.6,43>, <46.2,32.2>, <64.2,32.2>],fillColor="blue", lineWidth=0)>,
          		   			//box(fig=text("N1"), fillColor="red", lineDashing=[1,1,1,1,1,1], size=<50,50>)>,
     	    	   <"N2" ,	 ellipse(fig=text("N2"), fillColor="lightblue", size=<80,80>)>
     	  		];
list[Figure] edges1 = [ edge("N0","N1", "N0-N1", lineWidth=4), 
						edge("N1","N2", "N1-N2", lineColor="red", lineWidth=3, lineOpacity=0.3), 
						edge("N2","N0", "N2-N0", lineColor="blue", lineDashing=[4,2,4,2]),
						edge("N0","N2", "N0-N2", lineColor="yellow", lineDashing=[4,2,4,2])
					  ];        

void graph1(){
	ex("graph1", graph(nodes=nodes1, edges=edges1, layerSep=50));
}

void graph2(){
	ex("graph2", graph(nodes=nodes1, edges=edges1, flavor="springGraph", lineColor="black", size=<200,200>));
}

void graph3(){
	ex("graph3", hcat(figs= [ graph(nodes=nodes1, edges=edges1),
							  graph(nodes=nodes1, edges=edges1, flavor="springGraph", size=<200,200>)
							 ]));
}

void graph4(){
	ex("graph4", hcat(figs=[ barChart(size=<400,300>, dataset=exampleBarData()),
						     graph(nodes=nodes1, edges=edges1),
					         lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   		   yAxis=axis(label="Volutage (v)", tick=".02f"),	
							   		   dataset= sinAndCos(), 
							   		   size=<400,400>)
					], gap=<50,50>));
}

map[str,Figure] nodes2 =
        ("A": box(size=<20,20>, fillColor="green"),
     	 "B": box(size=<20,20>, fillColor="red"),
     	 "C": box(size=<20,20>, fillColor="blue"),
     	 "D": box( size=<20,20>, fillColor="purple"),
     	 "E": box(size=<20,20>, fillColor="lightblue"),
     	 "F": box(size=<20,20>, fillColor="orange")
     	);
     	
list[Figure] edges2 = 
    	[ edge("A", "B", ""),
    	  edge("B", "C", ""),
    	  edge("C", "D", ""),
    	  edge("D", "E", ""),
    	  edge("E", "F", ""),
    	  edge("F", "A", "")
    	];
    	
public void graph5(){ 
    render("graph5", graph(nodes=nodes2, edges=edges2));
}

public void graph6(){ 
    render("graph6", graph(nodes=nodes2, edges=edges2,flavor="springGraph", size=<300,300>));
}

public void graph7(){ 
    render("graph7", hcat(figs=[graph(nodes=nodes2, edges=edges2),
    					   graph(nodes=nodes2, edges=edges2,flavor="springGraph", size=<300,300>)
    					  ]));
}

public void graph8(){

	Figure b(str label) =  box(fig = text(label), fillColor="whitesmoke", rounded=<5,5>, gap=<5,5>, grow=1.2);

    states = [ 	<"CLOSED", 		ngon(n=6, fig=text("CLOSED"), fillColor="#f77", rounded=<5,5>, gap=<5,5>, grow=1.1)>, 
    			<"LISTEN", 		b("LISTEN")>,
    			<"SYN RCVD", 	b("SYN RCVD")>,
				<"SYN SENT", 	b("SYN SENT")>,
                <"ESTAB",	 	box(fig=text("ESTAB"), fillColor="#7f7", rounded=<5,5>, gap=<5,5>, grow=1.2)>,
                <"FINWAIT-1", 	b("FINWAIT-1")>,
                <"CLOSE WAIT", 	box(fig=text("CLOSE WAIT"), fillColor="whitesmoke", lineDashing=[1,1,1,1],  rounded=<5,5>, gap=<5,5>, grow=1.2)>,
                <"FINWAIT-2", 	b("FINWAIT-2")>,
                   
                <"CLOSING", b("CLOSING")>,
                <"LAST-ACK", b("LAST-ACK")>,
                <"TIME WAIT", b("TIME WAIT")>
                ];
 	
    edges = [	edge("CLOSED", 		"LISTEN",  	 "open"),
    			edge("LISTEN",		"SYN RCVD",  "rcv SYN"),
    			edge("LISTEN",		"SYN SENT",  "send"),
    			edge("LISTEN",		"CLOSED",    "close"),
    			edge("SYN RCVD", 	"FINWAIT-1", "close"),
    			edge("SYN RCVD", 	"ESTAB",     "rcv ACK of SYN"),
    			edge("SYN SENT",   	"SYN RCVD",  "rcv SYN"),
   				edge("SYN SENT",   	"ESTAB",     "rcv SYN, ACK"),
    			edge("SYN SENT",   	"CLOSED",    "close"),
    			edge("ESTAB", 		"FINWAIT-1", "close"),
    			edge("ESTAB", 		"CLOSE WAIT", "rcv FIN"),
    			edge("FINWAIT-1",  	"FINWAIT-2",  "rcv ACK of FIN"),
    			edge("FINWAIT-1",  	"CLOSING",    "rcv FIN"),
    			edge("CLOSE WAIT", 	"LAST-ACK",  "close"),
    			edge("FINWAIT-2",  	"TIME WAIT",  "rcv FIN"),
    			edge("CLOSING",    	"TIME WAIT",  "rcv ACK of FIN"),
    			edge("LAST-ACK",   	"CLOSED",     "rcv ACK of FIN", lineColor="green"),
    			edge("TIME WAIT",  	"CLOSED",     "timeout=2MSL")
  			];
  			
  	render("graph8", graph(nodes=states, edges=edges));
}

/************** text *****************/

public Figure text0 = text("Hello");
void ttext0(){ ex("text0", text0); }

public Figure text1 = text("Hello", fontSize=14, lineWidth=1);
void ttext1(){ ex("text1", text1); }

public Figure text2 = text("Hello", fontSize=20);
void ttext2(){ ex("text2", text2); }

public Figure text3 = text("Hello", fontStyle="italic", fontSize=20);
void ttext3(){	ex("text3", text3); }

public Figure text4 = text("Hello", fontWeight="bold", fontSize=20);
void ttext4(){ ex("text4", text4); }

public Figure text5 = text("Hello", fontWeight="bold", fontStyle="italic", fontSize=20);
void ttext5(){ ex("text5", text5); }


public Figure text6 = text("Hello", fontSize=14, textDecoration="underline");
void ttext6(){ ex("text6", text6); }

public Figure text7 = text("Hello", fontSize=14, textDecoration="line-through");
void ttext7(){ ex("text7", text7); }

public Figure text8 = text("Hello", fontSize=14, textDecoration="overline");
void ttext8(){ ex("text8", text8); }


public Figure text9 = text("Hello", fontSize=14, fontColor="red");
void ttext9(){ ex("text9", text9); }

public Figure text10 = box(fig=text("Hello", fillColor="black", fontWeight="bold", fontStyle="italic", fillColor="black", fontSize=20), fillColor="yellow");
void ttext10(){ ex("text10", text10); }

public Figure text11 = hcat(figs=[ box(fig=text("Hello", fillColor="black"), fillColor="white"),
					  text("World")
					], fontSize=20);
void ttext11(){ ex("text11", text11); }

void texts(){
	ex("texts", grid(gap=<20,20>,align=left, 
	                 figArray=[
							[text0, text1, text2, text3, text4, text5],
							[text6, text7, text8, text9],
							[text10,text11]
							
						  ]));
}	


/************** markdown *****************/

void markdown1(){
	ex("markdown1", markdown("#A markdown example:
							 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
							 '
							 '
							 '* item 1
							 '* item 2
							 '
							 '```literal``` and _emphasis_
							 '"	, size=<200,50>));
}

/************** math *****************/

void math1(){
	ex("math1", math("x+\\sqrt{1-x^2}"));
}

/************** Interaction *****************/

data COUNTER = COUNTER(int counter);

int f(str event, str utag, int x) {if (event=="click") {println("aap"); return x+1;} else return x;}

void counter1(){	
	render("counter1",  #COUNTER, COUNTER(666), Figure (str event, str uutag, COUNTER m) {
			return
				vcat(figs=[ box(fig=text("Click me", event=on("click", bind(m.counter, f(event, m.counter))), fontSize=20, gap=<2,2>), fillColor="whitesmoke"),
					        text(m.counter, size=<150,50>,fontSize=30)
				     ], align=topLeft);
			});
}

void counter2(){
	
	render("counter2",  #COUNTER, COUNTER(666),  Figure (str event, str utag, COUNTER m) {
			return
				vcat(figs=[ box(fig=text("Click me 1", event=on("click", bind(m.counter, m.counter + 1)), fontSize=20, gap=<2,2>), fillColor="whitesmoke"),
					   text(m.counter, size=<150,50>,fontSize=30),
					   box(fig=text("Click me 2", event=on("click", bind(m.counter, m.counter + 1)), fontSize=20, gap=<2,2>), fillColor="whitesmoke"),
					   text(m.counter, size=<150,50>, fontSize=50),
					   text(m.counter, size=<150,50>, fontSize=80)
				     ]);
			});
}



void counter3(){
	
	render("counter3",  #COUNTER, COUNTER(666), Figure (str event, str utag, COUNTER m) {
			return
				vcat(figs=[ buttonInput(trueText="Click me", falseText="Click me", event=on("click", bind(m.counter, f(event, m.counter))), size=<80,40>),
					   text(m.counter, size=<150,50>,fontSize=30)
				     ]);
			});
}

void counter4(){
	
	render("counter4",  #COUNTER, COUNTER(666), Figure (str event, str utag, COUNTER m) {
			return
				vcat(figs=[ buttonInput( trueText="Click me", falseText="Click me", event=on("click", bind(m.counter, m.counter + 1)), size=<80,40>),
					   text(m.counter, size=<150,50>,fontSize=30),
					   buttonInput( trueText="Click me", falseText="Click me", event=on("click", bind(m.counter, m.counter + 1)), size=<100,40>),
					   text(m.counter, size=<150,50>, fontSize=50),
					   text(m.counter, size=<150,50>, fontSize=80)
				     ]);
			});
}

data COUNTER11 = COUNTER11(int counter1, int counter2);

int f1(str event, str utag, int x) {if (event=="click"&& utag== "text1") {return x+1;} else return x;}

int f2(str event, str utag, int x) {if (event=="click"&& utag== "text2") {return x+2;} else return x;}

void counter11(){	
	render("counter1",  #COUNTER11, COUNTER11(1, 1), Figure (str event, str utag, COUNTER11 m) {
			return
				vcat(figs=[ box(fig=text("Click me", id = "text1", event=on("click", bind(m.counter1, f1(event, utag, m.counter1))), fontSize=20, gap=<2,2>), fillColor="whitesmoke"),
					        text(m.counter1, size=<150,50>,fontSize=30),
					        box(fig=text("Click me", id = "text2",  event=on("click", bind(m.counter2, f2(event, utag, m.counter2))), fontSize=20, gap=<2,2>), fillColor="whitesmoke"),
					        text(m.counter2, size=<150,50>,fontSize=30)				      
				     ], align=topLeft);
			});
}

data ECHO = ECHO(str TXT);

void echo1(){
	render("echo1", #ECHO, ECHO("abc"), Figure (str event, str utag, ECHO m) {
			return
				hcat(figs=[ strInput(event=on("submit", bind(m.TXT))), 
	                        text(m.TXT, size=<150,50>, fontSize=50),
	                        text(m.TXT, size=<150,50>, fontSize=80)
				          ], align=bottom, gap=<20,20>);
			});
}

data ECHO2 = ECHO2(num NUM);

void echo2(){
	render("echo2", #ECHO2, ECHO2(0), Figure (str event, str utag,  ECHO2 m) {
			return
				hcat(figs=[ text("Enter number:", fontSize=18),
							numInput(event=on("input", bind(m.NUM))), 
	                        text(m.NUM, size=<150,50>, fontSize=50),
	                        text(m.NUM, size=<150,50>, fontSize=80)
				          ], gap=<20,20>);
			});
}

data BORDER = BORDER(str C);

void border1(){
	render("border1", #BORDER, BORDER("red"), Figure (str event, str utag, BORDER m) {
			return
				hcat(figs=[ text("Enter:", fontSize=18), 
				
	                   strInput(event=on("submit", bind(m.C))), 
	                   
	                   box(lineColor=m.C, lineWidth=10, size=<100,100>),
	                   
	                   box(lineColor=m.C, lineWidth=10, size=<100,100>)
				   ], gap=<20,20>);
			  });
}

void border2(){
	render("border2", #BORDER, BORDER("red"), Figure (str event, str utag, BORDER m) {
			return
				hcat(figs=[ text("Enter:", fontSize=18), 
				
	                   colorInput(event=on("change", bind(m.C)), size=<100,25>), 
	                   
	                   box(lineColor=m.C, lineWidth=10, size=<100,100>),
	                   
	                   box(lineColor=m.C, lineWidth=10, size=<100,100>)
				   ], gap=<20,20>);
			  });
}

data CONTROL = CONTROL(str FC, int LW, int WIDTH , int HEIGHT);

void control1(){
	render("control1", #CONTROL, CONTROL("red",1,100,100), Figure (str event, str utag, CONTROL m) {
			return
				vcat(figs=[
					hcat(figs=[ text("  fillColor:", size=<150,50>, fontSize=20), colorInput(event=on("submit", bind(m.FC)), size=<100,25>),
				
					       text("lineWidth:", size=<150,50>, fontSize=20), numInput(event=on("input", bind(m.LW)), size=<80,25>),
					
					       text("     width:", size=<150,50>, fontSize=20), numInput(event=on("input", bind(m.WIDTH)), size=<100,25>),
					
					       text("    height:", size=<150,50>, fontSize=20), numInput(event=on("input", bind(m.HEIGHT)), size=<100,25>)
					     ]),
					
					box(size=<100,100>, lineWidth=0),
					
	                box(fillColor=m.FC, lineWidth=m.LW, width=m.WIDTH, height=m.HEIGHT)
	                   
				   ], gap=<30,30>);
			  });
}

data CHOICE = CHOICE(int SEL);

void choice1(){
	render("choice1", #CHOICE, CHOICE(0),  Figure (str event, str utag, CHOICE m) {
			return
			hcat(figs=[ text("Enter:", size=<150,50>, fontSize=18), 
			
	               numInput(event=on("submit", bind(m.SEL)), size=<100,25>),
	               
				   choice(selection=m.SEL, 
				   		   figs = [ box(fillColor="red", size=<100,100>),
								      box(fillColor="white", size=<100,100>),
								      box(fillColor="blue", size=<100,100>)
								    ])],
					gap=<30,30>);
					});
}

void choice2(){
	render("choice2", #CHOICE, CHOICE(0),  Figure (str event, str utag, CHOICE m) {
			return
			hcat(figs=[ text("Enter:", size=<150,50>, fontSize=18), 
			
	               choiceInput(choices=["red", "white", "blue"], event=on("change", bind(m.SEL)), size=<100,25>),
	               
				   choice(selection=m.SEL, 
				   		  figs = [ box(fillColor="red", size=<100,100>),
								     box(fillColor="white", size=<100,100>),
								     box(fillColor="blue", size=<100,100>)
								   ])],
					gap=<30,30>);
					});
}

data SLIDER = SLIDER(int SLIDER);

void slider1(){
	render("slider1", #SLIDER, SLIDER(50), Figure (str event, str utag, SLIDER m) {
			return
			vcat(figs=[ hcat(figs=[text("0"), rangeInput(low=0,high=100,step=5, event=on("change", bind(m.SLIDER)), size=<150,50>), text("100")]),
			
				   text(m.SLIDER, size=<150,50>,fontSize=30)
	             ],			  
				 gap=<10,20>);
				 });
}

data DIM = DIM(int WIDTH, int HEIGHT);

void slider2(){

	render("slider2", #DIM, DIM(50,50), Figure (str event, str utag, DIM m) {
			return vcat(figs=[ hcat(figs=[text("WIDTH"), text("0"), rangeInput(low=0,high=100,step=5, event=on("change", bind(m.WIDTH)), size=<150,50>), text("100")]),
			       hcat(figs=[text("HEIGHT"), text("0"), rangeInput(low=0,high=100,step=5, event=on("change", bind(m.HEIGHT)), size=<150,50>), text("100")]),
			
				   box(width=m.WIDTH, height=m.HEIGHT, fillColor="pink")
	             ],			  
				 gap=<10,20>);
		});
}

void slider3(){
	render("slider3", #SLIDER, SLIDER(25), Figure (str event, str utag, SLIDER m) {
			return 
			vcat(figs=[ rangeInput(low=0, high=50, step=5, event=on("change", bind(m.SLIDER)), size=<200,50>),
				   box(size=<50,50>, lineWidth=0),
				   box(lineWidth=m.SLIDER, size=<150,50>, fillColor="red")
	             ], align=topLeft,		  
				 gap=<80,80>);
				 });
}

// Resize barChart

data SIZE = SIZE(int SIZE);

void slider4(){

	render("slider4", #SIZE, SIZE(300), Figure (str event, str utag, SIZE m) {
			low = 100;
			high = 500;
			return vcat(figs=[ hcat(figs=[text("SIZE"), text(low), rangeInput(low=low,high=high,step=5, event=on("change", bind(m.SIZE)), size=<500,50>), text(high) ]),
				               barChart(width=m.SIZE, height=m.SIZE, dataset=exampleBarData())
	             ],			  
				 gap=<10,20>);
		});
}


data VISABLE = VISABLE(str event, str utag, bool VISABLE);

void visible1(){
	render("visible1", #VISABLE, VISABLE(true), Figure (VISABLE m) {
			return 
			vcat(figs=[ buttonInput( trueText="hide", falseText="show", event=on("click", bind(m.VISABLE)),size=<50,50>),
				   
				   visible(condition=m.VISABLE,  fig=box(size=<150,50>, fillColor="red"))
	             ], align=topLeft,		  
				 gap=<30,30>);
				 });
}

void visible2(){
	render("visible2", #VISABLE, VISABLE(true), Figure (str event, str utag, VISABLE m) {
			return 
			vcat(figs=[ checkboxInput(event=on("click", bind(m.VISABLE)), size=<50,50>),
				   
				   visible(condition=m.VISABLE,  fig=box(size=<150,50>, fillColor="red"))
	             ], align=topLeft,		  
				 gap=<30,30>);
				 });
}

// Tooltip

data EMPTY = EMPTY();

Event tooltip(str txt) = on("mouseover", box(size=<100, 20>, fig=text(txt, fontSize=12, lineColor="black"), fillColor="yellow"));

void tooltip1(){
	ex("tooltip1", box(fillColor="red", width=200, height=100, event=tooltip("I am a red box")));
}

void tooltip2(){
	render("tooltip2", #EMPTY, EMPTY(), Figure (bool e, EMPTY m) {
			return 
				vcat(figs=[ box(size=<200,50>, lineColor="white"),
					   hcat(figs=[ box(fillColor="red", width=100, height=100, event=tooltip("I am a red box")),
					   			   box(fillColor="white", width=100, height=100),
						           box(fillColor="blue", width=100, height=100, event=tooltip("I am a blue box"))
	                        ])
	                  ],		  
				 gap=<10,20>);
		});
}

data COLOR1 = COLOR1(str C);

void boxcolor1(){
          
	render("boxcolor1", #COLOR1, COLOR1("white"), Figure (str event, str utag, COLOR1 m) {
			return box(size=<100,100>, fig=colorInput(event=on("change", bind(m.C)), size=<50,20>, fillColor=m.C, rounded=<10,10>, gap=<20,20>,lineDashing=[1,1,1,1,1,1]));
		});
}

data COLOR2 = COLOR2(str C1, str C2);

void boxcolor2(){
          
	render("boxcolor2", #COLOR2, COLOR2("white", "blue"), Figure (str event, str utag, COLOR2 m) {
			return hcat(figs=[ box(size=<100,100>, fig=colorInput(event=on("change", bind(m.C1)), size=<50,20>, fillColor=m.C1, rounded=<10,10>, gap=<20,20>,lineDashing=[1,1,1,1,1,1])),
						       box(size=<100,100>, fig=colorInput(event=on("change", bind(m.C2)), size=<50,20>, fillColor=m.C2, rounded=<10,10>, gap=<20,20>,lineDashing=[1,1,1,1,1,1]))
						     ], gap=<20,30>);
		});
}

// ------------- ALL TESTS -------------------------

void allExamples(){
	boxes();
	hcats();
	vcats();
	overlays();
	polygons();
	ngons();
	shapes();
	markers();
	circles();
	ellipses();
	texts();
	images();
	hilberts();
	ats();
	rotates();
	scales();
}


