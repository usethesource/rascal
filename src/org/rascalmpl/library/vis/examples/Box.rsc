module vis::examples::Box

import vis::Figure;
import vis::Render;
import Number;

import List;
import Set;
import IO;

// Unfilled box of 100x200
public void box1(){
	render(box(width(100), height(200)));
}

// Unfilled box of 100x200
public void box2(){
	render(box(size(100,200)));
}

// Green box of 100x200
public void box3(){
	render(box(size(100,200), fillColor("green")));
}

// Green box of 100x200 with red border
public void box4(){
	render(box(size(100,200), fillColor("green"), lineColor("red")));
}

// Unsized blue outer box with white inner box of 40x40
public void box5(){
	render(box(box(size(40), fillColor("white")), fillColor("mediumblue"), gap(10)));
}

// Unsized blue outer box with white inner ellipse of 40x60

public void box6(){
	render(box(ellipse(size(40,60), fillColor("white")), fillColor("mediumblue"), gap(10)));
}

// Unsized blue outer box with black inner text

public void box7(){
	render(box(text("een label"), fillColor("mediumblue"), gap(10)));
}

// Unsized blue outer box with white inner text

public void box8(){
	render(box(text("een label", fontColor("white")), fillColor("mediumblue"), gap(10)));
}

// Blue outer box of 20x20 with black large inner text (only visible on mouse over)

public void box9(){
	render(box(text("een label"), width(20), height(20), fillColor("mediumblue"), gap(10)));
}

// Unsized outer box, with centered inner box of 100x200

public void bbc(){
	render(box(box(size(100,200), fillColor("green"), lineColor("red")), gap(5, 30)));
}

// Sized outer box, with left-aligned inner box of 100x200

public void bbl(){
	render(box(box(size(100,200), fillColor("green"), lineColor("red")), width(150), height(300), gap(5, 30), left()));
}

// Sized outer box, with top-aligned inner box of 100x200

public void bblt(){
	render(box(box(size(100,200), fillColor("green"), lineColor("red")), width(150), height(300), gap(5,30), left(), top()));
}

// Sized outer box, with bottom-aligned inner box of 100x200
public void bblb(){
	render(box(box(size(100,200), fillColor("green"), lineColor("red")), width(150), height(300), gap(5,30), left(), bottom()));
}

// Sized outer box, with right-aligned inner box of 100x200
public void bbr(){
	render(box(box(size(100,200), fillColor("green"), lineColor("red")), width(150), height(300), gap(5,30), right()));
}

// Sized outer box, with top-aligned and right-aligned inner box of 100x200
public void bbrt(){
	render(box(box(size(100,200), fillColor("green"), lineColor("red")), width(150), height(300), gap(5,30), right(), top()));
}

// Sized outer box, with bottom-aligned and right-aligned inner box of 100x200
public void bbrb(){
	render(box(box(size(100,200), fillColor("green"), lineColor("red")), width(150), height(300), gap(5,30), right(), bottom()));
}