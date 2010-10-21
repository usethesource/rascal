module vis::examples::Box

import vis::Figure;
import vis::Render;
import Number;

import List;
import Set;
import IO;

// Unfilled box of 100x200
public void box1(){
	render(box([ width(100), height(200) ]));
}

// Unfilled box of 100x200
public void box2(){
	render(box([ size(100,200) ]));
}

// Green box of 100x200
public void box3(){
	render(box([ size(100,200), fillColor("green") ]));
}

// Green box of 100x200 with red border
public void box4(){
	render(box([ size(100,200), fillColor("green"), lineColor("red")]));
}

// Unsized blue outer box with white inner box of 40x40
public void box5(){
	render(box([fillColor("mediumblue"), gap(10)], box([size(40), fillColor("white")])));
}

// Unsized blue outer box with white inner ellipse of 40x60

public void box6(){
	render(box([fillColor("mediumblue"), gap(10)], ellipse([size(40,60), fillColor("white")])));
}

// Unsized blue outer box with black inner text

public void box7(){
	render(box([fillColor("mediumblue"), gap(10)], text("een label")));
}

// Unsized blue outer box with white inner text

public void box8(){
	render(box([fillColor("mediumblue"), gap(10)], text([fontColor("white")], "een label")));
}

// Blue outer box of 20x20 with black large inner text (only visible on mouse over)

public void box9(){
	render(box([width(20), height(20), fillColor("mediumblue"), gap(10)], text("een label")));
}

// Unsized outer box, with centered inner box of 100x200

public void bbc(){
	render(box([gap(5, 30)], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with left-aligned inner box of 100x200

public void bbl(){
	render(box([width(150), height(300), gap(5, 30), left()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with top-aligned inner box of 100x200

public void bblt(){
	render(box([width(150), height(300), gap(5,30), left(), top()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with bottom-aligned inner box of 100x200
public void bblb(){
	render(box([width(150), height(300), gap(5,30), left(), bottom()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with right-aligned inner box of 100x200
public void bbr(){
	render(box([width(150), height(300), gap(5,30), right()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with top-aligned and right-aligned inner box of 100x200
public void bbrt(){
	render(box([width(150), height(300), gap(5,30), right(), top()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with bottom-aligned and right-aligned inner box of 100x200
public void bbrb(){
	render(box([width(150), height(300), gap(5,30), right(), bottom()], box([size(100,200), fillColor("green"), lineColor("red")])));
}