module experiments::Processing::mouse1
import Processing::Core;
import IO;

public void mySetup() {
	size(400, 400); 
	stroke(255);
	background(192, 64, 0);
}

public void myDraw() { 
    line(150, 25, mouseX(), mouseY());
}

public void main(){
	P = processing(setup(mySetup), draw(myDraw));
}