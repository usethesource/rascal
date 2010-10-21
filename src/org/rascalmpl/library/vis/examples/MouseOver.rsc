module vis::examples::MouseOver

import vis::Figure;
import vis::Render; 
import Set;


//TODO Not yet decided what to do with this ******

// Blue outer box of 20x20 with yellow larger inner box (only visible on mouse over)
//Note: left/top borders of innerbox is not visible
public void box10(){
	render(box([width(20), height(20), fillColor("mediumblue"), gap(10)], box([size(30,30), fillColor("yellow")])));
}

public FProperty tip(str S){ 
	return mouseOver(box([fillColor("yellow")], text([fontColor("green")], S)));
}

public void mo1(){
	render(text([tip("XXX")], "A VERY LONG STRING"));
}

public void mo2(){
	render(box([ size(30,30), tip("XXX")]));
}

public void mo3(){
	render(box([ size(100,200), fillColor("green"), mouseOver(box([size(100,200), fillColor("red")])) ]));
}

public void mo4(){
	render(box([fillColor("blue"), gap(30), tip("Outer Box")], 
	           box([fillColor("grey"), tip("Middle Box")], 
	               box([fillColor("red"), size(200,200), tip("Inner Box")])
	              )
	          ));
}

public void mo5(){
	render(hcat( [ mouseOver([lineColor("red")]) ],
				    [
	                 box([ width(100), height(200), text("A very wide label A"), fillColor(color("mediumblue", 0.05)) ]),
	                 box([ width(100), height(200), text("A very wide label B"), fillColor(color("mediumblue", 0.2)) ]),
	                 box([ width(100), height(200), text("A very wide label C"), fillColor(color("mediumblue", 0.4)) ]),
	                 box([ width(100), height(200), text("A very wide label D"),  fillColor(color("mediumblue", 0.6)) ]),
	                 box([ width(100), height(200), text("A very wide label E"), fillColor(color("mediumblue", 0.8)) ]),
	                 box([ width(100), height(200), text("A very wide label F"), fillColor(color("mediumblue", 1.0)) ])
	                ]));
}

public void xx1(){
	render(hcat([text("ABC"), text("DEF"), text("GHI")]));
}
