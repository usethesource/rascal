module vis::examples::Ellipse

import vis::Figure;
import vis::Render;
import Number;

import List;
import Set;
import IO;

// Ellipse of 50x100

public void e1(){
	render(ellipse([width(50), height(100)]));
}

// Unsized blue ellipse with sized white inner box
public void e2(){
	render(ellipse([fillColor("mediumblue"), gap(20)], box([size(40), fillColor("white")])));
}

// Unsized blue ellipse with sized white inner text
public void e3(){
	render(ellipse([fillColor("mediumblue"), gap(10)], text([fontColor("white")], "een label")));
}

// Sized ellipse with inner text that appears on mouseOver.
public void e4(){
	render(ellipse([width(40), height(20), fillColor("mediumblue"), gap(10)], text([fontColor("white")], "een label")));
}